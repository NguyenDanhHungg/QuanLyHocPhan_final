package university.registration.controller;

import university.registration.model.Student;
import university.registration.service.AuthenticationService;

/**
 * Controller xử lý logic nghiệp vụ cho màn hình LoginFrame
 * 
 * Lớp này chịu trách nhiệm:
 * - Xử lý logic đăng nhập (gọi AuthenticationService)
 * - Xác định loại tài khoản (Admin hay Student)
 * - Trả về kết quả đăng nhập để UI xử lý
 * 
 * UI (LoginFrame) chỉ nên gọi các method của Controller này,
 * Controller sẽ gọi Service để xử lý logic nghiệp vụ.
 */
public class LoginController {
    
    // Service xử lý logic đăng nhập và xác thực
    private final AuthenticationService authService = new AuthenticationService();
    
    /**
     * Kết quả đăng nhập
     */
    public enum LoginResultType {
        ADMIN,      // Đăng nhập Admin thành công
        STUDENT,    // Đăng nhập Sinh viên thành công
        FAILED      // Đăng nhập thất bại
    }
    
    /**
     * Kết quả đăng nhập (dùng để trả về cho UI)
     */
    public static class LoginResult {
        public final LoginResultType type;
        public final Student student;  // Chỉ có giá trị nếu type = STUDENT
        public final String errorMessage;  // Chỉ có giá trị nếu type = FAILED
        
        private LoginResult(LoginResultType type, Student student, String errorMessage) {
            this.type = type;
            this.student = student;
            this.errorMessage = errorMessage;
        }
        
        public static LoginResult admin() {
            return new LoginResult(LoginResultType.ADMIN, null, null);
        }
        
        public static LoginResult student(Student student) {
            return new LoginResult(LoginResultType.STUDENT, student, null);
        }
        
        public static LoginResult failed(String errorMessage) {
            return new LoginResult(LoginResultType.FAILED, null, errorMessage);
        }
    }
    
    /**
     * Xử lý đăng nhập
     * 
     * Quy trình:
     * 1. Validate dữ liệu đầu vào (không được để trống)
     * 2. Thử đăng nhập với tài khoản Admin trước
     * 3. Nếu không phải Admin, thử đăng nhập với tài khoản Sinh viên
     * 4. Trả về kết quả để UI xử lý
     * 
     * @param username Tên đăng nhập (có thể là username Admin, MSSV, hoặc Email)
     * @param password Mật khẩu
     * @return LoginResult chứa kết quả đăng nhập
     */
    public LoginResult login(String username, String password) {
        // Validate dữ liệu đầu vào
        if (username == null || username.trim().isEmpty() ||
            password == null || password.isEmpty()) {
            return LoginResult.failed("Vui lòng nhập đầy đủ thông tin.");
        }
        
        String user = username.trim();
        
        // Thử đăng nhập với tài khoản Admin (PĐT) trước
        if (authService.verifyAdmin(user, password)) {
            return LoginResult.admin();
        }
        
        // Nếu không phải Admin, thử đăng nhập với tài khoản Sinh viên
        // AuthenticationService sẽ tự động tìm theo MSSV hoặc Email
        Student student = authService.loginStudent(user, password);
        
        if (student != null) {
            return LoginResult.student(student);
        }
        
        // Nếu cả hai đều thất bại
        return LoginResult.failed("Tài khoản hoặc mật khẩu không đúng.");
    }
}

