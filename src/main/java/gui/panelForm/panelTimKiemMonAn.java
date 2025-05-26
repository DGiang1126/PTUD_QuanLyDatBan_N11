package gui.panelForm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

import dao.DAO_MonAn;
import entity.MonAn;
import gui.tabbed.CheckBoxTableHeaderRenderer;
import gui.tabbed.TableHeaderAlignment;

public class panelTimKiemMonAn extends JPanel {
    private JTable table;
    private JLabel lbTitle;
    private JTextField txtSearch;
    private JComboBox<String> cbSearchCriteria;
    private JButton btnRefresh;
    private DAO_MonAn daoMonAn;

    public panelTimKiemMonAn() {
        try {
            daoMonAn = new DAO_MonAn();
            initComponents();
            init();
            loadDataToTable(); // Load dữ liệu khi khởi tạo
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khởi tạo: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initComponents() {
        setSize(1535, 850);
        FlatRobotoFont.install();
        FlatLaf.registerCustomDefaultsSource("themes");
        FlatIntelliJLaf.setup();
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));

        lbTitle = new JLabel("TÌM KIẾM MÓN ĂN");
        lbTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        
        UIManager.put("Panel.background", new Color(247, 248, 252)); // Trắng xám nhạt nhẹ nhàng
        UIManager.put("Button.background", new Color(52, 102, 255)); // Xanh dương nhẹ cho nút
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.disabledBackground", new Color(209, 213, 219)); // Xám nhạt khi vô hiệu
        UIManager.put("Label.foreground", new Color(17, 24, 39)); // Xám đen đậm cho chữ
        UIManager.put("Component.borderColor", new Color(229, 231, 235)); // Viền xám nhạt
        
        // Thanh tìm kiếm với placeholder
        String placeholder = "Tìm kiếm món ăn tại đây";
        txtSearch = new JTextField();
        txtSearch.setBounds(30, 60, 238, 35);
        txtSearch.setText(placeholder);
        txtSearch.setForeground(Color.GRAY);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtSearch.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtSearch.getText().equals(placeholder)) {
                    txtSearch.setText("");
                    txtSearch.setForeground(Color.BLACK);
                    txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtSearch.getText().isEmpty()) {
                    txtSearch.setText(placeholder);
                    txtSearch.setForeground(Color.GRAY);
                    txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                }
            }
        });

        // Nút tìm kiếm bên trong
        JButton searchButton = new JButton();
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/img/search.png"));
            Image scaledIcon = icon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            searchButton.setIcon(new ImageIcon(scaledIcon));
        } catch (Exception e) {
            searchButton.setText("🔍");
        }
        searchButton.setBorder(null);
        searchButton.setBackground(txtSearch.getBackground());
        searchButton.setFocusPainted(false);
        searchButton.setContentAreaFilled(false);
        txtSearch.setLayout(new BorderLayout());
        txtSearch.add(searchButton, BorderLayout.EAST);
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
            txtSearch.getBorder(),
            BorderFactory.createEmptyBorder(0, 5, 0, 5)
        ));
        add(txtSearch);

        // ComboBox tiêu chí tìm kiếm
        cbSearchCriteria = new JComboBox<>();
        cbSearchCriteria.setBounds(278, 60, 150, 35);
        cbSearchCriteria.addItem("Mã món ăn");
        cbSearchCriteria.addItem("Tên món ăn");
        add(cbSearchCriteria);

        // Nút làm mới
        btnRefresh = new JButton("Làm mới");
        btnRefresh.setBounds(438, 60, 100, 35);
        btnRefresh.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnRefresh.setBackground(new Color(52, 102, 255));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        add(btnRefresh);

        RoundedScrollPane scrollPane = new RoundedScrollPane(table, 30);
        scrollPane.setBounds(30, 120, 1474, 700);
        scrollPane.setBackground(Color.WHITE);
        table = new JTable();

        table.setModel(new DefaultTableModel(
                new Object[][] {},
                new String[] {
                        "#", "STT", "Mã món ăn", "Tên món ăn", "Giá", "Loại món ăn", "Ghi chú"
                }
        ) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                Class[] types = new Class[] {
                        Boolean.class,
                        Object.class,
                        Object.class,
                        Object.class,
                        Object.class,
                        Object.class,
                        Object.class
                };
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return columnIndex == 0;
            }
        });

        table.getTableHeader().setReorderingAllowed(false);
        scrollPane.setViewportView(table);

        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setMaxWidth(100);
            table.getColumnModel().getColumn(1).setPreferredWidth(100);
            table.getColumnModel().getColumn(2).setPreferredWidth(150);
            table.getColumnModel().getColumn(3).setPreferredWidth(200);
            table.getColumnModel().getColumn(4).setPreferredWidth(150);
            table.getColumnModel().getColumn(5).setPreferredWidth(150);
            table.getColumnModel().getColumn(6).setPreferredWidth(300);
        }

        table.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxTableHeaderRenderer(table, 0));
        table.getTableHeader().setDefaultRenderer(new TableHeaderAlignment(table));

        setLayout(null);
        add(scrollPane);

        JLabel lblNewLabel = new JLabel("TÌM KIẾM MÓN ĂN");
        lblNewLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setBounds(18, 10, 1507, 27);
        add(lblNewLabel);
        
        // Sự kiện cho nút tìm kiếm
        searchButton.addActionListener(e -> {
            String query = txtSearch.getText();
            String criteria = cbSearchCriteria.getSelectedItem().toString();
            if (!query.equals(placeholder) && !query.isEmpty()) {
                try {
                    List<MonAn> result = daoMonAn.searchMonAn(query, criteria);
                    
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    model.setRowCount(0); // Xóa dữ liệu cũ
                    int stt = 1;
                    for (MonAn ma : result) {
                        model.addRow(new Object[] {
                            Boolean.FALSE,
                            stt++,
                            ma.getMaMonAn(),
                            ma.getTenMonAn(),
                            ma.getGia(),
                            ma.getLoaiMonAn(),
                            ma.getGhiChu()
                        });
                    }
                    if (result.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Không tìm thấy món ăn!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Lỗi khi tìm kiếm: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập từ khóa tìm kiếm!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Sự kiện cho nút làm mới
        btnRefresh.addActionListener(e -> {
            try {
                loadDataToTable();
                txtSearch.setText(placeholder);
                txtSearch.setForeground(Color.GRAY);
                cbSearchCriteria.setSelectedIndex(0);
                JOptionPane.showMessageDialog(null, "Dữ liệu đã được làm mới!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Lỗi khi làm mới dữ liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void init() {
        table.getTableHeader().putClientProperty(FlatClientProperties.STYLE, ""
                + "height:30;"
                + "hoverBackground:null;"
                + "pressedBackground:null;"
                + "separatorColor:$TableHeader.background;"
                + "font:bold;");

        table.putClientProperty(FlatClientProperties.STYLE, ""
                + "rowHeight:70;"
                + "showHorizontalLines:true;"
                + "intercellSpacing:0,1;"
                + "cellFocusColor:$TableHeader.hoverBackground;"
                + "selectionBackground:$TableHeader.hoverBackground;"
                + "selectionForeground:$Table.foreground;");
    }

    // Hàm load dữ liệu từ DAO vào bảng
    private void loadDataToTable() {
        try {
            List<MonAn> list = daoMonAn.getAllMonAn();
            
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0); // Xóa dữ liệu cũ
            int stt = 1;
            for (MonAn ma : list) {
                model.addRow(new Object[] {
                    Boolean.FALSE,
                    stt++,
                    ma.getMaMonAn(),
                    ma.getTenMonAn(),
                    ma.getGia(),
                    ma.getLoaiMonAn(),
                    ma.getGhiChu()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tải dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Class RoundedScrollPane
    class RoundedScrollPane extends JScrollPane {
        private int cornerRadius;

        public RoundedScrollPane(Component view, int radius) {
            super(view);
            this.cornerRadius = radius;
            setOpaque(false);
            setBorder(new EmptyBorder(5, 5, 5, 5));
            getViewport().setOpaque(false);
            getViewport().setBackground(new Color(0, 0, 0, 0));
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.GRAY);
            g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius));
            g2.dispose();
        }
    }
}