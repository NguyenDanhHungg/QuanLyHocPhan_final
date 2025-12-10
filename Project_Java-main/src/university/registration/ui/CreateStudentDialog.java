package university.registration.ui;

import university.registration.model.Student;
import university.registration.store.Memory;
import university.registration.ui.components.CardPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.regex.Pattern;

public class CreateStudentDialog extends JDialog {

    // Các trường nhập liệu của form
    JTextField tfId = new JTextField();
    JTextField tfName = new JTextField();
    JTextField tfDob = new JTextField("2004-01-01");
    JTextField tfAddr = new JTextField();
    JTextField tfEmail = new JTextField();

    // Mật khẩu và xác nhận mật khẩu
    JPasswordField pf1 = new JPasswordField();
    JPasswordField pf2 = new JPasswordField();

    // Chọn chương trình đào tạo
    JComboBox<String> cbProgram = new JComboBox<>();

    public CreateStudentDialog(JFrame owner){
        super(owner, "Tạo tài khoản sinh viên", true);
        setSize(650, 750);
        setLocationRelativeTo(owner);
        setResizable(false);
        
        setBackground(new Color(249, 250, 251));
        setLayout(new BorderLayout());

        // Load danh sách CTĐT từ Memory
        for (String p : Memory.programs) cbProgram.addItem(p);

        // ========== TOP BLUE BAR ==========
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(16, 82, 138));
        topBar.setBorder(new EmptyBorder(12, 24, 12, 24));
        
        JLabel topTitle = new JLabel("Tạo tài khoản sinh viên");
        topTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        topTitle.setForeground(Color.WHITE);
        
        topBar.add(topTitle, BorderLayout.WEST);
        add(topBar, BorderLayout.NORTH);

        // ========== HEADER SECTION ==========
        JPanel headerBar = new JPanel(new BorderLayout());
        headerBar.setBackground(new Color(249, 250, 251));
        headerBar.setBorder(new EmptyBorder(20, 32, 20, 32));
        
        JLabel logo = new JLabel("TRƯỜNG ĐẠI HỌC AN GIANG");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        logo.setForeground(new Color(16, 82, 138));
        
        headerBar.add(logo, BorderLayout.WEST);
        add(headerBar, BorderLayout.NORTH);

        // ========== MAIN CONTENT ==========
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(new Color(249, 250, 251));
        mainContent.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Card chứa form
        CardPanel card = new CardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        // Loại bỏ border mặc định của CardPanel và set border mới
        card.setBorder(new EmptyBorder(40, 40, 40, 40));
        card.setBackground(Color.WHITE);

        // Title - căn trái
        JLabel title = new JLabel("Đăng ký tài khoản sinh viên");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(31, 41, 55));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        title.setBorder(new EmptyBorder(0, 0, 12, 0));
        card.add(title);

        // Subtitle - căn trái
        JLabel subtitle = new JLabel("Nhập thông tin cá nhân bên dưới. Tài khoản sẽ được kích hoạt ngay sau khi tạo.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(107, 114, 128));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitle.setBorder(new EmptyBorder(0, 0, 32, 0));
        card.add(subtitle);

        // Form container - căn trái
        JPanel formContainer = new JPanel();
        formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));
        formContainer.setOpaque(false);
        formContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        formContainer.setMaximumSize(new Dimension(520, Integer.MAX_VALUE));

        // Form fields
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 15);
        
        formContainer.add(createFieldGroup("Mã số sinh viên", tfId, 520, labelFont, inputFont));
        formContainer.add(Box.createVerticalStrut(16));
        formContainer.add(createFieldGroup("Họ tên đầy đủ", tfName, 520, labelFont, inputFont));
        formContainer.add(Box.createVerticalStrut(16));
        formContainer.add(createFieldGroup("Ngày sinh (YYYY-MM-DD)", tfDob, 520, labelFont, inputFont));
        formContainer.add(Box.createVerticalStrut(16));
        formContainer.add(createFieldGroup("Địa chỉ liên hệ", tfAddr, 520, labelFont, inputFont));
        formContainer.add(Box.createVerticalStrut(16));
        formContainer.add(createFieldGroup("Email sinh viên", tfEmail, 520, labelFont, inputFont));
        formContainer.add(Box.createVerticalStrut(16));
        formContainer.add(createComboBoxGroup("Chương trình học", cbProgram, 520, labelFont, inputFont));
        formContainer.add(Box.createVerticalStrut(16));
        formContainer.add(createFieldGroup("Mật khẩu đăng nhập", pf1, 520, labelFont, inputFont));
        formContainer.add(Box.createVerticalStrut(16));
        formContainer.add(createFieldGroup("Xác nhận mật khẩu", pf2, 520, labelFont, inputFont));
        formContainer.add(Box.createVerticalStrut(24));

        // Thêm form container vào card - căn trái
        card.add(formContainer);

        // Action buttons - căn trái
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        actions.setOpaque(false);
        actions.setAlignmentX(Component.LEFT_ALIGNMENT);
        actions.setBorder(new EmptyBorder(8, 0, 0, 0));

        JButton btnCancel = new JButton("Đóng");
        btnCancel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnCancel.setBackground(Color.WHITE);
        btnCancel.setForeground(new Color(107, 114, 128));
        btnCancel.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));
        btnCancel.setPreferredSize(new Dimension(150, 48));
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.addActionListener(e -> dispose());
        btnCancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCancel.setBackground(new Color(249, 250, 251));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCancel.setBackground(Color.WHITE);
            }
        });

        JButton btnCreate = new JButton("Tạo tài khoản");
        btnCreate.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnCreate.setBackground(new Color(59, 130, 246));
        btnCreate.setForeground(Color.WHITE);
        btnCreate.setBorderPainted(false);
        btnCreate.setPreferredSize(new Dimension(180, 48));
        btnCreate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCreate.addActionListener(e -> create());
        btnCreate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCreate.setBackground(new Color(37, 99, 235));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCreate.setBackground(new Color(59, 130, 246));
            }
        });

        actions.add(btnCancel);
        actions.add(btnCreate);
        card.add(actions);

        // Scroll pane để có thể cuộn nếu cần
        JScrollPane scrollPane = new JScrollPane(card);
        scrollPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(new Color(249, 250, 251));

        mainContent.add(scrollPane, BorderLayout.CENTER);
        add(mainContent, BorderLayout.CENTER);

        setVisible(true);
    }

    /**
     * Helper: tạo field group với label và field
     */
    private JPanel createFieldGroup(String labelText, JComponent field, int width, Font labelFont, Font inputFont) {
        JPanel group = new JPanel();
        group.setLayout(new BoxLayout(group, BoxLayout.Y_AXIS));
        group.setOpaque(false);
        group.setAlignmentX(Component.LEFT_ALIGNMENT);
        group.setMaximumSize(new Dimension(width, Integer.MAX_VALUE));

        JLabel label = new JLabel(labelText + ":");
        label.setFont(labelFont);
        label.setForeground(new Color(55, 65, 81));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(new EmptyBorder(0, 0, 8, 0));

        if (field instanceof JTextField) {
            JTextField tf = (JTextField) field;
            tf.setFont(inputFont);
            tf.setPreferredSize(new Dimension(width, 44));
            tf.setMaximumSize(new Dimension(width, 44));
            tf.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
                    BorderFactory.createEmptyBorder(11, 16, 11, 16)
            ));
            tf.setAlignmentX(Component.LEFT_ALIGNMENT);
        } else if (field instanceof JPasswordField) {
            JPasswordField pf = (JPasswordField) field;
            pf.setFont(inputFont);
            pf.setPreferredSize(new Dimension(width, 44));
            pf.setMaximumSize(new Dimension(width, 44));
            pf.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
                    BorderFactory.createEmptyBorder(11, 16, 11, 16)
            ));
            pf.setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        group.add(label);
        group.add(field);

        return group;
    }

    /**
     * Helper: tạo combo box group với label
     */
    private JPanel createComboBoxGroup(String labelText, JComboBox<String> combo, int width, Font labelFont, Font inputFont) {
        JPanel group = new JPanel();
        group.setLayout(new BoxLayout(group, BoxLayout.Y_AXIS));
        group.setOpaque(false);
        group.setAlignmentX(Component.LEFT_ALIGNMENT);
        group.setMaximumSize(new Dimension(width, Integer.MAX_VALUE));

        JLabel label = new JLabel(labelText + ":");
        label.setFont(labelFont);
        label.setForeground(new Color(55, 65, 81));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(new EmptyBorder(0, 0, 8, 0));

        combo.setFont(inputFont);
        combo.setPreferredSize(new Dimension(width, 44));
        combo.setMaximumSize(new Dimension(width, 44));
        combo.setBorder(BorderFactory.createLineBorder(new Color(209, 213, 219), 1));
        combo.setAlignmentX(Component.LEFT_ALIGNMENT);

        group.add(label);
        group.add(combo);

        return group;
    }

    /**
     * Nút "Tạo tài khoản" gọi vào đây
     */
    void create(){
        try{
            // Lấy dữ liệu từ form
            String id = tfId.getText().trim();
            String name = tfName.getText().trim();
            String dob = tfDob.getText().trim();
            String addr = tfAddr.getText().trim();
            String email = tfEmail.getText().trim();
            String program = (String) cbProgram.getSelectedItem();

            String p1 = new String(pf1.getPassword());
            String p2 = new String(pf2.getPassword());

            // Validation
            if(id.isEmpty() || name.isEmpty() || email.isEmpty() ||
                    p1.isEmpty() || p2.isEmpty()){
                JOptionPane.showMessageDialog(
                        this,
                        "Vui lòng điền đầy đủ các trường bắt buộc.",
                        "Thông báo",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            if(!p1.equals(p2)){
                JOptionPane.showMessageDialog(
                        this,
                        "Mật khẩu xác nhận không khớp.",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            if(!isValidEmail(email)){
                JOptionPane.showMessageDialog(
                        this,
                        "Email không hợp lệ. Vui lòng nhập email đúng định dạng (ví dụ: example@agu.edu.vn).",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Kiểm tra MSSV đã tồn tại chưa
            if(Memory.studentsById.containsKey(id)){
                JOptionPane.showMessageDialog(
                        this,
                        "Mã số sinh viên đã tồn tại. Vui lòng sử dụng mã số khác.",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Kiểm tra email đã tồn tại chưa
            if(Memory.emailIndex.containsKey(email.toLowerCase())){
                JOptionPane.showMessageDialog(
                        this,
                        "Email đã được sử dụng. Vui lòng sử dụng email khác.",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Lưu dữ liệu
            Memory.addStudent(
                    new Student(id, name, dob, addr, email, program),
                    p1
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Tạo tài khoản thành công!\n\nMã số sinh viên: " + id + "\nBạn có thể đăng nhập ngay bây giờ.",
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE
            );
            dispose();

        }catch(Exception ex){
            JOptionPane.showMessageDialog(
                    this,
                    "Lỗi: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Check email bằng regex
     */
    boolean isValidEmail(String email){
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.compile(regex).matcher(email).matches();
    }
}
