package university.registration.service;

import university.registration.model.Student;
import university.registration.store.Memory;

/**
 * Service xử lý logic đăng nhập và xác thực người dùng
 * 
 * Lớp này chịu trách nhiệm:
 * - Xác thực tài khoản Admin (PĐT)
 * - Xác thực tài khoản Sinh viên
 * - Tìm kiếm sinh viên theo MSSV hoặc Email
 */
public class AuthenticationService {
    
    /**
     * Xác thực tài khoản Admin (Phòng Đào Tạo)
     * 
     * @param username Tên đăng nhập của admin
     * @param password Mật khẩu của admin
     * @return true nếu đăng nhập thành công, false nếu sai thông tin
     */
    public boolean verifyAdmin(String username, String password) {
        return Memory.verifyAdmin(username, password);
    }
    
    /**
     * Xác thực tài khoản Sinh viên
     * 
     * @param studentId Mã số sinh viên
     * @param password Mật khẩu
     * @return true nếu đăng nhập thành công, false nếu sai thông tin
     */
    public boolean verifyStudent(String studentId, String password) {
        return Memory.verifyStudent(studentId, password);
    }
    
    /**
     * Tìm kiếm sinh viên theo MSSV hoặc Email
     * Hỗ trợ đăng nhập bằng cả MSSV và Email
     * 
     * @param identifier MSSV hoặc Email của sinh viên
     * @return Đối tượng Student nếu tìm thấy, null nếu không tìm thấy
     */
    public Student findStudentByIdentifier(String identifier) {
        // Thử tìm theo MSSV trước
        Student student = Memory.studentsById.get(identifier);
        
        // Nếu không tìm thấy, thử tìm theo Email (chuyển về lowercase để so sánh)
        if (student == null) {
            String studentId = Memory.emailIndex.get(identifier.toLowerCase());
            if (studentId != null) {
                student = Memory.studentsById.get(studentId);
            }
        }
        
        return student;
    }
    
    /**
     * Kiểm tra đăng nhập và trả về đối tượng Student nếu thành công
     * 
     * @param identifier MSSV hoặc Email
     * @param password Mật khẩu
     * @return Đối tượng Student nếu đăng nhập thành công, null nếu thất bại
     */
    public Student loginStudent(String identifier, String password) {
        Student student = findStudentByIdentifier(identifier);
        
        if (student != null && verifyStudent(student.studentId, password)) {
            return student;
        }
        
        return null;
    }
}

