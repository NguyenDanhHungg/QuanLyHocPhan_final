package university.registration.ui;

import university.registration.controller.LoginController;
import university.registration.ui.components.CardPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Màn hình đăng nhập chính của ứng dụng
 * 
 * Lớp này CHỈ chịu trách nhiệm về UI (Presentation Layer):
 * - Hiển thị form đăng nhập (tài khoản/email, mật khẩu)
 * - Tạo và bố trí các UI components (text fields, buttons, labels)
 * - Xử lý sự kiện UI (nhấn nút, click)
 * - Chuyển hướng đến màn hình tiếp theo dựa trên kết quả từ Controller
 * 
 * Logic nghiệp vụ được xử lý bởi LoginController
 * Controller sẽ gọi Service layer để xử lý logic thực tế
 */
public class LoginFrame extends JFrame {

    // Controller xử lý logic nghiệp vụ (gọi Service layer)
    private final LoginController loginController = new LoginController();

    // ========== UI COMPONENTS ==========
    // Ô nhập tài khoản / email
    JTextField tfUser = new JTextField();
    // Ô nhập mật khẩu
    JPasswordField pfPass = new JPasswordField();

    public LoginFrame(){
        setTitle("Đăng nhập - Đăng ký học phần");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        setBackground(new Color(249, 250, 251));
        setLayout(new BorderLayout());

        // ========== HEADER SECTION ==========
        JPanel headerBar = new JPanel(new BorderLayout());
        headerBar.setBackground(new Color(249, 250, 251));
        headerBar.setBorder(new EmptyBorder(32, 48, 32, 48));
        
        JLabel logo = new JLabel("TRƯỜNG ĐẠI HỌC AN GIANG");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        logo.setForeground(new Color(16, 82, 138));
        
        headerBar.add(logo, BorderLayout.WEST);
        add(headerBar, BorderLayout.NORTH);

        // ========== MAIN CONTENT AREA ==========
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(new Color(249, 250, 251));
        
        // Center panel để căn giữa card
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(40, 0, 60, 0));

        // ====== CARD ĐĂNG NHẬP ======
        CardPanel card = new CardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(48, 48, 48, 48));
        card.setPreferredSize(new Dimension(500, 580));

        // Tiêu đề card - căn giữa
        JLabel title = new JLabel("ĐĂNG NHẬP HỆ THỐNG");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(31, 41, 55));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(new EmptyBorder(0, 0, 36, 0));
        card.add(title);

        // Label + field tài khoản
        JLabel lbUser = new JLabel("Tài khoản / Email AGU:");
        lbUser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbUser.setForeground(new Color(55, 65, 81));
        lbUser.setBorder(new EmptyBorder(0, 0, 8, 0));
        
        tfUser.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tfUser.setPreferredSize(new Dimension(400, 44));
        tfUser.setMaximumSize(new Dimension(400, 44));
        tfUser.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
                BorderFactory.createEmptyBorder(11, 16, 11, 16)
        ));
        
        JPanel userGroup = new JPanel(new BorderLayout(0, 8));
        userGroup.setOpaque(false);
        userGroup.setAlignmentX(Component.LEFT_ALIGNMENT);
        userGroup.setBorder(new EmptyBorder(0, 0, 20, 0));
        userGroup.add(lbUser, BorderLayout.NORTH);
        userGroup.add(tfUser, BorderLayout.CENTER);

        // Label + field mật khẩu
        JLabel lbPass = new JLabel("Mật khẩu:");
        lbPass.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbPass.setForeground(new Color(55, 65, 81));
        lbPass.setBorder(new EmptyBorder(0, 0, 8, 0));
        
        pfPass.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        pfPass.setPreferredSize(new Dimension(400, 44));
        pfPass.setMaximumSize(new Dimension(400, 44));
        pfPass.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
                BorderFactory.createEmptyBorder(11, 16, 11, 16)
        ));
        
        JPanel passGroup = new JPanel(new BorderLayout(0, 8));
        passGroup.setOpaque(false);
        passGroup.setAlignmentX(Component.LEFT_ALIGNMENT);
        passGroup.setBorder(new EmptyBorder(0, 0, 12, 0));
        passGroup.add(lbPass, BorderLayout.NORTH);
        passGroup.add(pfPass, BorderLayout.CENTER);

        // Link "Quên mật khẩu?"
        JLabel forgot = new JLabel("<html><u>Quên mật khẩu?</u></html>");
        forgot.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgot.setForeground(new Color(239, 68, 68));
        forgot.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        forgot.setBorder(new EmptyBorder(0, 0, 32, 0));
        forgot.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                JOptionPane.showMessageDialog(
                        LoginFrame.this,
                        "Vui lòng liên hệ Phòng Đào tạo để được hỗ trợ đặt lại mật khẩu."
                );
            }
        });

        // Nút Đăng nhập
        JButton btnLogin = new JButton("Đăng nhập");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.setBackground(new Color(59, 130, 246));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBorderPainted(false);
        btnLogin.setPreferredSize(new Dimension(400, 48));
        btnLogin.setMaximumSize(new Dimension(400, 48));
        btnLogin.setMinimumSize(new Dimension(400, 48));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        btnLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(37, 99, 235));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(59, 130, 246));
            }
        });
        btnLogin.addActionListener(e -> doLogin());

        // Nút Tạo tài khoản
        JButton btnCreate = new JButton("Tạo tài khoản sinh viên");
        btnCreate.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnCreate.setBackground(Color.WHITE);
        btnCreate.setForeground(new Color(59, 130, 246));
        btnCreate.setBorder(BorderFactory.createLineBorder(new Color(59, 130, 246), 1));
        btnCreate.setPreferredSize(new Dimension(400, 48));
        btnCreate.setMaximumSize(new Dimension(400, 48));
        btnCreate.setMinimumSize(new Dimension(400, 48));
        btnCreate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        btnCreate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCreate.setBackground(new Color(239, 246, 255));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCreate.setBackground(Color.WHITE);
            }
        });
        btnCreate.addActionListener(e -> new CreateStudentDialog(this));

        // Container để chứa tất cả form fields và căn trái
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        formPanel.add(userGroup);
        formPanel.add(passGroup);
        formPanel.add(forgot);
        formPanel.add(btnLogin);
        formPanel.add(Box.createVerticalStrut(12));
        formPanel.add(btnCreate);

        // Wrapper để căn giữa form panel trong card
        JPanel formWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        formWrapper.setOpaque(false);
        formWrapper.add(formPanel);
        
        card.add(formWrapper);

        centerPanel.add(card);
        mainContent.add(centerPanel, BorderLayout.CENTER);

        // ========== FOOTER ==========
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(20, 48, 20, 48));
        footer.setBackground(new Color(249, 250, 251));

        JLabel footerLeft = new JLabel(
                "© 2025 Trường Đại học An Giang • Trung tâm CNTT – Hotline: 0296 3831 265"
        );
        footerLeft.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        footerLeft.setForeground(new Color(107, 114, 128));

        JLabel footerRight = new JLabel("Cổng dịch vụ sinh viên AGU • v1.0");
        footerRight.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        footerRight.setForeground(new Color(107, 114, 128));

        footer.add(footerLeft, BorderLayout.WEST);
        footer.add(footerRight, BorderLayout.EAST);
        
        mainContent.add(footer, BorderLayout.SOUTH);
        add(mainContent, BorderLayout.CENTER);

        setVisible(true);
    }

    /**
     * Xử lý sự kiện khi người dùng nhấn nút "Đăng nhập"
     * 
     * UI chỉ làm việc:
     * 1. Lấy dữ liệu từ form
     * 2. Gọi Controller để xử lý logic
     * 3. Dựa vào kết quả từ Controller để:
     *    - Mở màn hình tương ứng (AdminFrame hoặc StudentRegistrationFrame)
     *    - Hoặc hiển thị thông báo lỗi
     * 
     * Tất cả logic nghiệp vụ (validate, xác thực) được xử lý trong Controller
     */
    void doLogin(){
        // Lấy thông tin từ form
        String user = tfUser.getText();
        String pass = new String(pfPass.getPassword());

        // Gọi Controller để xử lý logic đăng nhập
        // Controller sẽ gọi Service layer để xử lý logic thực tế
        LoginController.LoginResult result = loginController.login(user, pass);
        
        // Xử lý kết quả từ Controller (UI chỉ lo hiển thị và điều hướng)
        switch (result.type) {
            case ADMIN:
                // Đăng nhập Admin thành công: mở màn hình quản trị
                new AdminFrame(this);
                dispose(); // Đóng màn hình đăng nhập
                break;
                
            case STUDENT:
                // Đăng nhập sinh viên thành công: mở màn hình đăng ký học phần
                new StudentRegistrationFrame(this, result.student);
                dispose(); // Đóng màn hình đăng nhập
                break;
                
            case FAILED:
                // Đăng nhập thất bại: hiển thị thông báo lỗi
                JOptionPane.showMessageDialog(this, result.errorMessage);
                break;
        }
    }
}
