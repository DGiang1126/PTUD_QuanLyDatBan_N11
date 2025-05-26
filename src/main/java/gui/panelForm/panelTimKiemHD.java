package gui.panelForm;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.toedter.calendar.JDateChooser;

import dao.DAO_CTHoaDon;
import dao.DAO_HoaDon;
import entity.CTHoaDon;
import entity.HoaDon;
import java.time.format.DateTimeFormatter;

public class panelTimKiemHD extends JPanel {
    private static final long serialVersionUID = 1L;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtMaHD, txtMaKH, txtMBan, txtSDT;
    private JComboBox<String> cbTrangThai;
    private JDateChooser dateStart, dateEnd;
    private JLabel lblMaHD, lblMaKH, lblMBan, lblSDT, lblDateStart, lblDateEnd, lblTrangThai, lblGia;
    private JPanel detailPanel;
    private JButton btnNewButton, btnSearch;
    private RoundedScrollPane scrollPane;
    private DAO_HoaDon daoHoaDon;
    private DAO_CTHoaDon daoCTHoaDon;
    private final double TAX_RATE = 0.08;
    private final DecimalFormat df = new DecimalFormat("#,###");
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private JTextField txtDetailMaHD, txtNhanVien, txtPhongBan, txtKhachHang, txtDetailSDT, txtThoiGian, txtThue, txtTongTien, txtKhuyenMai;
    private DefaultTableModel detailTableModel;
    private JTable detailTable;

    public panelTimKiemHD() {
        setBackground(SystemColor.controlHighlight);
        setLayout(null);
        setSize(1535, 850);

        daoHoaDon = new DAO_HoaDon();
        daoCTHoaDon = new DAO_CTHoaDon();

        FlatRobotoFont.install();
        FlatLaf.registerCustomDefaultsSource("themes");
        FlatIntelliJLaf.setup();
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));

        JPanel searchPanel = new RoundedPane(20);
        searchPanel.setBounds(13, 12, 1500, 110);
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        searchPanel.setLayout(null);
        add(searchPanel);

        txtMaHD = new JTextField(10);
        setupPlaceholder(txtMaHD, "Mã hóa đơn");
        txtMaHD.setBounds(108, 20, 194, 25);
        searchPanel.add(txtMaHD);

        lblMaHD = new JLabel("Mã hóa đơn:");
        lblMaHD.setBounds(10, 26, 88, 13);
        searchPanel.add(lblMaHD);

        txtMaKH = new JTextField(10);
        setupPlaceholder(txtMaKH, "Mã khách hàng");
        txtMaKH.setBounds(400, 20, 194, 25);
        searchPanel.add(txtMaKH);

        lblMaKH = new JLabel("Mã khách hàng:");
        lblMaKH.setBounds(312, 26, 88, 13);
        searchPanel.add(lblMaKH);

        txtMBan = new JTextField(10);
        setupPlaceholder(txtMBan, "Mã bàn");
        txtMBan.setBounds(692, 20, 194, 25);
        searchPanel.add(txtMBan);

        lblMBan = new JLabel("Bàn:");
        lblMBan.setBounds(604, 26, 88, 13);
        searchPanel.add(lblMBan);

        txtSDT = new JTextField(10);
        setupPlaceholder(txtSDT, "Số điện thoại");
        txtSDT.setBounds(984, 20, 194, 25);
        searchPanel.add(txtSDT);

        lblSDT = new JLabel("SDT:");
        lblSDT.setBounds(896, 26, 88, 13);
        searchPanel.add(lblSDT);

        dateStart = new JDateChooser();
        dateStart.setDateFormatString("dd/MM/yyyy");
        dateStart.setBounds(108, 66, 194, 25);
        searchPanel.add(dateStart);

        lblDateStart = new JLabel("Từ ngày:");
        lblDateStart.setBounds(10, 72, 88, 13);
        searchPanel.add(lblDateStart);

        dateEnd = new JDateChooser();
        dateEnd.setDateFormatString("dd/MM/yyyy");
        dateEnd.setBounds(400, 66, 194, 25);
        searchPanel.add(dateEnd);

        lblDateEnd = new JLabel("Đến ngày:");
        lblDateEnd.setBounds(312, 72, 88, 13);
        searchPanel.add(lblDateEnd);

        String[] trangThaiOptions = {"Tất cả", "Chưa thanh toán", "Đã thanh toán"};
        cbTrangThai = new JComboBox<>(trangThaiOptions);
        cbTrangThai.setBounds(692, 66, 194, 25);
        searchPanel.add(cbTrangThai);

        lblTrangThai = new JLabel("Trạng thái:");
        lblTrangThai.setBounds(604, 72, 88, 13);
        searchPanel.add(lblTrangThai);

        lblGia = new JLabel("Giá:");
        lblGia.setBounds(896, 72, 88, 13);
        searchPanel.add(lblGia);

        btnSearch = new JButton("Tìm kiếm");
        btnSearch.setBounds(1350, 22, 100, 30);
        btnSearch.addActionListener(e -> performSearch());
        searchPanel.add(btnSearch);

        String[] columnNames = {"Mã hóa đơn", "Bàn", "Thời gian", "Khách hàng", "Trạng thái", "Tổng tiền", "Giá giảm", "Đã trả"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        JTableHeader header = table.getTableHeader();
        header.setFont(header.getFont().deriveFont(Font.BOLD));
        int[] columnWidths = {100, 25, 175, 120, 100, 100, 80, 100};
        for (int i = 0; i < columnWidths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
        }
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        table.setOpaque(false);
        table.setBackground(new Color(0, 0, 0, 0));
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        DefaultTableCellRenderer centreRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        centreRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i <= 4; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centreRenderer);
        }
        for (int i = 5; i <= 7; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
        }

        scrollPane = new RoundedScrollPane(table, 30);
        scrollPane.setBounds(13, 122, 1480, 705);
        scrollPane.setBackground(Color.WHITE);
        add(scrollPane);

        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/img/icons8-view-details-50.png"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(36, 36, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(scaledImage);
        btnNewButton = new JButton("");
        btnNewButton.setIcon(resizedIcon);
        btnNewButton.setBounds(1492, 147, 30, 36);
        btnNewButton.addActionListener(e -> toggleDetailPanel());
        add(btnNewButton);

        detailPanel = new RoundedPane(30);
        detailPanel.setLayout(null);
        detailPanel.setBounds(1010, 122, 500, 705);
        detailPanel.setBackground(Color.WHITE);
        detailPanel.setVisible(false);
        add(detailPanel);

        JLabel lblTitle_1 = new JLabel("THÔNG TIN HÓA ĐƠN", SwingConstants.CENTER);
        lblTitle_1.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle_1.setBounds(100, 10, 300, 30);
        detailPanel.add(lblTitle_1);

        JLabel lblNhaHang = new JLabel("Nhà hàng: CERBUS");
        lblNhaHang.setBounds(20, 50, 200, 25);
        detailPanel.add(lblNhaHang);

        JLabel lblDiaChi = new JLabel("Địa chỉ: 4 Nguyễn Văn Bảo, P5, Gò Vấp");
        lblDiaChi.setBounds(20, 80, 300, 25);
        detailPanel.add(lblDiaChi);

        JLabel lblDetailMaHD = new JLabel("Mã hóa đơn:");
        lblDetailMaHD.setBounds(20, 120, 100, 25);
        txtDetailMaHD = new JTextField();
        txtDetailMaHD.setBounds(130, 120, 200, 25);
        txtDetailMaHD.setEditable(false);
        detailPanel.add(lblDetailMaHD);
        detailPanel.add(txtDetailMaHD);

        JLabel lblNhanVien = new JLabel("Nhân viên:");
        lblNhanVien.setBounds(20, 160, 100, 25);
        txtNhanVien = new JTextField();
        txtNhanVien.setBounds(130, 160, 200, 25);
        txtNhanVien.setEditable(false);
        detailPanel.add(lblNhanVien);
        detailPanel.add(txtNhanVien);

        JLabel lblPhongBan = new JLabel("Phòng/Bàn:");
        lblPhongBan.setBounds(20, 200, 100, 25);
        txtPhongBan = new JTextField();
        txtPhongBan.setBounds(130, 200, 200, 25);
        txtPhongBan.setEditable(false);
        detailPanel.add(lblPhongBan);
        detailPanel.add(txtPhongBan);

        JLabel lblKhachHang = new JLabel("Khách hàng:");
        lblKhachHang.setBounds(20, 240, 100, 25);
        txtKhachHang = new JTextField();
        txtKhachHang.setBounds(130, 240, 200, 25);
        txtKhachHang.setEditable(false);
        detailPanel.add(lblKhachHang);
        detailPanel.add(txtKhachHang);

        JLabel lblDetailSDT = new JLabel("SĐT:");
        lblDetailSDT.setBounds(20, 280, 100, 25);
        txtDetailSDT = new JTextField();
        txtDetailSDT.setBounds(130, 280, 200, 25);
        txtDetailSDT.setEditable(false);
        detailPanel.add(lblDetailSDT);
        detailPanel.add(txtDetailSDT);

        JLabel lblThoiGian = new JLabel("Thời gian:");
        lblThoiGian.setBounds(20, 320, 100, 25);
        txtThoiGian = new JTextField();
        txtThoiGian.setBounds(130, 320, 200, 25);
        txtThoiGian.setEditable(false);
        detailPanel.add(lblThoiGian);
        detailPanel.add(txtThoiGian);

        JLabel lblThue = new JLabel("Thuế (8%):");
        lblThue.setBounds(20, 360, 100, 25);
        txtThue = new JTextField();
        txtThue.setBounds(130, 360, 200, 25);
        txtThue.setEditable(false);
        detailPanel.add(lblThue);
        detailPanel.add(txtThue);

        JLabel lblKhuyenMai = new JLabel("Khuyến mãi:");
        lblKhuyenMai.setBounds(20, 400, 100, 25);
        txtKhuyenMai = new JTextField();
        txtKhuyenMai.setBounds(130, 400, 200, 25);
        txtKhuyenMai.setEditable(false);
        detailPanel.add(lblKhuyenMai);
        detailPanel.add(txtKhuyenMai);

        JLabel lblTongTien = new JLabel("Tổng tiền:");
        lblTongTien.setBounds(20, 440, 100, 25);
        txtTongTien = new JTextField();
        txtTongTien.setBounds(130, 440, 200, 25);
        txtTongTien.setEditable(false);
        detailPanel.add(lblTongTien);
        detailPanel.add(txtTongTien);

        String[] detailColumnNames = {"Mã SP", "Tên SP", "Số lượng", "Giá bán", "Thành tiền"};
        detailTableModel = new DefaultTableModel(detailColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        detailTable = new JTable(detailTableModel);
        detailTable.getTableHeader().setReorderingAllowed(false);
        JScrollPane detailScrollPane = new JScrollPane(detailTable);
        detailScrollPane.setBounds(20, 480, 450, 110);
        detailPanel.add(detailScrollPane);

        JButton btnInHoaDon = new JButton("In Hóa Đơn");
        btnInHoaDon.setBounds(180, 600, 150, 30);
        btnInHoaDon.addActionListener(e -> showInvoice());
        detailPanel.add(btnInHoaDon);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    String maHD = (String) tableModel.getValueAt(row, 0);
                    populateDetailPanel(maHD);
                    if (!detailPanel.isVisible()) {
                        toggleDetailPanel();
                    }
                }
            }
        });

        loadTableData();
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        List<HoaDon> hoaDons = null;
        try {
            hoaDons = daoHoaDon.getAllHoaDon();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách hóa đơn: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (hoaDons == null || hoaDons.isEmpty()) {
            return;
        }
        for (HoaDon hd : hoaDons) {
            List<CTHoaDon> ctList = null;
            try {
                ctList = daoCTHoaDon.getChiTietHoaDonByMaHDFromSP(hd.getMaHD());
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi tải chi tiết hóa đơn: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            if (ctList == null) {
                ctList = new ArrayList<>();
            }
            double subTotal = ctList.stream().mapToDouble(CTHoaDon::getThanhTien).sum();
            double tax = subTotal * TAX_RATE;
            double totalBeforeDiscount = subTotal + tax;

            double giamGiaPercent = hd.getTrangThai().equalsIgnoreCase("Đã thanh toán") && hd.getKhuyenMai() != null 
                ? hd.getKhuyenMai().getPhanTramGiamGia() 
                : 0;
            double giamGiaAmount = (giamGiaPercent / 100.0) * subTotal;

            double total = totalBeforeDiscount - giamGiaAmount;
            double daTra = hd.getTrangThai().equalsIgnoreCase("Đã thanh toán") ? total : 0;

            Object[] row = {
                hd.getMaHD(),
                hd.getBan() != null ? hd.getBan().getMaBan() : "",
                hd.getNgayLap() != null ? dtf.format(hd.getNgayLap()) : "",
                hd.getKhachHang() != null ? hd.getKhachHang().getMaKH() : "Khách lẻ",
                hd.getTrangThai(),
                df.format(total),
                df.format(giamGiaAmount),
                df.format(daTra)
            };
            tableModel.addRow(row);
        }
    }

    private void performSearch() {
        String maHD = txtMaHD.getText().equals("Mã hóa đơn") ? "" : txtMaHD.getText();
        String maKH = txtMaKH.getText().equals("Mã khách hàng") ? "" : txtMaKH.getText();
        String maBan = txtMBan.getText().equals("Mã bàn") ? "" : txtMBan.getText();
        String sdt = txtSDT.getText().equals("Số điện thoại") ? "" : txtSDT.getText();
        String trangThai = (String) cbTrangThai.getSelectedItem();

        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        if (dateStart.getDate() != null) {
            startDate = dateStart.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
        if (dateEnd.getDate() != null) {
            endDate = dateEnd.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        }

        List<HoaDon> hoaDons = null;
        try {
            hoaDons = daoHoaDon.searchHoaDon(maHD, maKH, maBan, sdt, trangThai, startDate, endDate, null);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm hóa đơn: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (hoaDons == null) {
            hoaDons = new ArrayList<>();
        }
        tableModel.setRowCount(0);
        for (HoaDon hd : hoaDons) {
            List<CTHoaDon> ctList = null;
            ctList = daoCTHoaDon.getCTHDByMaHD(hd.getMaHD());
            if (ctList == null) {
                ctList = new ArrayList<>();
            }

            // Calculate subtotal (tongCong) by summing ThanhTien from ctList
            int tongCong = (int) ctList.stream().mapToDouble(CTHoaDon::getDonGia).sum();
            System.out.println(tongCong);
            // Fixed VAT rate (8%)
            double vatPercentage = 0.08;
            int vat = (int) (tongCong * vatPercentage);

            // Discount percentage
            double khuyenMai = 0;
            if (hd.getTrangThai().equalsIgnoreCase("Đã thanh toán") && hd.getKhuyenMai() != null) {
                khuyenMai = hd.getKhuyenMai().getPhanTramGiamGia() / 100.0; // Convert to decimal
//            	khuyenMai = 0;
            }
            // Calculate final total: (tongCong + vat) * (1 - khuyenMai)
            int tienCanTra = (int) ((tongCong + vat) * (1 - khuyenMai));

            // Calculate giamGiaAmount for display
            double giamGiaAmount = (khuyenMai * tongCong); // Discount amount based on subtotal

            // Calculate daTra based on status
            double daTra = hd.getTrangThai().equalsIgnoreCase("Đã thanh toán") ? tienCanTra : 0;

            // Populate table row
            Object[] row = {
                hd.getMaHD(),
                hd.getBan() != null ? hd.getBan().getMaBan() : "",
                hd.getNgayLap() != null ? dtf.format(hd.getNgayLap()) : "",
                hd.getKhachHang() != null ? hd.getKhachHang().getMaKH() : "Khách lẻ",
                hd.getTrangThai(),
                df.format(tienCanTra), // Total after VAT and discount
                df.format(giamGiaAmount), // Discount amount
                df.format(daTra) // Paid amount
            };
            tableModel.addRow(row);
        }
    }

    private void populateDetailPanel(String maHD) {
        HoaDon hd = null;
        try {
            hd = daoHoaDon.getAllHoaDon().stream()
                    .filter(h -> h.getMaHD().equals(maHD))
                    .findFirst()
                    .orElse(null);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải hóa đơn: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (hd == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn có mã: " + maHD, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        List<CTHoaDon> ctList = null;
        ctList = daoCTHoaDon.getCTHDByMaHD(maHD);
        if (ctList == null) {
            ctList = new ArrayList<>();
        }
        double subTotal = (int) ctList.stream().mapToDouble(CTHoaDon::getDonGia).sum();
        System.out.println("tim kiem :" + subTotal);
        double tax = subTotal * TAX_RATE;
        double totalBeforeDiscount = subTotal + tax;

        double giamGiaPercent = hd.getTrangThai().equalsIgnoreCase("Đã thanh toán") && hd.getKhuyenMai() != null 
            ? hd.getKhuyenMai().getPhanTramGiamGia() 
            : 0;
        System.err.println("tim kiem:" + giamGiaPercent);
//        double giamGiaPercent = 0;
        double giamGiaAmount = (giamGiaPercent / 100.0) * totalBeforeDiscount;
        double total = totalBeforeDiscount * (1 - giamGiaPercent);

        txtDetailMaHD.setText(hd.getMaHD() != null ? hd.getMaHD() : "");
        txtNhanVien.setText(hd.getNhanVien() != null ? hd.getNhanVien().getMaNV() : "");
        txtPhongBan.setText(hd.getBan() != null ? hd.getBan().getMaBan() : "");
        txtKhachHang.setText(hd.getKhachHang() != null ? hd.getKhachHang().getMaKH() : "Khách lẻ");
        txtDetailSDT.setText(hd.getKhachHang() != null ? hd.getKhachHang().getSdt() : "");
        txtThoiGian.setText(hd.getNgayLap() != null ? dtf.format(hd.getNgayLap()) : "");
        txtThue.setText(df.format(tax));
        txtKhuyenMai.setText(giamGiaPercent + "%");
        txtTongTien.setText(df.format(total));

        detailTableModel.setRowCount(0);
        for (CTHoaDon ct : ctList) {
            Object[] row = {
                ct.getMaMonAn(),
                ct.getMaMonAn(),
                ct.getSoLuong(),
                df.format(ct.getDonGia()),
                df.format(ct.getThanhTien())
            };
            detailTableModel.addRow(row);
        }
    }

    private void toggleDetailPanel() {
        if (detailPanel.isVisible()) {
            detailPanel.setVisible(false);
            setComponentZOrder(detailPanel, 0);
            setComponentZOrder(btnNewButton, 0);
            btnNewButton.setBounds(1492, 147, 30, 36);
            scrollPane.setBounds(13, 122, 1480, 705);
        } else {
            setComponentZOrder(detailPanel, 0);
            detailPanel.setVisible(true);
            btnNewButton.setBounds(980, 147, 30, 36);
            scrollPane.setBounds(13, 122, 965, 705);
            detailPanel.setBounds(1010, 122, 500, 705);
        }
        repaint();
        revalidate();
    }

    private void showInvoice() {
        try {
            String maHD = txtDetailMaHD.getText();
            HoaDon hd = daoHoaDon.getAllHoaDon().stream()
                    .filter(h -> h.getMaHD().equals(maHD))
                    .findFirst()
                    .orElse(null);
            if (hd == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<CTHoaDon> ctList = daoCTHoaDon.getChiTietHoaDonByMaHDFromSP(maHD);
            if (ctList == null || ctList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy chi tiết hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double subTotal = (int) ctList.stream().mapToDouble(CTHoaDon::getDonGia).sum();
            double tax = subTotal * TAX_RATE;
            double total = subTotal + tax;

            PrinterJob printerJob = PrinterJob.getPrinterJob();
            printerJob.setPrintable(new Printable() {
                @Override
                public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                    if (pageIndex > 0) {
                        return NO_SUCH_PAGE;
                    }

                    Graphics2D g2d = (Graphics2D) graphics;
                    g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

                    g2d.setFont(new Font("Arial", Font.BOLD, 14));
                    g2d.setColor(Color.BLACK);

                    g2d.drawString("Nhà hàng CERBUS", 100, 50);
                    g2d.drawString("Địa chỉ: 4 Nguyễn Văn Bảo, P5, Gò Vấp", 100, 70);
                    g2d.drawString("HÓA ĐƠN", 250, 100);

                    g2d.setFont(new Font("Arial", Font.PLAIN, 12));
                    int y = 130;
                    g2d.drawString("Mã hóa đơn: " + txtDetailMaHD.getText(), 100, y); y += 20;
                    g2d.drawString("Nhân viên: " + txtNhanVien.getText(), 100, y); y += 20;
                    g2d.drawString("Phòng/Bàn: " + txtPhongBan.getText(), 100, y); y += 20;
                    g2d.drawString("Khách hàng: " + txtKhachHang.getText(), 100, y); y += 20;
                    g2d.drawString("SĐT: " + txtDetailSDT.getText(), 100, y); y += 40;

                    y += 20;
                    g2d.setFont(new Font("Arial", Font.BOLD, 12));
                    int[] columnWidths = {80, 150, 80, 100, 100};
                    int x = 100;
                    g2d.drawString("Mã SP", x, y); x += columnWidths[0];
                    g2d.drawString("Tên SP", x, y); x += columnWidths[1];
                    g2d.drawString("Số lượng", x, y); x += columnWidths[2];
                    g2d.drawString("Giá bán", x, y); x += columnWidths[3];
                    g2d.drawString("Thành tiền", x, y);

                    y += 20;
                    g2d.setFont(new Font("Arial", Font.PLAIN, 12));
                    for (CTHoaDon ct : ctList) {
                        x = 100;
                        g2d.drawString(ct.getMaMonAn(), x, y); x += columnWidths[0];
                        g2d.drawString(ct.getMaMonAn(), x, y); x += columnWidths[1];
                        g2d.drawString(String.valueOf(ct.getSoLuong()), x, y); x += columnWidths[2];
                        g2d.drawString(df.format(ct.getDonGia()), x, y); x += columnWidths[3];
                        g2d.drawString(df.format(ct.getThanhTien()), x, y);
                        y += 20;
                    }

                    y += 20;
                    g2d.drawString("Thời gian: " + txtThoiGian.getText(), 100, y); y += 20;
                    g2d.drawString("Thuế (8%): " + txtThue.getText(), 100, y); y += 20;
                    g2d.drawString("Tổng tiền: " + txtTongTien.getText(), 100, y);

                    return PAGE_EXISTS;
                }
            });

            if (printerJob.printDialog()) {
                printerJob.print();
                JOptionPane.showMessageDialog(this, "In hóa đơn thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi SQL: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (PrinterException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi in: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi không xác định: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
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