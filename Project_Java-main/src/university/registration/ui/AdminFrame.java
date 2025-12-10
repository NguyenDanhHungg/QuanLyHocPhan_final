package university.registration.ui;

import university.registration.model.Course;
import university.registration.model.Offering;
import university.registration.model.RegItem;
import university.registration.model.Student;
import university.registration.store.Memory;
import university.registration.ui.components.CardPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class AdminFrame extends JFrame {

    // CardLayout và panel container
    CardLayout contentCardLayout;
    JPanel contentCards;
    JLabel headerTitle;
    
    // Combobox chọn học kỳ
    JComboBox<String> cbTerm = new JComboBox<>();
    // Bảng & model hiển thị danh sách học phần
    JTable table;
    DefaultTableModel model;

    // Các field nhập thông tin học phần
    JTextField tfCode = new JTextField(); // Mã HP
    JTextField tfName = new JTextField(); // Tên HP
    // Spinner chọn số tín chỉ (min=1, max=10, step=1, default=2)
    JSpinner spCredits = new JSpinner(new SpinnerNumberModel(2, 1, 10, 1));
    JComboBox<String> cbType = new JComboBox<>(new String[]{"Bắt buộc", "Tự chọn"});
    JTextField tfInstructor = new JTextField();
    JTextField tfPrerequisites = new JTextField(); // Nhập dạng "CT101,CT102"

    // Cấu hình offering cho kỳ hiện tại
    JComboBox<String> cbTermToOpen = new JComboBox<>(); // Chọn kỳ để mở
    JComboBox<String> cbAllowedProgram = new JComboBox<>();
    JComboBox<String> cbSchedule = new JComboBox<>(new String[]{
            "Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ nhật"
    }); // Thứ học
    JComboBox<String> cbTimeSlot = new JComboBox<>(new String[]{
            "Ca 1 (7:00-9:00)", "Ca 2 (9:00-11:00)", "Ca 3 (13:00-15:00)", 
            "Ca 4 (15:00-17:00)", "Ca 5 (17:00-19:00)"
    }); // Ca học
    JTextField tfRoom = new JTextField(); // Phòng học
    JSpinner spMaxCapacity = new JSpinner(new SpinnerNumberModel(50, 10, 200, 5));

    // Button để quản lý mở/đóng đăng ký cho các học kỳ
    JComboBox<String> cbTermStatus = new JComboBox<>();
    JButton btnTermManagement = new JButton("Quản lý học kỳ");
    
    // Thời gian đăng ký học phần - sử dụng JSpinner với DateModel để chọn ngày
    JSpinner spRegStartDate = new JSpinner(new SpinnerDateModel()); // Từ ngày
    JSpinner spRegEndDate = new JSpinner(new SpinnerDateModel()); // Đến ngày

    public AdminFrame(JFrame owner){
        setTitle("PĐT – Quản lý đăng ký học phần");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        setBackground(new Color(249, 250, 251));
        setLayout(new BorderLayout());

        // ========== SIDEBAR ==========
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(255, 255, 255));
        sidebar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(229, 231, 235)),
                new EmptyBorder(20, 0, 20, 0)
        ));
        sidebar.setPreferredSize(new Dimension(220, 0));

        // Logo/Title
        JLabel sidebarTitle = new JLabel("PHÒNG ĐÀO TẠO");
        sidebarTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sidebarTitle.setForeground(new Color(16, 82, 138));
        sidebarTitle.setBorder(new EmptyBorder(20, 20, 30, 20));
        sidebarTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(sidebarTitle);

        // Menu items
        String[] menuItems = {"Khóa học", "Duyệt đăng ký học phần", "Đăng ký", "Phân tích", "Cài đặt"};
        JButton[] menuButtons = new JButton[menuItems.length];
        
        for (int i = 0; i < menuItems.length; i++) {
            final int idx = i;
            JButton menuBtn = new JButton(menuItems[i]);
            menuBtn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            menuBtn.setHorizontalAlignment(SwingConstants.LEFT);
            menuBtn.setBorderPainted(false);
            menuBtn.setContentAreaFilled(false);
            menuBtn.setForeground(new Color(107, 114, 128));
            menuBtn.setPreferredSize(new Dimension(200, 50));
            menuBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            menuBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            menuBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            // Padding cho text
            menuBtn.setBorder(new EmptyBorder(0, 20, 0, 20));
            
            if (i == 0) { // Khóa học - selected
                menuBtn.setForeground(new Color(59, 130, 246));
                menuBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
                menuBtn.setBackground(new Color(239, 246, 255));
                menuBtn.setContentAreaFilled(true);
            }
            
            menuBtn.addActionListener(e -> {
                // Reset all buttons
                for (JButton btn : menuButtons) {
                    btn.setForeground(new Color(107, 114, 128));
                    btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
                    btn.setContentAreaFilled(false);
                }
                // Set selected button
                menuBtn.setForeground(new Color(59, 130, 246));
                menuBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
                menuBtn.setBackground(new Color(239, 246, 255));
                menuBtn.setContentAreaFilled(true);
                
                // Navigate logic
                if (idx == 0) {
                    // Khóa học
                    headerTitle.setText("Quản lý Học phần");
                    contentCardLayout.show(contentCards, "COURSES");
                } else if (idx == 1) {
                    // Duyệt đăng ký học phần
                    headerTitle.setText("Duyệt đăng ký học phần");
                    contentCardLayout.show(contentCards, "APPROVAL");
                } else {
                    JOptionPane.showMessageDialog(this, "Chức năng " + menuItems[idx] + " đang phát triển");
                }
            });
            
            menuBtn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    if (idx != 0 || menuBtn.getForeground().equals(new Color(107, 114, 128))) {
                        menuBtn.setBackground(new Color(249, 250, 251));
                        menuBtn.setContentAreaFilled(true);
                    }
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    if (idx != 0 || menuBtn.getForeground().equals(new Color(107, 114, 128))) {
                        menuBtn.setBackground(Color.WHITE);
                        menuBtn.setContentAreaFilled(false);
                    }
                }
            });
            
            menuButtons[i] = menuBtn;
            sidebar.add(menuBtn);
            sidebar.add(Box.createVerticalStrut(4));
        }
        
        // Spacer để đẩy nút đăng xuất xuống dưới
        sidebar.add(Box.createVerticalGlue());
        
        // Nút đăng xuất ở cuối sidebar
        JButton btnLogout = new JButton("Đăng xuất");
        btnLogout.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btnLogout.setHorizontalAlignment(SwingConstants.LEFT);
        btnLogout.setBorderPainted(false);
        btnLogout.setContentAreaFilled(false);
        btnLogout.setForeground(new Color(239, 68, 68));
        btnLogout.setPreferredSize(new Dimension(200, 50));
        btnLogout.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLogout.setBorder(new EmptyBorder(0, 20, 0, 20));
        
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });
        
        btnLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogout.setBackground(new Color(254, 242, 242));
                btnLogout.setContentAreaFilled(true);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogout.setBackground(Color.WHITE);
                btnLogout.setContentAreaFilled(false);
            }
        });
        
        sidebar.add(btnLogout);
        sidebar.add(Box.createVerticalStrut(20));

        // ========== MAIN CONTENT AREA ==========
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(new Color(249, 250, 251));

        // Top Header Bar (dynamic title)
        JPanel headerBar = new JPanel(new BorderLayout());
        headerBar.setBackground(Color.WHITE);
        headerBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(229, 231, 235)),
                new EmptyBorder(16, 24, 16, 24)
        ));
        
        headerTitle = new JLabel("Quản lý Học phần");
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerTitle.setForeground(new Color(31, 41, 55));
        
        headerBar.add(headerTitle, BorderLayout.WEST);
        mainContent.add(headerBar, BorderLayout.NORTH);
        
        // Thêm sidebar vào frame
        add(sidebar, BorderLayout.WEST);
        
        // CardLayout để switch giữa các panel
        contentCardLayout = new CardLayout();
        contentCards = new JPanel(contentCardLayout);
        contentCards.setBackground(new Color(249, 250, 251));

        // Content Area with padding
        JPanel overlay = new JPanel(new BorderLayout());
        overlay.setOpaque(false);
        overlay.setBorder(new EmptyBorder(24, 24, 24, 24));

        // ========== MAIN AREA ==========
        JPanel mainArea = new JPanel(new BorderLayout(0, 16));
        mainArea.setOpaque(false);

        // Thêm Action Buttons vào top của main area
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        actionPanel.setOpaque(false);
        actionPanel.setBorder(new EmptyBorder(0, 0, 16, 0));
        
        JComboBox<String> cbStatus = new JComboBox<>(new String[]{"Tất cả", "Mở", "Đóng"});
        cbStatus.setPreferredSize(new Dimension(140, 36));
        
        JComboBox<String> cbDept = new JComboBox<>(new String[]{"Tất cả"});
        cbDept.setPreferredSize(new Dimension(140, 36));
        for(String p: Memory.programs) cbDept.addItem(p);
        
        JButton btnAddCourse = new JButton("+ Thêm học phần mới");
        btnAddCourse.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAddCourse.setBackground(new Color(59, 130, 246));
        btnAddCourse.setForeground(Color.WHITE);
        btnAddCourse.setBorderPainted(false);
        btnAddCourse.setPreferredSize(new Dimension(200, 40));
        btnAddCourse.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAddCourse.addActionListener(e -> clearInputs());
        
        actionPanel.add(new JLabel("Trạng thái:"));
        actionPanel.add(cbStatus);
        actionPanel.add(new JLabel("Khoa:"));
        actionPanel.add(cbDept);
        actionPanel.add(btnAddCourse);
        
        mainArea.add(actionPanel, BorderLayout.NORTH);

        // ----- FORM SECTION -----
        CardPanel formCard = new CardPanel();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBorder(new EmptyBorder(20, 20, 20, 20));
        formCard.setBackground(Color.WHITE);

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font sectionFont = new Font("Segoe UI", Font.BOLD, 16);

        // ===== SEMESTER SELECTION =====
        JPanel semesterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        semesterPanel.setOpaque(false);
        semesterPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        semesterPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lbTerm = new JLabel("Học kỳ:");
        lbTerm.setFont(labelFont);
        semesterPanel.add(lbTerm);

        cbTerm.setFont(inputFont);
        cbTerm.setPreferredSize(new Dimension(140, 36));
        for(String t: Memory.loadTerms()) cbTerm.addItem(t);
        semesterPanel.add(cbTerm);

        JLabel lbTermStatus = new JLabel("Trạng thái:");
        lbTermStatus.setFont(labelFont);
        semesterPanel.add(lbTermStatus);
        
        cbTermStatus.setFont(inputFont);
        cbTermStatus.setPreferredSize(new Dimension(140, 36));
        cbTermStatus.addItem("Đóng");
        cbTermStatus.addItem("Mở");
        semesterPanel.add(cbTermStatus);

        btnTermManagement.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnTermManagement.setBackground(Color.WHITE);
        btnTermManagement.setForeground(new Color(59, 130, 246));
        btnTermManagement.setBorder(BorderFactory.createLineBorder(new Color(59, 130, 246), 1));
        btnTermManagement.setPreferredSize(new Dimension(160, 36));
        btnTermManagement.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTermManagement.addActionListener(e -> showTermManagementDialog());
        semesterPanel.add(Box.createHorizontalStrut(24));
        semesterPanel.add(btnTermManagement);
        
        // ===== SECTION: QUẢN LÝ HỌC KỲ VÀ THỜI GIAN ĐĂNG KÝ =====
        JPanel termManagementSection = createSection("Quản lý học kỳ và thời gian đăng ký", sectionFont);
        
        // Row 1: Chọn học kỳ, trạng thái và nút quản lý
        JPanel termRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        termRow.setOpaque(false);
        termRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel termGroup = createComboBoxGroup("Học kỳ", cbTerm, 180, labelFont, inputFont);
        termRow.add(termGroup);
        
        JPanel statusGroup = createComboBoxGroup("Trạng thái", cbTermStatus, 180, labelFont, inputFont);
        termRow.add(statusGroup);
        
        btnTermManagement.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnTermManagement.setBackground(Color.WHITE);
        btnTermManagement.setForeground(new Color(59, 130, 246));
        btnTermManagement.setBorder(BorderFactory.createLineBorder(new Color(59, 130, 246), 1));
        btnTermManagement.setPreferredSize(new Dimension(180, 42));
        btnTermManagement.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTermManagement.addActionListener(e -> showTermManagementDialog());
        
        JPanel btnGroup = new JPanel(new BorderLayout(0, 6));
        btnGroup.setOpaque(false);
        btnGroup.add(new JLabel(), BorderLayout.NORTH); // Spacer để căn với các field khác
        btnGroup.add(btnTermManagement, BorderLayout.CENTER);
        termRow.add(btnGroup);
        
        termManagementSection.add(termRow);
        termManagementSection.add(Box.createVerticalStrut(16));
        
        // Row 2: Thời gian đăng ký
        JPanel dateRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        dateRow.setOpaque(false);
        dateRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Cấu hình date picker cho "Từ ngày"
        JSpinner.DateEditor startDateEditor = new JSpinner.DateEditor(spRegStartDate, "yyyy-MM-dd");
        spRegStartDate.setEditor(startDateEditor);
        ((JSpinner.DefaultEditor) spRegStartDate.getEditor()).getTextField().setEditable(false);
        spRegStartDate.setPreferredSize(new Dimension(180, 36));
        spRegStartDate.setFont(inputFont);
        
        JPanel startDateGroup = new JPanel(new BorderLayout(0, 6));
        startDateGroup.setOpaque(false);
        JLabel lbStartDate = new JLabel("Từ ngày:");
        lbStartDate.setFont(labelFont);
        startDateGroup.add(lbStartDate, BorderLayout.NORTH);
        startDateGroup.add(spRegStartDate, BorderLayout.CENTER);
        dateRow.add(startDateGroup);
        
        // Cấu hình date picker cho "Đến ngày"
        JSpinner.DateEditor endDateEditor = new JSpinner.DateEditor(spRegEndDate, "yyyy-MM-dd");
        spRegEndDate.setEditor(endDateEditor);
        ((JSpinner.DefaultEditor) spRegEndDate.getEditor()).getTextField().setEditable(false);
        spRegEndDate.setPreferredSize(new Dimension(180, 36));
        spRegEndDate.setFont(inputFont);
        
        JPanel endDateGroup = new JPanel(new BorderLayout(0, 6));
        endDateGroup.setOpaque(false);
        JLabel lbEndDate = new JLabel("Đến ngày:");
        lbEndDate.setFont(labelFont);
        endDateGroup.add(lbEndDate, BorderLayout.NORTH);
        endDateGroup.add(spRegEndDate, BorderLayout.CENTER);
        dateRow.add(endDateGroup);
        
        termManagementSection.add(dateRow);
        
        formCard.add(termManagementSection);
        formCard.add(Box.createVerticalStrut(20));

        // ===== SECTION 1: THÔNG TIN HỌC PHẦN =====
        JPanel section1 = createSection("Thông tin học phần", sectionFont);
        
        // Row 1: Mã HP, Tên HP
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        row1.setOpaque(false);
        row1.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel codeGroup = createFieldGroup("Mã HP", tfCode, 140, labelFont, inputFont);
        row1.add(codeGroup);
        
        JPanel nameGroup = createFieldGroup("Tên học phần", tfName, 400, labelFont, inputFont);
        row1.add(nameGroup);
        
        JPanel creditsGroup = createSpinnerGroup("Số TC", spCredits, 100, labelFont, inputFont);
        row1.add(creditsGroup);
        section1.add(row1);
        
        section1.add(Box.createVerticalStrut(16));
        
        // Row 2: Loại HP, Giảng viên, ĐK tiên quyết
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        row2.setOpaque(false);
        row2.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel typeGroup = new JPanel(new BorderLayout(0, 6));
        typeGroup.setOpaque(false);
        JLabel lbType = new JLabel("Loại HP:");
        lbType.setFont(labelFont);
        cbType.setFont(inputFont);
        cbType.setPreferredSize(new Dimension(150, 36));
        typeGroup.add(lbType, BorderLayout.NORTH);
        typeGroup.add(cbType, BorderLayout.CENTER);
        row2.add(typeGroup);
        
        JPanel instructorGroup = createFieldGroup("Giảng viên", tfInstructor, 280, labelFont, inputFont);
        row2.add(instructorGroup);
        
        JPanel prereqGroup = createFieldGroup("ĐK tiên quyết", tfPrerequisites, 250, labelFont, inputFont);
        tfPrerequisites.setToolTipText("VD: CT101,CT102");
        row2.add(prereqGroup);
        section1.add(row2);
        
        formCard.add(section1);
        formCard.add(Box.createVerticalStrut(20));

        // ===== SECTION 2: CẤU HÌNH LỚP HỌC =====
        JPanel section2 = createSection("Cấu hình lớp học", sectionFont);
        
        // Row 1: Chọn kỳ để mở và CTĐT
        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        row3.setOpaque(false);
        row3.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // ComboBox chọn kỳ để mở
        JPanel termOpenGroup = new JPanel(new BorderLayout(0, 6));
        termOpenGroup.setOpaque(false);
        JLabel lbTermOpen = new JLabel("Chọn kỳ để mở:");
        lbTermOpen.setFont(labelFont);
        cbTermToOpen.setFont(inputFont);
        cbTermToOpen.setPreferredSize(new Dimension(180, 36));
        // Nạp danh sách học kỳ
        cbTermToOpen.addItem("-- Chọn kỳ --");
        for(String t: Memory.loadTerms()) cbTermToOpen.addItem(t);
        termOpenGroup.add(lbTermOpen, BorderLayout.NORTH);
        termOpenGroup.add(cbTermToOpen, BorderLayout.CENTER);
        row3.add(termOpenGroup);
        
        JPanel programGroup = new JPanel(new BorderLayout(0, 6));
        programGroup.setOpaque(false);
        JLabel lbAllow = new JLabel("Chỉ CTĐT:");
        lbAllow.setFont(labelFont);
        cbAllowedProgram.setFont(inputFont);
        cbAllowedProgram.setPreferredSize(new Dimension(200, 36));
        cbAllowedProgram.addItem("Tất cả");
        for(String p: Memory.programs) cbAllowedProgram.addItem(p);
        programGroup.add(lbAllow, BorderLayout.NORTH);
        programGroup.add(cbAllowedProgram, BorderLayout.CENTER);
        row3.add(programGroup);
        
        section2.add(row3);
        section2.add(Box.createVerticalStrut(16));
        
        // Row 2: Lịch học, Ca học, Phòng học, Sức chứa
        JPanel row4 = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        row4.setOpaque(false);
        row4.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel scheduleGroup = createComboBoxGroup("Lịch học", cbSchedule, 180, labelFont, inputFont);
        cbSchedule.setToolTipText("Chọn thứ trong tuần");
        row4.add(scheduleGroup);
        
        JPanel timeGroup = createComboBoxGroup("Ca học", cbTimeSlot, 180, labelFont, inputFont);
        cbTimeSlot.setToolTipText("Chọn ca học");
        row4.add(timeGroup);
        
        JPanel roomGroup = createFieldGroup("Phòng học", tfRoom, 150, labelFont, inputFont);
        row4.add(roomGroup);
        
        JPanel capacityGroup = createSpinnerGroup("Sức chứa", spMaxCapacity, 120, labelFont, inputFont);
        row4.add(capacityGroup);
        section2.add(row4);
        
        formCard.add(section2);
        formCard.add(Box.createVerticalStrut(24));

        // ===== BUTTONS =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton btnAdd = new JButton("Lưu học phần");
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAdd.setBackground(new Color(59, 130, 246));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setBorderPainted(false);
        btnAdd.setPreferredSize(new Dimension(160, 40));
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JButton btnDelete = new JButton("Xóa học phần");
        btnDelete.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnDelete.setBackground(Color.WHITE);
        btnDelete.setForeground(new Color(239, 68, 68));
        btnDelete.setBorder(BorderFactory.createLineBorder(new Color(239, 68, 68), 1));
        btnDelete.setPreferredSize(new Dimension(160, 40));
        btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JButton btnClear = new JButton("Làm mới");
        btnClear.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnClear.setBackground(Color.WHITE);
        btnClear.setForeground(new Color(107, 114, 128));
        btnClear.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));
        btnClear.setPreferredSize(new Dimension(120, 40));
        btnClear.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClear.addActionListener(e -> clearInputs());
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        
        formCard.add(buttonPanel);

        mainArea.add(formCard, BorderLayout.NORTH);
        
        // Gắn action listeners
        btnAdd.addActionListener(e -> addOrUpdate());
        btnDelete.addActionListener(e -> deleteCourses());

        // ========== TABLE SECTION ==========
        model = new DefaultTableModel(new Object[]{
                "Mã HP","Tên học phần","Số TC","Loại","Giảng viên","Mở lớp?","Chỉ CTĐT",
                "Lịch học","Phòng","SV đã DK","Chọn"
        }, 0) {
            // Chỉ cho phép sửa ô cột cuối (checkbox chọn)
            @Override public boolean isCellEditable(int r,int c){ return c==10; }
            // Cột cuối trả về kiểu Boolean để JTable render checkbox
            @Override public Class<?> getColumnClass(int c){
                return (c==10) ? Boolean.class : String.class;
            }
        };

        table = new JTable(model) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                }
                return c;
            }
        };
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.setRowHeight(45);
        
        // Custom table header
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setBackground(new Color(230, 240, 250)); // Màu xanh nhạt
        header.setForeground(new Color(0, 64, 128)); // Màu xanh đậm cho text
        header.setPreferredSize(new Dimension(0, 50));
        header.setReorderingAllowed(false);
        
        // Custom header renderer để đảm bảo hiển thị đúng
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, 
                    boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setBackground(new Color(230, 240, 250));
                label.setForeground(new Color(0, 64, 128));
                label.setFont(new Font("Segoe UI", Font.BOLD, 15));
                label.setHorizontalAlignment(SwingConstants.LEFT);
                label.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 2, 2, new Color(0, 64, 128)),
                        BorderFactory.createEmptyBorder(14, 14, 14, 14)
                ));
                return label;
            }
        });
        
        table.setSelectionBackground(new Color(239, 246, 255));
        table.setSelectionForeground(Color.BLACK);
        table.setShowGrid(true);
        table.setGridColor(new Color(220, 220, 220)); // Grid màu xám nhạt
        table.setIntercellSpacing(new Dimension(1, 1));
        
        // Tùy chỉnh độ rộng cột
        table.getColumnModel().getColumn(0).setPreferredWidth(80);  // Mã HP
        table.getColumnModel().getColumn(1).setPreferredWidth(250); // Tên học phần
        table.getColumnModel().getColumn(2).setPreferredWidth(60);  // Số TC
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // Loại
        table.getColumnModel().getColumn(4).setPreferredWidth(150); // Giảng viên
        table.getColumnModel().getColumn(5).setPreferredWidth(80);  // Mở lớp?
        table.getColumnModel().getColumn(6).setPreferredWidth(150); // Chỉ CTĐT
        table.getColumnModel().getColumn(7).setPreferredWidth(120); // Lịch học
        table.getColumnModel().getColumn(8).setPreferredWidth(100); // Phòng
        table.getColumnModel().getColumn(9).setPreferredWidth(80);  // SV đã DK
        table.getColumnModel().getColumn(10).setPreferredWidth(60); // Chọn

        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        tableScrollPane.setViewportBorder(null);
        tableScrollPane.setPreferredSize(new Dimension(0, 500));
        tableScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        CardPanel tableCard = new CardPanel();
        tableCard.setLayout(new BorderLayout());
        tableCard.setBorder(new EmptyBorder(20, 20, 20, 20));
        tableCard.setBackground(Color.WHITE);
        
        JLabel tableTitle = new JLabel("Danh sách học phần");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tableTitle.setForeground(new Color(31, 41, 55));
        tableTitle.setBorder(new EmptyBorder(0, 0, 16, 0));
        
        tableCard.add(tableTitle, BorderLayout.NORTH);
        tableCard.add(tableScrollPane, BorderLayout.CENTER);
        
        mainArea.add(tableCard, BorderLayout.CENTER);

        // Wrap mainArea trong JScrollPane để có thể scroll
        JScrollPane mainScrollPane = new JScrollPane(mainArea);
        mainScrollPane.setBorder(null);
        mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        overlay.add(mainScrollPane, BorderLayout.CENTER);
        
        // Tạo panel cho phần Quản lý học phần
        JPanel courseManagementPanel = new JPanel(new BorderLayout());
        courseManagementPanel.setOpaque(false);
        courseManagementPanel.add(overlay, BorderLayout.CENTER);
        contentCards.add(courseManagementPanel, "COURSES");
        
        // Tạo panel cho phần Duyệt đăng ký học phần
        JPanel approvalPanel = createApprovalPanel();
        contentCards.add(approvalPanel, "APPROVAL");
        
        // Hiển thị panel mặc định
        contentCardLayout.show(contentCards, "COURSES");
        
        mainContent.add(contentCards, BorderLayout.CENTER);
        add(mainContent, BorderLayout.CENTER);

        // ========== GẮN ACTION ==========

        // Khi đổi học kỳ → load lại dữ liệu
        cbTerm.addActionListener(e -> refresh());

        // Khi chọn dòng trên bảng, đổ lại dữ liệu vào form
        table.getSelectionModel().addListSelectionListener(e -> fillFormFromSelection());

        // Lần đầu: load dữ liệu
        refresh();

        setVisible(true);
    }

    // Helper methods để tạo field groups
    private JPanel createFieldGroup(String labelText, JTextField field, int width, Font labelFont, Font inputFont) {
        JPanel group = new JPanel(new BorderLayout(0, 6));
        group.setOpaque(false);
        
        JLabel label = new JLabel(labelText + ":");
        label.setFont(labelFont);
        label.setForeground(new Color(55, 65, 81));
        
        field.setFont(inputFont);
        field.setPreferredSize(new Dimension(width, 36));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        group.add(label, BorderLayout.NORTH);
        group.add(field, BorderLayout.CENTER);
        
        return group;
    }
    
    private JPanel createSpinnerGroup(String labelText, JSpinner spinner, int width, Font labelFont, Font inputFont) {
        JPanel group = new JPanel(new BorderLayout(0, 6));
        group.setOpaque(false);
        
        JLabel label = new JLabel(labelText + ":");
        label.setFont(labelFont);
        label.setForeground(new Color(55, 65, 81));
        
        ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().setFont(inputFont);
        spinner.setPreferredSize(new Dimension(width, 36));
        spinner.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        
        group.add(label, BorderLayout.NORTH);
        group.add(spinner, BorderLayout.CENTER);
        
        return group;
    }
    
    private JPanel createComboBoxGroup(String labelText, JComboBox<String> combo, int width, Font labelFont, Font inputFont) {
        JPanel group = new JPanel(new BorderLayout(0, 6));
        group.setOpaque(false);
        
        JLabel label = new JLabel(labelText + ":");
        label.setFont(labelFont);
        label.setForeground(new Color(55, 65, 81));
        
        combo.setFont(inputFont);
        combo.setPreferredSize(new Dimension(width, 36));
        combo.setBorder(BorderFactory.createLineBorder(new Color(209, 213, 219), 1));
        
        group.add(label, BorderLayout.NORTH);
        group.add(combo, BorderLayout.CENTER);
        
        return group;
    }
    
    private JPanel createSection(String title, Font titleFont) {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setOpaque(false);
        section.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                        title,
                        0, 0, titleFont, new Color(75, 85, 99)
                ),
                new EmptyBorder(16, 0, 16, 16) // Giảm padding trái về 0 để căn trái bằng với phần học kỳ
        ));
        
        return section;
    }

    /**
     * Hiển thị dialog quản lý mở/đóng đăng ký cho các học kỳ
     */
    void showTermManagementDialog() {
        JDialog dialog = new JDialog(this, "Quản lý Học kỳ", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        // Panel chính
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Quản lý mở/đóng đăng ký học kỳ");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(31, 41, 55));
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        contentPanel.add(titleLabel);
        
        // Danh sách học kỳ với checkbox
        JPanel termsPanel = new JPanel();
        termsPanel.setLayout(new BoxLayout(termsPanel, BoxLayout.Y_AXIS));
        termsPanel.setOpaque(false);
        
        List<JCheckBox> termCheckboxes = new ArrayList<>();
        for (String term : Memory.loadTerms()) {
            JCheckBox chk = new JCheckBox(term);
            chk.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            chk.setSelected(Memory.isTermOpen(term));
            chk.setOpaque(false);
            chk.setForeground(Memory.isTermOpen(term) ? new Color(34, 197, 94) : new Color(107, 114, 128));
            termCheckboxes.add(chk);
            
            JPanel itemPanel = new JPanel(new BorderLayout());
            itemPanel.setOpaque(false);
            itemPanel.setBorder(new EmptyBorder(8, 0, 8, 0));
            itemPanel.add(chk, BorderLayout.WEST);
            
            // Status label
            JLabel statusLabel = new JLabel(Memory.isTermOpen(term) ? "Mở" : "Đóng");
            statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            statusLabel.setForeground(Memory.isTermOpen(term) ? new Color(34, 197, 94) : new Color(239, 68, 68));
            itemPanel.add(statusLabel, BorderLayout.EAST);
            
            termsPanel.add(itemPanel);
            termsPanel.add(Box.createVerticalStrut(4));
        }
        
        JScrollPane termsScroll = new JScrollPane(termsPanel);
        termsScroll.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));
        termsScroll.setPreferredSize(new Dimension(0, 250));
        contentPanel.add(termsScroll);
        
        dialog.add(contentPanel, BorderLayout.CENTER);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JButton btnCancel = new JButton("Hủy");
        btnCancel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnCancel.setBackground(Color.WHITE);
        btnCancel.setForeground(new Color(107, 114, 128));
        btnCancel.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));
        btnCancel.setPreferredSize(new Dimension(100, 36));
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.addActionListener(e -> dialog.dispose());
        
        JButton btnSave = new JButton("Lưu");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setBackground(new Color(59, 130, 246));
        btnSave.setForeground(Color.WHITE);
        btnSave.setBorderPainted(false);
        btnSave.setPreferredSize(new Dimension(100, 36));
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSave.addActionListener(e -> {
            // Lưu trạng thái mở/đóng cho từng học kỳ
            for (int i = 0; i < termCheckboxes.size(); i++) {
                JCheckBox chk = termCheckboxes.get(i);
                String term = chk.getText();
                Memory.setTermOpen(term, chk.isSelected());
            }
            refresh();
            dialog.dispose();
            JOptionPane.showMessageDialog(this, "Đã cập nhật trạng thái học kỳ thành công!");
        });
        
        buttonPanel.add(btnCancel);
        buttonPanel.add(btnSave);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    /**
     * Nạp lại dữ liệu bảng theo học kỳ đang chọn.
     * - Duyệt tất cả course master trong Memory.courses
     *   rồi lấy Offering + số lượng SV đăng ký ở kỳ đó.
     */
    void refresh(){
        String term = (String) cbTerm.getSelectedItem();
        if (term == null) return;

        // Cập nhật trạng thái học kỳ
        boolean termOpen = Memory.isTermOpen(term);
        cbTermStatus.setSelectedItem(termOpen ? "Mở" : "Đóng");

        // Xóa toàn bộ dòng cũ
        model.setRowCount(0);

        // Thêm từng course vào bảng
        for (Course c : Memory.courses.values()) {
            Offering off = Memory.getOffering(term, c.code);

            String ap = (off == null)
                    ? "Tất cả"
                    : off.allowedProgram;

            int count = Memory.countRegByCourse(term, c.code);

            String type = "Tự chọn"; // Default
            String instructor = "-";
            String schedule = "-";
            String room = "-";
            String openDisplay = (off != null && off.open) ? "Mở" : "Đóng";
            
            model.addRow(new Object[]{
                    c.code,
                    c.name,
                    String.valueOf(c.credits),
                    type,
                    instructor,
                    openDisplay,
                    ap,
                    schedule,
                    room,
                    String.valueOf(count),
                    false   // cột "Chọn" ban đầu = false
            });
        }
    }

    /**
     * Khi chọn một dòng trên bảng → đưa dữ liệu lại vào form để sửa.
     */
    void fillFormFromSelection(){
        int r = table.getSelectedRow();
        if(r < 0) return; // chưa chọn dòng

        String code = (String)model.getValueAt(r,0);
        tfCode.setText(code);
        tfName.setText((String)model.getValueAt(r,1));

        try{
            spCredits.setValue(
                    Integer.parseInt((String)model.getValueAt(r,2))
            );
        }catch(Exception ignore){}
        
        // Load thông tin từ Course (nếu có thêm fields trong model)
        Course c = Memory.courses.get(code);
        if (c != null) {
            cbType.setSelectedItem("Tự chọn"); // Default
        }

        String term = (String)cbTerm.getSelectedItem();
        var offering = Memory.getOffering(term, code);
        
        // Set kỳ được chọn trong combobox "Chọn kỳ để mở"
        if (offering != null && offering.open) {
            cbTermToOpen.setSelectedItem(term);
        } else {
            cbTermToOpen.setSelectedIndex(0); // "-- Chọn kỳ --"
        }

        cbAllowedProgram.setSelectedItem(
                offering != null && offering.allowedProgram != null 
                ? offering.allowedProgram : "Tất cả"
        );
        
        // Reset các field schedule, timeSlot, room, capacity về mặc định
        cbSchedule.setSelectedIndex(0);
        cbTimeSlot.setSelectedIndex(0);
        tfRoom.setText("");
        spMaxCapacity.setValue(50);
    }

    /**
     * Thêm mới hoặc cập nhật học phần:
     * - Validate input
     * - Cập nhật course master trong Memory
     * - Cập nhật Offering cho học kỳ đang chọn
     * - Clear form + refresh bảng
     */
    void addOrUpdate(){
        String code = tfCode.getText().trim().toUpperCase();
        String name = tfName.getText().trim();
        int cr = (int) spCredits.getValue();

        if(code.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Nhập đầy đủ Mã HP và Tên học phần."
            );
            return;
        }

        // Tạo course với constructor đơn giản (vì model Course hiện tại chỉ có 3 params)
        Course course = new Course(code, name, cr);
        Memory.addCourse(course);

        // Cập nhật offering theo kỳ được chọn
        String selectedTerm = (String)cbTermToOpen.getSelectedItem();
        if (selectedTerm == null || selectedTerm.equals("-- Chọn kỳ --")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn kỳ để mở lớp.");
            return;
        }
        
        boolean open = true; // Nếu đã chọn kỳ thì mặc định là mở
        String ap = (String)cbAllowedProgram.getSelectedItem();
        
        // Gọi setOffering với signature phù hợp (chỉ có open và allowedProgram)
        Memory.setOffering(selectedTerm, code, open, ap);
        
        // Lưu thời gian đăng ký nếu có
        Date startDate = (Date) spRegStartDate.getValue();
        Date endDate = (Date) spRegEndDate.getValue();
        
        if (startDate != null && endDate != null) {
            // Kiểm tra ngày bắt đầu phải trước ngày kết thúc
            if (startDate.after(endDate)) {
                JOptionPane.showMessageDialog(this, "Ngày bắt đầu phải trước ngày kết thúc!");
                return;
            }
            // Có thể lưu vào TermSetting hoặc một map riêng
            // Hiện tại chỉ validate, có thể mở rộng sau
        }

        clearInputs();
        refresh();
    }

    /**
     * Xóa các học phần được tick ở cột "Chọn":
     * - Thu thập danh sách courseCode cần xóa
     * - Kiểm tra từng course: nếu đã có SV đăng ký bất kỳ kỳ nào → báo lỗi
     * - Hỏi confirm
     * - Xóa khỏi Memory.courses và offerings
     */
    void deleteCourses(){
        List<String> del = new ArrayList<>();

        // Duyệt bảng, lấy mã HP của các dòng có checkbox = true
        for (int i=0; i < table.getRowCount(); i++){
            Object v = model.getValueAt(i,10);
            if(v instanceof Boolean && (Boolean)v)
                del.add((String)model.getValueAt(i,0));
        }

        if(del.isEmpty()){
            JOptionPane.showMessageDialog(
                    this,
                    "Chọn các dòng cần xóa (cột Chọn)."
            );
            return;
        }

        // Kiểm tra từng course có thể xóa không
        for(String code: del){
            if(!Memory.canDeleteCourse(code)){
                JOptionPane.showMessageDialog(
                        this,
                        "Không thể xóa " + code +
                                " vì đã có sinh viên đăng ký ở một số học kỳ."
                );
                return;
            }
        }

        // Hỏi xác nhận
        int r = JOptionPane.showConfirmDialog(
                this,
                "Xóa " + del.size() + " học phần đã chọn?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION
        );
        if(r != JOptionPane.YES_OPTION) return;

        // Xóa khỏi Memory
        for(String code: del) Memory.deleteCourse(code);

        refresh();
    }

    /** Reset form về trạng thái ban đầu */
    void clearInputs(){
        tfCode.setText("");
        tfName.setText("");
        spCredits.setValue(2);
        cbType.setSelectedIndex(1); // "Tự chọn"
        tfInstructor.setText("");
        tfPrerequisites.setText("");
        cbTermToOpen.setSelectedIndex(0); // "-- Chọn kỳ --"
        cbAllowedProgram.setSelectedItem("Tất cả");
        cbSchedule.setSelectedIndex(0);
        cbTimeSlot.setSelectedIndex(0);
        tfRoom.setText("");
        spMaxCapacity.setValue(50);
        spRegStartDate.setValue(new Date()); // Set về ngày hiện tại
        spRegEndDate.setValue(new Date()); // Set về ngày hiện tại
    }
    
    /**
     * Tạo panel cho phần Duyệt đăng ký học phần
     */
    JPanel createApprovalPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(24, 24, 24, 24));
        
        // Import nội dung từ RegistrationApprovalFrame
        ApprovalPanelContent content = new ApprovalPanelContent();
        panel.add(content.createContent(), BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Inner class để tạo nội dung panel duyệt đăng ký
     */
    class ApprovalPanelContent {
        JTable approvalTable;
        DefaultTableModel approvalModel;
        JTextField searchField = new JTextField();
        JComboBox<String> cbStatus = new JComboBox<>(new String[]{"Tất cả", "Chờ xử lý", "Đã duyệt", "Đã từ chối"});
        JComboBox<String> cbTerm = new JComboBox<>();
        JComboBox<String> cbDept = new JComboBox<>(new String[]{"Tất cả"});
        JLabel paginationInfoLabel;
        
        JPanel createContent() {
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setOpaque(false);
            
            // Filter and Search
            CardPanel filterCard = new CardPanel();
            filterCard.setLayout(new BorderLayout());
            filterCard.setBorder(new EmptyBorder(16, 20, 16, 20));
            filterCard.setBackground(Color.WHITE);

            JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
            filterPanel.setOpaque(false);

            searchField.setPreferredSize(new Dimension(400, 40));
            searchField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                    BorderFactory.createEmptyBorder(10, 14, 10, 14)
            ));
            searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            searchField.setToolTipText("Tìm theo Mã/Tên Sinh viên, Mã/Tên Học phần...");

            cbStatus.setPreferredSize(new Dimension(150, 40));
            cbStatus.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            cbTerm.setPreferredSize(new Dimension(140, 40));
            cbTerm.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            cbTerm.addItem("Tất cả");
            for (String t : Memory.loadTerms()) cbTerm.addItem(t);

            cbDept.setPreferredSize(new Dimension(150, 40));
            cbDept.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            for (String p : Memory.programs) cbDept.addItem(p);

            filterPanel.add(new JLabel("Tìm kiếm:"));
            filterPanel.add(searchField);
            filterPanel.add(new JLabel("Trạng thái:"));
            filterPanel.add(cbStatus);
            filterPanel.add(new JLabel("Học kỳ:"));
            filterPanel.add(cbTerm);
            filterPanel.add(new JLabel("Khoa/Viện:"));
            filterPanel.add(cbDept);

            filterCard.add(filterPanel, BorderLayout.CENTER);
            mainPanel.add(filterCard, BorderLayout.NORTH);

            // Table
            approvalModel = new DefaultTableModel(new Object[]{
                    "MÃ ĐK", "TÊN SINH VIÊN", "MÃ SV", "TÊN HỌC PHẦN",
                    "TÍN CHỈ", "THỜI GIAN ĐK", "TRẠNG THÁI", "HỌC KỲ", "HÀNH ĐỘNG"
            }, 0) {
                @Override
                public boolean isCellEditable(int r, int c) {
                    return false;
                }
            };

            approvalTable = new JTable(approvalModel) {
                @Override
                public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                    Component c = super.prepareRenderer(renderer, row, column);
                    if (!isRowSelected(row)) {
                        c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                    }
                    if (column == 6) {
                        String status = (String) getValueAt(row, column);
                        if ("Đã duyệt".equals(status) || "Thành công".equals(status)) {
                            c.setBackground(new Color(220, 255, 220));
                        } else if ("Từ chối".equals(status) || "Đã từ chối".equals(status)) {
                            c.setBackground(new Color(255, 220, 220));
                        } else if ("Chờ xử lý".equals(status) || "Đã gửi".equals(status) || "Tạm".equals(status)) {
                            c.setBackground(new Color(255, 255, 220));
                        }
                    }
                    return c;
                }
            };
            approvalTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            approvalTable.setRowHeight(50);

            // Custom table header
            JTableHeader header = approvalTable.getTableHeader();
            header.setFont(new Font("Segoe UI", Font.BOLD, 14));
            header.setBackground(new Color(230, 240, 250));
            header.setForeground(new Color(0, 64, 128));
            header.setPreferredSize(new Dimension(0, 50));
            header.setReorderingAllowed(false);

            header.setDefaultRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                        boolean hasFocus, int row, int column) {
                    JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    label.setBackground(new Color(230, 240, 250));
                    label.setForeground(new Color(0, 64, 128));
                    label.setFont(new Font("Segoe UI", Font.BOLD, 14));
                    label.setHorizontalAlignment(SwingConstants.LEFT);
                    label.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createMatteBorder(0, 0, 2, 2, new Color(0, 64, 128)),
                            BorderFactory.createEmptyBorder(14, 14, 14, 14)
                    ));
                    return label;
                }
            });

            approvalTable.setSelectionBackground(new Color(239, 246, 255));
            approvalTable.setSelectionForeground(Color.BLACK);
            approvalTable.setShowGrid(true);
            approvalTable.setGridColor(new Color(220, 220, 220));
            approvalTable.setIntercellSpacing(new Dimension(1, 1));

            // Column widths
            approvalTable.getColumnModel().getColumn(0).setPreferredWidth(100);
            approvalTable.getColumnModel().getColumn(1).setPreferredWidth(200);
            approvalTable.getColumnModel().getColumn(2).setPreferredWidth(120);
            approvalTable.getColumnModel().getColumn(3).setPreferredWidth(250);
            approvalTable.getColumnModel().getColumn(4).setPreferredWidth(80);
            approvalTable.getColumnModel().getColumn(5).setPreferredWidth(150);
            approvalTable.getColumnModel().getColumn(6).setPreferredWidth(120);
            approvalTable.getColumnModel().getColumn(7).setPreferredWidth(0);
            approvalTable.getColumnModel().getColumn(7).setMinWidth(0);
            approvalTable.getColumnModel().getColumn(7).setMaxWidth(0);
            approvalTable.getColumnModel().getColumn(8).setPreferredWidth(200);

            // Custom renderer cho cột HÀNH ĐỘNG
            approvalTable.getColumnModel().getColumn(8).setCellRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                        boolean hasFocus, int row, int column) {
                    JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
                    panel.setOpaque(true);
                    panel.setBackground(isSelected ? table.getSelectionBackground() : 
                        (row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252)));

                    JButton btnApprove = new JButton("✓");
                    btnApprove.setFont(new Font("Segoe UI", Font.BOLD, 16));
                    btnApprove.setBackground(new Color(34, 197, 94));
                    btnApprove.setForeground(Color.WHITE);
                    btnApprove.setBorderPainted(false);
                    btnApprove.setPreferredSize(new Dimension(35, 35));
                    btnApprove.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    btnApprove.setToolTipText("Duyệt");

                    JButton btnReject = new JButton("✗");
                    btnReject.setFont(new Font("Segoe UI", Font.BOLD, 16));
                    btnReject.setBackground(new Color(239, 68, 68));
                    btnReject.setForeground(Color.WHITE);
                    btnReject.setBorderPainted(false);
                    btnReject.setPreferredSize(new Dimension(35, 35));
                    btnReject.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    btnReject.setToolTipText("Từ chối");

                    JButton btnEdit = new JButton("✎");
                    btnEdit.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                    btnEdit.setBackground(new Color(107, 114, 128));
                    btnEdit.setForeground(Color.WHITE);
                    btnEdit.setBorderPainted(false);
                    btnEdit.setPreferredSize(new Dimension(35, 35));
                    btnEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    btnEdit.setToolTipText("Sửa");

                    panel.add(btnApprove);
                    panel.add(btnReject);
                    panel.add(btnEdit);
                    return panel;
                }
            });
            
            // MouseListener để detect click vào buttons
            approvalTable.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    int row = approvalTable.rowAtPoint(e.getPoint());
                    int col = approvalTable.columnAtPoint(e.getPoint());
                    
                    if (row >= 0 && col == 8) {
                        Component comp = approvalTable.prepareRenderer(
                                approvalTable.getCellRenderer(row, col), row, col);
                        Rectangle cellRect = approvalTable.getCellRect(row, col, false);
                        comp.setBounds(cellRect);
                        comp.doLayout();
                        
                        Point relativePoint = new Point(e.getX() - cellRect.x, e.getY() - cellRect.y);
                        Component clickedComp = SwingUtilities.getDeepestComponentAt(comp, relativePoint.x, relativePoint.y);
                        
                        if (clickedComp instanceof JButton) {
                            JButton clickedBtn = (JButton) clickedComp;
                            String tooltip = clickedBtn.getToolTipText();
                            
                            if ("Duyệt".equals(tooltip)) {
                                approveRegistration(row);
                            } else if ("Từ chối".equals(tooltip)) {
                                rejectRegistration(row);
                            } else if ("Sửa".equals(tooltip)) {
                                editRegistration(row);
                            }
                        }
                    }
                }
            });

            JScrollPane scrollPane = new JScrollPane(approvalTable);
            scrollPane.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));
            scrollPane.setViewportBorder(null);

            CardPanel tableCard = new CardPanel();
            tableCard.setLayout(new BorderLayout());
            tableCard.setBorder(new EmptyBorder(20, 20, 20, 20));
            tableCard.setBackground(Color.WHITE);
            tableCard.add(scrollPane, BorderLayout.CENTER);
            mainPanel.add(tableCard, BorderLayout.CENTER);

            // Pagination
            JPanel paginationPanel = new JPanel(new BorderLayout());
            paginationPanel.setOpaque(false);
            paginationPanel.setBorder(new EmptyBorder(16, 20, 16, 20));
            paginationPanel.setBackground(Color.WHITE);

            paginationInfoLabel = new JLabel("Hiển thị 1 - 0 trên 0 kết quả");
            paginationInfoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            paginationInfoLabel.setForeground(new Color(107, 114, 128));

            JPanel paginationControls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            paginationControls.setOpaque(false);
            JButton btnPrev = new JButton("←");
            btnPrev.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            btnPrev.setBackground(Color.WHITE);
            btnPrev.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));
            btnPrev.setPreferredSize(new Dimension(40, 40));
            JButton btnNext = new JButton("→");
            btnNext.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            btnNext.setBackground(Color.WHITE);
            btnNext.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));
            btnNext.setPreferredSize(new Dimension(40, 40));
            paginationControls.add(btnPrev);
            paginationControls.add(btnNext);

            paginationPanel.add(paginationInfoLabel, BorderLayout.WEST);
            paginationPanel.add(paginationControls, BorderLayout.EAST);
            mainPanel.add(paginationPanel, BorderLayout.SOUTH);

            // Actions
            searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                public void changedUpdate(javax.swing.event.DocumentEvent e) { filterApprovalTable(); }
                public void removeUpdate(javax.swing.event.DocumentEvent e) { filterApprovalTable(); }
                public void insertUpdate(javax.swing.event.DocumentEvent e) { filterApprovalTable(); }
            });
            cbStatus.addActionListener(e -> filterApprovalTable());
            cbTerm.addActionListener(e -> filterApprovalTable());
            cbDept.addActionListener(e -> filterApprovalTable());

            // Load dữ liệu
            filterApprovalTable();
            
            return mainPanel;
        }
        
        void filterApprovalTable() {
            String searchText = searchField.getText().toLowerCase();
            String selectedStatus = (String) cbStatus.getSelectedItem();
            String selectedTerm = (String) cbTerm.getSelectedItem();
            String selectedDept = (String) cbDept.getSelectedItem();

            approvalModel.setRowCount(0);
            int regCount = 0;

            for (Map.Entry<String, Map<String, List<RegItem>>> studentEntry : Memory.regs.entrySet()) {
                String studentId = studentEntry.getKey();
                Student student = Memory.studentsById.get(studentId);
                if (student == null) continue;

                if (selectedDept != null && !selectedDept.equals("Tất cả")) {
                    if (!selectedDept.equals(student.program)) continue;
                }

                Map<String, List<RegItem>> termRegs = studentEntry.getValue();
                for (Map.Entry<String, List<RegItem>> termEntry : termRegs.entrySet()) {
                    String term = termEntry.getKey();
                    
                    if (selectedTerm != null && !selectedTerm.equals("Tất cả")) {
                        if (!selectedTerm.equals(term)) continue;
                    }

                    List<RegItem> regItems = termEntry.getValue();
                    for (RegItem item : regItems) {
                        String status = item.status;
                        if ("Tạm".equals(status) || "Đã gửi".equals(status)) {
                            status = "Chờ xử lý";
                        }

                        if (selectedStatus != null && !selectedStatus.equals("Tất cả")) {
                            if (!selectedStatus.equals(status)) continue;
                        }

                        if (!searchText.isEmpty()) {
                            if (!student.fullName.toLowerCase().contains(searchText) &&
                                !studentId.toLowerCase().contains(searchText) &&
                                !item.course.name.toLowerCase().contains(searchText) &&
                                !item.course.code.toLowerCase().contains(searchText)) {
                                continue;
                            }
                        }

                        regCount++;
                        String regCode = "DK" + String.format("%03d", regCount);
                        
                        approvalModel.addRow(new Object[]{
                                regCode, student.fullName, studentId, item.course.name,
                                String.valueOf(item.course.credits), item.date, status, term, ""
                        });
                    }
                }
            }
            
            SwingUtilities.invokeLater(() -> {
                if (paginationInfoLabel != null) {
                    int count = approvalModel.getRowCount();
                    if (count > 0) {
                        paginationInfoLabel.setText("Hiển thị 1 - " + count + " trên " + count + " kết quả");
                    } else {
                        paginationInfoLabel.setText("Không có kết quả nào");
                    }
                }
            });
        }
        
        void approveRegistration(int row) {
            String studentId = (String) approvalModel.getValueAt(row, 2);
            String courseName = (String) approvalModel.getValueAt(row, 3);
            String term = (String) approvalModel.getValueAt(row, 7);

            List<RegItem> regs = Memory.loadReg(studentId, term);
            for (RegItem item : regs) {
                if (item.course.name.equals(courseName)) {
                    item.status = "Đã duyệt";
                    break;
                }
            }

            filterApprovalTable();
            JOptionPane.showMessageDialog(AdminFrame.this, "Đã duyệt đăng ký thành công!");
        }

        void rejectRegistration(int row) {
            String studentId = (String) approvalModel.getValueAt(row, 2);
            String courseName = (String) approvalModel.getValueAt(row, 3);
            String term = (String) approvalModel.getValueAt(row, 7);

            List<RegItem> regs = Memory.loadReg(studentId, term);
            for (RegItem item : regs) {
                if (item.course.name.equals(courseName)) {
                    item.status = "Đã từ chối";
                    break;
                }
            }

            filterApprovalTable();
            JOptionPane.showMessageDialog(AdminFrame.this, "Đã từ chối đăng ký!");
        }

        void editRegistration(int row) {
            JOptionPane.showMessageDialog(AdminFrame.this, "Chức năng sửa đăng ký đang phát triển");
        }
    }
}
