package university.registration.ui;

import university.registration.model.Course;
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
import java.text.SimpleDateFormat;
import java.util.*;

public class StudentRegistrationFrame extends JFrame {

    // Sinh viên đang đăng nhập
    final Student student;

    // Chọn học kỳ & học phần
    JComboBox<String> cbTerm = new JComboBox<>();
    JComboBox<CourseItem> cbCourse = new JComboBox<>();

    // Label tiêu đề bảng, tổng TC, thông báo trống, thông báo khóa kỳ
    JLabel lbTableTitle = new JLabel();
    JLabel lbTotal = new JLabel("0");
    JLabel lbEmpty = new JLabel("Sinh viên chưa đăng ký HP nào trong kỳ này");
    JLabel lbTermLock = new JLabel();

    // Các nút thao tác
    JButton btnAdd, btnSubmit, btnDelete;

    // Bảng & model
    JTable table;
    DefaultTableModel model;
    
    // Tabbed pane để chuyển giữa đăng ký và lịch sử
    JTabbedPane tabbedPane;

    public StudentRegistrationFrame(JFrame owner, Student s){
        this.student = s;

        setTitle("Sinh viên – Đăng ký học phần | " +
                s.fullName + " (" + s.studentId + ")");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        setBackground(new Color(249, 250, 251));
        setLayout(new BorderLayout());

        // ========== TOP HEADER BAR ==========
        JPanel headerBar = new JPanel(new BorderLayout());
        headerBar.setBackground(Color.WHITE);
        headerBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(229, 231, 235)),
                new EmptyBorder(16, 24, 16, 24)
        ));
        
        JLabel headerTitle = new JLabel("Đăng ký Học phần");
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerTitle.setForeground(new Color(31, 41, 55));
        
        JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        headerRight.setOpaque(false);
        
        JLabel lbUser = new JLabel(student.fullName + " (" + student.studentId + ")");
        lbUser.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lbUser.setForeground(new Color(107, 114, 128));
        
        JButton btnLogoutTop = new JButton("Đăng xuất");
        btnLogoutTop.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogoutTop.setBackground(new Color(59, 130, 246));
        btnLogoutTop.setForeground(Color.WHITE);
        btnLogoutTop.setBorderPainted(false);
        btnLogoutTop.setPreferredSize(new Dimension(120, 36));
        btnLogoutTop.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogoutTop.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        headerRight.add(lbUser);
        headerRight.add(btnLogoutTop);
        
        headerBar.add(headerTitle, BorderLayout.WEST);
        headerBar.add(headerRight, BorderLayout.EAST);
        add(headerBar, BorderLayout.NORTH);

        // ========== MAIN CONTENT ==========
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(new Color(249, 250, 251));
        mainContent.setBorder(new EmptyBorder(24, 24, 24, 24));

        // ========== BODY ==========
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.setBorder(BorderFactory.createEmptyBorder());
        
        // Tab 1: Danh mục học phần
        JPanel catalogPanel = createCatalogPanel();
        
        // Tab 2: Đăng ký học phần
        JPanel registrationPanel = new JPanel(new BorderLayout(0, 18));
        registrationPanel.setOpaque(false);
        registrationPanel.setBackground(new Color(249, 250, 251));
        
        // Tab 3: Lịch sử đăng ký
        JPanel historyPanel = createHistoryPanel();
        
        JPanel body = registrationPanel;

        // ----- CARD TÌM KIẾM VÀ ĐĂNG KÝ -----
        CardPanel searchCard = new CardPanel();
        searchCard.setLayout(new BorderLayout());
        searchCard.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(10, 12, 10, 12);
        g.anchor = GridBagConstraints.WEST;

        Font fTopLabel = new Font("Segoe UI", Font.BOLD, 18);
        Font fTopText  = new Font("Segoe UI", Font.PLAIN, 18);

        int row = 0;

        // Tiêu đề form đăng ký
        JLabel formTitle = new JLabel("Đăng ký học phần mới");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        formTitle.setForeground(new Color(16, 82, 138));
        g.gridx = 0; g.gridy = row; g.gridwidth = 5;
        form.add(formTitle, g);
        row++;

        // Hàng 0: Chọn học kỳ + hiển thị CTĐT
        JLabel lbTerm = new JLabel("Học kỳ:");
        lbTerm.setFont(fTopLabel);
        addCell(form, g, 0, row, lbTerm);

        cbTerm.removeAllItems();
        for (String t : Memory.loadTerms()) cbTerm.addItem(t);
        cbTerm.setFont(fTopText);
        cbTerm.setPreferredSize(new Dimension(180, 42));
        addCell(form, g, 1, row, cbTerm);

        JLabel lbProgram = new JLabel("Chương trình: " + student.program);
        lbProgram.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lbProgram.setForeground(new Color(80,80,80));
        lbProgram.setBorder(new EmptyBorder(0, 20, 0, 0));
        addCell(form, g, 2, row, lbProgram, 3);

        row++;

        // Hàng 1: Chọn Mã học phần để đăng ký
        JLabel lbCourse = new JLabel("Chọn học phần:");
        lbCourse.setFont(fTopLabel);
        addCell(form, g, 0, row, lbCourse);

        cbCourse.setFont(fTopText);
        cbCourse.setPreferredSize(new Dimension(500, 42));

        // Renderer custom: hiển thị "CODE - NAME (x TC)"
        cbCourse.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {

                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setFont(fTopText);

                if (value instanceof CourseItem) {
                    CourseItem ci = (CourseItem) value;
                    setText(ci.code + " - " + ci.name + " (" + ci.credits + " TC)");
                }
                return this;
            }
        });

        addCell(form, g, 1, row, cbCourse, 3);

        btnAdd = new JButton("Đăng ký");
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAdd.setBackground(new Color(59, 130, 246));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setBorderPainted(false);
        btnAdd.setPreferredSize(new Dimension(180, 42));
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addCell(form, g, 4, row, btnAdd);

        row++;

        // Hàng 2: Label báo "Học kỳ đang khóa đăng ký" (nếu có)
        lbTermLock.setForeground(new Color(200, 0, 0));
        lbTermLock.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lbTermLock.setBorder(new EmptyBorder(8, 0, 0, 0));
        addCell(form, g, 0, row, lbTermLock, 5);

        CardPanel formCard = new CardPanel();
        formCard.setLayout(new BorderLayout());
        formCard.setBorder(new EmptyBorder(16, 20, 16, 20)); // Giảm padding trên/dưới
        formCard.setBackground(Color.WHITE);
        formCard.add(form, BorderLayout.CENTER);
        body.add(formCard, BorderLayout.NORTH);

        // ----- BẢNG ĐĂNG KÝ -----
        CardPanel tableCard = new CardPanel();
        tableCard.setLayout(new BorderLayout());
        tableCard.setBorder(new EmptyBorder(16, 16, 16, 16)); // Giảm padding để bảng rộng hơn
        tableCard.setBackground(Color.WHITE);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        lbTableTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lbTableTitle.setForeground(new Color(16, 82, 138));
        
        JLabel tableSubtitle = new JLabel("Danh sách học phần đã đăng ký trong học kỳ này");
        tableSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableSubtitle.setForeground(new Color(120, 120, 120));
        titlePanel.add(lbTableTitle, BorderLayout.NORTH);
        titlePanel.add(tableSubtitle, BorderLayout.CENTER);
        titlePanel.setBorder(new EmptyBorder(0, 0, 12, 0));
        
        tableCard.add(titlePanel, BorderLayout.NORTH);

        model = new DefaultTableModel(
                new Object[]{"Mã HP","Tên học phần","Ngày đăng ký",
                        "Trạng thái","Số TC","Chọn"}, 0) {
            @Override public boolean isCellEditable(int r,int c){ return c==5; }
            @Override public Class<?> getColumnClass(int c){
                return (c==5) ? Boolean.class : String.class;
            }
        };

        table = new JTable(model) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                }
                // Tô màu cột trạng thái
                if (column == 3) {
                    String status = (String) getValueAt(row, column);
                    if ("Đã duyệt".equals(status) || "Thành công".equals(status)) {
                        c.setBackground(new Color(220, 255, 220));
                    } else if ("Từ chối".equals(status)) {
                        c.setBackground(new Color(255, 220, 220));
                    } else if ("Đã gửi".equals(status) || "Tạm".equals(status)) {
                        c.setBackground(new Color(255, 255, 220));
                    }
                }
                return c;
            }
        };
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.setRowHeight(48);
        
        // Custom table header với màu xanh nhạt
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setBackground(new Color(230, 240, 250)); // Màu xanh nhạt #E6F0FA
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
        table.setGridColor(new Color(220, 220, 220));
        table.setIntercellSpacing(new Dimension(1, 1));
        
        // Tùy chỉnh độ rộng cột - tăng độ rộng để xem được nhiều thông tin hơn
        table.getColumnModel().getColumn(0).setPreferredWidth(120);  // Mã HP
        table.getColumnModel().getColumn(1).setPreferredWidth(400); // Tên học phần
        table.getColumnModel().getColumn(2).setPreferredWidth(150); // Ngày đăng ký
        table.getColumnModel().getColumn(3).setPreferredWidth(150); // Trạng thái
        table.getColumnModel().getColumn(4).setPreferredWidth(100);  // Số TC
        table.getColumnModel().getColumn(5).setPreferredWidth(80);  // Chọn
        
        // Cho phép table tự động điều chỉnh độ rộng cột
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        sp.setViewportBorder(null);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        tableCard.add(sp, BorderLayout.CENTER);

        lbEmpty.setHorizontalAlignment(SwingConstants.CENTER);
        lbEmpty.setForeground(new Color(120,120,120));
        lbEmpty.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lbEmpty.setBorder(new EmptyBorder(20,0,20,0));
        tableCard.add(lbEmpty, BorderLayout.SOUTH);
        
        // Đặt preferred size cho tableCard để chiếm nhiều không gian hơn
        tableCard.setPreferredSize(new Dimension(0, 600));
        
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        tablePanel.add(tableCard, BorderLayout.CENTER);

        body.add(tablePanel, BorderLayout.CENTER);

        // ----- PHẦN DƯỚI: TỔNG TC + NÚT XOÁ / GỬI -----
        CardPanel actionCard = new CardPanel();
        actionCard.setLayout(new BorderLayout());
        actionCard.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Dòng trên: tổng số TC + nút xóa
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        
        JPanel leftInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftInfo.setOpaque(false);
        JLabel lbSumText = new JLabel("Tổng số tín chỉ đăng ký: ");
        lbSumText.setFont(new Font("Segoe UI", Font.BOLD, 18));
        JLabel lbTotal = this.lbTotal;
        lbTotal.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lbTotal.setForeground(new Color(16, 82, 138));
        leftInfo.add(lbSumText);
        leftInfo.add(lbTotal);
        
        JPanel rightActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightActions.setOpaque(false);
        btnDelete = new JButton("Xóa các HP đã chọn");
        btnDelete.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnDelete.setBackground(Color.WHITE);
        btnDelete.setForeground(new Color(239, 68, 68));
        btnDelete.setBorder(BorderFactory.createLineBorder(new Color(239, 68, 68), 1));
        btnDelete.setPreferredSize(new Dimension(200, 40));
        btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rightActions.add(btnDelete);
        
        topRow.add(leftInfo, BorderLayout.WEST);
        topRow.add(rightActions, BorderLayout.EAST);
        
        // Dòng dưới: nút gửi đăng ký
        JPanel submitRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        submitRow.setOpaque(false);
        submitRow.setBorder(new EmptyBorder(15, 0, 0, 0));
        btnSubmit = new JButton("Gửi đăng ký học phần");
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnSubmit.setBackground(new Color(59, 130, 246));
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setBorderPainted(false);
        btnSubmit.setPreferredSize(new Dimension(300, 50));
        btnSubmit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitRow.add(btnSubmit);
        
        actionCard.add(topRow, BorderLayout.CENTER);
        actionCard.add(submitRow, BorderLayout.SOUTH);
        
        body.add(actionCard, BorderLayout.SOUTH);
        
        // Bây giờ mới thêm các tab vào tabbedPane (sau khi đã tạo xong nội dung)
        tabbedPane.addTab("Danh mục Học phần", catalogPanel);
        tabbedPane.addTab("Đăng ký học phần", registrationPanel);
        tabbedPane.addTab("Lịch sử đăng ký", historyPanel);

        // ====== GẮN ACTION ======
        cbTerm.addActionListener(e -> refreshAll());
        btnAdd.addActionListener(e -> addSelected());
        btnDelete.addActionListener(e -> deleteSelected());
        btnSubmit.addActionListener(e -> submit());

        // Chọn kỳ đầu tiên, sau đó load dữ liệu
        if (cbTerm.getItemCount() > 0) {
            cbTerm.setSelectedIndex(0);
            refreshAll();
        }

        // Thêm tabbedPane vào mainContent
        mainContent.add(tabbedPane, BorderLayout.CENTER);
        add(mainContent, BorderLayout.CENTER);

        setVisible(true);
    }

    // Helper add component vào GridBagLayout
    void addCell(JPanel p, GridBagConstraints g, int x, int y, Component comp) {
        addCell(p, g, x, y, comp, 1);
    }
    void addCell(JPanel p, GridBagConstraints g, int x, int y, Component comp, int w) {
        g.gridx = x; g.gridy = y; g.gridwidth = w;
        p.add(comp, g);
    }

    // Item cho combobox course
    static class CourseItem {
        final String code, name;
        final int credits;
        CourseItem(String c, String n, int cr){ code=c; name=n; credits=cr; }
    }

    /**
     * Tạo panel hiển thị danh mục học phần có thể đăng ký.
     */
    JPanel createCatalogPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(true);
        panel.setBackground(new Color(249, 250, 251));
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        // Search and Filter
        CardPanel filterCard = new CardPanel();
        filterCard.setLayout(new BorderLayout());
        filterCard.setBorder(new EmptyBorder(16, 20, 16, 20));
        filterCard.setBackground(Color.WHITE);
        
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        filterPanel.setOpaque(false);
        
        JLabel searchLabel = new JLabel("Tìm kiếm:");
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        searchLabel.setForeground(new Color(31, 41, 55));
        
        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(300, 40));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                BorderFactory.createEmptyBorder(10, 14, 10, 14)
        ));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        searchField.setToolTipText("Tìm kiếm theo tên hoặc mã học phần...");
        
        JComboBox<String> cbDept = new JComboBox<>(new String[]{"Tất cả"});
        cbDept.setPreferredSize(new Dimension(180, 40));
        cbDept.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        
        JComboBox<String> cbCredits = new JComboBox<>(new String[]{"Tất cả", "1", "2", "3", "4", "5+"});
        cbCredits.setPreferredSize(new Dimension(120, 40));
        cbCredits.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        
        JComboBox<String> cbType = new JComboBox<>(new String[]{"Tất cả", "Bắt buộc", "Tự chọn"});
        cbType.setPreferredSize(new Dimension(140, 40));
        cbType.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        
        JLabel deptLabel = new JLabel("Khoa/Ngành:");
        deptLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        deptLabel.setForeground(new Color(31, 41, 55));
        
        JLabel creditLabel = new JLabel("Số tín chỉ:");
        creditLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        creditLabel.setForeground(new Color(31, 41, 55));
        
        JLabel typeLabel = new JLabel("Loại học phần:");
        typeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        typeLabel.setForeground(new Color(31, 41, 55));
        
        filterPanel.add(searchLabel);
        filterPanel.add(searchField);
        filterPanel.add(deptLabel);
        filterPanel.add(cbDept);
        filterPanel.add(creditLabel);
        filterPanel.add(cbCredits);
        filterPanel.add(typeLabel);
        filterPanel.add(cbType);
        
        filterCard.add(filterPanel, BorderLayout.CENTER);
        panel.add(filterCard, BorderLayout.NORTH);

        // Course List Table
        DefaultTableModel catalogModel = new DefaultTableModel(new Object[]{
                "MÃ HP", "TÊN HỌC PHẦN", "SỐ TÍN CHỈ", "KHOA/BỘ MÔN", "HỌC PHẦN TIÊN QUYẾT"
        }, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable catalogTable = new JTable(catalogModel) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                }
                return c;
            }
        };
        catalogTable.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        catalogTable.setRowHeight(48);
        
        // Custom table header với màu xanh nhạt như hình ảnh
        JTableHeader catalogHeader = catalogTable.getTableHeader();
        catalogHeader.setFont(new Font("Segoe UI", Font.BOLD, 15));
        catalogHeader.setBackground(new Color(230, 240, 250)); // Màu xanh nhạt #E6F0FA
        catalogHeader.setForeground(new Color(0, 64, 128)); // Màu xanh đậm cho text
        catalogHeader.setPreferredSize(new Dimension(0, 50));
        catalogHeader.setReorderingAllowed(false);
        
        // Custom header renderer để đảm bảo hiển thị đúng
        catalogHeader.setDefaultRenderer(new DefaultTableCellRenderer() {
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
        
        catalogTable.setSelectionBackground(new Color(239, 246, 255));
        catalogTable.setSelectionForeground(Color.BLACK);
        catalogTable.setShowGrid(true);
        catalogTable.setGridColor(new Color(220, 220, 220));
        catalogTable.setIntercellSpacing(new Dimension(1, 1));
        
        // Populate catalog function
        Runnable populateCatalog = () -> {
            catalogModel.setRowCount(0);
            String currentTerm = (String) cbTerm.getSelectedItem();
            if (currentTerm == null && !Memory.loadTerms().isEmpty()) {
                currentTerm = Memory.loadTerms().get(0);
            }
            
            String searchText = searchField.getText().toLowerCase();
            String selectedCredits = (String) cbCredits.getSelectedItem();
            
            for (Course c : Memory.courses.values()) {
                var off = Memory.getOffering(currentTerm, c.code);
                if (off != null && off.open) {
                    // Filter by search text
                    if (!searchText.isEmpty()) {
                        if (!c.code.toLowerCase().contains(searchText) && 
                            !c.name.toLowerCase().contains(searchText)) {
                            continue;
                        }
                    }
                    
                    // Filter by credits
                    if (selectedCredits != null && !selectedCredits.equals("Tất cả")) {
                        int credits = c.credits;
                        if (selectedCredits.equals("5+")) {
                            if (credits < 5) continue;
                        } else {
                            if (credits != Integer.parseInt(selectedCredits)) continue;
                        }
                    }
                    
                    // Filter by type (placeholder - Course model doesn't have type field yet)
                    // Can be implemented later when Course model is extended
                    
                    String prereq = "Không có";
                    catalogModel.addRow(new Object[]{
                            c.code,
                            c.name,
                            String.valueOf(c.credits),
                            "Công nghệ thông tin", // Default
                            prereq
                    });
                }
            }
        };
        
        // Thêm action listeners cho filter
        searchField.addActionListener(e -> populateCatalog.run());
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { populateCatalog.run(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { populateCatalog.run(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { populateCatalog.run(); }
        });
        cbDept.addActionListener(e -> populateCatalog.run());
        cbCredits.addActionListener(e -> populateCatalog.run());
        cbType.addActionListener(e -> populateCatalog.run());
        cbTerm.addActionListener(e -> populateCatalog.run());
        
        // Populate initial data
        populateCatalog.run();
        
        JScrollPane catalogSp = new JScrollPane(catalogTable);
        catalogSp.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));
        
        CardPanel catalogCard = new CardPanel();
        catalogCard.setLayout(new BorderLayout());
        catalogCard.setBorder(new EmptyBorder(20, 20, 20, 20));
        catalogCard.setBackground(Color.WHITE);
        catalogCard.add(catalogSp, BorderLayout.CENTER);
        
        panel.add(catalogCard, BorderLayout.CENTER);
        
        // Pagination
        JPanel paginationPanel = new JPanel(new BorderLayout());
        paginationPanel.setOpaque(false);
        paginationPanel.setBorder(new EmptyBorder(16, 0, 0, 0));
        
        JLabel paginationInfo = new JLabel("Hiển thị 1 đến " + catalogModel.getRowCount() + " của " + catalogModel.getRowCount() + " kết quả");
        paginationInfo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        paginationInfo.setForeground(new Color(107, 114, 128));
        
        paginationPanel.add(paginationInfo, BorderLayout.WEST);
        catalogCard.add(paginationPanel, BorderLayout.SOUTH);

        return panel;
    }
    
    /**
     * Tạo panel hiển thị lịch sử đăng ký học phần.
     */
    JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(true);
        panel.setBackground(new Color(249, 250, 251));
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        CardPanel historyCard = new CardPanel();
        historyCard.setLayout(new BorderLayout());
        historyCard.setBorder(new EmptyBorder(20, 20, 20, 20));
        historyCard.setBackground(Color.WHITE);

        JLabel title = new JLabel("Lịch sử đăng ký học phần");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(31, 41, 55));
        title.setBorder(new EmptyBorder(0, 0, 16, 0));
        historyCard.add(title, BorderLayout.NORTH);

        DefaultTableModel historyModel = new DefaultTableModel(
                new Object[]{"Học kỳ", "Mã HP", "Tên học phần", "Số TC", 
                        "Ngày đăng ký", "Trạng thái", "Điểm"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable historyTable = new JTable(historyModel) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                }
                // Color status column
                if (column == 5) {
                    String status = (String) getValueAt(row, column);
                    if ("Đã duyệt".equals(status) || "Thành công".equals(status)) {
                        c.setBackground(new Color(220, 255, 220));
                    } else if ("Từ chối".equals(status)) {
                        c.setBackground(new Color(255, 220, 220));
                    } else if ("Đã gửi".equals(status) || "Tạm".equals(status)) {
                        c.setBackground(new Color(255, 255, 220));
                    }
                }
                return c;
            }
        };
        historyTable.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        historyTable.setRowHeight(48);
        
        // Custom table header với màu xanh nhạt
        JTableHeader historyHeader = historyTable.getTableHeader();
        historyHeader.setFont(new Font("Segoe UI", Font.BOLD, 15));
        historyHeader.setBackground(new Color(230, 240, 250));
        historyHeader.setForeground(new Color(0, 64, 128));
        historyHeader.setPreferredSize(new Dimension(0, 50));
        historyHeader.setReorderingAllowed(false);
        
        // Custom header renderer để đảm bảo hiển thị đúng
        historyHeader.setDefaultRenderer(new DefaultTableCellRenderer() {
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
        
        historyTable.setSelectionBackground(new Color(239, 246, 255));
        historyTable.setSelectionForeground(Color.BLACK);
        historyTable.setShowGrid(true);
        historyTable.setGridColor(new Color(220, 220, 220));
        historyTable.setIntercellSpacing(new Dimension(1, 1));
        
        // Load lịch sử
        var allRegs = Memory.regs.get(student.studentId);
        if (allRegs != null) {
            for (var entry : allRegs.entrySet()) {
                String term = entry.getKey();
                for (RegItem item : entry.getValue()) {
                    historyModel.addRow(new Object[]{
                            term,
                            item.course.code,
                            item.course.name,
                            String.valueOf(item.course.credits),
                            item.date,
                            item.status,
                            "N/A"
                    });
                }
            }
        }
        
        JScrollPane historySp = new JScrollPane(historyTable);
        historySp.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));
        historyCard.add(historySp, BorderLayout.CENTER);
        
        panel.add(historyCard, BorderLayout.CENTER);
        
        return panel;
    }

    /**
     * Xây lại danh sách học phần trong combobox,
     * chỉ bao gồm những HP:
     *  - có Offering cho kỳ chọn
     *  - off.open == true
     *  - allowedProgram = "Tất cả" hoặc đúng CTĐT của sinh viên
     */
    void rebuildCourseList() {
        cbCourse.removeAllItems();
        String term = (String) cbTerm.getSelectedItem();

        for (Course c : Memory.courses.values()) {
            var off = Memory.getOffering(term, c.code);

            boolean allow = (off != null && off.open &&
                    ("Tất cả".equals(off.allowedProgram) ||
                            student.program.equals(off.allowedProgram)));

            if (allow)
                cbCourse.addItem(new CourseItem(c.code, c.name, c.credits));
        }
    }

    /**
     * Refresh toàn bộ UI theo kỳ được chọn:
     *  - cập nhật tiêu đề, thông báo khóa kỳ
     *  - enable/disable nút
     *  - rebuild danh sách HP có thể đăng ký
     *  - refresh bảng đăng ký
     */
    void refreshAll(){
        String term = (String) cbTerm.getSelectedItem();
        boolean open = Memory.isTermOpen(term);

        lbTableTitle.setText(
                "Bảng đăng ký học phần kỳ " + term +
                        " của sinh viên " + student.studentId
        );

        lbTermLock.setText(open ? "" : "Học kỳ đang khóa đăng ký.");

        btnAdd.setEnabled(open);
        btnSubmit.setEnabled(open);

        rebuildCourseList();
        refreshTable();
    }

    /**
     * Load bảng đăng ký hiện tại từ Memory cho kỳ đang chọn.
     * Tính lại tổng số tín chỉ, ẩn/hiện label "lbEmpty".
     */
    void refreshTable(){
        String term=(String)cbTerm.getSelectedItem();
        model.setRowCount(0);

        var list = Memory.loadReg(student.studentId, term);
        int total = 0;

        for(RegItem it : list){
            model.addRow(new Object[]{
                    it.course.code,
                    it.course.name,
                    it.date,
                    it.status,
                    String.valueOf(it.course.credits),
                    false   // cột "Chọn" = false ban đầu
            });
            total += it.course.credits;
        }

        lbTotal.setText(String.valueOf(total));
        lbEmpty.setVisible(list.isEmpty());
    }

    /**
     * Đăng ký học phần đang chọn trong combobox.
     */
    void addSelected() {
        CourseItem ci = (CourseItem) cbCourse.getSelectedItem();
        if (ci == null) {
            JOptionPane.showMessageDialog(this,
                    "Không có học phần phù hợp để đăng ký.");
            return;
        }

        Course c = Memory.courses.get(ci.code);
        String term = (String)cbTerm.getSelectedItem();

        String today = new SimpleDateFormat("yyyy-MM-dd")
                .format(new Date());

        boolean ok = Memory.addReg(
                student.studentId,
                term,
                new RegItem(c, today, "Tạm")
        );

        if(!ok){
            JOptionPane.showMessageDialog(
                    this,
                    "Bạn đã đăng ký học phần này trong học kỳ " + term + "."
            );
            return;
        }

        refreshTable();
        JOptionPane.showMessageDialog(this, "Đã thêm học phần vào danh sách đăng ký tạm.");
    }

    /**
     * Xóa các dòng đã tick ở cột "Chọn".
     */
    void deleteSelected(){
        int rows = model.getRowCount();
        Set<String> del = new HashSet<>();

        for(int i=0; i<rows; i++){
            Object v = model.getValueAt(i,5);
            if(v instanceof Boolean && (Boolean)v)
                del.add((String)model.getValueAt(i,0)); // mã HP
        }

        if(del.isEmpty()){
            JOptionPane.showMessageDialog(
                    this,
                    "Hãy tích chọn những dòng muốn xóa."
            );
            return;
        }

        String term = (String)cbTerm.getSelectedItem();
        Memory.deleteByCourseCodes(student.studentId, term, del);
        refreshTable();
    }

    /**
     * Gửi kết quả đăng ký:
     *  - Nếu chưa có HP nào → báo
     *  - Ngược lại: set status = "Đã gửi" cho toàn bộ RegItem của kỳ
     */
    void submit(){
        String term = (String)cbTerm.getSelectedItem();
        var list = Memory.loadReg(student.studentId, term);

        if(list.isEmpty()){
            JOptionPane.showMessageDialog(
                    this,
                    "Chưa có học phần nào để gửi."
            );
            return;
        }

        for(RegItem it : list)
            it.status = "Đã gửi";

        refreshTable();
        JOptionPane.showMessageDialog(
                this,
                "Đã gửi kết quả đăng ký cho kỳ " + term + "."
        );
    }
}
