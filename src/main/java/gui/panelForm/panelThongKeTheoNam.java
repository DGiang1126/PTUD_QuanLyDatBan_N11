
package gui.panelForm;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import dao.DAO_ThongKe;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;

public class panelThongKeTheoNam extends JPanel {
    private JLabel lblTongDoanhThuValue;
    private JLabel lblSoHoaDonValue;
    private JLabel lblDoanhThuTang1Value;
    private JLabel lblDoanhThuTang2Value;
    private JLabel lblDoanhThuTang3Value;
    private JLabel lblSoBanValue;
    private JLabel lblDoanhThuTrungBinhValue;
    private DefaultTableModel tableModelTongHop;
    private JTable tableDetails;
    private DAO_ThongKe daoThongKe = new DAO_ThongKe();
    private int selectedMonth = -1;
    private int currentNam = -1;
    private JPanel panel_1; // Lưu panel_1 làm biến instance
    private JLabel lblBieuDo; // Lưu lblBieuDo
    private JButton btnXemChiTiet; // Lưu btnXemChiTiet
    private JButton btnXuatExcel; // Lưu btnXuatExcel

    public panelThongKeTheoNam() {
        setBackground(SystemColor.controlHighlight);
        setLayout(null);
        setSize(1535, 850);

        // Cài đặt font FlatLaf
        FlatRobotoFont.install();
        FlatLaf.registerCustomDefaultsSource("themes");
        FlatIntelliJLaf.setup();
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));

        // Tùy chỉnh màu sắc qua UIManager
        UIManager.put("Panel.background", new Color(247, 248, 252));
        UIManager.put("Button.background", new Color(52, 102, 255));
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.disabledBackground", new Color(209, 213, 219));
        UIManager.put("Label.foreground", new Color(17, 24, 39));
        UIManager.put("Component.borderColor", new Color(229, 231, 235));
        UIManager.put("Table.gridColor", new Color(229, 231, 235));
        UIManager.put("TableHeader.background", new Color(243, 244, 246));
        UIManager.put("TableHeader.foreground", new Color(17, 24, 39));

        // Tiêu đề
        JLabel lblTieuDe = new JLabel("THỐNG KÊ THEO NĂM");
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTieuDe.setHorizontalAlignment(SwingConstants.CENTER);
        lblTieuDe.setBounds(0, 10, 1535, 27);
        add(lblTieuDe);

        // Panel trái
        JPanel panel = new JPanel();
        panel.setBounds(10, 47, 533, 435);
        panel.setLayout(null);
        add(panel);

        JLabel lblChonNam = new JLabel("Chọn năm");
        lblChonNam.setHorizontalAlignment(SwingConstants.CENTER);
        lblChonNam.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblChonNam.setBounds(10, 10, 146, 21);
        panel.add(lblChonNam);

        // Thay JYearChooser bằng JComboBox
        Integer[] years = new Integer[2025 - 2000 + 1];
        for (int i = 0; i < years.length; i++) {
            years[i] = 2000 + i;
        }
        JComboBox<Integer> yearComboBox = new JComboBox<>(years);
        yearComboBox.setBounds(250, 10, 150, 25);
        yearComboBox.setSelectedIndex(-1); // Không chọn mặc định
        panel.add(yearComboBox);

        JLabel lblTongDoanhThu = new JLabel("Tổng doanh thu:");
        lblTongDoanhThu.setHorizontalAlignment(SwingConstants.CENTER);
        lblTongDoanhThu.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTongDoanhThu.setBounds(10, 94, 146, 21);
        panel.add(lblTongDoanhThu);

        lblTongDoanhThuValue = new JLabel("0 VNĐ");
        lblTongDoanhThuValue.setHorizontalAlignment(SwingConstants.CENTER);
        lblTongDoanhThuValue.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTongDoanhThuValue.setBounds(258, 94, 265, 21);
        panel.add(lblTongDoanhThuValue);

        JLabel lblSoHoaDon = new JLabel("Tổng số hóa đơn:");
        lblSoHoaDon.setHorizontalAlignment(SwingConstants.CENTER);
        lblSoHoaDon.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblSoHoaDon.setBounds(10, 148, 146, 21);
        panel.add(lblSoHoaDon);

        lblSoHoaDonValue = new JLabel("0");
        lblSoHoaDonValue.setHorizontalAlignment(SwingConstants.CENTER);
        lblSoHoaDonValue.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblSoHoaDonValue.setBounds(258, 148, 265, 21);
        panel.add(lblSoHoaDonValue);

        JLabel lblDoanhThuTang1 = new JLabel("Doanh thu tầng 1");
        lblDoanhThuTang1.setHorizontalAlignment(SwingConstants.CENTER);
        lblDoanhThuTang1.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDoanhThuTang1.setBounds(10, 200, 146, 21);
        panel.add(lblDoanhThuTang1);

        lblDoanhThuTang1Value = new JLabel("0 VNĐ");
        lblDoanhThuTang1Value.setHorizontalAlignment(SwingConstants.CENTER);
        lblDoanhThuTang1Value.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblDoanhThuTang1Value.setBounds(258, 200, 265, 21);
        panel.add(lblDoanhThuTang1Value);

        JLabel lblDoanhThuTang2 = new JLabel("Doanh thu tầng 2");
        lblDoanhThuTang2.setHorizontalAlignment(SwingConstants.CENTER);
        lblDoanhThuTang2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDoanhThuTang2.setBounds(10, 247, 146, 21);
        panel.add(lblDoanhThuTang2);

        lblDoanhThuTang2Value = new JLabel("0 VNĐ");
        lblDoanhThuTang2Value.setHorizontalAlignment(SwingConstants.CENTER);
        lblDoanhThuTang2Value.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblDoanhThuTang2Value.setBounds(258, 252, 265, 21);
        panel.add(lblDoanhThuTang2Value);

        JLabel lblDoanhThuTang3 = new JLabel("Doanh thu tầng 3");
        lblDoanhThuTang3.setHorizontalAlignment(SwingConstants.CENTER);
        lblDoanhThuTang3.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDoanhThuTang3.setBounds(10, 298, 146, 21);
        panel.add(lblDoanhThuTang3);

        lblDoanhThuTang3Value = new JLabel("0 VNĐ");
        lblDoanhThuTang3Value.setHorizontalAlignment(SwingConstants.CENTER);
        lblDoanhThuTang3Value.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblDoanhThuTang3Value.setBounds(258, 303, 265, 21);
        panel.add(lblDoanhThuTang3Value);

        JLabel lblSoBan = new JLabel("Tổng số bàn được đặt:");
        lblSoBan.setHorizontalAlignment(SwingConstants.CENTER);
        lblSoBan.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblSoBan.setBounds(10, 350, 161, 21);
        panel.add(lblSoBan);

        lblSoBanValue = new JLabel("0");
        lblSoBanValue.setHorizontalAlignment(SwingConstants.CENTER);
        lblSoBanValue.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblSoBanValue.setBounds(258, 350, 265, 21);
        panel.add(lblSoBanValue);

        JLabel lblDoanhThuTrungBinh = new JLabel("Doanh thu trung bình:");
        lblDoanhThuTrungBinh.setHorizontalAlignment(SwingConstants.CENTER);
        lblDoanhThuTrungBinh.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblDoanhThuTrungBinh.setBounds(10, 401, 161, 21);
        panel.add(lblDoanhThuTrungBinh);

        lblDoanhThuTrungBinhValue = new JLabel("0 VNĐ");
        lblDoanhThuTrungBinhValue.setHorizontalAlignment(SwingConstants.CENTER);
        lblDoanhThuTrungBinhValue.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblDoanhThuTrungBinhValue.setBounds(258, 401, 265, 21);
        panel.add(lblDoanhThuTrungBinhValue);

        // Bảng tổng hợp theo tháng
        String[] columnTongHop = {"Tháng", "Tổng số HĐ", "Tổng tiền"};
        tableModelTongHop = new DefaultTableModel(columnTongHop, 0);
        tableDetails = new JTable(tableModelTongHop);
        tableDetails.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableDetails.setRowHeight(25);
        tableDetails.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane_1 = new JScrollPane(tableDetails);
        scrollPane_1.setBounds(10, 505, 533, 300);
        add(scrollPane_1);

        // Panel phải
        panel_1 = new JPanel();
        panel_1.setBounds(553, 47, 972, 753);
        panel_1.setLayout(null);
        add(panel_1);

        lblBieuDo = new JLabel("BIỂU ĐỒ THỐNG KÊ DOANH THU THEO NĂM");
        lblBieuDo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblBieuDo.setHorizontalAlignment(SwingConstants.CENTER);
        lblBieuDo.setBounds(10, 10, 972, 42);
        panel_1.add(lblBieuDo);

        btnXemChiTiet = new JButton("Xem chi tiết");
        btnXemChiTiet.setFont(new Font("Tahoma", Font.BOLD, 15));
        btnXemChiTiet.setBounds(84, 700, 150, 30);
        btnXemChiTiet.setEnabled(false); // Mặc định tắt
        panel_1.add(btnXemChiTiet);

        btnXuatExcel = new JButton("Xuất file excel");
        btnXuatExcel.setFont(new Font("Tahoma", Font.BOLD, 15));
        btnXuatExcel.setBounds(595, 700, 150, 30);
        panel_1.add(btnXuatExcel);

        // Sự kiện chọn tháng trong bảng tổng hợp
        tableDetails.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int row = tableDetails.getSelectedRow();
                    if (row >= 0) {
                        selectedMonth = (int) tableModelTongHop.getValueAt(row, 0);
                        btnXemChiTiet.setEnabled(true);
                    } else {
                        selectedMonth = -1;
                        btnXemChiTiet.setEnabled(false);
                    }
                }
            }
        });

        // Sự kiện nút Xem chi tiết
        btnXemChiTiet.addActionListener(e -> {
            if (selectedMonth == -1 || currentNam == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một tháng để xem chi tiết!", 
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                showChiTietHoaDonFrame(selectedMonth, currentNam);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi hiển thị chi tiết: " + ex.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Sự kiện nút Xuất Excel
        btnXuatExcel.addActionListener(e -> {
            if (currentNam == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn năm để xuất Excel!", 
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            exportToExcel(currentNam);
        });

        // Sự kiện chọn năm từ JComboBox
        yearComboBox.addActionListener(e -> {
            Integer selectedYear = (Integer) yearComboBox.getSelectedItem();
            if (selectedYear != null) {
                // Reset giao diện trước khi cập nhật
                resetThongKe();

                // Kiểm tra năm hợp lệ (không tương lai)
                Calendar now = Calendar.getInstance();
                if (selectedYear > now.get(Calendar.YEAR)) {
                    JOptionPane.showMessageDialog(panelThongKeTheoNam.this, 
                        "Vui lòng chọn năm hợp lệ (không chọn năm tương lai)!", 
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    yearComboBox.setSelectedIndex(-1); // Reset về trạng thái không chọn
                    return;
                }

                try {
                    currentNam = selectedYear;
                    updateThongKe(currentNam);
                    btnXuatExcel.setEnabled(true); // Kích hoạt nút Xuất Excel khi có năm
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panelThongKeTheoNam.this, 
                        "Lỗi khi cập nhật thống kê: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void updateThongKe(int nam) throws Exception {
        try {
            // Cập nhật chỉ số
            Map<String, Object> thongKe = daoThongKe.getThongKeTheoNam(nam);
            lblTongDoanhThuValue.setText(String.format("%,d VNĐ", (Long) thongKe.getOrDefault("TongDoanhThu", 0L)));
            lblSoHoaDonValue.setText(String.valueOf(thongKe.getOrDefault("SoHoaDon", 0)));
            lblSoBanValue.setText(String.valueOf(thongKe.getOrDefault("SoBan", 0)));
            lblDoanhThuTrungBinhValue.setText(String.format("%,d VNĐ", (Long) thongKe.getOrDefault("DoanhThuTrungBinh", 0L)));

            Map<Integer, Long> doanhThuTang = daoThongKe.getDoanhThuTheoTangNam(nam);
            lblDoanhThuTang1Value.setText(String.format("%,d VNĐ", doanhThuTang.getOrDefault(1, 0L)));
            lblDoanhThuTang2Value.setText(String.format("%,d VNĐ", doanhThuTang.getOrDefault(2, 0L)));
            lblDoanhThuTang3Value.setText(String.format("%,d VNĐ", doanhThuTang.getOrDefault(3, 0L)));

            // Cập nhật bảng tổng hợp
            tableModelTongHop.setRowCount(0);
            List<Object[]> tongHop = daoThongKe.getTongHopTheoThang(nam);
            if (tongHop.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không có dữ liệu cho năm " + nam + ".", 
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                for (Object[] row : tongHop) {
                    tableModelTongHop.addRow(row);
                }
            }

            // Cập nhật biểu đồ
            updateChart(nam);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lấy dữ liệu: " + e.getMessage(), 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            throw e;
        }
    }

    private void resetThongKe() {
        // Reset các giá trị hiển thị về mặc định
        lblTongDoanhThuValue.setText("0 VNĐ");
        lblSoHoaDonValue.setText("0");
        lblSoBanValue.setText("0");
        lblDoanhThuTrungBinhValue.setText("0 VNĐ");
        lblDoanhThuTang1Value.setText("0 VNĐ");
        lblDoanhThuTang2Value.setText("0 VNĐ");
        lblDoanhThuTang3Value.setText("0 VNĐ");

        // Reset bảng tổng hợp
        tableModelTongHop.setRowCount(0);

        // Reset biểu đồ
        panel_1.removeAll();
        panel_1.add(lblBieuDo);
        panel_1.add(btnXemChiTiet);
        panel_1.add(btnXuatExcel);
        panel_1.revalidate();
        panel_1.repaint();

        // Reset trạng thái chọn tháng và nút Xem chi tiết
        selectedMonth = -1;
        btnXemChiTiet.setEnabled(false);
        btnXuatExcel.setEnabled(false);
        currentNam = -1;
    }

    private void updateChart(int nam) throws Exception {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        try {
            List<Object[]> tongHop = daoThongKe.getTongHopTheoThang(nam);
            if (tongHop.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không có dữ liệu để hiển thị biểu đồ cho năm " + nam + ".", 
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                for (Object[] row : tongHop) {
                    String thang = String.valueOf(row[0]);
                    long doanhThu = Long.parseLong(row[2].toString().replace(",", ""));
                    dataset.addValue(doanhThu, "Doanh thu", "Tháng " + thang);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lấy dữ liệu biểu đồ: " + e.getMessage(), 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            throw e;
        }

        JFreeChart chart = ChartFactory.createBarChart(
            "Doanh thu năm " + nam,
            "Tháng",
            "Doanh thu (VNĐ)",
            dataset
        );
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBounds(10, 62, 952, 628);
        panel_1.removeAll();
        panel_1.add(chartPanel);
        panel_1.add(lblBieuDo);
        panel_1.add(btnXemChiTiet);
        panel_1.add(btnXuatExcel);
        panel_1.revalidate();
        panel_1.repaint();
    }

    private void showChiTietHoaDonFrame(int thang, int nam) throws Exception {
        JFrame frame = new JFrame("Chi tiết hóa đơn tháng " + nam);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);

        JLabel lblTieuDe = new JLabel("CHI TIẾT HÓA ĐƠN THÁNG " + thang + "/" + nam);
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTieuDe.setHorizontalAlignment(SwingConstants.CENTER);
        lblTieuDe.setBounds(0, 10, 800, 30);
        frame.add(lblTieuDe);

        String[] columnNames = {"Mã Hóa Đơn", "SĐT", "Tên", "Mã Nhân Viên", "Tên Nhân Viên", "Tổng Tiền"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(25);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 50, 760, 500);
        frame.add(scrollPane);

        try {
            List<Object[]> chiTiet = daoThongKe.getChiTietHoaDonThang(thang, nam);
            if (chiTiet.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Không có hóa đơn nào cho tháng " + thang + "/" + nam + ".", 
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                for (Object[] row : chiTiet) {
                    tableModel.addRow(row);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Lỗi khi lấy dữ liệu: " + e.getMessage(), 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            throw e;
        }

        frame.setVisible(true);
    }

    private void exportToExcel(int nam) {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("ThongKeNam_" + nam);

            // Tiêu đề
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("THỐNG KÊ DOANH THU NĂM " + nam);
            CellStyle titleStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 16);
            titleStyle.setFont(titleFont);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));

            // Chỉ số thống kê
            Row row = sheet.createRow(2);
            row.createCell(0).setCellValue("Tổng doanh thu:");
            row.createCell(1).setCellValue(lblTongDoanhThuValue.getText());
            row = sheet.createRow(3);
            row.createCell(0).setCellValue("Tổng số hóa đơn:");
            row.createCell(1).setCellValue(lblSoHoaDonValue.getText());
            row = sheet.createRow(4);
            row.createCell(0).setCellValue("Doanh thu tầng 1:");
            row.createCell(1).setCellValue(lblDoanhThuTang1Value.getText());
            row = sheet.createRow(5);
            row.createCell(0).setCellValue("Doanh thu tầng 2:");
            row.createCell(1).setCellValue(lblDoanhThuTang2Value.getText());
            row = sheet.createRow(6);
            row.createCell(0).setCellValue("Doanh thu tầng 3:");
            row.createCell(1).setCellValue(lblDoanhThuTang3Value.getText());
            row = sheet.createRow(7);
            row.createCell(0).setCellValue("Tổng số bàn được đặt:");
            row.createCell(1).setCellValue(lblSoBanValue.getText());
            row = sheet.createRow(8);
            row.createCell(0).setCellValue("Doanh thu trung bình:");
            row.createCell(1).setCellValue(lblDoanhThuTrungBinhValue.getText());

            // Bảng tổng hợp
            Row headerRow = sheet.createRow(10);
            for (int i = 0; i < tableModelTongHop.getColumnCount(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(tableModelTongHop.getColumnName(i));
                CellStyle headerStyle = workbook.createCellStyle();
                org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerStyle.setFont(headerFont);
                headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
                headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                cell.setCellStyle(headerStyle);
            }

            for (int i = 0; i < tableModelTongHop.getRowCount(); i++) {
                Row dataRow = sheet.createRow(11 + i);
                for (int j = 0; j < tableModelTongHop.getColumnCount(); j++) {
                    dataRow.createCell(j).setCellValue(tableModelTongHop.getValueAt(i, j).toString());
                }
            }

            // Tự động điều chỉnh cột
            for (int i = 0; i < tableModelTongHop.getColumnCount(); i++) {
                sheet.autoSizeColumn(i);
            }

            // Sử dụng JFileChooser để chọn nơi lưu file
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn nơi lưu file Excel");
            fileChooser.setSelectedFile(new File("ThongKeNam_" + nam + ".xlsx"));
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel Files (*.xlsx)", "xlsx");
            fileChooser.setFileFilter(filter);

            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                String filePath = fileToSave.getAbsolutePath();

                // Đảm bảo đuôi file là .xlsx
                if (!filePath.toLowerCase().endsWith(".xlsx")) {
                    filePath += ".xlsx";
                    fileToSave = new File(filePath);
                }

                // Kiểm tra nếu file đã tồn tại
                if (fileToSave.exists()) {
                    int response = JOptionPane.showConfirmDialog(this,
                        "File đã tồn tại. Bạn có muốn ghi đè không?",
                        "Xác nhận ghi đè",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                    if (response != JOptionPane.YES_OPTION) {
                        return;
                    }
                }

                // Lưu file
                try (FileOutputStream out = new FileOutputStream(fileToSave)) {
                    workbook.write(out);
                    JOptionPane.showMessageDialog(this, "Xuất file Excel thành công: " + fileToSave.getAbsolutePath(), 
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Xuất file đã bị hủy.", 
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi xuất Excel: " + ex.getMessage(), 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}