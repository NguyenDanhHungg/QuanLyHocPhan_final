package university.registration.controller;

import university.registration.model.Course;
import university.registration.model.Offering;
import university.registration.model.RegItem;
import university.registration.model.Student;
import university.registration.service.CourseService;
import university.registration.service.RegistrationService;
import university.registration.service.TermService;
import university.registration.store.Memory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Controller xử lý logic nghiệp vụ cho màn hình AdminFrame
 * 
 * Lớp này chịu trách nhiệm:
 * - Xử lý logic nghiệp vụ (gọi Service layer)
 * - Điều phối giữa UI và Service
 * - Xử lý dữ liệu để hiển thị (format, transform)
 * 
 * UI (AdminFrame) chỉ nên gọi các method của Controller này,
 * Controller sẽ gọi Service để xử lý logic nghiệp vụ.
 */
public class AdminController {
    
    // Các service để xử lý logic nghiệp vụ
    private final CourseService courseService = new CourseService();
    private final TermService termService = new TermService();
    private final RegistrationService registrationService = new RegistrationService();
    
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
     * Cập nhật trạng thái mở/đóng đăng ký cho một học kỳ
     * 
     * @param term Học kỳ cần cập nhật
     * @param open true để mở, false để đóng
     */
    public void setTermOpen(String term, boolean open) {
        termService.setTermOpen(term, open);
    }
    
    /**
     * Lấy dữ liệu để hiển thị bảng danh sách học phần
     * 
     * @param term Học kỳ cần lấy dữ liệu
     * @return Danh sách các mảng Object, mỗi mảng chứa thông tin 1 học phần
     */
    public List<Object[]> getCourseTableData(String term) {
        List<Object[]> rows = new ArrayList<>();
        
        // Duyệt tất cả học phần trong hệ thống
        for (Course course : courseService.getAllCourses().values()) {
            // Lấy Offering (cấu hình mở lớp) cho học phần này trong kỳ đang chọn
            Offering offering = termService.getOffering(term, course.code);
            
            // Lấy chương trình đào tạo được phép (mặc định "Tất cả" nếu chưa có offering)
            String allowedProgram = (offering == null) ? "Tất cả" : offering.allowedProgram;
            
            // Đếm số lượng sinh viên đã đăng ký
            int registrationCount = courseService.countRegistrationsByCourse(term, course.code);
            
            // Chuẩn bị dữ liệu hiển thị (một số field chưa có trong model, để mặc định)
            String type = "Tự chọn"; // TODO: Thêm field type vào Course model
            String instructor = "-"; // TODO: Thêm field instructor vào Course/Offering model
            String schedule = "-"; // TODO: Thêm field schedule vào Offering model
            String room = "-"; // TODO: Thêm field room vào Offering model
            String openDisplay = (offering != null && offering.open) ? "Mở" : "Đóng";
            
            // Tạo mảng Object chứa dữ liệu cho 1 dòng trong bảng
            Object[] row = new Object[]{
                course.code,              // Mã HP
                course.name,              // Tên học phần
                String.valueOf(course.credits), // Số TC
                type,                     // Loại
                instructor,               // Giảng viên
                openDisplay,              // Mở lớp?
                allowedProgram,           // Chỉ CTĐT
                schedule,                 // Lịch học
                room,                     // Phòng
                String.valueOf(registrationCount), // SV đã DK
                false                     // Chọn (checkbox)
            };
            
            rows.add(row);
        }
        
        return rows;
    }
    
    /**
     * Thêm hoặc cập nhật học phần
     * 
     * @param code Mã học phần
     * @param name Tên học phần
     * @param credits Số tín chỉ
     * @param selectedTerm Học kỳ được chọn để mở lớp
     * @param allowedProgram Chương trình đào tạo được phép
     * @return Thông báo lỗi nếu có (null nếu thành công)
     */
    public String addOrUpdateCourse(String code, String name, int credits, 
                                     String selectedTerm, String allowedProgram) {
        // Validate dữ liệu đầu vào
        if (code == null || code.trim().isEmpty() || 
            name == null || name.trim().isEmpty()) {
            return "Nhập đầy đủ Mã HP và Tên học phần.";
        }
        
        if (selectedTerm == null || selectedTerm.equals("-- Chọn kỳ --")) {
            return "Vui lòng chọn kỳ để mở lớp.";
        }
        
        // Tạo đối tượng Course và lưu vào hệ thống
        Course course = new Course(code.trim().toUpperCase(), name.trim(), credits);
        courseService.addCourse(course);
        
        // Thiết lập Offering (mở lớp) cho học phần này trong kỳ đã chọn
        boolean open = true; // Mặc định là mở khi đã chọn kỳ
        termService.setOffering(selectedTerm, course.code, open, allowedProgram);
        
        return null; // Thành công
    }
    
    /**
     * Xóa các học phần đã chọn
     * 
     * @param courseCodes Danh sách mã học phần cần xóa
     * @return Thông báo lỗi nếu có (null nếu có thể xóa)
     */
    public String validateDeleteCourses(List<String> courseCodes) {
        if (courseCodes == null || courseCodes.isEmpty()) {
            return "Chọn các dòng cần xóa (cột Chọn).";
        }
        
        // Kiểm tra từng học phần có thể xóa được không
        for (String code : courseCodes) {
            if (!courseService.canDeleteCourse(code)) {
                return "Không thể xóa " + code + 
                       " vì đã có sinh viên đăng ký ở một số học kỳ.";
            }
        }
        
        return null; // Có thể xóa
    }
    
    /**
     * Thực hiện xóa các học phần
     * 
     * @param courseCodes Danh sách mã học phần cần xóa
     */
    public void deleteCourses(List<String> courseCodes) {
        for (String code : courseCodes) {
            courseService.deleteCourse(code);
        }
    }
    
    /**
     * Lấy thông tin học phần để hiển thị trong form
     * 
     * @param courseCode Mã học phần
     * @param term Học kỳ
     * @return Mảng Object chứa thông tin: [course, offering] hoặc null nếu không tìm thấy
     */
    public Object[] getCourseInfoForForm(String courseCode, String term) {
        Course course = courseService.getCourseByCode(courseCode);
        if (course == null) {
            return null;
        }
        
        Offering offering = termService.getOffering(term, courseCode);
        return new Object[]{course, offering};
    }
    
    /**
     * Lấy danh sách đăng ký để hiển thị trong bảng duyệt đăng ký
     * 
     * @param searchText Từ khóa tìm kiếm
     * @param selectedStatus Trạng thái lọc ("Tất cả", "Chờ xử lý", "Đã duyệt", "Đã từ chối")
     * @param selectedTerm Học kỳ lọc ("Tất cả" hoặc mã học kỳ cụ thể)
     * @param selectedDept Khoa/Viện lọc ("Tất cả" hoặc tên khoa)
     * @return Danh sách các mảng Object, mỗi mảng chứa thông tin 1 đăng ký
     */
    public List<Object[]> getRegistrationApprovalData(String searchText, String selectedStatus, 
                                                       String selectedTerm, String selectedDept) {
        List<Object[]> rows = new ArrayList<>();
        String searchLower = (searchText != null) ? searchText.toLowerCase() : "";
        
        int regCount = 0;
        
        // Duyệt tất cả sinh viên và các học kỳ của họ
        for (Map.Entry<String, Map<String, List<RegItem>>> studentEntry : Memory.regs.entrySet()) {
            String studentId = studentEntry.getKey();
            Student student = Memory.studentsById.get(studentId);
            if (student == null) continue;
            
            // Lọc theo khoa/viện
            if (selectedDept != null && !selectedDept.equals("Tất cả")) {
                if (!selectedDept.equals(student.program)) {
                    continue;
                }
            }
            
            Map<String, List<RegItem>> termRegs = studentEntry.getValue();
            for (Map.Entry<String, List<RegItem>> termEntry : termRegs.entrySet()) {
                String term = termEntry.getKey();
                
                // Lọc theo học kỳ
                if (selectedTerm != null && !selectedTerm.equals("Tất cả")) {
                    if (!selectedTerm.equals(term)) {
                        continue;
                    }
                }
                
                List<RegItem> regItems = termEntry.getValue();
                for (RegItem item : regItems) {
                    // Chuẩn hóa trạng thái
                    String status = item.status;
                    if ("Tạm".equals(status) || "Đã gửi".equals(status)) {
                        status = "Chờ xử lý";
                    }
                    
                    // Lọc theo trạng thái
                    if (selectedStatus != null && !selectedStatus.equals("Tất cả")) {
                        if (!selectedStatus.equals(status)) {
                            continue;
                        }
                    }
                    
                    // Lọc theo từ khóa tìm kiếm
                    if (!searchLower.isEmpty()) {
                        if (!student.fullName.toLowerCase().contains(searchLower) &&
                            !studentId.toLowerCase().contains(searchLower) &&
                            !item.course.name.toLowerCase().contains(searchLower) &&
                            !item.course.code.toLowerCase().contains(searchLower)) {
                            continue;
                        }
                    }
                    
                    // Thêm vào danh sách kết quả
                    regCount++;
                    String regCode = "DK" + String.format("%03d", regCount);
                    
                    rows.add(new Object[]{
                        regCode,                    // Mã ĐK
                        student.fullName,           // Tên sinh viên
                        studentId,                  // Mã SV
                        item.course.name,           // Tên học phần
                        String.valueOf(item.course.credits), // Tín chỉ
                        item.date,                  // Thời gian ĐK
                        status,                     // Trạng thái
                        term,                       // Học kỳ
                        ""                          // Hành động (để UI xử lý)
                    });
                }
            }
        }
        
        return rows;
    }
    
    /**
     * Duyệt đăng ký học phần
     * 
     * @param studentId Mã số sinh viên
     * @param term Học kỳ
     * @param courseName Tên học phần (để tìm chính xác)
     * @return true nếu thành công, false nếu không tìm thấy
     */
    public boolean approveRegistration(String studentId, String term, String courseName) {
        // Tìm course code từ course name
        String courseCode = null;
        List<RegItem> regs = registrationService.getRegistrations(studentId, term);
        for (RegItem item : regs) {
            if (item.course.name.equals(courseName)) {
                courseCode = item.course.code;
                break;
            }
        }
        
        if (courseCode != null) {
            return registrationService.approveRegistration(studentId, term, courseCode);
        }
        
        return false;
    }
    
    /**
     * Từ chối đăng ký học phần
     * 
     * @param studentId Mã số sinh viên
     * @param term Học kỳ
     * @param courseName Tên học phần (để tìm chính xác)
     * @return true nếu thành công, false nếu không tìm thấy
     */
    public boolean rejectRegistration(String studentId, String term, String courseName) {
        // Tìm course code từ course name
        String courseCode = null;
        List<RegItem> regs = registrationService.getRegistrations(studentId, term);
        for (RegItem item : regs) {
            if (item.course.name.equals(courseName)) {
                courseCode = item.course.code;
                break;
            }
        }
        
        if (courseCode != null) {
            return registrationService.rejectRegistration(studentId, term, courseCode);
        }
        
        return false;
    }
}

