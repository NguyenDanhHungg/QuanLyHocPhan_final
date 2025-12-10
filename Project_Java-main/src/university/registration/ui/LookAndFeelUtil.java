package university.registration.util;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;

public class LookAndFeelUtil {

    /** Thiết lập giao diện chung cho toàn bộ ứng dụng */
    public static void setupLookAndFeel() {
        try {
            // ---------------------------------------------
            // 1. NẠP FLATLAF THEME
            // ---------------------------------------------
            // Load class FlatMacLightLaf (một theme của FlatLaf)
            Class<?> flatLight = Class.forName("com.formdev.flatlaf.themes.FlatMacLightLaf");

            // Gọi phương thức static FlatMacLightLaf.setup()
            flatLight.getMethod("setup").invoke(null);

            // ---------------------------------------------
            // 2. TÙY CHỈNH BO GÓC & FONT TOÀN CỤC
            // ---------------------------------------------

            // Bo góc mặc định của mọi component
            UIManager.put("Component.arc", 16);

            // Bo góc của Button
            UIManager.put("Button.arc", 18);

            // Bo góc cho TextField / PasswordField
            UIManager.put("TextComponent.arc", 16);

            // Font mặc định toàn bộ ứng dụng = Segoe UI, size 18
            UIManager.put(
                    "defaultFont",
                    new FontUIResource("Segoe UI", Font.PLAIN, 18)
            );

            // ---------------------------------------------
            // 3. TÙY BIẾN BẢNG (JTable)
            // ---------------------------------------------

            // Hiển thị đường ngang
            UIManager.put("Table.showHorizontalLines", Boolean.TRUE);

            // Ẩn đường dọc → bảng nhìn hiện đại hơn
            UIManager.put("Table.showVerticalLines", Boolean.FALSE);

            // Khoảng cách giữa các ô
            UIManager.put("Table.intercellSpacing", new Dimension(0, 1));

            // Màu nền xen kẽ giữa các dòng
            UIManager.put("Table.alternateRowColor", new Color(248, 250, 252));

            // ---------------------------------------------
            // 4. TÙY BIẾN SCROLLBAR
            // ---------------------------------------------

            // Bo góc track và thumb cực cong (999 = hình tròn)
            UIManager.put("ScrollBar.trackArc", 999);
            UIManager.put("ScrollBar.thumbArc", 999);

            // Chiều rộng scrollbar
            UIManager.put("ScrollBar.width", 14);

            // ---------------------------------------------
            // 5. TÙY CHỈNH VIỀN & FOCUS
            // ---------------------------------------------

            // Độ rộng viền khi component được focus
            UIManager.put("Component.focusWidth", 1);

            // Không thêm “inner focus ring”
            UIManager.put("Component.innerFocusWidth", 0);

            // Màu nền của button khi được focus
            UIManager.put("Button.focusedBackground", new Color(250, 250, 250));

            // Màu viền mặc định của các component
            UIManager.put("Component.borderColor", new Color(220, 224, 230));

        } catch (Throwable ignore) {
            // Nếu FlatLaf không load được (thiếu lib) → fallback system LAF
            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName()
                );
            } catch (Exception ignored) {}
        }
    }
}
