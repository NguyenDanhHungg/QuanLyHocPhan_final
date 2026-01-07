package university.registration.model;

/**
 * Lớp biểu diễn MÔN HỌC (Course) trong hệ thống đăng ký học phần
 * 
 * Mỗi đối tượng Course đại diện cho một học phần trong chương trình đào tạo,
 * chứa thông tin cơ bản: mã học phần, tên học phần và số tín chỉ.
 * 
 * Các thuộc tính đều là final (hằng số) để đảm bảo tính bất biến (immutable):
 * - Một khi đã tạo Course, không thể thay đổi thông tin của nó
 * - Điều này giúp tránh lỗi khi nhiều nơi cùng tham chiếu đến cùng một Course
 * 
 * Ví dụ sử dụng:
 *   Course course = new Course("CT101", "Lập trình cơ bản", 3);
 *   System.out.println(course.code);    // In ra: CT101
 *   System.out.println(course.name);    // In ra: Lập trình cơ bản
 *   System.out.println(course.credits); // In ra: 3
 */
public class Course {
    /**
     * Mã học phần (ví dụ: "CT101", "MA101", "PE2101")
     * 
     * Mã học phần thường tuân theo quy tắc đặt tên của trường:
     * - 2-3 chữ cái đầu: viết tắt của khoa/bộ môn (CT = Công nghệ thông tin, MA = Toán, PE = Thể dục)
     * - Số tiếp theo: mã số của học phần
     * 
     * Thuộc tính final: không thể thay đổi sau khi khởi tạo
     */
    public final String code;
    
    /**
     * Tên đầy đủ của học phần (ví dụ: "Lập trình cơ bản", "Giải tích 1")
     * 
     * Thuộc tính final: không thể thay đổi sau khi khởi tạo
     */
    public final String name;
    
    /**
     * Số tín chỉ của học phần (ví dụ: 1, 2, 3, 4, 5)
     * 
     * Tín chỉ là đơn vị đo khối lượng học tập của một học phần.
     * Thông thường, một học phần có từ 1 đến 5 tín chỉ.
     * 
     * Thuộc tính final: không thể thay đổi sau khi khởi tạo
     */
    public final int credits;
    
    /**
     * Constructor: tạo một đối tượng Course mới
     * 
     * @param c Mã học phần (code) - không được null hoặc rỗng
     * @param n Tên học phần (name) - không được null hoặc rỗng
     * @param cr Số tín chỉ (credits) - phải là số dương (thường từ 1 đến 5)
     * 
     * Ví dụ:
     *   Course course = new Course("CT101", "Lập trình cơ bản", 3);
     */
    public Course(String c, String n, int cr) {
        // Gán giá trị mã học phần
        code = c;
        
        // Gán giá trị tên học phần
        name = n;
        
        // Gán giá trị số tín chỉ
        credits = cr;
    }
}
