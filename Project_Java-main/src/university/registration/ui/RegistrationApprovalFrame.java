package university.registration.ui;

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
import java.util.List;
import java.util.Map;

public class RegistrationApprovalFrame extends JFrame {

    // Bảng & model hiển thị danh sách đăng ký
    JTable table;
    DefaultTableModel model;
    
    // Filter và search
    JTextField searchField = new JTextField();
    JComboBox<String> cbStatus = new JComboBox<>(new String[]{"Tất cả", "Chờ xử lý", "Đã duyệt", "Đã từ chối"});
    JComboBox<String> cbTerm = new JComboBox<>();
    JComboBox<String> cbDept = new JComboBox<>(new String[]{"Tất cả"});

    public RegistrationApprovalFrame(JFrame owner) {
        setTitle("PĐT – Duyệt đăng ký học phần");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1400, 800);
        setLocationRelativeTo(owner);

        setBackground(new Color(249, 250, 251));
        setLayout(new BorderLayout());

        // ========== HEADER ==========
        JPanel headerBar = new JPanel(new BorderLayout());
        headerBar.setBackground(Color.WHITE);
        headerBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(229, 231, 235)),
                new EmptyBorder(16, 24, 16, 24)
        ));

        JLabel headerTitle = new JLabel("Quản lý Đăng ký Học phần");
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerTitle.setForeground(new Color(31, 41, 55));

        JLabel headerSub = new JLabel("Duyệt, từ chối hoặc chỉnh sửa trạng thái đăng ký học phần của sinh viên.");
        headerSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        headerSub.setForeground(new Color(107, 114, 128));

        JPanel headerLeft = new JPanel();
        headerLeft.setOpaque(false);
        headerLeft.setLayout(new BoxLayout(headerLeft, BoxLayout.Y_AXIS));
        headerLeft.add(headerTitle);
        headerLeft.add(Box.createVerticalStrut(4));
        headerLeft.add(headerSub);

        JButton btnAdd = new JButton("+ Thêm Đăng ký Mới");
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAdd.setBackground(new Color(59, 130, 246));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setBorderPainted(false);
        btnAdd.setPreferredSize(new Dimension(200, 40));
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Chức năng thêm đăng ký mới đang phát triển");
        });

        headerBar.add(headerLeft, BorderLayout.WEST);
        headerBar.add(btnAdd, BorderLayout.EAST);
        add(headerBar, BorderLayout.NORTH);

        // ========== FILTER AND SEARCH ==========
        CardPanel filterCard = new CardPanel();
        filterCard.setLayout(new BorderLayout());
        filterCard.setBorder(new EmptyBorder(16, 20, 16, 20));
        filterCard.setBackground(Color.WHITE);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        filterPanel.setOpaque(false);

        // Search field
        searchField.setPreferredSize(new Dimension(400, 40));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                BorderFactory.createEmptyBorder(10, 14, 10, 14)
        ));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setToolTipText("Tìm theo Mã/Tên Sinh viên, Mã/Tên Học phần...");

        // Status filter
        cbStatus.setPreferredSize(new Dimension(150, 40));
        cbStatus.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Term filter
        cbTerm.setPreferredSize(new Dimension(140, 40));
        cbTerm.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbTerm.addItem("Tất cả");
        for (String t : Memory.loadTerms()) cbTerm.addItem(t);

        // Department filter
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
        add(filterCard, BorderLayout.NORTH);

        // ========== TABLE ==========
        model = new DefaultTableModel(new Object[]{
                "MÃ ĐK", "TÊN SINH VIÊN", "MÃ SV", "TÊN HỌC PHẦN",
                "TÍN CHỈ", "THỜI GIAN ĐK", "TRẠNG THÁI", "HỌC KỲ", "HÀNH ĐỘNG"
        }, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return c == 8; // Chỉ cột HÀNH ĐỘNG có thể chỉnh sửa
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
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(50);

        // Custom table header
        JTableHeader header = table.getTableHeader();
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

        table.setSelectionBackground(new Color(239, 246, 255));
        table.setSelectionForeground(Color.BLACK);
        table.setShowGrid(true);
        table.setGridColor(new Color(220, 220, 220));
        table.setIntercellSpacing(new Dimension(1, 1));

        // Tùy chỉnh độ rộng cột
        table.getColumnModel().getColumn(0).setPreferredWidth(100);  // MÃ ĐK
        table.getColumnModel().getColumn(1).setPreferredWidth(200); // TÊN SINH VIÊN
        table.getColumnModel().getColumn(2).setPreferredWidth(120); // MÃ SV
        table.getColumnModel().getColumn(3).setPreferredWidth(250); // TÊN HỌC PHẦN
        table.getColumnModel().getColumn(4).setPreferredWidth(80);  // TÍN CHỈ
        table.getColumnModel().getColumn(5).setPreferredWidth(150); // THỜI GIAN ĐK
        table.getColumnModel().getColumn(6).setPreferredWidth(120); // TRẠNG THÁI
        table.getColumnModel().getColumn(7).setPreferredWidth(0);   // HỌC KỲ (ẩn)
        table.getColumnModel().getColumn(7).setMinWidth(0);
        table.getColumnModel().getColumn(7).setMaxWidth(0);
        table.getColumnModel().getColumn(8).setPreferredWidth(200); // HÀNH ĐỘNG

        // Custom renderer và editor cho cột HÀNH ĐỘNG
        table.getColumnModel().getColumn(8).setCellRenderer(new DefaultTableCellRenderer() {
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
        
        // Thêm MouseListener để detect click vào buttons
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                
                if (row >= 0 && col == 8) { // Cột HÀNH ĐỘNG
                    Component comp = table.prepareRenderer(table.getCellRenderer(row, col), row, col);
                    Rectangle cellRect = table.getCellRect(row, col, false);
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

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));
        scrollPane.setViewportBorder(null);

        CardPanel tableCard = new CardPanel();
        tableCard.setLayout(new BorderLayout());
        tableCard.setBorder(new EmptyBorder(20, 20, 20, 20));
        tableCard.setBackground(Color.WHITE);
        tableCard.add(scrollPane, BorderLayout.CENTER);

        add(tableCard, BorderLayout.CENTER);

        // ========== PAGINATION ==========
        JPanel paginationPanel = new JPanel(new BorderLayout());
        paginationPanel.setOpaque(false);
        paginationPanel.setBorder(new EmptyBorder(16, 20, 16, 20));
        paginationPanel.setBackground(Color.WHITE);

        JLabel paginationInfo = new JLabel("Hiển thị 1 - 0 trên 0 kết quả");
        paginationInfo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        paginationInfo.setForeground(new Color(107, 114, 128));

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

        paginationPanel.add(paginationInfo, BorderLayout.WEST);
        paginationPanel.add(paginationControls, BorderLayout.EAST);
        add(paginationPanel, BorderLayout.SOUTH);

        // ========== GẮN ACTION ==========
        // Make paginationInfo accessible in filterTable
        paginationInfoLabel = paginationInfo;
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
        });
        cbStatus.addActionListener(e -> filterTable());
        cbTerm.addActionListener(e -> filterTable());
        cbDept.addActionListener(e -> filterTable());

        // Load dữ liệu ban đầu
        filterTable();
        setVisible(true);
    }


    JLabel paginationInfoLabel;
    
    /**
     * Lọc bảng theo search text, status, term, và department
     */
    void filterTable() {
        String searchText = searchField.getText().toLowerCase();
        String selectedStatus = (String) cbStatus.getSelectedItem();
        String selectedTerm = (String) cbTerm.getSelectedItem();
        String selectedDept = (String) cbDept.getSelectedItem();

        // Load lại toàn bộ dữ liệu
        model.setRowCount(0);
        int regCount = 0;

        // Duyệt tất cả sinh viên và các học kỳ của họ
        for (Map.Entry<String, Map<String, List<RegItem>>> studentEntry : Memory.regs.entrySet()) {
            String studentId = studentEntry.getKey();
            Student student = Memory.studentsById.get(studentId);
            if (student == null) continue;

            // Filter by department
            if (selectedDept != null && !selectedDept.equals("Tất cả")) {
                if (!selectedDept.equals(student.program)) {
                    continue;
                }
            }

            Map<String, List<RegItem>> termRegs = studentEntry.getValue();
            for (Map.Entry<String, List<RegItem>> termEntry : termRegs.entrySet()) {
                String term = termEntry.getKey();
                
                // Filter by term
                if (selectedTerm != null && !selectedTerm.equals("Tất cả")) {
                    if (!selectedTerm.equals(term)) {
                        continue;
                    }
                }

                List<RegItem> regItems = termEntry.getValue();
                for (RegItem item : regItems) {
                    // Chuẩn hóa trạng thái
                    String status = item.status;
                    if ("Tạm".equals(status) || "Đã gửi".equals(status)) {
                        status = "Chờ xử lý";
                    }

                    // Filter by status
                    if (selectedStatus != null && !selectedStatus.equals("Tất cả")) {
                        if (!selectedStatus.equals(status)) {
                            continue;
                        }
                    }

                    // Filter by search text
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
                    
                    model.addRow(new Object[]{
                            regCode,
                            student.fullName,
                            studentId,
                            item.course.name,
                            String.valueOf(item.course.credits),
                            item.date,
                            status,
                            term,
                            ""
                    });
                }
            }
        }
        
        // Update pagination after filtering
        SwingUtilities.invokeLater(() -> {
            if (paginationInfoLabel != null) {
                int count = model.getRowCount();
                if (count > 0) {
                    paginationInfoLabel.setText("Hiển thị 1 - " + count + " trên " + count + " kết quả");
                } else {
                    paginationInfoLabel.setText("Không có kết quả nào");
                }
            }
        });
    }

    /**
     * Duyệt đăng ký tại row được chỉ định
     */
    void approveRegistration(int row) {
        String studentId = (String) model.getValueAt(row, 2);
        String courseName = (String) model.getValueAt(row, 3);
        String term = (String) model.getValueAt(row, 7);

        // Cập nhật status trong Memory
        List<RegItem> regs = Memory.loadReg(studentId, term);
        for (RegItem item : regs) {
            if (item.course.name.equals(courseName)) {
                item.status = "Đã duyệt";
                break;
            }
        }

        filterTable();
        JOptionPane.showMessageDialog(this, "Đã duyệt đăng ký thành công!");
    }

    /**
     * Từ chối đăng ký tại row được chỉ định
     */
    void rejectRegistration(int row) {
        String studentId = (String) model.getValueAt(row, 2);
        String courseName = (String) model.getValueAt(row, 3);
        String term = (String) model.getValueAt(row, 7);

        // Cập nhật status trong Memory
        List<RegItem> regs = Memory.loadReg(studentId, term);
        for (RegItem item : regs) {
            if (item.course.name.equals(courseName)) {
                item.status = "Đã từ chối";
                break;
            }
        }

        filterTable();
        JOptionPane.showMessageDialog(this, "Đã từ chối đăng ký!");
    }

    /**
     * Sửa đăng ký tại row được chỉ định
     */
    void editRegistration(int row) {
        JOptionPane.showMessageDialog(this, "Chức năng sửa đăng ký đang phát triển");
    }
}

