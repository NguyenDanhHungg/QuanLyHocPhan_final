package university.registration.ui.components;

import javax.swing.*;
import java.awt.*;

public class NeutralButton extends JButton {

    // Độ cong của góc nút (bo tròn)
    private final int arc = 18;

    public NeutralButton(String text){
        super(text); // Gọi constructor JButton để đặt text

        // Font chữ hiện đại
        setFont(new Font("Segoe UI", Font.BOLD, 18));

        // Màu chữ đen trung tính
        setForeground(Color.BLACK);

        // Không dùng nền mặc định của JButton
        setOpaque(false);
        setContentAreaFilled(false);

        // Tắt viền focus xanh lam xấu xí của Swing
        setFocusPainted(false);

        // Padding bên trong nút (trên, trái, dưới, phải)
        setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));

        // Cursor chuyển thành hình bàn tay khi hover
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Kích thước đề xuất
        setPreferredSize(new Dimension(180, 44));

        // Loại bỏ kiểu nút đặc biệt của FlatLaf (nếu sử dụng)
        putClientProperty("JButton.buttonType", null);
    }

    @Override
    protected void paintComponent(Graphics g){

        // Tạo Graphics2D để vẽ mượt
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // ======= VẼ NỀN TRẮNG BO GÓC =======
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, w - 1, h - 1, arc, arc);

        // ======= VẼ VIỀN XÁM NHẠT =======
        g2.setColor(new Color(180, 180, 180));
        g2.drawRoundRect(0, 0, w - 1, h - 1, arc, arc);

        g2.dispose();

        // Vẽ text & icon của JButton (nằm trên nền vừa vẽ)
        super.paintComponent(g);
    }
}
