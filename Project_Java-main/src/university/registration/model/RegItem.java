package university.registration.model;

/**
 * Lớp biểu diễn một "mục đăng ký" (RegItem) - một học phần mà sinh viên đã đăng ký trong một học kỳ
 * 
 * Mỗi RegItem đại diện cho một bản ghi đăng ký học phần của một sinh viên,
 * chứa thông tin về học phần đã đăng ký, ngày đăng ký và trạng thái đăng ký.
 * 
 * Trạng thái đăng ký (status) có thể là:
 * - "Tạm": Sinh viên đã thêm vào giỏ nhưng chưa gửi đăng ký
 * - "Đã gửi": Sinh viên đã gửi đăng ký, đang chờ PĐT duyệt
 * - "Chờ xử lý": Tương đương "Đã gửi", đang chờ PĐT xử lý
 * - "Đã duyệt": PĐT đã duyệt, sinh viên được học học phần này
 * - "Đã từ chối": PĐT đã từ chối đăng ký này
 * 
 * Cấu trúc dữ liệu trong Memory:
 *   Memory.regs = {
 *     "SV001": {                    // MSSV
 *       "20252": [                  // Học kỳ
 *         RegItem(course=CT101, date="2025-01-15", status="Đã duyệt"),
 *         RegItem(course=MA101, date="2025-01-15", status="Đã gửi")
 *       ]
 *     }
 *   }
 * 
 * Ví dụ sử dụng:
 *   Course course = new Course("CT101", "Lập trình cơ bản", 3);
 *   RegItem item = new RegItem(course, "2025-01-15", "Đã duyệt");
 *   System.out.println(item.course.name);  // In ra: Lập trình cơ bản
 *   System.out.println(item.status);       // In ra: Đã duyệt
 */
public class RegItem {
    /**
     * Học phần mà sinh viên đã đăng ký
     * 
     * Thuộc tính final: không thể thay đổi sau khi khởi tạo.
     * Điều này đảm bảo một RegItem luôn gắn với một Course cụ thể,
     * không thể thay đổi học phần sau khi đã tạo đăng ký.
     * 
     * Nếu cần thay đổi học phần, phải xóa RegItem cũ và tạo RegItem mới.
     */
    public final Course course;
    
    /**
     * Ngày đăng ký học phần (định dạng chuỗi "yyyy-MM-dd", ví dụ: "2025-01-15")
     * 
     * Ngày này được tự động gán khi sinh viên thêm học phần vào đăng ký
     * hoặc khi gửi đăng ký (nếu đã có sẵn thì cập nhật lại ngày).
     * 
     * Có thể thay đổi: khi sinh viên gửi lại đăng ký, ngày sẽ được cập nhật
     */
    public String date;
    
    /**
     * Trạng thái đăng ký học phần
     * 
     * Các giá trị có thể:
     * - "Tạm": Sinh viên đã thêm vào giỏ nhưng chưa gửi (có thể xóa)
     * - "Đã gửi": Sinh viên đã gửi, đang chờ PĐT duyệt
     * - "Chờ xử lý": Tương đương "Đã gửi" (được chuẩn hóa từ "Tạm" hoặc "Đã gửi")
     * - "Đã duyệt": PĐT đã duyệt, sinh viên được học
     * - "Đã từ chối": PĐT đã từ chối đăng ký
     * 
     * Có thể thay đổi: trạng thái được cập nhật bởi sinh viên (gửi) hoặc admin (duyệt/từ chối)
     */
    public String status;

    /**
     * Constructor: tạo một đối tượng RegItem mới
     * 
     * @param c Học phần (Course) mà sinh viên đăng ký - không được null
     * @param d Ngày đăng ký (định dạng "yyyy-MM-dd", ví dụ: "2025-01-15")
     * @param s Trạng thái đăng ký ("Tạm", "Đã gửi", "Đã duyệt", "Đã từ chối", v.v.)
     * 
     * Ví dụ:
     *   Course course = new Course("CT101", "Lập trình cơ bản", 3);
     *   RegItem item = new RegItem(course, "2025-01-15", "Tạm");
     */
    public RegItem(Course c, String d, String s) {
        // Gán học phần cho mục đăng ký (không thể thay đổi sau này)
        course = c;
        
        // Gán ngày đăng ký
        date = d;
        
        // Gán trạng thái đăng ký
        status = s;
    }
}

