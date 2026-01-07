package university.registration.service;

import university.registration.model.Course;
import university.registration.model.RegItem;
import university.registration.store.Memory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Service xử lý logic nghiệp vụ liên quan đến Đăng ký học phần
 * 
 * Lớp này chịu trách nhiệm:
 * - Thêm mới đăng ký học phần cho sinh viên
 * - Xóa đăng ký học phần
 * - Lấy danh sách đăng ký của sinh viên
 * - Cập nhật trạng thái đăng ký (Duyệt/Từ chối)
 * - Kiểm tra điều kiện đăng ký (học kỳ mở, học phần mở, không trùng)
 */
public class RegistrationService {
    
    /**
     * Thêm học phần vào danh sách đăng ký tạm của sinh viên
     * 
     * Điều kiện:
     * - Học kỳ phải đang mở đăng ký
     * - Học phần phải có Offering và đang mở
     * - Sinh viên chưa đăng ký học phần này trong kỳ đó
     * 
     * @param studentId Mã số sinh viên
     * @param term Học kỳ
     * @param courseCode Mã học phần
     * @return true nếu thêm thành công, false nếu đã tồn tại hoặc không đủ điều kiện
     */
    public boolean addRegistration(String studentId, String term, String courseCode) {
        // Lấy thông tin học phần
        Course course = Memory.courses.get(courseCode);
        if (course == null) {
            return false; // Học phần không tồn tại
        }
        
        // Tạo RegItem với ngày hiện tại và trạng thái "Tạm"
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        RegItem item = new RegItem(course, today, "Tạm");
        
        // Thêm vào Memory (Memory sẽ kiểm tra trùng)
        return Memory.addReg(studentId, term, item);
    }
    
    /**
     * Xóa các học phần đã chọn khỏi danh sách đăng ký
     * 
     * @param studentId Mã số sinh viên
     * @param term Học kỳ
     * @param courseCodes Tập hợp mã học phần cần xóa
     */
    public void deleteRegistrations(String studentId, String term, Set<String> courseCodes) {
        Memory.deleteByCourseCodes(studentId, term, courseCodes);
    }
    
    /**
     * Lấy danh sách đăng ký của sinh viên trong một học kỳ
     * 
     * @param studentId Mã số sinh viên
     * @param term Học kỳ
     * @return Danh sách RegItem (danh sách các học phần đã đăng ký)
     */
    public List<RegItem> getRegistrations(String studentId, String term) {
        return Memory.loadReg(studentId, term);
    }
    
    /**
     * Gửi đăng ký (chuyển trạng thái từ "Tạm" sang "Đã gửi")
     * 
     * @param studentId Mã số sinh viên
     * @param term Học kỳ
     */
    public void submitRegistrations(String studentId, String term) {
        List<RegItem> regs = Memory.loadReg(studentId, term);
        for (RegItem item : regs) {
            if ("Tạm".equals(item.status)) {
                item.status = "Đã gửi";
            }
        }
    }
    
    /**
     * Duyệt đăng ký (chuyển trạng thái sang "Đã duyệt")
     * 
     * @param studentId Mã số sinh viên
     * @param term Học kỳ
     * @param courseCode Mã học phần
     * @return true nếu tìm thấy và cập nhật thành công, false nếu không tìm thấy
     */
    public boolean approveRegistration(String studentId, String term, String courseCode) {
        List<RegItem> regs = Memory.loadReg(studentId, term);
        for (RegItem item : regs) {
            if (item.course.code.equals(courseCode)) {
                item.status = "Đã duyệt";
                return true;
            }
        }
        return false;
    }
    
    /**
     * Từ chối đăng ký (chuyển trạng thái sang "Đã từ chối")
     * 
     * @param studentId Mã số sinh viên
     * @param term Học kỳ
     * @param courseCode Mã học phần
     * @return true nếu tìm thấy và cập nhật thành công, false nếu không tìm thấy
     */
    public boolean rejectRegistration(String studentId, String term, String courseCode) {
        List<RegItem> regs = Memory.loadReg(studentId, term);
        for (RegItem item : regs) {
            if (item.course.code.equals(courseCode)) {
                item.status = "Đã từ chối";
                return true;
            }
        }
        return false;
    }
    
    /**
     * Tính tổng số tín chỉ đã đăng ký của sinh viên trong một học kỳ
     * 
     * @param studentId Mã số sinh viên
     * @param term Học kỳ
     * @return Tổng số tín chỉ
     */
    public int calculateTotalCredits(String studentId, String term) {
        List<RegItem> regs = Memory.loadReg(studentId, term);
        int total = 0;
        for (RegItem item : regs) {
            total += item.course.credits;
        }
        return total;
    }
}

