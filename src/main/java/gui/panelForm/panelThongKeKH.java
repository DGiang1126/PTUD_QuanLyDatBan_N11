
package gui.panelForm;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.toedter.calendar.JDateChooser;

import dao.DAO_ThongKe;

public class panelThongKeKH extends JPanel {
    private JDateChooser dateChooserNgay; // Chọn ngày
    private JDateChooser dateChooserThang; // Chọn tháng
    private JComboBox<Integer> comboBoxNam; // Chọn năm
    private JRadioButton rdbtnChonNgay;
    private JRadioButton rdbtnChonThang;
    private JRadioButton rdbtnChonNam;
    private DefaultTableModel tableModel;
    private DAO_ThongKe daoThongKe = new DAO_ThongKe();

    public panelThongKeKH() {
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
        JLabel lblTieuDe = new JLabel("THỐNG KÊ KHÁCH HÀNG");
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTieuDe.setHorizontalAlignment(SwingConstants.CENTER);
        lblTieuDe.setBounds(0, 10, 1535, 27);
        add(lblTieuDe);

        // Panel chứa các thành phần chọn ngày/tháng/năm
        JPanel panel = new JPanel();
        panel.setBounds(10, 47, 1515, 100);
        panel.setLayout(null);
        add(panel);

        // Radio button và DateChooser cho ngày
        rdbtnChonNgay = new JRadioButton("Chọn ngày");
        rdbtnChonNgay.setFont(new Font("Segoe UI", Font.BOLD, 15));
        rdbtnChonNgay.setBounds(81, 44, 103, 21);
        panel.add(rdbtnChonNgay);

        dateChooserNgay = new JDateChooser();
        dateChooserNgay.setDateFormatString("dd/MM/yyyy");
        dateChooserNgay.setBounds(208, 44, 125, 25);
        dateChooserNgay.setEnabled(false); // Mặc định tắt
        panel.add(dateChooserNgay);

        // Radio button và DateChooser cho tháng
        rdbtnChonThang = new JRadioButton("Chọn tháng");
        rdbtnChonThang.setFont(new Font("Segoe UI", Font.BOLD, 15));
        rdbtnChonThang.setBounds(583, 44, 119, 25);
        panel.add(rdbtnChonThang);

        dateChooserThang = new JDateChooser();
        dateChooserThang.setDateFormatString("MM/yyyy");
        dateChooserThang.setBounds(720, 44, 125, 25);
        dateChooserThang.setEnabled(false); // Mặc định tắt
        panel.add(dateChooserThang);

        // Radio button và ComboBox cho năm
        rdbtnChonNam = new JRadioButton("Chọn năm");
        rdbtnChonNam.setFont(new Font("Segoe UI", Font.BOLD, 15));
        rdbtnChonNam.setBounds(1017, 44, 103, 21);
        panel.add(rdbtnChonNam);

        Integer[] years = new Integer[2025 - 2000 + 1];
        for (int i = 0; i < years.length; i++) {
            years[i] = 2000 + i;
        }
        comboBoxNam = new JComboBox<>(years);
        comboBoxNam.setBounds(1126, 44, 138, 21);
        comboBoxNam.setEnabled(false); // Mặc định tắt
        comboBoxNam.setSelectedIndex(-1); // Không chọn mặc định
        panel.add(comboBoxNam);

        // Nhóm các radio button
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(rdbtnChonNgay);
        buttonGroup.add(rdbtnChonThang);
        buttonGroup.add(rdbtnChonNam);

        // Nút Thống kê
        JButton btnThongKe = new JButton("Thống kê");
        btnThongKe.setFont(new Font("Tahoma", Font.BOLD, 15));
        btnThongKe.setBounds(1300, 44, 150, 30);
        panel.add(btnThongKe);

        // Bảng hiển thị thông tin khách hàng
        String[] columnNames = {"STT", "Mã Khách Hàng", "Tên Khách Hàng", "SĐT", "Tổng Tiền Đã Mua"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(25);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 157, 1515, 683);
        add(scrollPane);

        // Sự kiện chọn radio button
        rdbtnChonNgay.addActionListener(e -> {
            dateChooserNgay.setEnabled(true);
            dateChooserThang.setEnabled(false);
            comboBoxNam.setEnabled(false);
            dateChooserThang.setDate(null);
            comboBoxNam.setSelectedIndex(-1);
        });

        rdbtnChonThang.addActionListener(e -> {
            dateChooserNgay.setEnabled(false);
            dateChooserThang.setEnabled(true);
            comboBoxNam.setEnabled(false);
            dateChooserNgay.setDate(null);
            comboBoxNam.setSelectedIndex(-1);
        });

        rdbtnChonNam.addActionListener(e -> {
            dateChooserNgay.setEnabled(false);
            dateChooserThang.setEnabled(false);
            comboBoxNam.setEnabled(true);
            dateChooserNgay.setDate(null);
            dateChooserThang.setDate(null);
        });

        // Sự kiện nút Thống kê
        btnThongKe.addActionListener(e -> {
            tableModel.setRowCount(0); // Reset bảng

            if (!rdbtnChonNgay.isSelected() && !rdbtnChonThang.isSelected() && !rdbtnChonNam.isSelected()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một tiêu chí thống kê (ngày, tháng, hoặc năm)!", 
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                if (rdbtnChonNgay.isSelected()) {
                    Date selectedDate = dateChooserNgay.getDate();
                    if (selectedDate == null) {
                        JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày!", 
                            "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    // Kiểm tra ngày không trong tương lai
                    Calendar now = Calendar.getInstance();
                    if (selectedDate.after(now.getTime())) {
                        JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày hợp lệ (không chọn ngày tương lai)!", 
                            "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                        dateChooserNgay.setDate(null);
                        return;
                    }
                    thongKeTheoNgay(selectedDate);
                } else if (rdbtnChonThang.isSelected()) {
                    Date selectedMonth = dateChooserThang.getDate();
                    if (selectedMonth == null) {
                        JOptionPane.showMessageDialog(this, "Vui lòng chọn tháng!", 
                            "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    // Kiểm tra tháng không trong tương lai
                    Calendar selectedCal = Calendar.getInstance();
                    selectedCal.setTime(selectedMonth);
                    Calendar now = Calendar.getInstance();
                    if (selectedCal.get(Calendar.YEAR) > now.get(Calendar.YEAR) || 
                        (selectedCal.get(Calendar.YEAR) == now.get(Calendar.YEAR) && 
                         selectedCal.get(Calendar.MONTH) > now.get(Calendar.MONTH))) {
                        JOptionPane.showMessageDialog(this, "Vui lòng chọn tháng hợp lệ (không chọn tháng tương lai)!", 
                            "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                        dateChooserThang.setDate(null);
                        return;
                    }
                    int thang = selectedCal.get(Calendar.MONTH) + 1;
                    int nam = selectedCal.get(Calendar.YEAR);
                    thongKeTheoThang(thang, nam);
                } else if (rdbtnChonNam.isSelected()) {
                    Integer selectedYear = (Integer) comboBoxNam.getSelectedItem();
                    if (selectedYear == null) {
                        JOptionPane.showMessageDialog(this, "Vui lòng chọn năm!", 
                            "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    // Kiểm tra năm không trong tương lai
                    Calendar now = Calendar.getInstance();
                    if (selectedYear > now.get(Calendar.YEAR)) {
                        JOptionPane.showMessageDialog(this, "Vui lòng chọn năm hợp lệ (không chọn năm tương lai)!", 
                            "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                        comboBoxNam.setSelectedIndex(-1);
                        return;
                    }
                    thongKeTheoNam(selectedYear);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi lấy dữ liệu: " + ex.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        });
    }

    private void thongKeTheoNgay(Date ngay) throws Exception {
        List<Object[]> data = daoThongKe.getThongKeKhachHangTheoNgay(ngay);
        if (data.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu cho ngày " + new SimpleDateFormat("dd/MM/yyyy").format(ngay) + ".", 
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } else {
            for (int i = 0; i < data.size(); i++) {
                Object[] row = data.get(i);
                tableModel.addRow(new Object[]{i + 1, row[0], row[1], row[2], String.format("%,d VNĐ", (Long) row[3])});
            }
        }
    }

    private void thongKeTheoThang(int thang, int nam) throws Exception {
        List<Object[]> data = daoThongKe.getThongKeKhachHangTheoThang(thang, nam);
        if (data.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu cho tháng " + thang + "/" + nam + ".", 
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } else {
            for (int i = 0; i < data.size(); i++) {
                Object[] row = data.get(i);
                tableModel.addRow(new Object[]{i + 1, row[0], row[1], row[2], String.format("%,d VNĐ", (Long) row[3])});
            }
        }
    }

    private void thongKeTheoNam(int nam) throws Exception {
        List<Object[]> data = daoThongKe.getThongKeKhachHangTheoNam(nam);
        if (data.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu cho năm " + nam + ".", 
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } else {
            for (int i = 0; i < data.size(); i++) {
                Object[] row = data.get(i);
                tableModel.addRow(new Object[]{i + 1, row[0], row[1], row[2], String.format("%,d VNĐ", (Long) row[3])});
            }
        }
    }
}