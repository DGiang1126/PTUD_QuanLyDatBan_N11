package gui.panelForm;

import dao.DAO_Ban;
import entity.Ban;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

public class panelQlyban extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private DAO_Ban daoBan;
    private JComboBox<String> statusComboBox;
    private JTextField tableNameField;
    private List<Ban> banList; // Store the list of tables
    private boolean isUpdating = false; // Flag to prevent recursive updates

    public panelQlyban() {
        daoBan = new DAO_Ban();
        banList = new ArrayList<>();
        initializeUI();
        loadDataFromDatabase();
    }

    private void initializeUI() {

        setLayout(null);

        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(null);
        filterPanel.setBounds(10, 10, 200, 800); 
        filterPanel.setBorder(BorderFactory.createTitledBorder("Bộ lọc"));

        JLabel statusLabel = new JLabel("Trạng thái");
        statusLabel.setBounds(10, 30, 180, 25);
        filterPanel.add(statusLabel);

        statusComboBox = new JComboBox<>(new String[]{"CÒN TRỐNG", "ĐÃ ĐẶT", "ĐANG SỬ DỤNG", "BẢO TRÌ"});
        statusComboBox.setBounds(10, 55, 160, 30);
        statusComboBox.setEnabled(false);
        statusComboBox.addActionListener(e -> {
            if (!isUpdating && statusComboBox.isEnabled() && table.getSelectedRow() != -1) {
                updateSelectedTableStatus();
            }
        });
        filterPanel.add(statusComboBox);


        JLabel tableNameLabel = new JLabel("Mã bàn");

        tableNameLabel.setBounds(10, 100, 180, 25);
        filterPanel.add(tableNameLabel);

        // TextField nhập tên bàn
        tableNameField = new JTextField();
        tableNameField.setBounds(10, 125, 160, 30);
        tableNameField.addActionListener(e -> filterData());
        filterPanel.add(tableNameField);

        add(filterPanel); 
        
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableModel.addColumn("Mã bàn");
        tableModel.addColumn("Tên bàn");
        tableModel.addColumn("Trạng thái");
        tableModel.addColumn("Tầng");
        tableModel.addColumn("Số chỗ");

        
        table = new JTable(tableModel);
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(80);
        columnModel.getColumn(1).setPreferredWidth(100);
        columnModel.getColumn(2).setPreferredWidth(120);
        columnModel.getColumn(3).setPreferredWidth(80);
        columnModel.getColumn(4).setPreferredWidth(80);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    String status = (String) tableModel.getValueAt(selectedRow, 2);
                    isUpdating = true;
                    statusComboBox.removeAllItems();
                    statusComboBox.addItem("CÒN TRỐNG");
                    statusComboBox.addItem("ĐÃ ĐẶT");
                    statusComboBox.addItem("ĐANG SỬ DỤNG");
                    statusComboBox.addItem("BẢO TRÌ");
                    statusComboBox.setSelectedItem(status);
                    if (status.equals("CÒN TRỐNG") || status.equals("BẢO TRÌ")) {
                        statusComboBox.removeAllItems();
                        statusComboBox.addItem("CÒN TRỐNG");
                        statusComboBox.addItem("BẢO TRÌ");
                        statusComboBox.setSelectedItem(status);
                        statusComboBox.setEnabled(true);
                    } else {
                        statusComboBox.setEnabled(false);
                    }
                    isUpdating = false;
                }
            }
        });

        RoundedScrollPane scrollPane = new RoundedScrollPane(table,30);
        scrollPane.setBounds(220, 10, 1300, 800); 
        add(scrollPane);
    }

    private void updateSelectedTableStatus() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String tenBan = (String) tableModel.getValueAt(selectedRow, 1);
            String currentStatus = (String) tableModel.getValueAt(selectedRow, 2);
            String newStatus = (String) statusComboBox.getSelectedItem();

            if ((currentStatus.equals("CÒN TRỐNG") && newStatus.equals("BẢO TRÌ")) ||
                (currentStatus.equals("BẢO TRÌ") && newStatus.equals("CÒN TRỐNG"))) {
                if (daoBan.capNhatTrangThaiBan(tenBan, newStatus)) {
                    loadDataFromDatabase();
                    filterData();
                    JOptionPane.showMessageDialog(null, "Cập nhật trạng thái thành công!");
                    table.repaint(); 
                    tableModel.setValueAt(newStatus, selectedRow, 2);
                } else {
                    isUpdating = true; 
                    statusComboBox.setSelectedItem(currentStatus); 
                    isUpdating = false;
                    JOptionPane.showMessageDialog(null, "Cập nhật trạng thái thất bại! Kiểm tra console để biết thêm chi tiết.");
                }
            } else {
                isUpdating = true; 
                statusComboBox.setSelectedItem(currentStatus);
                isUpdating = false;
                JOptionPane.showMessageDialog(null, "Chỉ có thể chuyển giữa CÒN TRỐNG và BẢO TRÌ!");
            }
        }
    }

    private void loadDataFromDatabase() {
        banList = daoBan.getAllBans();
        tableModel.setRowCount(0);
        for (Ban ban : banList) {
            System.out.println("Loading table: " + ban.getTenBan() + ", Số chỗ: " + ban.getSoCho());
            tableModel.addRow(new Object[]{
                ban.getMaBan(),
                ban.getTenBan(),
                ban.getTrangThai(),
                ban.getTang(),
                String.valueOf(ban.getSoCho()) // Convert int to String for display
            });
        }
    }

    private void filterData() {
        String maBanFilter = tableNameField.getText().trim().toLowerCase();
        tableModel.setRowCount(0);
        for (Ban ban : banList) {
            boolean idMatch = maBanFilter.isEmpty() || ban.getMaBan().toLowerCase().contains(maBanFilter);
            if (idMatch) {
                tableModel.addRow(new Object[]{
                    ban.getMaBan(),
                    ban.getTenBan(),
                    ban.getTrangThai(),
                    ban.getTang(),
                    String.valueOf(ban.getSoCho())
                });
            }
        }
    }

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