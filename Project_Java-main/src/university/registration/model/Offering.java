package university.registration.model;

/**
 * Lớp biểu diễn "đợt mở lớp" hoặc cấu hình mở lớp cho một học phần trong một học kỳ cụ thể
 * 
 * Mỗi học phần (Course) có thể được mở lớp ở nhiều học kỳ khác nhau.
 * Mỗi lần mở lớp, cần có một Offering để cấu hình:
 * - Trạng thái mở/đóng đăng ký
 * - Chương trình đào tạo nào được phép đăng ký học phần này
 * 
 * Ví dụ:
 * - Học kỳ 20252: Course "CT101" có Offering {open=true, allowedProgram="Tất cả"}
 *   → Tất cả sinh viên đều có thể đăng ký
 * - Học kỳ 20252: Course "ET4010" có Offering {open=true, allowedProgram="Kỹ thuật Điện tử - Viễn thông 2021"}
 *   → Chỉ sinh viên thuộc chương trình "Kỹ thuật Điện tử - Viễn thông 2021" mới được đăng ký
 * 
 * Lưu ý: Offering không phải là immutable (có thể thay đổi open và allowedProgram)
 * vì admin cần có thể cập nhật cấu hình mở lớp theo thời gian.
 */
public class Offering {
    /**
     * Trạng thái mở/đóng đăng ký cho học phần này
     * 
     * - true: Học phần đang mở đăng ký, sinh viên có thể đăng ký
     * - false: Học phần đã đóng đăng ký, sinh viên không thể đăng ký
     * 
     * Admin có thể thay đổi giá trị này để mở/đóng đăng ký cho học phần
     */
    public boolean open;
    
    /**
     * Chương trình đào tạo được phép đăng ký học phần này
     * 
     * Giá trị có thể là:
     * - "Tất cả": Tất cả sinh viên thuộc mọi chương trình đào tạo đều có thể đăng ký
     * - Tên chương trình cụ thể: Chỉ sinh viên thuộc chương trình đó mới được đăng ký
     *   (ví dụ: "Kỹ thuật Điện tử - Viễn thông 2021", "Công nghệ Thông tin 2021")
     * 
     * Khi sinh viên đăng ký, hệ thống sẽ kiểm tra:
     * - Nếu allowedProgram = "Tất cả" → cho phép tất cả
     * - Nếu allowedProgram = tên CTĐT cụ thể → chỉ cho phép sinh viên có program trùng khớp
     */
    public String allowedProgram;

    /**
     * Constructor: tạo một đối tượng Offering mới
     * 
     * @param o Trạng thái mở đăng ký (true = mở, false = đóng)
     * @param ap Chương trình đào tạo được phép đăng ký ("Tất cả" hoặc tên CTĐT cụ thể)
     * 
     * Ví dụ:
     *   // Mở lớp cho tất cả sinh viên
     *   Offering offering1 = new Offering(true, "Tất cả");
     *   
     *   // Mở lớp chỉ cho sinh viên ngành Điện tử - Viễn thông
     *   Offering offering2 = new Offering(true, "Kỹ thuật Điện tử - Viễn thông 2021");
     *   
     *   // Đóng lớp (không cho đăng ký)
     *   Offering offering3 = new Offering(false, "Tất cả");
     */
    public Offering(boolean o, String ap) {
        // Gán trạng thái mở/đóng đăng ký
        open = o;
        
        // Gán chương trình đào tạo được phép đăng ký
        allowedProgram = ap;
    }
}
