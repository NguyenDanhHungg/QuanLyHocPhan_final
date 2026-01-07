package university.registration;

import university.registration.store.Memory;
import university.registration.ui.LoginFrame;
import university.registration.util.LookAndFeelUtil;

import javax.swing.*;

/**
 * Lớp App - Entry point (điểm khởi đầu) của ứng dụng Đăng ký Học phần
 * 
 * Đây là lớp chính chứa phương thức main() - nơi chương trình bắt đầu chạy.
 * 
 * Quy trình khởi động ứng dụng:
 * 1. Thiết lập giao diện (Look & Feel) - sử dụng FlatLaf theme
 * 2. Khởi tạo dữ liệu demo (học phần, học kỳ, sinh viên mẫu, tài khoản admin)
 * 3. Hiển thị màn hình đăng nhập (LoginFrame)
 * 
 * Lưu ý về Swing:
 * - Tất cả các thao tác với UI phải được thực hiện trong Event Dispatch Thread (EDT)
 * - SwingUtilities.invokeLater() đảm bảo code chạy trong EDT
 * - Nếu không, có thể gây ra lỗi thread-safety và UI không hiển thị đúng
 * 
 * Ví dụ chạy ứng dụng:
 *   java university.registration.App
 * 
 * Hoặc từ IDE: Run App.main()
 */
public class App {

    /**
     * Phương thức main - Entry point của ứng dụng
     * 
     * Đây là phương thức đầu tiên được gọi khi chạy chương trình.
     * 
     * @param args Tham số dòng lệnh (command-line arguments) - không sử dụng trong ứng dụng này
     * 
     * Quy trình khởi động:
     * 1. Thiết lập Look & Feel (giao diện):
     *    - Load FlatLaf theme (FlatMacLightLaf)
     *    - Cấu hình font, bo góc, style cho các component
     *    - Nếu FlatLaf không có, fallback về System Look & Feel
     * 
     * 2. Khởi tạo dữ liệu demo:
     *    - Tạo tài khoản admin mặc định (username: "pdt", password: "pdt123")
     *    - Thêm các chương trình đào tạo (CTĐT)
     *    - Thêm các học kỳ (ví dụ: "20252", "20251", "20242")
     *    - Thêm hàng trăm học phần (Course) vào hệ thống
     *    - Mở lớp (Offering) cho tất cả học phần trong học kỳ mới nhất
     *    - Tạo sinh viên demo (MSSV: "SV001", password: "sv123")
     * 
     * 3. Hiển thị màn hình đăng nhập:
     *    - Tạo và hiển thị LoginFrame
     *    - Người dùng có thể đăng nhập bằng:
     *      + Tài khoản admin (username: "pdt", password: "pdt123")
     *      + Tài khoản sinh viên (MSSV: "SV001", password: "sv123" hoặc email: "sv001@university.edu")
     * 
     * Lưu ý về Thread Safety:
     * - SwingUtilities.invokeLater() đảm bảo code chạy trong Event Dispatch Thread (EDT)
     * - Tất cả thao tác với UI (tạo frame, hiển thị component) phải chạy trong EDT
     * - Nếu không, có thể gây ra lỗi "Not on EDT" và UI không hiển thị đúng
     */
    public static void main(String[] args) {
        // Chạy UI trong Event Dispatch Thread (EDT) - tiêu chuẩn của Swing
        // invokeLater() đảm bảo code bên trong được thực thi trong EDT,
        // tránh lỗi thread-safety và đảm bảo UI hoạt động đúng
        SwingUtilities.invokeLater(() -> {
            // ========== BƯỚC 1: THIẾT LẬP GIAO DIỆN ==========
            // Thiết lập Look & Feel (theme) cho toàn bộ ứng dụng
            // - Load FlatLaf theme (FlatMacLightLaf - theme sáng, hiện đại)
            // - Cấu hình font mặc định (Segoe UI, size 18)
            // - Cấu hình bo góc cho các component (Button, TextField, v.v.)
            // - Cấu hình style cho Table, ScrollBar, v.v.
            // - Nếu FlatLaf không có, tự động fallback về System Look & Feel
            LookAndFeelUtil.setupLookAndFeel();

            // ========== BƯỚC 2: KHỞI TẠO DỮ LIỆU DEMO ==========
            // Khởi tạo dữ liệu mặc định cho toàn bộ hệ thống:
            // - Tài khoản admin: username="pdt", password="pdt123"
            // - Danh sách chương trình đào tạo (CTĐT)
            // - Danh sách học kỳ (ví dụ: "20252", "20251", "20242")
            // - Hàng trăm học phần (Course) thuộc các loại khác nhau:
            //   + GDTC (Giáo dục thể chất)
            //   + QP-AN (Quốc phòng An ninh)
            //   + Ngoại ngữ
            //   + Lý luận chính trị
            //   + Toán-KHCB
            //   + Cơ sở ngành
            //   + Mô đun chuyên ngành
            // - Mở lớp (Offering) cho tất cả học phần trong học kỳ mới nhất
            // - Tạo sinh viên demo: MSSV="SV001", password="sv123", email="sv001@university.edu"
            Memory.init();

            // ========== BƯỚC 3: HIỂN THỊ MÀN HÌNH ĐĂNG NHẬP ==========
            // Tạo và hiển thị màn hình đăng nhập (LoginFrame)
            // Người dùng có thể:
            // - Đăng nhập bằng tài khoản admin (PĐT)
            // - Đăng nhập bằng tài khoản sinh viên (MSSV hoặc Email)
            // - Tạo tài khoản sinh viên mới
            new LoginFrame();
        });
    }
}
