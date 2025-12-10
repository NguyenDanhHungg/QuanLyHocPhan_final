package university.registration.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class UI {

    /**
     * Tạo label tiêu đề lớn cho các màn hình.
     * Font: Segoe UI 32px, bold.
     */
    public static JLabel title(String txt){
        JLabel l = new JLabel(txt);
        l.setFont(new Font("Segoe UI", Font.BOLD, 32));
        return l;
    }

    /**
     * Tạo một TextField chuẩn cho toàn bộ UI.
     * Font Segoe UI 18px.
     * Dùng nhiều ở LoginFrame.
     */
    public static JTextField field(){
        JTextField f = new JTextField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        return f;
    }

    /**
     * Footer chung cho mọi frame.
     * Hiển thị bản quyền + hotline + version portal.
     */
    public static JPanel footer(){
        JPanel p = new JPanel(new BorderLayout());

        // Label trái: thông tin trường + hotline
        JLabel left = new JLabel(
                "© 2025 Trường Đại học An Giang • Trung tâm CNTT – Hotline: 0296 3831 265"
        );
        left.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        left.setForeground(new Color(255, 255, 255, 230));   // trắng gần như full

        // Label phải: phiên bản sản phẩm
        JLabel right = new JLabel("Cổng dịch vụ sinh viên AGU • v1.0");
        right.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        right.setForeground(new Color(255, 255, 255, 200));  // trắng mờ hơn một chút

        // Đặt 2 label ở 2 đầu panel
        p.add(left, BorderLayout.WEST);
        p.add(right, BorderLayout.EAST);

        // Padding trên/dưới/trái/phải
        p.setBorder(new EmptyBorder(8, 24, 12, 24));

        p.setOpaque(false); // trong suốt để nền HeroBackground hiện phía sau
        return p;
    }
}
