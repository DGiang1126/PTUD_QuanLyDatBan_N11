package gui.panelForm;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

import dao.DAO_NhanVien;

import entity.NhanVien;

public class panelTKNhanVien extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField textField;
    private JTextField textField_1;
    private JTextField textField_2;
    private JTextField textField_5;
    private JTextField textField_6;
    private JTextField textField_7;
    private JTextField textField_8;
    private JTable table;
    private RoundedPane panelThongTin;
    private RoundedScrollPane scrollPane;
    private DAO_NhanVien DAONV;
    private JLabel lblNewLabel_1;
    private JButton btnChooseImage;
    private String selectedImagePath;
    private JComboBox<String> comboBoxGioiTinh;
    private JComboBox<String> comboBoxChucVu;
    private JTextField txtTimKiem;
    private TableRowSorter<DefaultTableModel> sorter;
    private DefaultTableModel tableModel;
    private Rectangle initialScrollPaneBounds = new Rectangle(13, 65, 1515, 750);
    private Rectangle expandedScrollPaneBounds = new Rectangle(13, 325, 1515, 505); 
    private Rectangle panelThongTinBounds = new Rectangle(13, 65, 1515, 250);
    private boolean isPanelThongTinVisible = false;
	private AbstractButton btnDelete;
	private AbstractButton btnUpdate;
	private AbstractButton btnAdd;

    public panelTKNhanVien() throws Exception {
        setBackground(SystemColor.controlHighlight);
        setLayout(null); // Sử dụng null layout để thiết lập bounds tuyệt đối
        setSize(1535, 850);

        FlatRobotoFont.install();
        FlatLaf.registerCustomDefaultsSource("themes");
        FlatIntelliJLaf.setup();
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));

        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelTop.setBackground(SystemColor.controlHighlight);
        panelTop.setBounds(10, 25, 500, 40); // Thiết lập bounds cho panel top
        add(panelTop);

        JLabel lblTimKiem = new JLabel("Tìm kiếm:");
        lblTimKiem.setFont(new Font("Tahoma", Font.BOLD, 13));
        panelTop.add(lblTimKiem);

        txtTimKiem = new JTextField();
        txtTimKiem.setFont(new Font("Tahoma", Font.PLAIN, 13));
        txtTimKiem.setColumns(20);
        panelTop.add(txtTimKiem);

        JButton btnTimKiem = new JButton("Tìm");
        btnTimKiem.setBackground(Color.BLUE);
        btnTimKiem.setForeground(Color.WHITE);
        btnTimKiem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
					filterAndShowDetails();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
        panelTop.add(btnTimKiem);

        panelThongTin = new RoundedPane(30);
        panelThongTin.setPreferredSize(new java.awt.Dimension(1480, 250));
        panelThongTin.setLayout(null);
        panelThongTin.setVisible(false); // Ẩn panel thông tin ban đầu
        panelThongTin.setBounds(panelThongTinBounds); // Thiết lập bounds ban đầu
        add(panelThongTin);

        lblNewLabel_1 = new JLabel("");
        lblNewLabel_1.setBounds(50, 50, 183, 150);
        lblNewLabel_1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panelThongTin.add(lblNewLabel_1);

        // Khởi tạo các JTextField
        textField = new JTextField();
        textField_1 = new JTextField();
        textField_2 = new JTextField();
        textField_5 = new JTextField();
        textField_6 = new JTextField();
        textField_7 = new JTextField();
        textField_8 = new JTextField();

        // Khởi tạo JComboBox cho Giới tính
        comboBoxGioiTinh = new JComboBox<>(new String[] { "Nam", "Nữ" });

        // Khởi tạo JComboBox cho Chức vụ
        comboBoxChucVu = new JComboBox<>(new String[] { "Nhân viên lễ tân", "Nhân viên quản lý" });

        String[] labels = {"Mã NV:", "Tên NV:", "SĐT:", "Giới tính:", "Chức vụ:", "Tuổi:", "Email:", "Hệ số lương:", "Lương:"};
        int[] x1 = {400, 400, 400, 400, 930, 930, 930, 930, 400};
        int[] x2 = {520, 520, 520, 520, 1060, 1060, 1060, 1060, 520};
        int[] y = {62, 92, 122, 152, 62, 92, 122, 152, 182};

        JTextField[] textFields = {textField, textField_1, textField_2, null, null,
                                 textField_5, textField_6, textField_7, textField_8};

        for (int i = 0; i < 9; i++) {
            JLabel label = new JLabel(labels[i]);
            label.setFont(new Font("Tahoma", Font.BOLD, 13));
            label.setBounds(x1[i], y[i], 115, 19);
            panelThongTin.add(label);

            if (i == 3) {
                comboBoxGioiTinh.setBounds(x2[i], y[i], 270, 25);
                panelThongTin.add(comboBoxGioiTinh);
            } else if (i == 4) {
                comboBoxChucVu.setBounds(x2[i], y[i], 270, 25);
                panelThongTin.add(comboBoxChucVu);
            } else if (textFields[i] != null) {
                textFields[i].setBounds(x2[i], y[i], 270, 25);
                panelThongTin.add(textFields[i]);
                textFields[i].setColumns(10);
            }
        }

        JLabel lblNewLabel_10 = new JLabel("Thông tin nhân viên");
        lblNewLabel_10.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblNewLabel_10.setBounds(50, 10, 183, 27);
        panelThongTin.add(lblNewLabel_10);

        DAONV = new DAO_NhanVien();

        String[] columnNames = {"Ảnh", "Mã NV", "Tên NV", "SĐT", "Giới tính", "Chức vụ", "Tuổi", "Email", "Hệ số lương", "Lương"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public Class<?> getColumnClass(int column) {
                if (column == 0) return ImageIcon.class;
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        table.setRowHeight(40); // Giảm chiều cao dòng để phù hợp với ảnh 36x36
        table.getColumnModel().getColumn(0).setCellRenderer(new ImageRenderer(36)); // Set ảnh về 36x36

        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);

        // Ngăn sắp xếp khi nhấp vào header
        table.setAutoCreateRowSorter(false);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1 && selectedRow < table.getRowCount()) {
                    // Hiển thị panelThongTin khi chọn một hàng
                    panelThongTin.setVisible(true);
                    isPanelThongTinVisible = true;
                    panelThongTin.setBounds(panelThongTinBounds);
                    scrollPane.setBounds(expandedScrollPaneBounds);
                    updateTextFieldsFromSelectedRow();
                } else {
                    // Ẩn panelThongTin nếu không có hàng nào được chọn
                    panelThongTin.setVisible(false);
                    isPanelThongTinVisible = false;
                    scrollPane.setBounds(initialScrollPaneBounds);
                    clearForm();
                }
                revalidate();
                repaint();
            }
        });

        scrollPane = new RoundedScrollPane(table, 30);
        scrollPane.setBounds(initialScrollPaneBounds); // Thiết lập kích thước ban đầu
        add(scrollPane);
        initialScrollPaneBounds = scrollPane.getBounds(); // Lưu lại kích thước ban đầu

        btnAdd = new JButton("Thêm");
        btnUpdate = new JButton("Cập Nhật");
        btnDelete = new JButton("Xóa"); // Đổi tên nút

        btnAdd.setBounds(1380, 62, 100, 30);
        btnAdd.setBackground(Color.BLUE);
        btnAdd.setForeground(Color.WHITE);
        btnUpdate.setBounds(1380, 122, 100, 30);
        btnUpdate.setBackground(Color.GREEN);
        btnUpdate.setForeground(Color.WHITE);
        btnDelete.setBounds(1380, 182, 100, 30);
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setBackground(Color.RED);

        // Thêm các nút vào panelThongTin
        panelThongTin.add(btnAdd);
        panelThongTin.add(btnUpdate);
        panelThongTin.add(btnDelete);

        btnAdd.addActionListener(e -> {
            panelThongTin.setVisible(true);
            isPanelThongTinVisible = true;
            panelThongTin.setBounds(panelThongTinBounds);
            revalidate();
            repaint();
            if (showConfirmation("Bạn có muốn thêm nhân viên này không?")) {
                if (validateInput() && addNhanVien()) {
                    scrollPane.setBounds(initialScrollPaneBounds);
                    panelThongTin.setVisible(false);
                    isPanelThongTinVisible = false;
                    revalidate();
                    repaint();
                }
            }
        });

        btnUpdate.addActionListener(e -> {
            if (table.getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhân viên để sửa.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            panelThongTin.setVisible(true);
            isPanelThongTinVisible = true;
            panelThongTin.setBounds(panelThongTinBounds);
            revalidate();
            repaint();
            if (showConfirmation("Bạn có muốn cập nhật nhân viên này không?")) {
                if (validateInput()) {
                    updateNhanVien();
                    scrollPane.setBounds(initialScrollPaneBounds);
                    panelThongTin.setVisible(false);
                    isPanelThongTinVisible = false;
                    revalidate();
                    repaint();
                }
            }
        });

        btnDelete.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhân viên để xóa.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (showConfirmation("Bạn có muốn xóa nhân viên này không?")) {
                scrollPane.setBounds(initialScrollPaneBounds);
                try {
					deleteNhanVien();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                panelThongTin.setVisible(false);
                isPanelThongTinVisible = false;
                revalidate();
                repaint();
            }
        });

        btnChooseImage = new JButton("Chọn Ảnh");
        btnChooseImage.setBounds(85, 210, 100, 30);
        btnChooseImage.setBackground(Color.BLUE);
        btnChooseImage.setForeground(Color.WHITE);
        panelThongTin.add(btnChooseImage);
        btnChooseImage.addActionListener(e -> chooseImage());

        loadDataToTable();
    }

    private void filterAndShowDetails() throws Exception {
        String text = txtTimKiem.getText().trim();
        tableModel.setRowCount(0); // Xóa dữ liệu cũ trong bảng

        if (text.isEmpty()) {
            // Nếu không có từ khóa, tải toàn bộ dữ liệu
            loadDataToTable();
            panelThongTin.setVisible(false);
            isPanelThongTinVisible = false;
            scrollPane.setBounds(initialScrollPaneBounds);
            revalidate();
            repaint();
        } else {
            // Gọi hàm tìm kiếm từ NVDAO
            List<NhanVien> listNV = DAONV.searchNhanVien(text);
            if (listNV == null || listNV.isEmpty()) {
                panelThongTin.setVisible(false);
                isPanelThongTinVisible = false;
                scrollPane.setBounds(initialScrollPaneBounds);
                clearForm();
                JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên nào!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Cập nhật bảng với kết quả tìm kiếm
                for (NhanVien nv : listNV) {
                    ImageIcon icon = null;
                    try {
                        String imagePath = nv.getLinkIMG();
                        System.out.println("Đường dẫn ảnh từ DB: " + imagePath);
                        if (imagePath != null && !imagePath.isEmpty()) {
                            imagePath = imagePath.replace("\\", "/");
                            java.io.File imageFile = new java.io.File(imagePath);
                            if (imageFile.exists() && imageFile.canRead()) {
                                icon = new ImageIcon(imagePath);
                            } else {
                                icon = loadDefaultImage();
                            }
                        } else {
                            icon = loadDefaultImage();
                        }
                        if (icon == null || icon.getImage() == null) {
                            icon = loadDefaultImage();
                        }
                    } catch (Exception e) {
                        icon = loadDefaultImage();
                        e.printStackTrace();
                    }

                    Object[] row = {icon, nv.getMaNV(), nv.getTenNV(), nv.getSdt(), nv.getGioiTinh(),
                                    nv.getChucVu(), nv.getTuoi(), nv.getEmail(), nv.getHeSoLuong(), nv.getLuongNV()};
                    tableModel.addRow(row);
                }

                // Hiển thị thông tin nhân viên đầu tiên trong kết quả tìm kiếm
                if (table.getRowCount() > 0) {
                    panelThongTin.setVisible(true);
                    isPanelThongTinVisible = true;
                    panelThongTin.setBounds(panelThongTinBounds);
                    scrollPane.setBounds(expandedScrollPaneBounds);
                    table.setRowSelectionInterval(0, 0);
                    updateTextFieldsFromSelectedRow();
                }
            }
            revalidate();
            repaint();
        }
    }

    private boolean showConfirmation(String message) {
        int option = JOptionPane.showConfirmDialog(this, message, "Xác nhận", JOptionPane.YES_NO_OPTION);
        return option == JOptionPane.YES_OPTION;
    }

    private void loadDataToTable() throws Exception {
        tableModel.setRowCount(0);

        List<NhanVien> listNV = DAONV.getAllNhanVien();
        if (listNV == null || listNV.isEmpty()) {
            return;
        }

        for (NhanVien nv : listNV) {
            ImageIcon icon = null;
            try {
                String imagePath = nv.getLinkIMG();
                System.out.println("Đường dẫn ảnh từ DB: " + imagePath);
                if (imagePath != null && !imagePath.isEmpty()) {
                    imagePath = imagePath.replace("\\", "/");
                    java.io.File imageFile = new java.io.File(imagePath);
                    if (imageFile.exists()) {
                        if (imageFile.canRead()) {
                            icon = new ImageIcon(imagePath);
                        } else {
                            icon = loadDefaultImage();
                        }
                    } else {
                        icon = loadDefaultImage();
                    }
                } else {
                    icon = loadDefaultImage();
                }
                if (icon == null || icon.getImage() == null) {
                    icon = loadDefaultImage();
                }
            } catch (Exception e) {
                icon = loadDefaultImage();
                e.printStackTrace();
            }

            Object[] row = {icon, nv.getMaNV(), nv.getTenNV(), nv.getSdt(), nv.getGioiTinh(),
                            nv.getChucVu(), nv.getTuoi(), nv.getEmail(), nv.getHeSoLuong(), nv.getLuongNV()};
            tableModel.addRow(row);
        }
    }

    private ImageIcon loadDefaultImage() {
        java.net.URL defaultImageUrl = getClass().getResource("/img/default.jpg");
        if (defaultImageUrl == null) {
            return new ImageIcon();
        }
        ImageIcon defaultIcon = new ImageIcon(defaultImageUrl);
        return defaultIcon;
    }

    private void updateTextFieldsFromSelectedRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0 && selectedRow < table.getRowCount()) {
            textField.setText(table.getValueAt(selectedRow, 1) != null ? table.getValueAt(selectedRow, 1).toString() : "");
            textField_1.setText(table.getValueAt(selectedRow, 2) != null ? table.getValueAt(selectedRow, 2).toString() : "");
            textField_2.setText(table.getValueAt(selectedRow, 3) != null ? table.getValueAt(selectedRow, 3).toString() : "");
            comboBoxGioiTinh.setSelectedItem(table.getValueAt(selectedRow, 4) != null ? table.getValueAt(selectedRow, 4).toString() : "");
            comboBoxChucVu.setSelectedItem(table.getValueAt(selectedRow, 5) != null ? table.getValueAt(selectedRow, 5).toString() : "");
            textField_5.setText(table.getValueAt(selectedRow, 6) != null ? table.getValueAt(selectedRow, 6).toString() : "");
            textField_6.setText(table.getValueAt(selectedRow, 7) != null ? table.getValueAt(selectedRow, 7).toString() : "");
            textField_7.setText(table.getValueAt(selectedRow, 8) != null ? table.getValueAt(selectedRow, 8).toString() : "");
            textField_8.setText(table.getValueAt(selectedRow, 9) != null ? table.getValueAt(selectedRow, 9).toString() : "");

            ImageIcon icon = (ImageIcon) table.getValueAt(selectedRow, 0);
            selectedImagePath = null;
            if (icon != null) {
                selectedImagePath = icon.getDescription();
            }

            try {
                ImageIcon displayIcon = null;
                if (selectedImagePath != null && !selectedImagePath.isEmpty()) {
                    displayIcon = new ImageIcon(selectedImagePath);
                } else {
                    displayIcon = loadDefaultImage();
                }

                if (displayIcon != null && displayIcon.getImage() != null) {
                    Image img = displayIcon.getImage().getScaledInstance(183, 150, Image.SCALE_SMOOTH);
                    lblNewLabel_1.setIcon(new ImageIcon(img));
                } else {
                    lblNewLabel_1.setIcon(null); // Hoặc set một icon lỗi khác
                }
            } catch (Exception e) {
                ImageIcon defaultIcon = loadDefaultImage();
                if (defaultIcon != null && defaultIcon.getImage() != null) {
                    Image img = defaultIcon.getImage().getScaledInstance(183, 150, Image.SCALE_SMOOTH);
                    lblNewLabel_1.setIcon(new ImageIcon(img));
                } else {
                    lblNewLabel_1.setIcon(null); // Hoặc set một icon lỗi khác
                }
                e.printStackTrace();
            }
        } else {
            clearForm();
            panelThongTin.setVisible(false);
            isPanelThongTinVisible = false;
            revalidate();
            repaint();
        }
    }

    private boolean addNhanVien() {
        if (!validateInput()) {
            return false;
        }
        try {
            NhanVien nv = new NhanVien();
            nv.setMaNV(textField.getText().trim());
            nv.setTenNV(textField_1.getText().trim());
            nv.setSdt(textField_2.getText().trim());
            nv.setGioiTinh(comboBoxGioiTinh.getSelectedItem().toString());
            nv.setChucVu(comboBoxChucVu.getSelectedItem().toString());
            nv.setTuoi(Integer.parseInt(textField_5.getText().trim()));
            nv.setEmail(textField_6.getText().trim());
            nv.setHeSoLuong(Double.parseDouble(textField_7.getText().trim()));
            nv.setLuongNV(Double.parseDouble(textField_8.getText().trim()));
            nv.setLinkIMG(selectedImagePath);

            String errorMessage = DAONV.insertNhanVien(nv);
            if (errorMessage == null) {
                clearForm();
                panelThongTin.setVisible(false);
                isPanelThongTinVisible = false;
                loadDataToTable(); // Tải lại dữ liệu từ cơ sở dữ liệu
                JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                revalidate();
                repaint();
                return true;
            } else {
                JOptionPane.showMessageDialog(this, errorMessage, "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi thêm nhân viên: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm nhân viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void updateNhanVien() {
        if (!validateInput()) {
            return;
        }
        try {
            NhanVien nv = new NhanVien();
            nv.setMaNV(textField.getText().trim());
            nv.setTenNV(textField_1.getText().trim());
            nv.setSdt(textField_2.getText().trim());
            nv.setGioiTinh(comboBoxGioiTinh.getSelectedItem().toString());
            nv.setChucVu(comboBoxChucVu.getSelectedItem().toString());
            nv.setTuoi(Integer.parseInt(textField_5.getText().trim()));
            nv.setEmail(textField_6.getText().trim());
            nv.setHeSoLuong(Double.parseDouble(textField_7.getText().trim()));
            nv.setLuongNV(Double.parseDouble(textField_8.getText().trim()));
            nv.setLinkIMG(selectedImagePath);

            String errorMessage = DAONV.updateNhanVien(nv);
            if (errorMessage == null) {
                clearForm();
                panelThongTin.setVisible(false);
                isPanelThongTinVisible = false;
                loadDataToTable(); // Tải lại dữ liệu từ cơ sở dữ liệu
                JOptionPane.showMessageDialog(this, "Cập nhật nhân viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                revalidate();
                repaint();
            } else {
                JOptionPane.showMessageDialog(this, errorMessage, "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi sửa nhân viên: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi sửa nhân viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteNhanVien() throws Exception {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            String maNV = table.getValueAt(selectedRow, 1).toString();
            DAONV.deleteNhanVien(maNV); // Gọi hàm xóa từ NVDAO
            clearForm();
            panelThongTin.setVisible(false);
            isPanelThongTinVisible = false;
            loadDataToTable(); // Tải lại dữ liệu từ cơ sở dữ liệu
            JOptionPane.showMessageDialog(this, "Xóa nhân viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            revalidate();
            repaint();
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhân viên để xóa.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void clearForm() {
        textField.setText("");
        textField_1.setText("");
        textField_2.setText("");
        comboBoxGioiTinh.setSelectedIndex(-1); // Đặt về trạng thái không chọn
        comboBoxChucVu.setSelectedIndex(-1);   // Đặt về trạng thái không chọn
        textField_5.setText("");
        textField_6.setText("");
        textField_7.setText("");
        textField_8.setText("");
        lblNewLabel_1.setIcon(null);
        selectedImagePath = null;
    }

    private boolean validateInput() {
        if (textField.getText().trim().isEmpty() || 
            textField_1.getText().trim().isEmpty() || 
            textField_2.getText().trim().isEmpty() || 
            textField_5.getText().trim().isEmpty() || 
            textField_6.getText().trim().isEmpty() || 
            textField_7.getText().trim().isEmpty() || 
            textField_8.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (comboBoxGioiTinh.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn giới tính!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (comboBoxChucVu.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn chức vụ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            Integer.parseInt(textField_5.getText().trim()); // Tuổi
            Float.parseFloat(textField_8.getText().trim()); // Lương
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Tuổi và lương phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void chooseImage() {
        JFileChooser fileChooser = new JFileChooser(new File("C:\\Users\\HP\\eclipse-workspace\\ProjectPTUD_QuanlyDatBan\\src\\main\\resources\\img"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "png", "jpeg"));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedImagePath = fileChooser.getSelectedFile().getAbsolutePath();
            try {
                ImageIcon icon = new ImageIcon(selectedImagePath);
                Image img = icon.getImage().getScaledInstance(183, 164, Image.SCALE_SMOOTH);
                lblNewLabel_1.setIcon(new ImageIcon(img));
            } catch (Exception e) {
                System.err.println("Lỗi khi chọn ảnh: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    class ImageRenderer extends DefaultTableCellRenderer {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int size;

        public ImageRenderer(int size) {
            this.size = size;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = new JLabel();
            label.setHorizontalAlignment(SwingConstants.CENTER);

            if (value instanceof ImageIcon) {
                ImageIcon icon = (ImageIcon) value;
                if (icon != null && icon.getImage() != null) {
                    Image img = icon.getImage().getScaledInstance(this.size, this.size, Image.SCALE_SMOOTH);
                    label.setIcon(new ImageIcon(img));
                } else {
                    label.setText("No Img");
                }
            } else {
                label.setText("No Img");
            }
            return label;
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

    class RoundedPane extends JPanel {
        private int cornerRadius;

        public RoundedPane(int radius) {
            this.cornerRadius = radius;
            setOpaque(false);
            setBorder(new EmptyBorder(5, 5, 5, 5));
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
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
            g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, cornerRadius, cornerRadius));
            g2.dispose();
        }
    }
}