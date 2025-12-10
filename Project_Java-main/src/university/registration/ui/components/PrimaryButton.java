package university.registration.ui.components;

import javax.swing.*;
import java.awt.*;

public class PrimaryButton extends JButton {

    // Độ bo tròn của nút
    private final int arc = 22;

    // Hai màu để tạo gradient (trên → dưới)
    private final Color accentTop = new Color(0, 168, 181);  // xanh ngọc sáng
    private final Color accentBottom = new Color(0, 108, 196); // xanh dương đậm

    public PrimaryButton(String text) {
        super(text); // Gọi constructor JButton, đặt text cho nút

        // Màu chữ trắng để nổi bật trên nền xanh
        setForeground(Color.WHITE);

        // Font đẹp, hiện đại, kích thước lớn hơn NeutralButton
        setFont(new Font("Segoe UI", Font.BOLD, 20));

        // Không dùng nền mặc định, chúng ta sẽ tự vẽ
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);

        // Padding để nút có cảm giác "dày" và sang
        setBorder(BorderFactory.createEmptyBorder(12, 28, 12, 28));

        // Con trỏ dạng bàn tay khi hover
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        // Bật khử răng cưa để bo tròn và text mượt
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // ======== VẼ NỀN GRADIENT CHO NÚT ========

        // Gradient từ trên xuống dưới
        GradientPaint gp = new GradientPaint(
                0, 0, accentTop,     // điểm bắt đầu → màu sáng
                0, h, accentBottom   // điểm kết thúc → màu đậm
        );

        g2.setPaint(gp);

        // Vẽ nền bo góc
        g2.fillRoundRect(0, 0, w, h, arc, arc);

        // ======== VẼ VIỀN NÚT ========
        // Viền xanh đậm giúp tạo chiều sâu
        g2.setColor(new Color(0, 89, 158));
        g2.drawRoundRect(0, 0, w, h, arc, arc);

        g2.dispose();

        // Vẽ text/icon lên trên nền gradient
        super.paintComponent(g);
    }
}
