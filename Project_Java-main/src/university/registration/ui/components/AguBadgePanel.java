package university.registration.ui.components;

import javax.swing.*;
import java.awt.*;

public class AguBadgePanel extends JPanel {

    public AguBadgePanel() {
        setOpaque(false); // Panel trong suốt, để thấy được nền phía sau
        setPreferredSize(new Dimension(120, 120)); // Kích thước đề xuất của badge
    }

    @Override
    protected void paintComponent(Graphics g) {

        // Tạo bản sao của Graphics và ép kiểu thành Graphics2D để vẽ nâng cao
        Graphics2D g2 = (Graphics2D) g.create();

        // Bật khử răng cưa cho hình vẽ mượt hơn
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // ======== TÍNH TOÁN KÍCH THƯỚC VÀ TỌA ĐỘ ========

        // Lấy kích thước nhỏ nhất để vẽ hình tròn không bị méo
        int size = Math.min(getWidth(), getHeight());

        // Tọa độ để hình tròn nằm giữa panel
        int x = (getWidth() - size) / 2;
        int y = (getHeight() - size) / 2;

        // ======== VẼ VÒNG TRÒN NGOÀI (MÀU VÀNG) ========
        g2.setColor(new Color(253, 219, 17)); // vàng AGU
        g2.fillOval(x, y, size, size);        // tô hình tròn

        // ======== VẼ VIỀN XANH BÊN NGOÀI ========
        g2.setStroke(new BasicStroke(4f));    // độ dày nét viền
        g2.setColor(new Color(0, 78, 146));   // xanh dương AGU
        g2.drawOval(x + 2, y + 2, size - 4, size - 4); // vẽ viền ngoài

        // ======== VÒNG TRÒN TRẮNG BÊN TRONG ========
        int inner = (int) (size * 0.75); // kích thước vòng trong (chiếm 75%)
        int ix = (getWidth() - inner) / 2;
        int iy = (getHeight() - inner) / 2;

        g2.setColor(Color.WHITE);
        g2.fillOval(ix, iy, inner, inner);    // vòng trắng

        // Viền xanh nhỏ bên trong
        g2.setColor(new Color(0, 78, 146));
        g2.drawOval(ix, iy, inner, inner);

        // ======== HÌNH POLYGON MÀU XANH LÁ (BIỂU TƯỢNG) ========
        g2.setColor(new Color(0, 155, 101)); // xanh lá AGU

        // 4 điểm tạo hình polygon
        int[] ax = {
                ix + inner / 2,
                ix + inner - 10,
                ix + inner - 10,
                ix + inner / 2
        };
        int[] ay = {
                iy + 12,
                iy + inner / 2,
                iy + inner - 12,
                iy + inner / 2 + 20
        };
        g2.fillPolygon(ax, ay, 4); // tô đa giác

        // ======== POLYLINE TRANG TRÍ BÊN TRONG ========
        g2.setColor(new Color(0, 78, 146));
        g2.setStroke(new BasicStroke(
                8f,
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND
        ));

        g2.drawPolyline(
                new int[]{ix + 12, ix + inner / 2, ix + inner / 2, ix + inner - 12},
                new int[]{iy + inner - 12, iy + inner - 12, iy + 12, iy + 12},
                4
        );

        // Giải phóng object graphics
        g2.dispose();

        // Gọi super để vẽ nền/biên mặc định nếu cần
        super.paintComponent(g);
    }
}
