package university.registration;

import university.registration.store.Memory;
import university.registration.ui.LoginFrame;
import university.registration.util.LookAndFeelUtil;

import javax.swing.*;

public class App {

    /**
     * Entry point chính của ứng dụng.
     */
    public static void main(String[] args) {

        // Chạy UI trong Event Dispatch Thread – tiêu chuẩn của Swing
        SwingUtilities.invokeLater(() -> {

            // 1. Thiết lập Look & Feel (FlatLaf + font + bo góc + UI style)
            LookAndFeelUtil.setupLookAndFeel();

            // 2. Khởi tạo dữ liệu mặc định toàn bộ hệ thống
            //    (course, offering, student demo, admin...)
            Memory.init();

            // 3. Mở màn hình đăng nhập đầu tiên
            new LoginFrame();
        });
    }
}
