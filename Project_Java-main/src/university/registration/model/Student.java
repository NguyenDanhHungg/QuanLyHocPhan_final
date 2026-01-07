package university.registration.model;

/**
 * Lớp biểu diễn SINH VIÊN (Student) trong hệ thống đăng ký học phần
 * 
 * Mỗi đối tượng Student chứa thông tin cá nhân và tài khoản của một sinh viên,
 * bao gồm: mã số sinh viên, họ tên, ngày sinh, địa chỉ, email, chương trình đào tạo và mật khẩu.
 * 
 * Hầu hết thông tin là final (không thể thay đổi) để đảm bảo tính nhất quán dữ liệu.
 * Chỉ có mật khẩu là có thể thay đổi (để sinh viên có thể đổi mật khẩu).
 * 
 * Dữ liệu được lưu trữ trong Memory:
 *   Memory.studentsById: Map<MSSV, Student> - tra cứu sinh viên theo MSSV
 *   Memory.emailIndex: Map<email_lowercase, MSSV> - tra cứu sinh viên theo email
 * 
 * Ví dụ sử dụng:
 *   Student student = new Student(
 *       "SV001", "Nguyễn Văn A", "2004-01-01", "Hà Nội",
 *       "sv001@university.edu", "Kỹ thuật Điện tử - Viễn thông 2021"
 *   );
 *   student.password = "sv123";  // Gán mật khẩu sau khi tạo
 */
public class Student {
    /**
     * Mã số sinh viên (MSSV) - định danh duy nhất của sinh viên
     * 
     * MSSV là khóa chính (primary key) để nhận diện sinh viên trong hệ thống.
     * Mỗi sinh viên có một MSSV duy nhất, không trùng lặp.
     * 
     * Thuộc tính final: không thể thay đổi sau khi khởi tạo
     * (đảm bảo tính nhất quán dữ liệu, tránh nhầm lẫn giữa các sinh viên)
     */
    public final String studentId;
    
    /**
     * Họ và tên đầy đủ của sinh viên
     * 
     * Thuộc tính final: không thể thay đổi sau khi khởi tạo
     */
    public final String fullName;
    
    /**
     * Ngày sinh của sinh viên (định dạng chuỗi "yyyy-MM-dd", ví dụ: "2004-01-01")
     * 
     * Thuộc tính final: không thể thay đổi sau khi khởi tạo
     */
    public final String dob;
    
    /**
     * Địa chỉ liên hệ của sinh viên
     * 
     * Thuộc tính final: không thể thay đổi sau khi khởi tạo
     */
    public final String address;
    
    /**
     * Email của sinh viên (dùng để đăng nhập và liên hệ)
     * 
     * Email phải là duy nhất trong hệ thống (không được trùng với sinh viên khác).
     * Hệ thống hỗ trợ đăng nhập bằng cả MSSV và Email.
     * 
     * Email được lưu trong Memory.emailIndex để tra cứu nhanh.
     * 
     * Thuộc tính final: không thể thay đổi sau khi khởi tạo
     */
    public final String email;
    
    /**
     * Chương trình đào tạo (CTĐT) mà sinh viên đang theo học
     * 
     * Ví dụ: "Kỹ thuật Điện tử - Viễn thông 2021", "Công nghệ Thông tin 2021"
     * 
     * CTĐT được dùng để:
     * - Kiểm tra quyền đăng ký học phần (một số học phần chỉ dành cho CTĐT cụ thể)
     * - Lọc danh sách sinh viên theo khoa/viện trong màn hình duyệt đăng ký
     * 
     * Thuộc tính final: không thể thay đổi sau khi khởi tạo
     */
    public final String program;

    /**
     * Mật khẩu đăng nhập của sinh viên
     * 
     * Mật khẩu KHÔNG phải final vì sinh viên cần có thể đổi mật khẩu.
     * 
     * Lưu ý: Trong hệ thống thực tế, mật khẩu nên được hash (băm) trước khi lưu,
     * không nên lưu mật khẩu dạng plain text như trong code này (đây chỉ là demo).
     * 
     * Mật khẩu được gán sau khi tạo đối tượng Student (không có trong constructor).
     */
    public String password;

    /**
     * Constructor: tạo một đối tượng Student mới
     * 
     * Lưu ý: Constructor này KHÔNG gán mật khẩu.
     * Mật khẩu sẽ được gán sau bằng cách: student.password = "matkhau";
     * 
     * @param id Mã số sinh viên (MSSV) - không được null hoặc rỗng, phải là duy nhất
     * @param name Họ tên đầy đủ của sinh viên
     * @param dob Ngày sinh (định dạng "yyyy-MM-dd")
     * @param addr Địa chỉ liên hệ
     * @param email Email của sinh viên - phải là duy nhất, không được trùng với sinh viên khác
     * @param program Chương trình đào tạo (tên đầy đủ, ví dụ: "Kỹ thuật Điện tử - Viễn thông 2021")
     * 
     * Ví dụ:
     *   Student student = new Student(
     *       "SV001", "Nguyễn Văn A", "2004-01-01", "Hà Nội",
     *       "sv001@university.edu", "Kỹ thuật Điện tử - Viễn thông 2021"
     *   );
     *   student.password = "sv123";  // Gán mật khẩu sau khi tạo
     */
    public Student(String id, String name, String dob, String addr, String email, String program) {
        // Gán mã số sinh viên
        this.studentId = id;
        
        // Gán họ tên đầy đủ
        this.fullName = name;
        
        // Gán ngày sinh
        this.dob = dob;
        
        // Gán địa chỉ liên hệ
        this.address = addr;
        
        // Gán email
        this.email = email;
        
        // Gán chương trình đào tạo
        this.program = program;
        
        // Lưu ý: password chưa được gán ở đây
        // Mật khẩu sẽ được gán sau bằng cách: student.password = "matkhau";
    }
}
