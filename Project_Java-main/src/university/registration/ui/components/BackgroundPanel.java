package university.registration.ui.components;

import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JPanel {

    public BackgroundPanel() {
        // Panel trong suốt, không vẽ nền mặc định của JPanel
        setOpaque(false);

        // Đặt layout mặc định là BorderLayout
        // Thường dùng làm panel nền chứa nhiều container con
        setLayout(new BorderLayout());
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Tạo Graphics2D để vẽ gradient
        Graphics2D g2 = (Graphics2D) g.create();

        // Bật khử răng cưa cho các đường và màu sắc mượt hơn
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // Lấy kích thước panel
        int w = getWidth(), h = getHeight();

        // Màu trên (top) là xám rất nhạt
        Color top = new Color(245, 247, 250);

        // Màu dưới (bottom) là trắng hoàn toàn
        Color bottom = Color.WHITE;

        // Tạo gradient từ trên xuống dưới
        GradientPaint gp = new GradientPaint(
                0, 0, top,      // điểm đầu (y=0) với màu "top"
                0, h, bottom    // điểm cuối (y=h) với màu "bottom"
        );

        // Set gradient làm paint chính
        g2.setPaint(gp);

        // Vẽ hình chữ nhật kín theo kích thước panel
        g2.fillRect(0, 0, w, h);

        // Giải phóng tài nguyên của Graphics2D
        g2.dispose();

        // Gọi super để giữ các behavior mặc định của JPanel
        super.paintComponent(g);
    }
}
