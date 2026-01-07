package university.registration.model;

import java.util.Date;

/**
 * Lớp cấu hình HỌC KỲ (TermSetting) - quản lý trạng thái và thông tin của một học kỳ
 * 
 * Mỗi học kỳ (ví dụ: "20252", "20251") có một TermSetting để:
 * - Quản lý trạng thái mở/đóng đăng ký học phần
 * - Lưu trữ thông tin về học kỳ (tên, năm học, ngày bắt đầu/kết thúc)
 * 
 * Dữ liệu được lưu trong Memory:
 *   Memory.termSettings: Map<term, TermSetting>
 *   Ví dụ: {"20252": TermSetting(registrationOpen=true, ...)}
 * 
 * Khi registrationOpen = true:
 * - Sinh viên có thể đăng ký học phần trong học kỳ này
 * - Admin có thể mở/đóng đăng ký bằng cách thay đổi giá trị này
 * 
 * Khi registrationOpen = false:
 * - Sinh viên không thể đăng ký học phần mới
 * - Các đăng ký đã gửi vẫn có thể được duyệt/từ chối bởi admin
 * 
 * Ví dụ sử dụng:
 *   // Tạo học kỳ mới với trạng thái mở đăng ký
 *   TermSetting term = new TermSetting(true);
 *   
 *   // Tạo học kỳ với đầy đủ thông tin
 *   Date start = new Date(2025, 0, 1);  // 1/1/2025
 *   Date end = new Date(2025, 5, 30);    // 30/6/2025
 *   TermSetting term2 = new TermSetting(
 *       true, "Học kỳ 2", "2024-2025", start, end
 *   );
 */
public class TermSetting {
    /**
     * Trạng thái mở/đóng đăng ký học phần trong học kỳ này
     * 
     * - true: Học kỳ đang mở đăng ký, sinh viên có thể đăng ký học phần mới
     * - false: Học kỳ đã đóng đăng ký, sinh viên không thể đăng ký học phần mới
     * 
     * Admin (PĐT) có thể thay đổi giá trị này để mở/đóng đăng ký theo thời gian.
     * 
     * Lưu ý: Khi đóng đăng ký, các đăng ký đã gửi trước đó vẫn có thể được duyệt/từ chối.
     */
    public boolean registrationOpen;
    
    /**
     * Tên học kỳ (ví dụ: "Học kỳ 1", "Học kỳ 2", "Học kỳ hè")
     * 
     * Thông tin này dùng để hiển thị cho người dùng, giúp dễ nhận biết học kỳ.
     * Có thể để rỗng nếu không cần thiết.
     */
    public String termName;
    
    /**
     * Năm học (ví dụ: "2024-2025", "2025-2026")
     * 
     * Thông tin này dùng để nhóm các học kỳ theo năm học.
     * Có thể để rỗng nếu không cần thiết.
     */
    public String academicYear;
    
    /**
     * Ngày bắt đầu của học kỳ
     * 
     * Dùng để xác định thời điểm học kỳ bắt đầu.
     * Có thể null nếu không cần thiết.
     * 
     * Kết hợp với endDate để kiểm tra học kỳ có đang diễn ra không (phương thức isActive()).
     */
    public Date startDate;
    
    /**
     * Ngày kết thúc của học kỳ
     * 
     * Dùng để xác định thời điểm học kỳ kết thúc.
     * Có thể null nếu không cần thiết.
     * 
     * Kết hợp với startDate để kiểm tra học kỳ có đang diễn ra không (phương thức isActive()).
     */
    public Date endDate;

    /**
     * Constructor đơn giản: tạo TermSetting chỉ với trạng thái mở/đóng đăng ký
     * 
     * Các thông tin khác (termName, academicYear, startDate, endDate) sẽ được
     * gán giá trị mặc định (rỗng hoặc null).
     * 
     * @param open Trạng thái mở đăng ký (true = mở, false = đóng)
     * 
     * Ví dụ:
     *   TermSetting term = new TermSetting(true);  // Mở đăng ký
     */
    public TermSetting(boolean open) {
        // Gán trạng thái mở/đóng đăng ký
        registrationOpen = open;
        
        // Gán giá trị mặc định cho các trường khác
        this.termName = "";
        this.academicYear = "";
        this.startDate = null;
        this.endDate = null;
    }
    
    /**
     * Constructor đầy đủ: tạo TermSetting với tất cả thông tin
     * 
     * @param open Trạng thái mở đăng ký (true = mở, false = đóng)
     * @param termName Tên học kỳ (ví dụ: "Học kỳ 1") - có thể null (sẽ chuyển thành "")
     * @param academicYear Năm học (ví dụ: "2024-2025") - có thể null (sẽ chuyển thành "")
     * @param startDate Ngày bắt đầu học kỳ - có thể null
     * @param endDate Ngày kết thúc học kỳ - có thể null
     * 
     * Ví dụ:
     *   Date start = new Date(2025, 0, 1);  // 1/1/2025
     *   Date end = new Date(2025, 5, 30);    // 30/6/2025
     *   TermSetting term = new TermSetting(
     *       true, "Học kỳ 2", "2024-2025", start, end
     *   );
     */
    public TermSetting(boolean open, String termName, String academicYear, Date startDate, Date endDate) {
        // Gán trạng thái mở/đóng đăng ký
        this.registrationOpen = open;
        
        // Gán tên học kỳ (nếu null thì chuyển thành chuỗi rỗng)
        this.termName = termName != null ? termName : "";
        
        // Gán năm học (nếu null thì chuyển thành chuỗi rỗng)
        this.academicYear = academicYear != null ? academicYear : "";
        
        // Gán ngày bắt đầu (có thể null)
        this.startDate = startDate;
        
        // Gán ngày kết thúc (có thể null)
        this.endDate = endDate;
    }
    
    /**
     * Kiểm tra học kỳ có đang hoạt động (active) không
     * 
     * Học kỳ được coi là đang hoạt động khi:
     * - registrationOpen = true (đang mở đăng ký)
     * - VÀ ngày hiện tại chưa vượt quá endDate (nếu endDate không null)
     * 
     * Nếu endDate = null, chỉ cần kiểm tra registrationOpen.
     * 
     * @return true nếu học kỳ đang hoạt động, false nếu đã kết thúc hoặc đã đóng đăng ký
     * 
     * Ví dụ:
     *   TermSetting term = new TermSetting(true, "Học kỳ 2", "2024-2025", start, end);
     *   if (term.isActive()) {
     *       System.out.println("Học kỳ đang diễn ra");
     *   }
     */
    public boolean isActive() {
        // Nếu không có ngày kết thúc, chỉ cần kiểm tra trạng thái mở/đóng
        if (endDate == null) {
            return registrationOpen;
        }
        
        // Kiểm tra: ngày hiện tại phải trước endDate VÀ đang mở đăng ký
        Date now = new Date();
        return now.before(endDate) && registrationOpen;
    }
}
