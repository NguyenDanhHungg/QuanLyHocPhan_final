package university.registration.controller;

import university.registration.model.Course;
import university.registration.model.RegItem;
import university.registration.model.Student;
import university.registration.service.CourseService;
import university.registration.service.RegistrationService;
import university.registration.service.TermService;
import university.registration.store.Memory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Controller xử lý logic nghiệp vụ cho màn hình StudentRegistrationFrame
 * 
 * Lớp này chịu trách nhiệm:
 * - Xử lý logic nghiệp vụ (gọi Service layer)
 * - Điều phối giữa UI và Service
 * - Xử lý dữ liệu để hiển thị (format, transform)
 * 
 * UI (StudentRegistrationFrame) chỉ nên gọi các method của Controller này,
 * Controller sẽ gọi Service để xử lý logic nghiệp vụ.
 */
public class StudentRegistrationController {
    
    // Các service để xử lý logic nghiệp vụ
    private final RegistrationService registrationService = new RegistrationService();
    private final CourseService courseService = new CourseService();
    private final TermService termService = new TermService();
    
    // Sinh viên đang sử dụng hệ thống
    private final Student student;
    
    /**
     * Constructor
     * 
     * @param student Sinh viên đang sử dụng hệ thống
     */
    public StudentRegistrationController(Student student) {
        this.student = student;
    }
    
    /**
     * Lấy danh sách tất cả học kỳ
     * 
     * @return Danh sách học kỳ
     */
    public List<String> getAllTerms() {
        return termService.getAllTerms();
    }
    
    /**
     * Kiểm tra học kỳ có đang mở đăng ký hay không
     * 
     * @param term Học kỳ cần kiểm tra
     * @return true nếu đang mở, false nếu đã đóng
     */
    public boolean isTermOpen(String term) {
        return termService.isTermOpen(term);
    }
    
    /**
     * Lấy danh sách học phần có thể đăng ký trong một học kỳ
     * 
     * @param term Học kỳ
     * @return Danh sách CourseItem (code, name, credits) có thể đăng ký
     */
    public List<CourseItem> getAvailableCourses(String term) {
        List<CourseItem> availableCourses = new ArrayList<>();
        
        // Duyệt tất cả học phần trong hệ thống
        for (Course course : courseService.getAllCourses().values()) {
            // Lấy Offering (cấu hình mở lớp) cho học phần này
            var offering = termService.getOffering(term, course.code);
            
            // Kiểm tra điều kiện: có offering, đang mở, và cho phép CTĐT của sinh viên
            boolean allow = (offering != null && offering.open &&
                    ("Tất cả".equals(offering.allowedProgram) ||
                            student.program.equals(offering.allowedProgram)));
            
            if (allow) {
                availableCourses.add(new CourseItem(course.code, course.name, course.credits));
            }
        }
        
        return availableCourses;
    }
    
    /**
     * Lớp inner để chứa thông tin học phần (dùng cho combobox)
     */
    public static class CourseItem {
        public final String code, name;
        public final int credits;
        
        public CourseItem(String c, String n, int cr) {
            code = c;
            name = n;
            credits = cr;
        }
    }
    
    /**
     * Thêm học phần vào danh sách đăng ký tạm
     * 
     * @param term Học kỳ
     * @param courseCode Mã học phần
     * @return Thông báo lỗi nếu có (null nếu thành công)
     */
    public String addRegistration(String term, String courseCode) {
        if (courseCode == null || courseCode.isEmpty()) {
            return "Không có học phần phù hợp để đăng ký.";
        }
        
        // Gọi service để thêm đăng ký
        boolean success = registrationService.addRegistration(student.studentId, term, courseCode);
        
        if (!success) {
            return "Bạn đã đăng ký học phần này trong học kỳ " + term + ".";
        }
        
        return null; // Thành công
    }
    
    /**
     * Xóa các học phần đã chọn khỏi danh sách đăng ký
     * 
     * @param term Học kỳ
     * @param courseCodes Tập hợp mã học phần cần xóa
     * @return Thông báo lỗi nếu có (null nếu thành công)
     */
    public String deleteRegistrations(String term, Set<String> courseCodes) {
        if (courseCodes == null || courseCodes.isEmpty()) {
            return "Hãy tích chọn những dòng muốn xóa.";
        }
        
        registrationService.deleteRegistrations(student.studentId, term, courseCodes);
        return null; // Thành công
    }
    
    /**
     * Gửi đăng ký (chuyển trạng thái từ "Tạm" sang "Đã gửi")
     * 
     * @param term Học kỳ
     * @return Thông báo lỗi nếu có (null nếu thành công)
     */
    public String submitRegistrations(String term) {
        List<RegItem> regs = registrationService.getRegistrations(student.studentId, term);
        
        if (regs.isEmpty()) {
            return "Chưa có học phần nào để gửi.";
        }
        
        registrationService.submitRegistrations(student.studentId, term);
        return null; // Thành công
    }
    
    /**
     * Lấy dữ liệu để hiển thị bảng đăng ký
     * 
     * @param term Học kỳ
     * @return Danh sách các mảng Object, mỗi mảng chứa thông tin 1 đăng ký
     */
    public List<Object[]> getRegistrationTableData(String term) {
        List<Object[]> rows = new ArrayList<>();
        
        // Lấy danh sách đăng ký từ service
        List<RegItem> regs = registrationService.getRegistrations(student.studentId, term);
        
        // Chuyển đổi sang format hiển thị
        for (RegItem item : regs) {
            rows.add(new Object[]{
                item.course.code,                    // Mã HP
                item.course.name,                    // Tên học phần
                item.date,                           // Ngày đăng ký
                item.status,                         // Trạng thái
                String.valueOf(item.course.credits), // Số TC
                false                                // Chọn (checkbox)
            });
        }
        
        return rows;
    }
    
    /**
     * Tính tổng số tín chỉ đã đăng ký
     * 
     * @param term Học kỳ
     * @return Tổng số tín chỉ
     */
    public int calculateTotalCredits(String term) {
        return registrationService.calculateTotalCredits(student.studentId, term);
    }
    
    /**
     * Lấy danh sách học phần cho danh mục (catalog)
     * 
     * @param term Học kỳ
     * @param searchText Từ khóa tìm kiếm
     * @return Danh sách các mảng Object, mỗi mảng chứa thông tin 1 học phần
     */
    public List<Object[]> getCatalogData(String term, String searchText) {
        List<Object[]> rows = new ArrayList<>();
        String searchLower = (searchText != null) ? searchText.toLowerCase() : "";
        
        // Duyệt tất cả học phần
        for (Course course : courseService.getAllCourses().values()) {
            var offering = termService.getOffering(term, course.code);
            
            // Chỉ hiển thị học phần đang mở
            if (offering != null && offering.open) {
                // Lọc theo từ khóa tìm kiếm
                if (!searchLower.isEmpty()) {
                    if (!course.code.toLowerCase().contains(searchLower) &&
                        !course.name.toLowerCase().contains(searchLower)) {
                        continue;
                    }
                }
                
                // Thêm vào danh sách (một số field chưa có trong model, để mặc định)
                String prereq = "Không có"; // TODO: Thêm field prerequisites vào Course model
                
                rows.add(new Object[]{
                    course.code,                    // Mã HP
                    course.name,                    // Tên học phần
                    String.valueOf(course.credits), // Số tín chỉ
                    "Công nghệ thông tin",          // Khoa/Bộ môn (TODO: Thêm vào model)
                    prereq                          // Học phần tiên quyết
                });
            }
        }
        
        return rows;
    }
    
    /**
     * Lấy dữ liệu lịch sử đăng ký
     * 
     * @return Danh sách các mảng Object, mỗi mảng chứa thông tin 1 đăng ký trong lịch sử
     */
    public List<Object[]> getHistoryData() {
        List<Object[]> rows = new ArrayList<>();
        
        // Lấy tất cả đăng ký của sinh viên từ tất cả các kỳ
        var allRegs = Memory.regs.get(student.studentId);
        if (allRegs != null) {
            for (var entry : allRegs.entrySet()) {
                String term = entry.getKey();
                for (RegItem item : entry.getValue()) {
                    rows.add(new Object[]{
                        term,                        // Học kỳ
                        item.course.code,            // Mã HP
                        item.course.name,            // Tên học phần
                        String.valueOf(item.course.credits), // Số TC
                        item.date,                   // Ngày đăng ký
                        item.status,                 // Trạng thái
                        "N/A"                        // Điểm (chưa có trong hệ thống)
                    });
                }
            }
        }
        
        return rows;
    }
}

