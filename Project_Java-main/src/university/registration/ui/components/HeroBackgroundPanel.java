package university.registration.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Random;

public class HeroBackgroundPanel extends JPanel {

    // Texture nền được tạo trước để tái sử dụng, tránh phải tính toán lại
    private final BufferedImage texture;

    public HeroBackgroundPanel() {
        setOpaque(false);              // Panel không vẽ nền mặc định
        setLayout(new BorderLayout()); // Cho phép chứa các component con

        // Tạo texture nền một lần duy nhất khi khởi tạo
        texture = createTexture();
    }

    /**
     * Tạo background hero với hiệu ứng gradient + ánh sáng mờ.
     * Ảnh kích thước lớn 1920x1080, sau đó được scale theo panel.
     */
    private BufferedImage createTexture() {

        int width = 1920;
        int height = 1080;

        // Ảnh nền để vẽ texture
        BufferedImage image =
                new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2 = image.createGraphics();

        // Bật khử răng cưa cho gradient mượt hơn
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // ======= 1. VẼ GRADIENT NỀN (XANH DƯƠNG → XANH LỤC) ========
        GradientPaint gp = new GradientPaint(
                0, 0,
                new Color(8, 86, 145),      // xanh dương đậm phía trên
                0, height,
                new Color(3, 130, 92)       // xanh lục đậm phía dưới
        );

        g2.setPaint(gp);
        g2.fillRect(0, 0, width, height);

        // ======= 2. SINH NGẪU NHIÊN VÒNG SÁNG (BOKEH LIGHTS) ========

        // Random có seed cố định để texture luôn giống nhau giữa các lần chạy
        Random rnd = new Random("BACHKHOA".hashCode());

        // Bảng màu mờ mờ để vẽ các ánh sáng
        Color[] palette = {
                new Color(255, 211, 0, 140),    // vàng mờ
                new Color(0, 173, 181, 130),    // xanh cyan
                new Color(255, 255, 255, 120),  // trắng mờ
                new Color(52, 138, 199, 120)    // xanh dương nhạt
        };

        // Tạo 18 vòng sáng mờ với kích thước và vị trí ngẫu nhiên
        for (int i = 0; i < 18; i++) {

            int size = 200 + rnd.nextInt(260); // kích thước ngẫu nhiên 200–460px
            int x = rnd.nextInt(width + size) - size / 2;
            int y = rnd.nextInt(height + size) - size / 2;

            // Chọn màu ngẫu nhiên trong bảng màu
            Color base = palette[rnd.nextInt(palette.length)];

            // Radial gradient: tâm sáng → rìa mờ dần
            RadialGradientPaint rg = new RadialGradientPaint(
                    new Point2D.Float(x + size / 2f, y + size / 2f), // tâm
                    size / 2f,                                      // bán kính
                    new float[]{0f, 1f},                            // 0% → 100%
                    new Color[]{
                            new Color(base.getRed(), base.getGreen(), base.getBlue(), 160), // màu đậm
                            new Color(base.getRed(), base.getGreen(), base.getBlue(), 0)   // tan biến
                    }
            );

            g2.setPaint(rg);
            g2.fillOval(x, y, size, size);
        }

        // Giải phóng graphics
        g2.dispose();

        return image;
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        // Vẽ texture lên panel và scale nó theo kích thước panel hiện tại
        g2.drawImage(texture, 0, 0, getWidth(), getHeight(), this);

        // Phủ lớp overlay tối (dark overlay) để tạo độ tương phản và chiều sâu
        g2.setColor(new Color(0, 23, 41, 140)); // đen xanh, độ mờ 140/255
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.dispose();

        // Vẽ các component con phía trên texture
        super.paintComponent(g);
    }
}
