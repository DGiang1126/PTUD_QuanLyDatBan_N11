package gui.panelForm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import com.toedter.calendar.JDateChooser;

import dao.DAO_Ban;
import dao.DAO_KhachHang;
import dao.DAO_PhieuDatBan;
import dao.DAO_CTPhieuDatBan;
import entity.KhachHang;
import entity.PhieuDatBan;
import entity.CTPhieuDatBan;


public class panelPhieuDatBanTang1 extends JPanel {
    private static final long serialVersionUID = 1L;
    private JTextField txtMaKhachHang;
    private JTextField txtTenKhachHang;
    private JTextField txtSoDienThoai;
    private JTextField txtSoBan;
    private JTextField txtSoLuong;
    private JComboBox<String> cbGioiTinh;
    private JDateChooser dateChooserNgayDat;
    private JDialog dialogChonKhachHang;
    private JDialog dialogThemKhachHang;
    private DAO_KhachHang khachHang_DAO;
    private DAO_Ban ban_DAO;
    private DAO_PhieuDatBan phieuDatBan_DAO;
    private DAO_CTPhieuDatBan ctPhieuDatBan_DAO;
    private Runnable refreshCallback;
    private CustomerInfoCallback customerInfoCallback;
    private Timer timeUpdateTimer;

    private final Color PRIMARY_COLOR = new Color(34, 197, 94);
    private final Color SECONDARY_COLOR = new Color(59, 130, 246);
    private final Color CANCEL_COLOR = new Color(239, 68, 68);
    private final Color TEXT_COLOR = new Color(17, 24, 39);
    private final Color BORDER_COLOR = new Color(203, 213, 225);

    public interface CustomerInfoCallback {
        void onCustomerSelected(String maPDB);
    }

    public panelPhieuDatBanTang1() {
        setBackground(Color.WHITE);
        setLayout(null);
        ban_DAO = new DAO_Ban();
        khachHang_DAO = new DAO_KhachHang();
        phieuDatBan_DAO = new DAO_PhieuDatBan();
        ctPhieuDatBan_DAO = new DAO_CTPhieuDatBan();

        RoundedPanel panel = new RoundedPanel(30);
        panel.setBounds(0, 0, 460, 450);
        panel.setBackground(Color.WHITE);
        panel.setLayout(null);
        add(panel);

        JLabel lblTitle = new JLabel("PHIẾU ĐẶT BÀN");
        lblTitle.setBounds(0, 20, 460, 50);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setFont(new Font("Roboto", Font.BOLD, 26));
        lblTitle.setForeground(TEXT_COLOR);
        panel.add(lblTitle);

        JLabel lblMaKhachHang = new JLabel("MÃ KHÁCH HÀNG:");
        lblMaKhachHang.setBounds(20, 110, 131, 20);
        lblMaKhachHang.setFont(new Font("Roboto", Font.PLAIN, 14));
        lblMaKhachHang.setForeground(TEXT_COLOR);
        panel.add(lblMaKhachHang);

        txtMaKhachHang = new JTextField();
        txtMaKhachHang.setBounds(160, 110, 270, 30);
        txtMaKhachHang.setFont(new Font("Roboto", Font.PLAIN, 14));
        txtMaKhachHang.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        panel.add(txtMaKhachHang);

        JLabel lblTenKhachHang = new JLabel("TÊN KHÁCH HÀNG:");
        lblTenKhachHang.setBounds(20, 145, 150, 20);
        lblTenKhachHang.setFont(new Font("Roboto", Font.PLAIN, 14));
        lblTenKhachHang.setForeground(TEXT_COLOR);
        panel.add(lblTenKhachHang);

        txtTenKhachHang = new JTextField();
        txtTenKhachHang.setBounds(160, 145, 270, 30);
        txtTenKhachHang.setFont(new Font("Roboto", Font.PLAIN, 14));
        txtTenKhachHang.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        panel.add(txtTenKhachHang);

        JLabel lblSoDienThoai = new JLabel("SỐ ĐIỆN THOẠI:");
        lblSoDienThoai.setBounds(20, 180, 120, 20);
        lblSoDienThoai.setFont(new Font("Roboto", Font.PLAIN, 14));
        lblSoDienThoai.setForeground(TEXT_COLOR);
        panel.add(lblSoDienThoai);

        txtSoDienThoai = new JTextField();
        txtSoDienThoai.setBounds(160, 180, 270, 30);
        txtSoDienThoai.setFont(new Font("Roboto", Font.PLAIN, 14));
        txtSoDienThoai.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        panel.add(txtSoDienThoai);

        JLabel lblGioiTinh = new JLabel("GIỚI TÍNH:");
        lblGioiTinh.setBounds(20, 215, 120, 20);
        lblGioiTinh.setFont(new Font("Roboto", Font.PLAIN, 14));
        lblGioiTinh.setForeground(TEXT_COLOR);
        panel.add(lblGioiTinh);

        cbGioiTinh = new JComboBox<>(new String[] { "", "Nam", "Nữ" });
        cbGioiTinh.setBounds(160, 215, 270, 30);
        cbGioiTinh.setFont(new Font("Roboto", Font.PLAIN, 14));
        cbGioiTinh.setBackground(Color.WHITE);
        cbGioiTinh.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        panel.add(cbGioiTinh);

        JLabel lblSoBan = new JLabel("SỐ BÀN:");
        lblSoBan.setBounds(20, 250, 120, 20);
        lblSoBan.setFont(new Font("Roboto", Font.PLAIN, 14));
        lblSoBan.setForeground(TEXT_COLOR);
        panel.add(lblSoBan);

        txtSoBan = new JTextField();
        txtSoBan.setBounds(160, 250, 270, 30);
        txtSoBan.setFont(new Font("Roboto", Font.PLAIN, 14));
        txtSoBan.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        panel.add(txtSoBan);

        JLabel lblSoLuong = new JLabel("SỐ LƯỢNG KHÁCH:");
        lblSoLuong.setBounds(20, 285, 150, 20);
        lblSoLuong.setFont(new Font("Roboto", Font.PLAIN, 14));
        lblSoLuong.setForeground(TEXT_COLOR);
        panel.add(lblSoLuong);

        txtSoLuong = new JTextField();
        txtSoLuong.setBounds(160, 285, 270, 30);
        txtSoLuong.setFont(new Font("Roboto", Font.PLAIN, 14));
        txtSoLuong.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        panel.add(txtSoLuong);

        JLabel lblNgayDat = new JLabel("NGÀY GIỜ ĐẶT:");
        lblNgayDat.setBounds(20, 320, 120, 20);
        lblNgayDat.setFont(new Font("Roboto", Font.PLAIN, 14));
        lblNgayDat.setForeground(TEXT_COLOR);
        panel.add(lblNgayDat);

        dateChooserNgayDat = new JDateChooser();
        dateChooserNgayDat.setBounds(160, 320, 270, 30);
        dateChooserNgayDat.setFont(new Font("Roboto", Font.PLAIN, 14));
        dateChooserNgayDat.setDateFormatString("dd/MM/yyyy HH:mm:ss");
        dateChooserNgayDat.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        dateChooserNgayDat.setDate(new Date());
        panel.add(dateChooserNgayDat);

        timeUpdateTimer = new Timer(1000, e -> {
            dateChooserNgayDat.setDate(new Date());
        });
        timeUpdateTimer.start();

        JButton btnChonKhachHang = createStyledButton("CHỌN KHÁCH HÀNG", SECONDARY_COLOR);
        btnChonKhachHang.setBounds(20, 70, 200, 35);
        btnChonKhachHang.addActionListener(e -> showDialogChonKhachHang());
        panel.add(btnChonKhachHang);

        JButton btnThemKhachHang = createStyledButton("THÊM KHÁCH HÀNG", SECONDARY_COLOR);
        btnThemKhachHang.setBounds(230, 70, 200, 35);
        btnThemKhachHang.addActionListener(e -> showDialogThemKhachHang());
        panel.add(btnThemKhachHang);

        JButton btnXacNhan = createStyledButton("XÁC NHẬN ĐẶT BÀN", PRIMARY_COLOR);
        btnXacNhan.setBounds(230, 375, 200, 35);
        btnXacNhan.addActionListener(e -> {
            String maKhachHang = txtMaKhachHang.getText().trim();
            String tenKhachHang = txtTenKhachHang.getText().trim();
            String soDienThoai = txtSoDienThoai.getText().trim();
            String gioiTinh = (String) cbGioiTinh.getSelectedItem();
            String soBan = txtSoBan.getText().trim();
            String soLuong = txtSoLuong.getText().trim();
            Date selectedDate = dateChooserNgayDat.getDate();

            if (maKhachHang.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn hoặc thêm khách hàng!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (soBan.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số bàn!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                txtSoBan.requestFocusInWindow();
                return;
            }

            if (soLuong.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng khách!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                txtSoLuong.requestFocusInWindow();
                return;
            }

            try {
                int soLuongKhach = Integer.parseInt(soLuong);
                if (soLuongKhach < 1 || soLuongKhach > 4) {
                    JOptionPane.showMessageDialog(this, "Số lượng khách tối đa 4 người", "Lỗi", JOptionPane.WARNING_MESSAGE);
                    txtSoLuong.requestFocusInWindow();
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Số lượng khách phải là một số hợp lệ!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                txtSoLuong.requestFocusInWindow();
                return;
            }

            if (selectedDate == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày giờ đặt!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đặt bàn " + soBan + " không?",
                    "Xác Nhận Đặt Bàn", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    boolean updateSuccess = ban_DAO.capNhatTrangThaiBan(soBan, "ĐÃ ĐẶT");
                    if (updateSuccess) {
                        String maPDB = phieuDatBan_DAO.getNextMaPDB();
                        PhieuDatBan pdb = new PhieuDatBan();
                        pdb.setMaPDB(maPDB);
                        pdb.setMaKH(maKhachHang);
                        pdb.setMaBan(ban_DAO.getMaBanByTenBan(soBan));
                        pdb.setMaNV("NV001");

                        boolean insertPDBSuccess = phieuDatBan_DAO.insertPhieuDatBan(pdb);

                        if (insertPDBSuccess) {
                            LocalDateTime timeNhanBan = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                            LocalDateTime timeTraBan = timeNhanBan.plusHours(2);

                            CTPhieuDatBan ctPDB = new CTPhieuDatBan();
                            ctPDB.setMaPDB(maPDB);
                            ctPDB.setTimeNhanBan(timeNhanBan);
                            ctPDB.setTimeTraBan(timeTraBan);
                            ctPDB.setSoNguoi(Integer.parseInt(soLuong));

                            boolean insertCTPDBSuccess = ctPhieuDatBan_DAO.insertCTPhieuDatBan(ctPDB);

                            if (insertCTPDBSuccess) {
                                JOptionPane.showMessageDialog(this, "Đã đặt bàn " + soBan + " thành công!", "Thông Báo",
                                        JOptionPane.INFORMATION_MESSAGE);

                                if (customerInfoCallback != null) {
                                    customerInfoCallback.onCustomerSelected(maPDB);
                                }

                                if (refreshCallback != null) {
                                    refreshCallback.run();
                                }

                                timeUpdateTimer.stop();

                                Container parent = getParent();
                                while (parent != null && !(parent instanceof JDialog || parent instanceof JFrame)) {
                                    parent = parent.getParent();
                                }
                                if (parent instanceof JDialog) {
                                    ((JDialog) parent).dispose();
                                } else if (parent instanceof JFrame) {
                                    ((JFrame) parent).dispose();
                                }
                            } else {
                                JOptionPane.showMessageDialog(this, "Thêm chi tiết phiếu đặt bàn thất bại!", "Lỗi",
                                        JOptionPane.ERROR_MESSAGE);
                                ban_DAO.capNhatTrangThaiBan(soBan, "CÒN TRỐNG");
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, "Thêm phiếu đặt bàn thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                            ban_DAO.capNhatTrangThaiBan(soBan, "CÒN TRỐNG");
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Cập nhật trạng thái bàn thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Lỗi khi thêm phiếu đặt bàn: " + ex.getMessage(), "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    ban_DAO.capNhatTrangThaiBan(soBan, "CÒN TRỐNG");
                }
            }
        });
        panel.add(btnXacNhan);

        JButton btnHuy = createStyledButton("HỦY", CANCEL_COLOR);
        btnHuy.setBounds(20, 375, 150, 35);
        btnHuy.addActionListener(e -> {
            timeUpdateTimer.stop();
            Container parent = getParent();
            while (parent != null && !(parent instanceof JDialog || parent instanceof JFrame)) {
                parent = parent.getParent();
            }
            if (parent instanceof JDialog) {
                ((JDialog) parent).dispose();
            } else if (parent instanceof JFrame) {
                ((JFrame) parent).dispose();
            }
        });
        panel.add(btnHuy);
    }

    public void setTableNumber(String tableNumber) {
        txtSoBan.setText(tableNumber);
    }

    public void setRefreshCallback(Runnable callback) {
        this.refreshCallback = callback;
    }

    public void setCustomerInfoCallback(CustomerInfoCallback callback) {
        this.customerInfoCallback = callback;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Roboto", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        return button;
    }

    private void showDialogChonKhachHang() {
        dialogChonKhachHang = new JDialog();
        dialogChonKhachHang.setTitle("Chọn Khách Hàng");
        dialogChonKhachHang.setModal(true);
        dialogChonKhachHang.setSize(600, 400);
        dialogChonKhachHang.setLocationRelativeTo(this);
        dialogChonKhachHang.getContentPane().setLayout(new BorderLayout());
        dialogChonKhachHang.getContentPane().setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("CHỌN KHÁCH HÀNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Roboto", Font.BOLD, 18));
        lblTitle.setForeground(TEXT_COLOR);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        dialogChonKhachHang.getContentPane().add(lblTitle, BorderLayout.NORTH);

        String[] columns = { "Mã KH", "Tên KH", "SĐT", "Giới Tính" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.setFont(new Font("Roboto", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        dialogChonKhachHang.getContentPane().add(scrollPane, BorderLayout.CENTER);

        List<KhachHang> danhSachKhachHang = khachHang_DAO.getAllKHs();
        for (KhachHang kh : danhSachKhachHang) {
            model.addRow(new Object[] {
                kh.getMaKH(),
                kh.getTenKH(),
                kh.getSdt(),
                kh.getGioiTinh()
            });
        }

        JPanel panelButtons = new JPanel();
        panelButtons.setBackground(Color.WHITE);
        panelButtons.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JButton btnChon = createStyledButton("CHỌN", PRIMARY_COLOR);
        btnChon.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                txtMaKhachHang.setText((String) model.getValueAt(selectedRow, 0));
                txtTenKhachHang.setText((String) model.getValueAt(selectedRow, 1));
                txtSoDienThoai.setText((String) model.getValueAt(selectedRow, 2));
                cbGioiTinh.setSelectedItem(model.getValueAt(selectedRow, 3));
                dialogChonKhachHang.dispose();
            } else {
                JOptionPane.showMessageDialog(dialogChonKhachHang, "Vui lòng chọn một khách hàng!");
            }
        });
        JButton btnHuy = createStyledButton("HỦY", CANCEL_COLOR);
        btnHuy.addActionListener(e -> dialogChonKhachHang.dispose());
        panelButtons.add(btnChon);
        panelButtons.add(btnHuy);
        dialogChonKhachHang.getContentPane().add(panelButtons, BorderLayout.SOUTH);

        dialogChonKhachHang.setVisible(true);
    }

    private void showDialogThemKhachHang() {
        dialogThemKhachHang = new JDialog();
        dialogThemKhachHang.setTitle("Thêm Khách Hàng");
        dialogThemKhachHang.setModal(true);
        dialogThemKhachHang.setSize(450, 350);
        dialogThemKhachHang.setLocationRelativeTo(this);
        dialogThemKhachHang.getContentPane().setLayout(null);
        dialogThemKhachHang.getContentPane().setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("THÊM KHÁCH HÀNG MỚI");
        lblTitle.setBounds(0, 20, 450, 30);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setFont(new Font("Roboto", Font.BOLD, 18));
        lblTitle.setForeground(TEXT_COLOR);
        dialogThemKhachHang.getContentPane().add(lblTitle);

        JLabel lblMaKH = new JLabel("Mã KH:");
        lblMaKH.setBounds(50, 70, 100, 20);
        lblMaKH.setFont(new Font("Roboto", Font.PLAIN, 14));
        lblMaKH.setForeground(TEXT_COLOR);
        dialogThemKhachHang.getContentPane().add(lblMaKH);

        JTextField txtMaKH = new JTextField();
        txtMaKH.setBounds(150, 70, 250, 30);
        txtMaKH.setFont(new Font("Roboto", Font.PLAIN, 14));
        txtMaKH.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        String nextMaKH = khachHang_DAO.getNextMaKH();
        txtMaKH.setText(nextMaKH.trim());
        txtMaKH.setEditable(false);
        dialogThemKhachHang.getContentPane().add(txtMaKH);

        JLabel lblTen = new JLabel("Tên KH:");
        lblTen.setBounds(50, 110, 100, 20);
        lblTen.setFont(new Font("Roboto", Font.PLAIN, 14));
        lblTen.setForeground(TEXT_COLOR);
        dialogThemKhachHang.getContentPane().add(lblTen);

        JTextField txtTen = new JTextField();
        txtTen.setBounds(150, 110, 250, 30);
        txtTen.setFont(new Font("Roboto", Font.PLAIN, 14));
        txtTen.requestFocusInWindow();
        txtTen.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        dialogThemKhachHang.getContentPane().add(txtTen);

        JLabel lblSdt = new JLabel("SĐT:");
        lblSdt.setBounds(50, 150, 100, 20);
        lblSdt.setFont(new Font("Roboto", Font.PLAIN, 14));
        lblSdt.setForeground(TEXT_COLOR);
        dialogThemKhachHang.getContentPane().add(lblSdt);

        JTextField txtSdt = new JTextField();
        txtSdt.setBounds(150, 150, 250, 30);
        txtSdt.setFont(new Font("Roboto", Font.PLAIN, 14));
        txtSdt.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        dialogThemKhachHang.getContentPane().add(txtSdt);

        JLabel lblGioiTinh = new JLabel("Giới Tính:");
        lblGioiTinh.setBounds(50, 190, 100, 20);
        lblGioiTinh.setFont(new Font("Roboto", Font.PLAIN, 14));
        lblGioiTinh.setForeground(TEXT_COLOR);
        dialogThemKhachHang.getContentPane().add(lblGioiTinh);

        JComboBox<String> cbGioiTinhMoi = new JComboBox<>(new String[] { "Nam", "Nữ" });
        cbGioiTinhMoi.setBounds(150, 190, 250, 30);
        cbGioiTinhMoi.setFont(new Font("Roboto", Font.PLAIN, 14));
        cbGioiTinhMoi.setBackground(Color.WHITE);
        cbGioiTinhMoi.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        dialogThemKhachHang.getContentPane().add(cbGioiTinhMoi);

        JButton btnLuu = createStyledButton("LƯU", PRIMARY_COLOR);
        btnLuu.setBounds(120, 250, 120, 35);
        btnLuu.addActionListener(e -> {
            String maKH = txtMaKH.getText().trim();
            String ten = txtTen.getText().trim();
            String sdt = txtSdt.getText().trim();
            String gioiTinh = (String) cbGioiTinhMoi.getSelectedItem();

            if (maKH.isEmpty() || ten.isEmpty() || sdt.isEmpty()) {
                JOptionPane.showMessageDialog(dialogThemKhachHang, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!ten.matches("^[\\p{L}\\s]+$")) {
                JOptionPane.showMessageDialog(dialogThemKhachHang, "Tên khách hàng chỉ được chứa chữ cái và khoảng trắng!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                txtTen.requestFocusInWindow();
                return;
            }

            if (!sdt.matches("\\d{10}")) {
                JOptionPane.showMessageDialog(dialogThemKhachHang, "Số điện thoại phải gồm đúng 10 chữ số!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                txtSdt.requestFocusInWindow();
                return;
            }

            KhachHang kh = new KhachHang(maKH, ten, sdt, gioiTinh);
            boolean result = khachHang_DAO.themKhachHang(kh);
            if (result) {
                txtMaKhachHang.setText(maKH);
                txtTenKhachHang.setText(ten);
                txtSoDienThoai.setText(sdt);
                cbGioiTinh.setSelectedItem(gioiTinh);
                JOptionPane.showMessageDialog(dialogThemKhachHang, "Thêm khách hàng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                dialogThemKhachHang.dispose();
            } else {
                JOptionPane.showMessageDialog(dialogThemKhachHang, "Thêm khách hàng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        dialogThemKhachHang.getContentPane().add(btnLuu);

        JButton btnHuy = createStyledButton("HỦY", CANCEL_COLOR);
        btnHuy.setBounds(280, 250, 120, 35);
        btnHuy.addActionListener(e -> dialogThemKhachHang.dispose());
        dialogThemKhachHang.getContentPane().add(btnHuy);

        dialogThemKhachHang.setVisible(true);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            timeUpdateTimer.start();
        } else {
            timeUpdateTimer.stop();
        }
    }

    private static class RoundedPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        private final int arc;

        public RoundedPanel(int arc) {
            this.arc = arc;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), arc, arc));
            g2.dispose();
            super.paintComponent(g);
        }
    }
}