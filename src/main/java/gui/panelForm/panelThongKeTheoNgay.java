
package gui.panelForm;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.toedter.calendar.JDateChooser;
import dao.DAO_ThongKe;
import javax.swing.JOptionPane;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;

public class panelThongKeTheoNgay extends JPanel {
    private JLabel lblTongDoanhThuValue;
    private JLabel lblSoHoaDonValue;
    private JLabel lblDoanhThuTang1Value;
    private JLabel lblDoanhThuTang2Value;
    private JLabel lblDoanhThuTang3Value;
    private JLabel lblSoBanValue;
    private JLabel lblDoanhThuTrungBinhValue;
    private DefaultTableModel tableModel;
    private DAO_ThongKe daoThongKe = new DAO_ThongKe();

    public panelThongKeTheoNgay() {
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
        JLabel lblTieuDe = new JLabel("THỐNG KÊ THEO NGÀY");
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTieuDe.setHorizontalAlignment(SwingConstants.CENTER);
        lblTieuDe.setBounds(0, 10, 1535, 27);
        add(lblTieuDe);

        // Panel chứa các thành phần
        JPanel panel = new JPanel();
        panel.setBounds(10, 47, 533, 758);
        panel.setLayout(null);
        add(panel);

        JLabel lblChonNgay = new JLabel("Chọn ngày");
        lblChonNgay.setHorizontalAlignment(SwingConstants.CENTER);
        lblChonNgay.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblChonNgay.setBounds(10, 10, 146, 21);
        panel.add(lblChonNgay);

        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy");
        dateChooser.setBounds(250, 10, 150, 25);
        panel.add(dateChooser);

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

        // Bảng chi tiết hóa đơn
        String[] columnNames = {"Mã Hóa Đơn", "SĐT", "Tên", "Mã Nhân Viên", "Tên Nhân Viên", "Tổng Tiền"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(25);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(553, 47, 972, 758);
        add(scrollPane);

        // Sự kiện chọn ngày
        dateChooser.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("date".equals(evt.getPropertyName())) {
                    Date selectedDate = dateChooser.getDate();
                    if (selectedDate != null && !selectedDate.after(new Date())) {
                        try {
							updateThongKe(selectedDate);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    } else if (selectedDate != null) {
                        JOptionPane.showMessageDialog(panelThongKeTheoNgay.this, 
                            "Vui lòng chọn ngày hợp lệ (không chọn ngày tương lai)!", 
                            "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                        dateChooser.setDate(null);
                    }
                }
            }
        });
    }

    private void updateThongKe(Date ngay) throws Exception {
        try {
            Map<String, Object> thongKe = daoThongKe.getThongKeTheoNgay(ngay);
            lblTongDoanhThuValue.setText(String.format("%,d VNĐ", (Long) thongKe.getOrDefault("TongDoanhThu", 0L)));
            lblSoHoaDonValue.setText(String.valueOf(thongKe.getOrDefault("SoHoaDon", 0)));
            lblSoBanValue.setText(String.valueOf(thongKe.getOrDefault("SoBan", 0)));
            lblDoanhThuTrungBinhValue.setText(String.format("%,d VNĐ", (Long) thongKe.getOrDefault("DoanhThuTrungBinh", 0L)));

            Map<Integer, Long> doanhThuTang = daoThongKe.getDoanhThuTheoTang(ngay);
            lblDoanhThuTang1Value.setText(String.format("%,d VNĐ", doanhThuTang.getOrDefault(1, 0L)));
            lblDoanhThuTang2Value.setText(String.format("%,d VNĐ", doanhThuTang.getOrDefault(2, 0L)));
            lblDoanhThuTang3Value.setText(String.format("%,d VNĐ", doanhThuTang.getOrDefault(3, 0L)));

            tableModel.setRowCount(0);
            List<Object[]> chiTiet = daoThongKe.getChiTietHoaDon(ngay);
            for (Object[] row : chiTiet) {
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lấy dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}