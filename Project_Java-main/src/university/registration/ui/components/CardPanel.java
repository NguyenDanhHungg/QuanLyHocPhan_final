package university.registration.ui.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CardPanel extends JPanel {

    // Độ cong góc (bo tròn)
    int radius = 20;

    public CardPanel() {
        setOpaque(false); // Không vẽ nền mặc định của JPanel (để tự vẽ nền trắng bo góc)

        // Thêm khoảng trống bên trong để nội dung không dính sát mép panel
        // top=28, left=36, bottom=28, right=36
        setBorder(new EmptyBorder(28, 36, 28, 36));
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Tạo đối tượng Graphics2D để vẽ mượt mà hơn
        Graphics2D g2 = (Graphics2D) g.create();

        // Bật khử răng cưa (anti-aliasing) cho đường viền và bo góc mượt hơn
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // ===== VẼ NỀN TRẮNG BO GÓC =====
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(
                0, 0,
                getWidth() - 1, getHeight() - 1,
                radius, radius     // độ cong góc
        );

        // ===== VẼ VIỀN XÁM NHẠT =====
        g2.setColor(new Color(220, 220, 220)); // màu viền
        g2.drawRoundRect(
                0, 0,
                getWidth() - 1, getHeight() - 1,
                radius, radius
        );

        // Giải phóng đồ họa
        g2.dispose();

        // Vẽ các component con của panel
        super.paintComponent(g);
    }
}
