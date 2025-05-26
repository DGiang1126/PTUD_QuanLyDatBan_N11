package gui.panelForm;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.RenderingHints;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

import dao.DAO_NhanVien;
import dao.DAO_TaiKhoan;
import entity.NhanVien;
import entity.TaiKhoan;
import raven.popup.DefaultOption;
import raven.popup.GlassPanePopup;
import raven.popup.component.SimplePopupBorder;

public class panelQLyTK extends JPanel {
    private JComboBox<String> cbMaNhanVien;
    private JTextField txtTenDangNhap;
    private JTextField txtMatKhau;
    private DefaultTableModel mdlDanhSachTaiKhoan;
    private RoundedScrollPane scrDanhSachTaiKhoan;
    private JTable tblDanhSachTaiKhoan;
    private DAO_TaiKhoan daoTaiKhoan;
    private DAO_NhanVien daoNhanVien;

    public panelQLyTK() throws Exception {
        setBackground(SystemColor.controlHighlight);
        setLayout(null);
        setSize(1535, 850);

        daoTaiKhoan = new DAO_TaiKhoan();
        daoNhanVien = new DAO_NhanVien();

        // Cài đặt font FlatLaf
        FlatLaf.registerCustomDefaultsSource("themes");
        FlatIntelliJLaf.setup();
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));

        UIManager.put("Panel.background", new Color(247, 248, 252));
        UIManager.put("Button.background", new Color(52, 102, 255));
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.disabledBackground", new Color(209, 213, 219));
        UIManager.put("Label.foreground", new Color(17, 24, 39));
        UIManager.put("Component.borderColor", new Color(229, 231, 235));

        // Tiêu đề
        JLabel lblTieuDe = new JLabel("QUẢN LÝ TÀI KHOẢN");
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTieuDe.setHorizontalAlignment(SwingConstants.CENTER);
        lblTieuDe.setBounds(0, 10, 1535, 27);
        add(lblTieuDe);

        // Panel nhập thông tin
        JPanel pnlNhapThongTin = new JPanel();
        pnlNhapThongTin.setBorder(new LineBorder(new Color(0, 0, 0), 3, true));
        pnlNhapThongTin.setPreferredSize(new Dimension(400, 100));
        pnlNhapThongTin.setLayout(null);

        JLabel lblMaNhanVien = new JLabel("Mã nhân viên");
        lblMaNhanVien.setBounds(1, 1, 193, 41);
        lblMaNhanVien.setHorizontalAlignment(SwingConstants.CENTER);
        lblMaNhanVien.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel lblTenDangNhap = new JLabel("Tên tài khoản");
        lblTenDangNhap.setBounds(1, 55, 193, 41);
        lblTenDangNhap.setHorizontalAlignment(SwingConstants.CENTER);
        lblTenDangNhap.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel lblMatKhau = new JLabel("Mật khẩu");
        lblMatKhau.setHorizontalAlignment(SwingConstants.CENTER);
        lblMatKhau.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblMatKhau.setBounds(11, 109, 193, 41);

        cbMaNhanVien = new JComboBox<>();
        cbMaNhanVien.setBounds(204, 7, 253, 30);

        txtTenDangNhap = new JTextField();
        txtTenDangNhap.setBounds(204, 56, 253, 41);
        setupPlaceholder(txtTenDangNhap, "Nhập tên tài khoản");
        txtTenDangNhap.setColumns(10);

        txtMatKhau = new JTextField();
        txtMatKhau.setColumns(10);
        setupPlaceholder(txtMatKhau, "Nhập mật khẩu");
        txtMatKhau.setBounds(204, 110, 253, 41);

        pnlNhapThongTin.add(lblMaNhanVien);
        pnlNhapThongTin.add(lblTenDangNhap);
        pnlNhapThongTin.add(lblMatKhau);
        pnlNhapThongTin.add(cbMaNhanVien);
        pnlNhapThongTin.add(txtTenDangNhap);
        pnlNhapThongTin.add(txtMatKhau);

        // Panel chức năng
        JPanel pnlChucNang = new JPanel();
        pnlChucNang.setBorder(new LineBorder(new Color(0, 0, 0), 3, true));
        pnlChucNang.setLayout(null);

        JButton btnThem = new JButton("Thêm");
        btnThem.setBounds(45, 50, 120, 50);
        JButton btnXoa = new JButton("Xóa");
        btnXoa.setBounds(240, 50, 120, 50);
        JButton btnSua = new JButton("Sửa");
        btnSua.setBounds(430, 50, 120, 50);
        JButton btnTim = new JButton("Tìm kiếm");
        btnTim.setBounds(620, 50, 120, 50);

        pnlChucNang.add(btnThem);
        pnlChucNang.add(btnXoa);
        pnlChucNang.add(btnSua);
        pnlChucNang.add(btnTim);

        // SplitPane chứa hai panel
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pnlNhapThongTin, pnlChucNang);
        splitPane.setBounds(50, 60, 1454, 161);
        splitPane.setDividerLocation(600);
        add(splitPane);

        // Bảng danh sách tài khoản
        String[] columnNames = {"STT", "Mã NV", "Tên tài khoản", "Mật khẩu", "Thời gian đăng nhập", "Thời gian đăng xuất", "Số giờ làm", "Trạng thái"};
        mdlDanhSachTaiKhoan = new DefaultTableModel(columnNames, 0);
        tblDanhSachTaiKhoan = new JTable(mdlDanhSachTaiKhoan);

        tblDanhSachTaiKhoan.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblDanhSachTaiKhoan.setRowHeight(25);
        tblDanhSachTaiKhoan.setShowGrid(true);

        JTableHeader tableHeader = tblDanhSachTaiKhoan.getTableHeader();
        tableHeader.setFont(new Font("Segoe UI", Font.BOLD, 12));

        scrDanhSachTaiKhoan = new RoundedScrollPane(tblDanhSachTaiKhoan, 30);
        scrDanhSachTaiKhoan.setBounds(50, 256, 1454, 563);
        add(scrDanhSachTaiKhoan);

        // Load dữ liệu
        loadTableData();
        loadComboBoxData();

        // Xử lý sự kiện cho các nút
        btnThem.addActionListener(e -> {
			try {
				themTaiKhoan();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
        btnXoa.addActionListener(e -> {
			try {
				xoaTaiKhoan();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
        btnSua.addActionListener(e -> {
			try {
				suaTaiKhoan();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
        btnTim.addActionListener(e -> {
			try {
				timTaiKhoan();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
        
        cbMaNhanVien.addActionListener(e -> {
            String maNV = (String) cbMaNhanVien.getSelectedItem();
            if (maNV != null) {
                try {
                    List<TaiKhoan> taiKhoanList = daoTaiKhoan.loadTaiKhoanData();
                    boolean hasAccount = taiKhoanList.stream().anyMatch(tk -> tk.getNhanVien().getMaNV().equals(maNV));
                    btnThem.setEnabled(!hasAccount);
                    if (hasAccount) {
                        JOptionPane.showMessageDialog(this, "Nhân viên này đã có tài khoản!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Lỗi khi kiểm tra tài khoản: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                } catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
        
        FlatRobotoFont.install();
    }

    private void loadTableData() throws Exception {
        mdlDanhSachTaiKhoan.setRowCount(0);
        List<TaiKhoan> taiKhoanList = daoTaiKhoan.loadTaiKhoanData();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        DecimalFormat df = new DecimalFormat("#.#"); // Định dạng 1 chữ số thập phân
        int stt = 1;
        for (TaiKhoan tk : taiKhoanList) {
            String gioVaoLam = tk.getGioVaoLam() != null ? sdf.format(Date.from(tk.getGioVaoLam().atZone(ZoneId.systemDefault()).toInstant())) : "";
            String gioNghi = tk.getGioNghi() != null ? sdf.format(Date.from(tk.getGioNghi().atZone(ZoneId.systemDefault()).toInstant())) : "";
            Object[] row = {
                stt++,
                tk.getNhanVien().getMaNV(),
                tk.getTenDangNhap(),
                tk.getMatKhau(),
                gioVaoLam,
                gioNghi,
                df.format(tk.getSoGioLam()), // Định dạng SoGioLam với 1 chữ số thập phân
                tk.getTrangThai()
            };
            mdlDanhSachTaiKhoan.addRow(row);
        }
    }

    private void loadComboBoxData() throws Exception {
        try {
            List<String> maNhanVienList = daoNhanVien.getAllMaNhanVien();
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
            for (String maNV : maNhanVienList) {
                model.addElement(maNV);
            }
            cbMaNhanVien.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách mã nhân viên: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void themTaiKhoan() throws Exception {
        String maNV = (String) cbMaNhanVien.getSelectedItem();
        String tenDangNhap = txtTenDangNhap.getText().trim();
        String matKhau = new String(txtMatKhau.getText()).trim();

        if (maNV == null || tenDangNhap.isEmpty() || tenDangNhap.equals("Nhập tên tài khoản") || matKhau.isEmpty() || matKhau.equals("Nhập mật khẩu")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (tenDangNhap.length() < 3) {
            JOptionPane.showMessageDialog(this, "Tên đăng nhập phải có ít nhất 3 ký tự!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (matKhau.length() < 6) {
            JOptionPane.showMessageDialog(this, "Mật khẩu phải có ít nhất 6 ký tự!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Kiểm tra xem tên đăng nhập đã tồn tại chưa
            List<TaiKhoan> taiKhoanList = daoTaiKhoan.loadTaiKhoanData();
            if (taiKhoanList.stream().anyMatch(tk -> tk.getTenDangNhap().equals(tenDangNhap))) {
                JOptionPane.showMessageDialog(this, "Tên đăng nhập đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            TaiKhoan tk = new TaiKhoan();
            tk.setNhanVien(daoNhanVien.getNVbyID(maNV));
            tk.setTenDangNhap(tenDangNhap);
            tk.setMatKhau(matKhau);
            tk.setTrangThai("Online");

            if (daoTaiKhoan.addTaiKhoan(tk)) {
                JOptionPane.showMessageDialog(this, "Thêm tài khoản thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                loadTableData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm tài khoản thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm tài khoản: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xoaTaiKhoan() throws SQLException {
        int selectedRow = tblDanhSachTaiKhoan.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản để xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maNV = (String) mdlDanhSachTaiKhoan.getValueAt(selectedRow, 1); // Lấy MaNV từ cột 1
        String tenDangNhap = (String) mdlDanhSachTaiKhoan.getValueAt(selectedRow, 2); // Lấy TenDangNhap để hiển thị
        int option = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc muốn xóa tài khoản " + tenDangNhap + "?", 
            "Xác nhận", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE);
        if (option == JOptionPane.YES_OPTION) {
            try {
				if (daoTaiKhoan.deleteTaiKhoan(maNV)) {
				    JOptionPane.showMessageDialog(this, "Xóa tài khoản thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
				    loadTableData();
				    clearForm();
				} else {
				    JOptionPane.showMessageDialog(this, "Xóa tài khoản thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
				}
			} catch (HeadlessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }

    private void suaTaiKhoan() throws Exception {
        int selectedRow = tblDanhSachTaiKhoan.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản để sửa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maNV = (String) cbMaNhanVien.getSelectedItem();
        String tenDangNhap = txtTenDangNhap.getText().trim();
        String matKhau = new String(txtMatKhau.getText()).trim();

        if (maNV == null || tenDangNhap.isEmpty() || tenDangNhap.equals("Nhập tên tài khoản") || matKhau.isEmpty() || matKhau.equals("Nhập mật khẩu")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (matKhau.length() < 6) {
            JOptionPane.showMessageDialog(this, "Mật khẩu phải có ít nhất 6 ký tự!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            TaiKhoan tk = new TaiKhoan();
            tk.setNhanVien(daoNhanVien.getNVbyID(maNV));
            tk.setTenDangNhap(tenDangNhap);
            tk.setMatKhau(matKhau);
            tk.setTrangThai((String) mdlDanhSachTaiKhoan.getValueAt(selectedRow, 7));

            if (daoTaiKhoan.updateTaiKhoan(tk)) {
                JOptionPane.showMessageDialog(this, "Cập nhật tài khoản thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                loadTableData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật tài khoản thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật tài khoản: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void timTaiKhoan() throws Exception {
        String tenDangNhap = txtTenDangNhap.getText().trim();
        if (tenDangNhap.isEmpty() || tenDangNhap.equals("Nhập tên tài khoản")) {
            try {
                loadTableData(); // Nếu không nhập, load toàn bộ
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
            return;
        }

        try {
            mdlDanhSachTaiKhoan.setRowCount(0);
            List<TaiKhoan> taiKhoanList = daoTaiKhoan.searchTaiKhoan(tenDangNhap);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            int stt = 1;
            for (TaiKhoan tk : taiKhoanList) {
                String gioVaoLam = tk.getGioVaoLam() != null ? sdf.format(Date.from(tk.getGioVaoLam().atZone(ZoneId.systemDefault()).toInstant())) : "";
                String gioNghi = tk.getGioNghi() != null ? sdf.format(Date.from(tk.getGioNghi().atZone(ZoneId.systemDefault()).toInstant())) : "";
                Object[] row = {
                    stt++,
                    tk.getNhanVien().getMaNV(),
                    tk.getTenDangNhap(),
                    tk.getMatKhau(), // Ẩn mật khẩu
                    gioVaoLam,
                    gioNghi,
                    tk.getSoGioLam(),
                    tk.getTrangThai()
                };
                mdlDanhSachTaiKhoan.addRow(row);
            }
            if (taiKhoanList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy tài khoản!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        cbMaNhanVien.setSelectedIndex(-1);
        txtTenDangNhap.setText("Nhập tên tài khoản");
        txtTenDangNhap.setForeground(Color.GRAY);
        txtMatKhau.setText("Nhập mật khẩu");
        txtMatKhau.setForeground(Color.GRAY);
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

    private void setupPlaceholder(JTextField textField, String placeholder) {
        textField.setText(placeholder);
        textField.setForeground(Color.GRAY);

        textField.addFocusListener(new java.awt.event.FocusListener() {
            @Override	
            public void focusGained(java.awt.event.FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(Color.GRAY);
                }
            }
        });
    }
}