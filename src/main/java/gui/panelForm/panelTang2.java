package gui.panelForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import connectDatabase.BaseDAO;
import dao.DAO_Ban;
import dao.DAO_CTHoaDon;
import dao.DAO_CTPhieuDatBan;
import dao.DAO_HoaDon;
import dao.DAO_KhachHang;
import dao.DAO_KhuyenMai;
import dao.DAO_GopBan;
import dao.DAO_MonAn;
import dao.DAO_PhieuDatBan;
import entity.Ban;
import entity.CTHoaDon;
import entity.CTPhieuDatBan;
import entity.KhachHang;
import entity.KhuyenMai;
import entity.MonAn;
import entity.PhieuDatBan;
import entity.PhuongThucThanhToan;
import uk.co.caprica.vlcj.factory.DialogQuestionType;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;

public class panelTang2 extends JPanel {
	private static final long serialVersionUID = 1L;
	private RoundedPanelWithShadow panel_ChiTietBan;
	private DAO_Ban Ban_DAO;
	private JTextField txt_tongCong;
	private JTextField txt_VAT;
	private JTextField txt_tienCanTra;
	private JPanel panel_Ban;
	private JPanel panelMenu;
	private JScrollPane scrollPaneMenu;
	private JTable table;
	private JComboBox<String> comboKM;
	private DAO_KhuyenMai khuyenMai_DAO;
	private DAO_HoaDon hoaDon_DAO;
	private DAO_CTHoaDon CTHD_DAO;
	private JButton btnHuyBan;
	private DAO_MonAn monAn_DAO;
	private JComboBox<String> comboLoaiMon;
	private DAO_PhieuDatBan PDB_DAO;
	private DAO_KhachHang KH_DAO;
	private DAO_CTPhieuDatBan CTPDB_DAO;
	private DAO_GopBan gopBan_DAO;
	private String soLuong;
	private String gioiTinh;
	private String tenKH;
	private String sdt;

	public panelTang2() {
		setSize(new Dimension(1535, 850));
		FlatRobotoFont.install();
		FlatLaf.registerCustomDefaultsSource("themes");
		FlatMacLightLaf.setup();

		UIManager.put("Panel.background", new Color(247, 248, 252));
		UIManager.put("Button.background", new Color(52, 102, 255));
		UIManager.put("Button.foreground", Color.WHITE);
		UIManager.put("Button.disabledBackground", new Color(209, 213, 219));
		UIManager.put("Label.foreground", new Color(17, 24, 39));
		UIManager.put("Component.borderColor", new Color(229, 231, 235));

		setLayout(null);
		setBackground(UIManager.getColor("Panel.background"));

		panel_ChiTietBan = new RoundedPanelWithShadow(30, new Color(255, 255, 255), 10, 5, new Color(0, 0, 0, 50));
		panel_ChiTietBan.setBounds(1055, 10, 470, 755);
		panel_ChiTietBan.setLayout(null);
		panel_ChiTietBan.setVisible(false);
		add(panel_ChiTietBan);

		panel_Ban = new RoundedPanelWithShadow(30, new Color(250, 251, 255), 5, 3, new Color(0, 0, 0, 30));
		panel_Ban.setBounds(10, 10, 1032, 755);
		panel_Ban.setLayout(null);

		panel_Ban.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				panel_ChiTietBan.setVisible(false);
			}
		});

		add(panel_Ban);
		refreshBanList();
	}

	private void refreshBanList() {
	    panel_Ban.removeAll();
	    Ban_DAO = new DAO_Ban();
	    java.util.List<Ban> dsBan = Ban_DAO.getAllBansinTang2();

	    // Kích thước panel_Ban: 1032x755
	    int panelWidth = 1032;
	    int panelHeight = 755;
	    int banWidth = 450; // Chiều rộng mỗi bàn
	    int banHeight = 300; // Chiều cao mỗi bàn
	    int spacingX = 50; // Khoảng cách ngang giữa các bàn
	    int spacingY = 50; // Khoảng cách dọc giữa các hàng

	    // Tính số lượng bàn tối đa theo hàng và cột
	    int maxCols = (panelWidth + spacingX) / (banWidth + spacingX);
	    int maxRows = (panelHeight + spacingY) / (banHeight + spacingY);

	    // Bắt đầu từ giữa panel
	    int totalWidth = maxCols * banWidth + (maxCols - 1) * spacingX;
	    int totalHeight = maxRows * banHeight + (maxRows - 1) * spacingY;
	    int startX = (panelWidth - totalWidth) / 2;
	    int startY = (panelHeight - totalHeight) / 2;

	    int x = startX, y = startY;
	    int count = 0;

	    for (Ban ban : dsBan) {
	        String trangThai = ban.getTrangThai().trim();
	        RoundedPanelWithShadow panelBan = createBanPanel(ban.getTenBan().trim(), ban.getMaBan().trim(), trangThai, x, y);
	        panelBan.setPreferredSize(new java.awt.Dimension(banWidth, banHeight));
	        panelBan.setBounds(x, y, banWidth, banHeight);
	        panel_Ban.add(panelBan);

	        count++;
	        if (count % maxCols == 0) { // Khi đủ bàn trong 1 hàng, chuyển xuống hàng dưới
	            x = startX; // Reset x về vị trí bắt đầu
	            y += banHeight + spacingY; // Chuyển xuống hàng tiếp theo
	        } else { // Thêm bàn vào cùng hàng
	            x += banWidth + spacingX;
	        }
	    }

	    panel_Ban.revalidate();
	    panel_Ban.repaint();
	}

	private RoundedPanelWithShadow createBanPanel(String tenBan, String maBan, String status, int x, int y) {
	    int panelWidth = 450; 
	    int panelHeight = 300;

	    // Tạo bảng tròn với bóng
	    RoundedPanelWithShadow panel = new RoundedPanelWithShadow(30, new Color(255, 255, 255), 8, 4,
	            new Color(0, 0, 0, 40));
	    panel.setBounds(x, y, panelWidth, panelHeight);
	    panel.setLayout(null);

	    // Hình ảnh của bàn
	    ImageIcon originalIcon = new ImageIcon(panelTang2.class.getResource("/img/tableTang2.jpg"));
	    Image scaledImage = originalIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
	    ImageIcon scaledIcon = new ImageIcon(scaledImage);

	    JLabel lblImage = new JLabel(scaledIcon);
	    lblImage.setBounds((panelWidth - 120) / 2, 40, 120, 120); // Căn giữa hình ảnh
	    panel.add(lblImage);

	    // Tiêu đề bàn
	    JLabel lblBanTitle = new JLabel(tenBan);
	    lblBanTitle.setForeground(UIManager.getColor("Label.foreground"));
	    lblBanTitle.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 20));
	    lblBanTitle.setHorizontalAlignment(SwingConstants.CENTER);
	    lblBanTitle.setBounds(0, 10, panelWidth, 30); // Căn giữa phía trên
	    panel.add(lblBanTitle);

	    // Trạng thái
	    JLabel lblStatus = new JLabel(status);
	    lblStatus.setFont(new Font("Dialog", Font.BOLD, 16));
	    lblStatus.setForeground(status.equalsIgnoreCase("CÒN TRỐNG") ? new Color(16, 185, 129) : new Color(239, 68, 68));
	    lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
	    lblStatus.setBounds(0, 170, panelWidth, 25); // Căn giữa ngay dưới hình ảnh
	    panel.add(lblStatus);

	    // Nút đặt bàn hoặc xem chi tiết
	    String buttonText = status.equals("CÒN TRỐNG") ? "ĐẶT BÀN" : "XEM CHI TIẾT";
	    JButton btnDatBan = new JButton(buttonText);
	    btnDatBan.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
	    btnDatBan.setForeground(Color.WHITE);
	    btnDatBan.setBackground(status.equals("BẢO TRÌ") ? Color.GRAY : new Color(37, 99, 235));
	    btnDatBan.setFocusPainted(false);
	    btnDatBan.setBounds((panelWidth - 150) / 2, 210, 150, 40); // Căn giữa phía dưới
	    btnDatBan.setBorderPainted(false);
	    btnDatBan.setCursor(Cursor.getPredefinedCursor(status.equals("BẢO TRÌ") ? Cursor.DEFAULT_CURSOR : Cursor.HAND_CURSOR));
	    btnDatBan.setEnabled(!status.equals("BẢO TRÌ"));

		if (!status.equals("BẢO TRÌ")) {
			btnDatBan.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent evt) {
					btnDatBan.setBackground(new Color(37, 99, 235));
				}

				@Override
				public void mouseExited(MouseEvent evt) {
					btnDatBan.setBackground(UIManager.getColor("Button.background"));
				}
			});
		}

		btnDatBan.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        if (status.equals("ĐANG SỬ DỤNG") || status.equals("ĐÃ ĐẶT")) {
		            try {
		                // Kiểm tra xem bàn có được gộp không
		                DAO_GopBan gopBan_DAO = new DAO_GopBan();
		                String maBanChinh = gopBan_DAO.getMaBanChinh(maBan);
		                if (maBanChinh != null) { // Nếu bàn đã được gộp
		                    Ban_DAO = new DAO_Ban();
		                    Ban banChinh = Ban_DAO.getBanbyMaBan(maBanChinh);
		                    JOptionPane.showMessageDialog(panelTang2.this, tenBan + " đã được gộp với " + banChinh.getTenBan() + "!");
		                    // Hiển thị chi tiết của bàn chính
		                    PDB_DAO = new DAO_PhieuDatBan();
		                    PhieuDatBan pdbChinh = PDB_DAO.getLatestPDBByMaBan(maBanChinh);
		                    if (pdbChinh != null) {
		                        KH_DAO = new DAO_KhachHang();
		                        KhachHang kh = KH_DAO.getKhachHangbyMa(pdbChinh.getMaKH());
		                        CTPDB_DAO = new DAO_CTPhieuDatBan();
		                        CTPhieuDatBan ctpdb = CTPDB_DAO.getCTPDBByMa(pdbChinh.getMaPDB());
		                        if (kh != null && ctpdb != null) {
		                            displayBanDetails(banChinh.getTenBan(), banChinh.getTrangThai(), kh.getMaKH(), kh.getTenKH(),
		                                    kh.getSdt(), kh.getGioiTinh(), String.valueOf(ctpdb.getSoNguoi()));
		                            panel_ChiTietBan.setVisible(true);
		                        } else {
		                            JOptionPane.showMessageDialog(panelTang2.this, "Không tìm thấy thông tin khách hàng của bàn chính!");
		                            panel_ChiTietBan.setVisible(false);
		                        }
		                    } else {
		                        JOptionPane.showMessageDialog(panelTang2.this, "Không tìm thấy phiếu đặt bàn của bàn chính!");
		                        panel_ChiTietBan.setVisible(false);
		                    }
		                } else { // Nếu bàn không được gộp
		                    PDB_DAO = new DAO_PhieuDatBan();
		                    PhieuDatBan pdb = PDB_DAO.getLatestPDBByMaBan(maBan);
		                    if (pdb != null) {
		                        KH_DAO = new DAO_KhachHang();
		                        KhachHang kh = KH_DAO.getKhachHangbyMa(pdb.getMaKH());
		                        CTPDB_DAO = new DAO_CTPhieuDatBan();
		                        CTPhieuDatBan ctpdb = CTPDB_DAO.getCTPDBByMa(pdb.getMaPDB());

		                        if (kh != null && ctpdb != null) {
		                            displayBanDetails(tenBan, status, kh.getMaKH(), kh.getTenKH(), kh.getSdt(),
		                                    kh.getGioiTinh(), String.valueOf(ctpdb.getSoNguoi()));
		                            panel_ChiTietBan.setVisible(true);
		                        } else {
		                            JOptionPane.showMessageDialog(panelTang2.this, "Không tìm thấy thông tin khách hàng!");
		                            displayBanDetails(tenBan, status, null, null, null, null, null);
		                            panel_ChiTietBan.setVisible(true);
		                        }
		                    } else {
		                        JOptionPane.showMessageDialog(panelTang2.this, "Không tìm thấy phiếu đặt bàn!");
		                        displayBanDetails(tenBan, status, null, null, null, null, null);
		                        panel_ChiTietBan.setVisible(true);
		                    }
		                }
		            } catch (SQLException ex) {
		                ex.printStackTrace();
		                JOptionPane.showMessageDialog(panelTang2.this, "Lỗi khi lấy thông tin: " + ex.getMessage());
		            } catch (Exception e1) {
		                e1.printStackTrace();
		            }
		        } else {
		            panel_ChiTietBan.setVisible(false);
		            JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(panelTang2.this), "ĐẶT BÀN",
		                    Dialog.ModalityType.APPLICATION_MODAL);
		            panelPhieuDatBanTang2 panelPDB = new panelPhieuDatBanTang2();
		            panelPDB.setTableNumber(tenBan);
		            panelPDB.setRefreshCallback(() -> refreshBanList());
		            panelPDB.setCustomerInfoCallback((maPDB) -> {
		                PDB_DAO = new DAO_PhieuDatBan();
		                PhieuDatBan PDB = PDB_DAO.getPDBbyMa(maPDB);
		                KH_DAO = new DAO_KhachHang();
		                KhachHang KH = KH_DAO.getKhachHangbyMa(PDB.getMaKH());
		                CTPDB_DAO = new DAO_CTPhieuDatBan();
		                CTPhieuDatBan CTPDB = CTPDB_DAO.getCTPDBByMa(maPDB);

		                try {
		                    displayBanDetails(tenBan, "ĐÃ ĐẶT", KH.getMaKH(), KH.getTenKH(), KH.getSdt(), KH.getGioiTinh(),
		                            String.valueOf(CTPDB.getSoNguoi()));
		                } catch (Exception e1) {
		                    e1.printStackTrace();
		                }
		                panel_ChiTietBan.setVisible(true);
		            });
		            dialog.setContentPane(panelPDB);
		            dialog.setUndecorated(false);
		            dialog.setSize(460, 455);
		            dialog.setLocationRelativeTo(null);
		            dialog.setVisible(true);
		        }
		    }
		});

		panel.add(btnDatBan);
		return panel;
	}

	private void displayBanDetails(String tenBan, String status, String maKH, String tenKH, String sdt, String gioiTinh,
			String soLuong) throws Exception {
		panel_ChiTietBan.removeAll();
		panel_ChiTietBan.setBackground(new Color(255, 255, 255));

		btnHuyBan = new JButton("HỦY BÀN");
		btnHuyBan.setFont(new Font("Dialog", Font.BOLD, 15));
		btnHuyBan.setBounds(10, 684, 140, 43);
		btnHuyBan.setBackground(
				status.equals("ĐÃ ĐẶT") ? new Color(239, 68, 68) : UIManager.getColor("Button.disabledBackground"));
		btnHuyBan.setForeground(Color.WHITE);
		btnHuyBan.setFocusPainted(false);
		btnHuyBan.setBorderPainted(false);
		btnHuyBan.setEnabled(status.equals("ĐÃ ĐẶT"));

		if (status.equals("ĐÃ ĐẶT")) {
			btnHuyBan.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					btnHuyBan.setBackground(new Color(220, 38, 38));
				}

				@Override
				public void mouseExited(MouseEvent e) {
					btnHuyBan.setBackground(new Color(239, 68, 68));
				}
			});
		}

		btnHuyBan.addActionListener(e -> {
			if (status.equals("ĐÃ ĐẶT")) {
				int confirm = JOptionPane.showConfirmDialog(panelTang2.this,
						"Bạn có muốn hủy bàn " + tenBan + " không?", "Xác Nhận Hủy Bàn", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE);
				if (confirm == JOptionPane.YES_OPTION) {
					try {
						Ban_DAO.capNhatTrangThaiBan(tenBan, "CÒN TRỐNG");
						PDB_DAO.deletePhieuDatBanByMaBan(Ban_DAO.getMaBanByTenBan(tenBan));
						panel_ChiTietBan.setVisible(false);
						refreshBanList();
						JOptionPane.showMessageDialog(panelTang2.this, "Hủy bàn thành công");
						panel_ChiTietBan.setVisible(false);
					} catch (SQLException ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(panelTang2.this, "Lỗi khi hủy bàn: " + ex.getMessage());
					}
				}
			}
		});
		panel_ChiTietBan.add(btnHuyBan);

		JButton btnGopBan = new JButton("GỘP BÀN");
		btnGopBan.setFont(new Font("Dialog", Font.BOLD, 15));
		btnGopBan.setBounds(160, 684, 140, 43);
		btnGopBan.setBackground(new Color(245, 158, 11));
		btnGopBan.setForeground(Color.WHITE);
		btnGopBan.setFocusPainted(false);
		btnGopBan.setBorderPainted(false);
		if(status.equals("ĐANG SỬ DỤNG") || status.equals("ĐÃ ĐẶT")) {
			btnGopBan.setEnabled(true);
		}else {
			btnGopBan.setEnabled(false);
			btnGopBan.setBackground(UIManager.getColor("Button.disableBackground"));
		}
		btnGopBan.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnGopBan.setBackground(new Color(217, 119, 6));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				btnGopBan.setBackground(new Color(245, 158, 11));
			}
		});
		btnGopBan.addActionListener(e -> {
			if(status.equals("ĐANG SỬ DỤNG") || status.equals("ĐÃ ĐẶT")) {
				try {
					showMergeTableDialog(tenBan, maKH);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		panel_ChiTietBan.add(btnGopBan);

		JButton btnChuyenBan = new JButton("CHUYỂN BÀN");
		btnChuyenBan.setFont(new Font("Dialog", Font.BOLD, 15));
		btnChuyenBan.setBounds(310, 684, 140, 43);
		btnChuyenBan.setBackground(new Color(59, 130, 246));
		btnChuyenBan.setForeground(Color.WHITE);
		btnChuyenBan.setFocusPainted(false);
		btnChuyenBan.setBorderPainted(false);
		btnChuyenBan.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnChuyenBan.setBackground(new Color(37, 99, 235));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				btnChuyenBan.setBackground(new Color(59, 130, 246));
			}
		});
		btnChuyenBan.addActionListener(e -> showChuyenBanDialog(tenBan, maKH, status));
		panel_ChiTietBan.add(btnChuyenBan);

		JPanel panel = new RoundedPanel(30);
		panel.setBounds(10, 20, 439, 654);
		panel.setBackground(Color.WHITE);
		panel.setLayout(null);
		panel_ChiTietBan.add(panel);

		JLabel lblChiTiet = new JLabel("CHI TIẾT BÀN");
		lblChiTiet.setBounds(10, 10, 419, 50);
		lblChiTiet.setFont(new Font("Roboto", Font.BOLD, 26));
		lblChiTiet.setForeground(new Color(17, 24, 39));
		lblChiTiet.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblChiTiet);

		JLabel lblSoBan = new JLabel("SỐ BÀN: " + tenBan);
		lblSoBan.setBounds(10, 51, 419, 20);
		lblSoBan.setHorizontalAlignment(SwingConstants.LEFT);
		lblSoBan.setFont(new Font("Roboto", Font.PLAIN, 15));
		lblSoBan.setForeground(new Color(55, 65, 81));
		panel.add(lblSoBan);

		JLabel lblTenKhachHang = new JLabel("TÊN KHÁCH HÀNG: " + (tenKH != null ? tenKH : "Chưa có"));
		lblTenKhachHang.setBounds(10, 73, 419, 20);
		lblTenKhachHang.setHorizontalAlignment(SwingConstants.LEFT);
		lblTenKhachHang.setFont(new Font("Roboto", Font.PLAIN, 15));
		lblTenKhachHang.setForeground(new Color(55, 65, 81));
		panel.add(lblTenKhachHang);

		JLabel lblSoLuong = new JLabel("SỐ LƯỢNG: " + (soLuong != null ? soLuong : "0"));
		lblSoLuong.setBounds(10, 92, 419, 20);
		lblSoLuong.setHorizontalAlignment(SwingConstants.LEFT);
		lblSoLuong.setFont(new Font("Roboto", Font.PLAIN, 15));
		lblSoLuong.setForeground(new Color(55, 65, 81));
		panel.add(lblSoLuong);

		JLabel lblDanhSachMon = new JLabel("DANH SÁCH MÓN ĂN ĐÃ GỌI");
		lblDanhSachMon.setBounds(10, 110, 419, 50);
		lblDanhSachMon.setHorizontalAlignment(SwingConstants.CENTER);
		lblDanhSachMon.setForeground(new Color(17, 24, 39));
		lblDanhSachMon.setFont(new Font("Roboto", Font.BOLD, 20));
		panel.add(lblDanhSachMon);

		DefaultTableModel model = new DefaultTableModel(new Object[][] {},
				new String[] { "Tên món ăn", "Số lượng", "Giá", "Thành tiền", "Xóa" }) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 4; // Only the delete button column is editable
			}
		};
		table = new JTable(model);
		table.setFont(new Font("Roboto", Font.PLAIN, 14));
		table.setRowHeight(30);
		table.setGridColor(new Color(229, 231, 235));
		table.setShowGrid(true);
		table.setBackground(Color.WHITE);
		table.setSelectionBackground(new Color(191, 219, 254));
		table.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 14));
		table.getTableHeader().setBackground(new Color(243, 244, 246));
		table.getTableHeader().setForeground(new Color(17, 24, 39));

		// Set column widths
		table.getColumnModel().getColumn(0).setPreferredWidth(100); // Tên món ăn
		table.getColumnModel().getColumn(1).setPreferredWidth(80); // Số lượng
		table.getColumnModel().getColumn(2).setPreferredWidth(80); // Giá
		table.getColumnModel().getColumn(3).setPreferredWidth(90); // Thành tiền
		table.getColumnModel().getColumn(4).setPreferredWidth(60); // Xóa

		// Add delete button renderer and editor
		table.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
		table.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox(), table, tenBan, this));

		if (maKH != null) {
			hoaDon_DAO = new DAO_HoaDon();
			String maHD = hoaDon_DAO.getMaHDByMaKHAndStatus(maKH, "Chưa thanh toán");
			if (maHD != null) {
				CTHD_DAO = new DAO_CTHoaDon();
				monAn_DAO = new DAO_MonAn();
				List<CTHoaDon> dsachCTHD = CTHD_DAO.getCTHDByMaHD(maHD);
				if (dsachCTHD != null) {
					for (CTHoaDon row : dsachCTHD) {
						String maMonAn = row.getMaMonAn();
						int soLuongMon = row.getSoLuong();
						MonAn monAn = monAn_DAO.getMonAnByMa(maMonAn);
						if (monAn != null) {
							int gia = (int) monAn.getGia();
							model.addRow(
									new Object[] { monAn.getTenMonAn(), soLuongMon, gia, soLuongMon * gia, "Xóa" });
						}
					}
				}
				if (dsachCTHD == null || dsachCTHD.isEmpty()) {
					table.setEnabled(false);
				} else {
					table.setEnabled(true);
				}
			}
		}

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(10, 170, 419, 308);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		panel.add(scrollPane);

		JButton btnGoiMon = new JButton("GỌI MÓN");
		btnGoiMon.setBounds(10, 611, 180, 43);
		btnGoiMon.setFont(new Font("Roboto", Font.BOLD, 15));
		btnGoiMon.setBackground(new Color(249, 115, 22));
		btnGoiMon.setForeground(Color.WHITE);
		btnGoiMon.setFocusPainted(false);
		btnGoiMon.setBorderPainted(false);
		btnGoiMon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnGoiMon.setBackground(new Color(234, 88, 12));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				btnGoiMon.setBackground(new Color(249, 115, 22));
			}
		});
		btnGoiMon.addActionListener(e -> {
			try {
				showMenuPanel(tenBan);
			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(panelTang2.this, "Lỗi khi hiển thị menu: " + ex.getMessage());
			}
		});
		panel.add(btnGoiMon);

		JButton btnThanhToan = new JButton("THANH TOÁN");
		btnThanhToan.setBounds(249, 611, 180, 43);
		btnThanhToan.setFont(new Font("Roboto", Font.BOLD, 15));
		btnThanhToan.setBackground(new Color(16, 185, 129));
		btnThanhToan.setForeground(Color.WHITE);
		btnThanhToan.setFocusPainted(false);
		btnThanhToan.setBorderPainted(false);
		btnThanhToan.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnThanhToan.setBackground(new Color(5, 150, 105));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				btnThanhToan.setBackground(new Color(16, 185, 129));
			}
		});
		btnThanhToan.addActionListener(e -> {
			try {
				String tienCanTraText = txt_tienCanTra.getText();
				if (tienCanTraText == null || tienCanTraText.isEmpty() || Integer.parseInt(tienCanTraText) <= 0) {
					JOptionPane.showMessageDialog(panelTang2.this, "Số tiền cần thanh toán không hợp lệ!");
					return;
				}
				long amount = Long.parseLong(tienCanTraText);

				if (maKH == null) {
					JOptionPane.showMessageDialog(panelTang2.this, "Không tìm thấy thông tin khách hàng!");
					return;
				}

				hoaDon_DAO = new DAO_HoaDon();
				String maHD = hoaDon_DAO.getMaHDByMaKHAndStatus(maKH, "Chưa thanh toán");
				if (maHD == null) {
					JOptionPane.showMessageDialog(panelTang2.this, "Không tìm thấy hóa đơn!");
					return;
				}

				showPaymentMethodDialog(tenBan, maKH, maHD, amount);
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(panelTang2.this, "Số tiền không hợp lệ!");
			}
		});
		panel.add(btnThanhToan);

		JLabel lblTongCong = new JLabel("TỔNG CỘNG");
		lblTongCong.setBounds(50, 488, 114, 20);
		lblTongCong.setHorizontalAlignment(SwingConstants.LEFT);
		lblTongCong.setFont(new Font("Roboto", Font.BOLD, 15));
		lblTongCong.setForeground(new Color(55, 65, 81));
		panel.add(lblTongCong);

		JLabel lblTongTienCanTra = new JLabel("TỔNG TIỀN CẦN TRẢ:");
		lblTongTienCanTra.setBounds(50, 581, 161, 20);
		lblTongTienCanTra.setHorizontalAlignment(SwingConstants.LEFT);
		lblTongTienCanTra.setFont(new Font("Roboto", Font.BOLD, 15));
		lblTongTienCanTra.setForeground(new Color(55, 65, 81));
		panel.add(lblTongTienCanTra);

		txt_tongCong = new JTextField("0");
		txt_tongCong.setBounds(250, 488, 136, 19);
		txt_tongCong.setFont(new Font("Roboto", Font.PLAIN, 14));
		txt_tongCong.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235)));
		txt_tongCong.setEditable(false);
		panel.add(txt_tongCong);

		txt_tienCanTra = new JTextField("0");
		txt_tienCanTra.setBounds(250, 580, 136, 19);
		txt_tienCanTra.setFont(new Font("Roboto", Font.PLAIN, 14));
		txt_tienCanTra.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235)));
		txt_tienCanTra.setEditable(false);
		panel.add(txt_tienCanTra);

		JLabel lblVat = new JLabel("VAT");
		lblVat.setBounds(50, 507, 114, 20);
		lblVat.setHorizontalAlignment(SwingConstants.LEFT);
		lblVat.setFont(new Font("Roboto", Font.BOLD, 15));
		lblVat.setForeground(new Color(55, 65, 81));
		panel.add(lblVat);

		JLabel lblKhuyenMai = new JLabel("KHUYẾN MÃI");
		lblKhuyenMai.setBounds(50, 530, 114, 20);
		lblKhuyenMai.setHorizontalAlignment(SwingConstants.LEFT);
		lblKhuyenMai.setFont(new Font("Roboto", Font.BOLD, 15));
		lblKhuyenMai.setForeground(new Color(55, 65, 81));
		panel.add(lblKhuyenMai);

		khuyenMai_DAO = new DAO_KhuyenMai();
		try {
			java.util.List<KhuyenMai> danhsachKM = khuyenMai_DAO.getAllKMs();
			java.util.List<String> kmOptions = new ArrayList<>();
			kmOptions.add("Không có");
			for (KhuyenMai km : danhsachKM) {
				kmOptions.add(String.format("%.0f%%", km.getPhanTramGiamGia()));
			}
			comboKM = new JComboBox<>(kmOptions.toArray(new String[0]));
		} catch (SQLException e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(panelTang2.this, "Lỗi khi lấy khuyến mãi: " + e1.getMessage());
		}

		comboKM.setBounds(250, 532, 136, 21);
		comboKM.setFont(new Font("Roboto", Font.PLAIN, 14));
		comboKM.setBackground(Color.WHITE);
		comboKM.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235)));
		comboKM.addActionListener(e -> updateTotal());
		panel.add(comboKM);

		txt_VAT = new JTextField("0");
		txt_VAT.setBounds(250, 510, 136, 19);
		txt_VAT.setFont(new Font("Roboto", Font.PLAIN, 14));
		txt_VAT.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235)));
		txt_VAT.setEditable(false);
		panel.add(txt_VAT);

		updateTotal();

		panel_ChiTietBan.revalidate();
		panel_ChiTietBan.repaint();
	}

//	private void showPaymentMethodDialog(String tenBan, String maKH, String maHD, long amount) {
//		JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Chọn Phương Thức Thanh Toán",
//				Dialog.ModalityType.APPLICATION_MODAL);
//		dialog.getContentPane().setLayout(null);
//		dialog.setSize(400, 300);
//		dialog.setLocationRelativeTo(null);
//		dialog.setUndecorated(true);
//
//		JLabel lblTitle = new JLabel("CHỌN PHƯƠNG THỨC THANH TOÁN");
//		lblTitle.setBounds(0, 20, 400, 30);
//		lblTitle.setFont(new Font("Roboto", Font.BOLD, 18));
//		lblTitle.setForeground(new Color(17, 24, 39));
//		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
//		dialog.getContentPane().add(lblTitle);
//
//		String[] paymentMethods = { "Chọn phương thức thanh toán", "Tiền mặt", "Chuyển khoản ngân hàng", "Ví điện tử Momo" };
//		JComboBox<String> comboPTTT = new JComboBox<>(paymentMethods);
//		comboPTTT.setBounds(50, 60, 300, 40);
//		comboPTTT.setFont(new Font("Roboto", Font.PLAIN, 14));
//		comboPTTT.setBackground(Color.WHITE);
//		dialog.getContentPane().add(comboPTTT);
//
//		JLabel lblCashAmount = new JLabel("SỐ TIỀN KHÁCH ĐƯA:");
//		lblCashAmount.setBounds(50, 110, 150, 30);
//		lblCashAmount.setFont(new Font("Roboto", Font.PLAIN, 14));
//		lblCashAmount.setVisible(false);
//		dialog.getContentPane().add(lblCashAmount);
//
//		JTextField txtCashAmount = new JTextField();
//		txtCashAmount.setBounds(200, 110, 150, 30);
//		txtCashAmount.setFont(new Font("Roboto", Font.PLAIN, 14));
//		txtCashAmount.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235)));
//		txtCashAmount.setVisible(false);
//		dialog.getContentPane().add(txtCashAmount);
//
//		JButton btnCancel = new JButton("HỦY");
//		btnCancel.setBounds(50, 200, 150, 40);
//		btnCancel.setFont(new Font("Roboto", Font.BOLD, 14));
//		btnCancel.setBackground(new Color(239, 68, 68));
//		btnCancel.setForeground(Color.WHITE);
//		btnCancel.setFocusPainted(false);
//		btnCancel.setBorderPainted(false);
//		btnCancel.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseEntered(MouseEvent e) {
//				btnCancel.setBackground(new Color(220, 38, 38));
//			}
//
//			@Override
//			public void mouseExited(MouseEvent e) {
//				btnCancel.setBackground(new Color(239, 68, 68));
//			}
//		});
//		btnCancel.addActionListener(e -> dialog.dispose());
//		dialog.getContentPane().add(btnCancel);
//
//		JButton btnConfirm = new JButton("XÁC NHẬN");
//		btnConfirm.setBounds(200, 200, 150, 40);
//		btnConfirm.setFont(new Font("Roboto", Font.BOLD, 14));
//		btnConfirm.setBackground(new Color(34, 197, 94));
//		btnConfirm.setForeground(Color.WHITE);
//		btnConfirm.setFocusPainted(false);
//		btnConfirm.setBorderPainted(false);
//		btnConfirm.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseEntered(MouseEvent e) {
//				btnConfirm.setBackground(new Color(22, 163, 74));
//			}
//
//			@Override
//			public void mouseExited(MouseEvent e) {
//				btnConfirm.setBackground(new Color(34, 197, 94));
//			}
//		});
//
//		comboPTTT.addActionListener(e -> {
//			String selectedMethod = (String) comboPTTT.getSelectedItem();
//			boolean isCash = selectedMethod.equals("Tiền mặt");
//			lblCashAmount.setVisible(isCash);
//			txtCashAmount.setVisible(isCash);
//		});
//
//		btnConfirm.addActionListener(e -> {
//			String selectedMethod = (String) comboPTTT.getSelectedItem();
//			try {
//				if (selectedMethod.equals("Tiền mặt")) {
//					String cashInput = txtCashAmount.getText().trim();
//					if (cashInput.isEmpty()) {
//						JOptionPane.showMessageDialog(dialog, "Vui lòng nhập số tiền khách đưa!");
//						return;
//					}
//					long cashAmount;
//					try {
//						cashAmount = Long.parseLong(cashInput);
//					} catch (NumberFormatException ex) {
//						JOptionPane.showMessageDialog(dialog, "Số tiền không hợp lệ!");
//						return;
//					}
//					if (cashAmount < amount) {
//						JOptionPane.showMessageDialog(dialog, "Số tiền khách đưa không đủ!");
//						return;
//					}
//					long change = cashAmount - amount;
//					hoaDon_DAO.capNhatTrangThaiHoaDon(maHD, "Đã thanh toán");
//					String maBanChinh = Ban_DAO.getMaBanByTenBan(tenBan);
//	                if (maBanChinh != null) {
//	                    // Lấy danh sách bàn phụ từ GopBan
//	                    DAO_GopBan gopBan_DAO = new DAO_GopBan();
//	                    List<String> mergedBans = gopBan_DAO.getMergedBans(maBanChinh);
//
//	                    // Cập nhật trạng thái tất cả bàn thành "CÒN TRỐNG"
//	                    Ban_DAO.capNhatTrangThaiBan(tenBan, "CÒN TRỐNG"); // Bàn chính
//	                    for (String maBanGop : mergedBans) {
//	                        Ban banGop = Ban_DAO.getBanbyMaBan(maBanGop);
//	                        if (banGop != null) {
//	                            Ban_DAO.capNhatTrangThaiBan(banGop.getTenBan(), "CÒN TRỐNG");
//	                        }
//	                    }
//
//	                    // Xóa thông tin gộp bàn
//	                    gopBan_DAO.deleteGopBanByMaBanChinh(maBanChinh);
//	                }
//					JOptionPane.showMessageDialog(dialog,
//							"Thanh toán thành công!\n" + "Phương thức: " + selectedMethod + "\n" + "Số tiền cần trả: "
//									+ amount + "\n" + "Số tiền khách đưa: " + cashAmount + "\n" + "Tiền thối: "
//									+ change);
//					refreshBanList();
//					panel_ChiTietBan.setVisible(false);
//					panel_Ban.setVisible(true);
//					dialog.dispose();
//				} else if (selectedMethod.equals("Ví điện tử Momo")) {
//					generateAndShowQRCode(tenBan);
//					hoaDon_DAO.capNhatTrangThaiHoaDon(maHD, "Đã thanh toán");
//		            String maBanChinh = Ban_DAO.getMaBanByTenBan(tenBan);
//		            if (maBanChinh != null) {
//		                // Lấy danh sách bàn phụ từ GopBan
//		                DAO_GopBan gopBan_DAO = new DAO_GopBan();
//		                List<String> mergedBans = gopBan_DAO.getMergedBans(maBanChinh);
//
//		                // Cập nhật trạng thái tất cả bàn thành "CÒN TRỐNG"
//		                Ban_DAO.capNhatTrangThaiBan(tenBan, "CÒN TRỐNG"); // Bàn chính
//		                for (String maBanGop : mergedBans) {
//		                    Ban banGop = Ban_DAO.getBanbyMaBan(maBanGop);
//		                    if (banGop != null) {
//		                        Ban_DAO.capNhatTrangThaiBan(banGop.getTenBan(), "CÒN TRỐNG");
//		                    }
//		                }
//
//		                // Xóa thông tin gộp bàn
//		                gopBan_DAO.deleteGopBanByMaBanChinh(maBanChinh);
//		            }
//		            JOptionPane.showMessageDialog(dialog,
//		                    "Thanh toán thành công!\n" +
//		                    "Phương thức: " + selectedMethod + "\n" +
//		                    "Số tiền cần trả: " + amount);
//		            refreshBanList();
//		            panel_ChiTietBan.setVisible(false);
//		            panel_Ban.setVisible(true);
//					dialog.dispose();
//				} else if (selectedMethod.equals("Chuyển khoản ngân hàng")) {
//					generateAndShowBank(tenBan);
//					hoaDon_DAO.capNhatTrangThaiHoaDon(maHD, "Đã thanh toán");
//		            String maBanChinh = Ban_DAO.getMaBanByTenBan(tenBan);
//		            if (maBanChinh != null) {
//		                // Lấy danh sách bàn phụ từ GopBan
//		                DAO_GopBan gopBan_DAO = new DAO_GopBan();
//		                List<String> mergedBans = gopBan_DAO.getMergedBans(maBanChinh);
//
//		                // Cập nhật trạng thái tất cả bàn thành "CÒN TRỐNG"
//		                Ban_DAO.capNhatTrangThaiBan(tenBan, "CÒN TRỐNG"); // Bàn chính
//		                for (String maBanGop : mergedBans) {
//		                    Ban banGop = Ban_DAO.getBanbyMaBan(maBanGop);
//		                    if (banGop != null) {
//		                        Ban_DAO.capNhatTrangThaiBan(banGop.getTenBan(), "CÒN TRỐNG");
//		                    }
//		                }
//
//		                // Xóa thông tin gộp bàn
//		                gopBan_DAO.deleteGopBanByMaBanChinh(maBanChinh);
//		            }
//		            JOptionPane.showMessageDialog(dialog,
//		                    "Thanh toán thành công!\n" +
//		                    "Phương thức: " + selectedMethod + "\n" +
//		                    "Số tiền cần trả: " + amount);
//		            refreshBanList();
//		            panel_ChiTietBan.setVisible(false);
//		            panel_Ban.setVisible(true);
//
//					dialog.dispose();
//				}
//			} catch (SQLException ex) {
//				ex.printStackTrace();
//				JOptionPane.showMessageDialog(dialog, "Lỗi khi cập nhật dữ liệu: " + ex.getMessage());
//			} catch (Exception ex) {
//				ex.printStackTrace();
//				JOptionPane.showMessageDialog(dialog, "Lỗi khi tạo mã QR: " + ex.getMessage());
//			}
//		});
//		dialog.getContentPane().add(btnConfirm);
//
//		dialog.setVisible(true);
//	}
	
	private void showPaymentMethodDialog(String tenBan, String maKH, String maHD, long amount) {
	    JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Chọn Phương Thức Thanh Toán",
	            Dialog.ModalityType.APPLICATION_MODAL);
	    dialog.setLayout(null);
	    dialog.setSize(400, 300);
	    dialog.setLocationRelativeTo(null);
	    dialog.setUndecorated(true);

	    JLabel lblTitle = new JLabel("CHỌN PHƯƠNG THỨC THANH TOÁN");
	    lblTitle.setBounds(0, 20, 400, 30);
	    lblTitle.setFont(new Font("Roboto", Font.BOLD, 18));
	    lblTitle.setForeground(new Color(17, 24, 39));
	    lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
	    dialog.add(lblTitle);

	    String[] paymentMethods = { "Chọn phương thức thanh toán", "Tiền mặt", "Chuyển khoản ngân hàng", "Ví điện tử Momo" };
	    JComboBox<String> comboPTTT = new JComboBox<>(paymentMethods);
	    comboPTTT.setBounds(50, 60, 300, 40);
	    comboPTTT.setFont(new Font("Roboto", Font.PLAIN, 14));
	    comboPTTT.setBackground(Color.WHITE);
	    dialog.add(comboPTTT);

	    JLabel lblCashAmount = new JLabel("SỐ TIỀN KHÁCH ĐƯA:");
	    lblCashAmount.setBounds(50, 110, 150, 30);
	    lblCashAmount.setFont(new Font("Roboto", Font.PLAIN, 14));
	    lblCashAmount.setVisible(false);
	    dialog.add(lblCashAmount);

	    JTextField txtCashAmount = new JTextField();
	    txtCashAmount.setBounds(200, 110, 150, 30);
	    txtCashAmount.setFont(new Font("Roboto", Font.PLAIN, 14));
	    txtCashAmount.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235)));
	    txtCashAmount.setVisible(false);
	    dialog.add(txtCashAmount);

	    JButton btnCancel = new JButton("HỦY");
	    btnCancel.setBounds(50, 200, 150, 40);
	    btnCancel.setFont(new Font("Roboto", Font.BOLD, 14));
	    btnCancel.setBackground(new Color(239, 68, 68));
	    btnCancel.setForeground(Color.WHITE);
	    btnCancel.setFocusPainted(false);
	    btnCancel.setBorderPainted(false);
	    btnCancel.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseEntered(MouseEvent e) {
	            btnCancel.setBackground(new Color(220, 38, 38));
	        }

	        @Override
	        public void mouseExited(MouseEvent e) {
	            btnCancel.setBackground(new Color(239, 68, 68));
	        }
	    });
	    btnCancel.addActionListener(e -> dialog.dispose());
	    dialog.add(btnCancel);

	    JButton btnConfirm = new JButton("XÁC NHẬN");
	    btnConfirm.setBounds(200, 200, 150, 40);
	    btnConfirm.setFont(new Font("Roboto", Font.BOLD, 14));
	    btnConfirm.setBackground(new Color(34, 197, 94));
	    btnConfirm.setForeground(Color.WHITE);
	    btnConfirm.setFocusPainted(false);
	    btnConfirm.setBorderPainted(false);
	    btnConfirm.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseEntered(MouseEvent e) {
	            btnConfirm.setBackground(new Color(22, 163, 74));
	        }

	        @Override
	        public void mouseExited(MouseEvent e) {
	            btnConfirm.setBackground(new Color(34, 197, 94));
	        }
	    });

	    comboPTTT.addActionListener(e -> {
	        String selectedMethod = (String) comboPTTT.getSelectedItem();
	        boolean isCash = selectedMethod.equals("Tiền mặt");
	        lblCashAmount.setVisible(isCash);
	        txtCashAmount.setVisible(isCash);
	    });

	    btnConfirm.addActionListener(e -> {
	        String selectedMethod = (String) comboPTTT.getSelectedItem();
	        try {
	            // Bước 1: Tìm tất cả các bàn liên quan đến maKH
	            Set<String> allRelatedBans = new HashSet<>();
	            
	            // Lấy tất cả maBan từ HoaDon liên quan đến maKH
	            List<String> maBansFromHoaDon = Ban_DAO.getMaBanByMaKH(maKH);
	            if (maBansFromHoaDon.isEmpty()) {
	                JOptionPane.showMessageDialog(dialog, "Không tìm thấy bàn nào liên quan đến khách hàng này!");
	                return;
	            }

	            // Duyệt qua tất cả maBan từ HoaDon để tìm chuỗi gộp
	            gopBan_DAO = new DAO_GopBan();
	            Queue<String> queue = new LinkedList<>();
	            queue.addAll(maBansFromHoaDon);
	            while (!queue.isEmpty()) {
	                String currentMaBan = queue.poll();
	                if (!allRelatedBans.add(currentMaBan)) {
	                    continue; // Nếu maBan đã được xử lý, bỏ qua
	                }

	                // Tìm các bàn phụ đã gộp với currentMaBan
	                List<String> mergedBans = gopBan_DAO.getMergedBans(currentMaBan);
	                for (String maBanGop : mergedBans) {
	                    if (!allRelatedBans.contains(maBanGop)) {
	                        queue.add(maBanGop);
	                    }
	                }

	                // Tìm bàn chính của currentMaBan (nếu có)
	                String maBanChinh = gopBan_DAO.getMaBanChinh(currentMaBan);
	                if (maBanChinh != null && !allRelatedBans.contains(maBanChinh)) {
	                    queue.add(maBanChinh);
	                }
	            }

	            // Bước 2: Xử lý thanh toán
	            if (selectedMethod.equals("Tiền mặt")) {
	                String cashInput = txtCashAmount.getText().trim();
	                if (cashInput.isEmpty()) {
	                    JOptionPane.showMessageDialog(dialog, "Vui lòng nhập số tiền khách đưa!");
	                    return;
	                }
	                long cashAmount;
	                try {
	                    cashAmount = Long.parseLong(cashInput);
	                } catch (NumberFormatException ex) {
	                    JOptionPane.showMessageDialog(dialog, "Số tiền không hợp lệ!");
	                    return;
	                }
	                if (cashAmount < amount) {
	                    JOptionPane.showMessageDialog(dialog, "Số tiền khách đưa không đủ!");
	                    return;
	                }
	                long change = cashAmount - amount;
	                hoaDon_DAO.capNhatTrangThaiHoaDon(maHD, "Đã thanh toán");

	                // Cập nhật trạng thái tất cả bàn liên quan thành "CÒN TRỐNG"
	                for (String maBan : allRelatedBans) {
	                    Ban ban = Ban_DAO.getBanbyMaBan(maBan);
	                    if (ban != null) {
	                        Ban_DAO.capNhatTrangThaiBan(ban.getTenBan(), "CÒN TRỐNG");
	                    }
	                }

	                // Xóa toàn bộ thông tin gộp liên quan đến các maBan
	                for (String maBan : allRelatedBans) {
	                    gopBan_DAO.deleteGopBanByMaBanChinh(maBan);
	                    gopBan_DAO.deleteGopBanByMaBanPhu(maBan); // Xóa các bản ghi mà maBan là maBanPhu
	                }

	                JOptionPane.showMessageDialog(dialog,
	                        "Thanh toán thành công!\n" + "Phương thức: " + selectedMethod + "\n" + "Số tiền cần trả: "
	                                + amount + "\n" + "Số tiền khách đưa: " + cashAmount + "\n" + "Tiền thối: "
	                                + change);
	                refreshBanList();
	                panel_ChiTietBan.setVisible(false);
	                panel_Ban.setVisible(true);
	                dialog.dispose();
	            } else if (selectedMethod.equals("Ví điện tử Momo")) {
	                generateAndShowQRCode(tenBan);
	                hoaDon_DAO.capNhatTrangThaiHoaDon(maHD, "Đã thanh toán");

	                // Cập nhật trạng thái tất cả bàn liên quan thành "CÒN TRỐNG"
	                for (String maBan : allRelatedBans) {
	                    Ban ban = Ban_DAO.getBanbyMaBan(maBan);
	                    if (ban != null) {
	                        Ban_DAO.capNhatTrangThaiBan(ban.getTenBan(), "CÒN TRỐNG");
	                    }
	                }

	                // Xóa toàn bộ thông tin gộp liên quan đến các maBan
	                for (String maBan : allRelatedBans) {
	                    gopBan_DAO.deleteGopBanByMaBanChinh(maBan);
	                    gopBan_DAO.deleteGopBanByMaBanPhu(maBan); // Xóa các bản ghi mà maBan là maBanPhu
	                }

	                JOptionPane.showMessageDialog(dialog,
	                        "Thanh toán thành công!\n" +
	                        "Phương thức: " + selectedMethod + "\n" +
	                        "Số tiền cần trả: " + amount);
	                refreshBanList();
	                panel_ChiTietBan.setVisible(false);
	                panel_Ban.setVisible(true);
	                dialog.dispose();
	            } else if (selectedMethod.equals("Chuyển khoản ngân hàng")) {
	                generateAndShowBank(tenBan);
	                hoaDon_DAO.capNhatTrangThaiHoaDon(maHD, "Đã thanh toán");

	                // Cập nhật trạng thái tất cả bàn liên quan thành "CÒN TRỐNG"
	                for (String maBan : allRelatedBans) {
	                    Ban ban = Ban_DAO.getBanbyMaBan(maBan);
	                    if (ban != null) {
	                        Ban_DAO.capNhatTrangThaiBan(ban.getTenBan(), "CÒN TRỐNG");
	                    }
	                }

	                // Xóa toàn bộ thông tin gộp liên quan đến các maBan
	                for (String maBan : allRelatedBans) {
	                    gopBan_DAO.deleteGopBanByMaBanChinh(maBan);
	                    gopBan_DAO.deleteGopBanByMaBanPhu(maBan); // Xóa các bản ghi mà maBan là maBanPhu
	                }

	                JOptionPane.showMessageDialog(dialog,
	                        "Thanh toán thành công!\n" +
	                        "Phương thức: " + selectedMethod + "\n" +
	                        "Số tiền cần trả: " + amount);
	                refreshBanList();
	                panel_ChiTietBan.setVisible(false);
	                panel_Ban.setVisible(true);
	                dialog.dispose();
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	            JOptionPane.showMessageDialog(dialog, "Lỗi khi cập nhật dữ liệu: " + ex.getMessage());
	        } catch (Exception ex) {
	            ex.printStackTrace();
	            JOptionPane.showMessageDialog(dialog, "Lỗi khi tạo mã QR: " + ex.getMessage());
	        }
	    });
	    dialog.add(btnConfirm);

	    dialog.setVisible(true);
	}

	private void generateAndShowBank(String tenBan) throws Exception {
		// MB Bank account details
		String bankBIN = "970422";
		String accountNumber = "0923358221";
		long amount = Long.parseLong(txt_tienCanTra.getText());
		String content = "Thanh toan ban " + tenBan.replaceAll("[^a-zA-Z0-9]", "");

		// Validate inputs
		if (accountNumber.length() < 6 || accountNumber.length() > 19) {
			throw new IllegalArgumentException("Account number must be 6-19 digits");
		}
		if (amount <= 0) {
			throw new IllegalArgumentException("Amount must be greater than 0");
		}
		if (content.length() > 100) {
			content = content.substring(0, 100); // Truncate to 100 chars
		}

		// Construct VietQR string
		String qrCodeText = constructVietQRString(bankBIN, accountNumber, amount, content);
		System.out.println("VietQR Payload: " + qrCodeText); // For debugging

		// Set QR code dimensions
		int width = 300;
		int height = 300;

		// Generate QR code
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, width, height);
		BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

//		// Load and resize MB Bank logo (optional)
		BufferedImage logo = null;
		try {
			java.net.URL logoURL = getClass().getResource("logo_mb_bank.png");
			logo = ImageIO.read(logoURL);
			logo = resizeImage(logo, 48, 48); // Reduced size to avoid obscuring QR code
			// Overlay logo
			Graphics2D g = qrImage.createGraphics();
			int x = (qrImage.getWidth() - logo.getWidth()) / 2;
			int y = (qrImage.getHeight() - logo.getHeight()) / 2;
			g.drawImage(logo, x, y, null);
			g.dispose();
		} catch (IOException e) {
			System.err.println("Warning: Could not load MB Bank logo: " + e.getMessage());
		}
//
//		// Save QR code to temporary file
		String filePath = "qrcode_mb_" + tenBan + "_" + System.currentTimeMillis() + ".png";
		File file = new File(filePath);
		try {
			ImageIO.write(qrImage, "PNG", file);
		} catch (IOException e) {
			throw new Exception("Failed to save QR code image: " + e.getMessage());
		}

//		// Display QR code in dialog
		JDialog qrDialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Mã QR Thanh Toán MB Bank",
				Dialog.ModalityType.APPLICATION_MODAL);
		qrDialog.getContentPane().setLayout(new BorderLayout());
		qrDialog.setSize(350, 450);
		qrDialog.setLocationRelativeTo(null);

		JLabel qrLabel = new JLabel(new ImageIcon(filePath));
		qrLabel.setHorizontalAlignment(SwingConstants.CENTER);
		qrDialog.getContentPane().add(qrLabel, BorderLayout.CENTER);

		JLabel instructionLabel = new JLabel("Quét mã QR bằng ứng dụng MB Bank");
		instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		instructionLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
		instructionLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		qrDialog.getContentPane().add(instructionLabel, BorderLayout.NORTH);

//		// Button panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		buttonPanel.setBackground(Color.WHITE);

		// Confirm Payment button
		JButton confirmButton = new JButton("XÁC NHẬN ĐÃ THANH TOÁN");
		confirmButton.setFont(new Font("Roboto", Font.BOLD, 14));
		confirmButton.setBackground(new Color(34, 197, 94));
		confirmButton.setForeground(Color.WHITE);
		confirmButton.setFocusPainted(false);
		confirmButton.setBorderPainted(false);
		confirmButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				confirmButton.setBackground(new Color(22, 163, 74));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				confirmButton.setBackground(new Color(34, 197, 94));
			}
		});
		confirmButton.addActionListener(e -> {
			try {
				String maBan = Ban_DAO.getMaBanByTenBan(tenBan);
				String maKH = getMaKHFromPhieuDatBan(maBan);
				String maHD = hoaDon_DAO.getMaHDByMaKHAndStatus(maKH, "Chưa thanh toán");
				hoaDon_DAO.capNhatTrangThaiHoaDon(maHD, "Đã thanh toán");
				Ban_DAO.capNhatTrangThaiBan(tenBan, "CÒN TRỐNG");
				refreshBanList();
				panel_ChiTietBan.setVisible(false);
				panel_Ban.setVisible(true);
				qrDialog.dispose();
			} catch (SQLException ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(qrDialog, "Lỗi khi cập nhật dữ liệu: " + ex.getMessage());
			}
		});
		buttonPanel.add(confirmButton);

		// Cancel button
		JButton cancelButton = new JButton("HỦY");
		cancelButton.setFont(new Font("Roboto", Font.BOLD, 14));
		cancelButton.setBackground(new Color(239, 68, 68));
		cancelButton.setForeground(Color.WHITE);
		cancelButton.setFocusPainted(false);
		cancelButton.setBorderPainted(false);
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				cancelButton.setBackground(new Color(220, 38, 38));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				cancelButton.setBackground(new Color(239, 68, 68));
			}
		});
		cancelButton.addActionListener(e -> {
			qrDialog.dispose();
		});
		buttonPanel.add(cancelButton);

		qrDialog.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

//		// Set dialog icon
		try {
			java.net.URL iconURL = getClass().getResource("mb_bank_logo.png");
			if (iconURL != null) {
				qrDialog.setIconImage(new ImageIcon(iconURL).getImage());
			}
		} catch (Exception e) {
			System.err.println("Warning: Could not set dialog icon: " + e.getMessage());
		}

		qrDialog.setVisible(true);

		// Delete temporary file when dialog closes
		qrDialog.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosed(java.awt.event.WindowEvent e) {
				if (file.exists()) {
					file.delete();
				}
			}
		});
	}

	private String constructVietQRString(String bankBIN, String accountNumber, long amount, String content) {
		// VietQR format components
		String version = "000201"; // Version
		String initMethod = "010212"; // Dynamic QR
		String guid = "0010A000000727"; // GUID
		String orgField = "0208QRIBFTTA"; // QRIBFTTA for VietQR
		String bankInfo = String.format("01%02d%s%s", bankBIN.length() + accountNumber.length(), bankBIN,
				accountNumber);
		String consumerInfo = String.format("38%02d%s", bankInfo.length() + guid.length() + orgField.length(),
				guid + bankInfo + orgField);
		String serviceCode = "52040000"; // Merchant category code
		String currency = "5303704"; // VND
		String amountStr = String.format("54%02d%s", String.valueOf(amount).length(), amount); // Amount
		String purpose = String.format("08%02d%s", content.length(), content); // Transaction purpose
		String additionalData = String.format("62%02d%s", purpose.length(), purpose);
		String countryCode = "5802VN"; // Vietnam
		String checksumPlaceholder = "6304"; // Checksum placeholder

		// Combine payload without checksum
		String payload = version + initMethod + consumerInfo + serviceCode + currency + amountStr + additionalData
				+ countryCode;

		// Calculate CRC-16 checksum
		String checksum = calculateCRC16(payload + checksumPlaceholder);
		payload += checksumPlaceholder + checksum;

		return payload;
	}

	private String calculateCRC16(String data) {
		int crc = 0xFFFF;
		byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
		for (byte b : bytes) {
			crc ^= (b & 0xFF);
			for (int i = 0; i < 8; i++) {
				if ((crc & 0x0001) != 0) {
					crc >>= 1;
					crc ^= 0x8408; // CRC-16-CCITT polynomial
				} else {
					crc >>= 1;
				}
			}
		}
		return String.format("%04X", crc).toUpperCase();
	}

	private void generateAndShowQRCode(String tenBan) throws Exception {
		String momoPhone = "0923358221";
		String name = "Nguyen Tan";
		String email = "nguyentan5434@gmail.com";
		long amount = Long.parseLong(txt_tienCanTra.getText());
		String qrCodeText = String.format("2|99|%s|%s|%s|0|0|%d", momoPhone, name, email, amount);

		int width = 300;
		int height = 300;

		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, width, height);
		BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

		BufferedImage logo = ImageIO.read(getClass().getResource("logo_momo.jpg"));
		logo = resizeImage(logo, 64, 64);

		Graphics2D g = qrImage.createGraphics();
		int x = (qrImage.getWidth() - logo.getWidth()) / 2;
		int y = (qrImage.getHeight() - logo.getHeight()) / 2;
		g.drawImage(logo, x, y, null);
		g.dispose();

		String filePath = "qrcode_" + tenBan + ".png";
		File file = new File(filePath);
		ImageIO.write(qrImage, "PNG", file);

		JDialog qrDialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Mã QR Thanh Toán",
				Dialog.ModalityType.APPLICATION_MODAL);
		qrDialog.getContentPane().setLayout(new BorderLayout());
		qrDialog.setSize(350, 450);
		qrDialog.setLocationRelativeTo(null);

		JLabel qrLabel = new JLabel(new ImageIcon(filePath));
		qrLabel.setHorizontalAlignment(SwingConstants.CENTER);
		qrDialog.getContentPane().add(qrLabel, BorderLayout.CENTER);

		java.net.URL iconURL = getClass().getResource("momo_logo.jpg");
		ImageIcon icon = new ImageIcon(iconURL);
		qrDialog.setIconImage(icon.getImage());

		JLabel instructionLabel = new JLabel("Quét mã QR bằng ứng dụng MoMo để thanh toán");
		instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		instructionLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
		qrDialog.getContentPane().add(instructionLabel, BorderLayout.NORTH);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		buttonPanel.setBackground(Color.WHITE);

		JButton confirmButton = new JButton("XÁC NHẬN ĐÃ THANH TOÁN");
		confirmButton.setFont(new Font("Roboto", Font.BOLD, 14));
		confirmButton.setBackground(new Color(34, 197, 94));
		confirmButton.setForeground(Color.WHITE);
		confirmButton.setFocusPainted(false);
		confirmButton.setBorderPainted(false);
		confirmButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				confirmButton.setBackground(new Color(22, 163, 74));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				confirmButton.setBackground(new Color(34, 197, 94));
			}
		});
		confirmButton.addActionListener(e -> {
			try {
				String maBan = Ban_DAO.getMaBanByTenBan(tenBan);
				String maKH = getMaKHFromPhieuDatBan(maBan);
				String maHD = hoaDon_DAO.getMaHDByMaKHAndStatus(maKH, "Chưa thanh toán");
				hoaDon_DAO.capNhatTrangThaiHoaDon(maHD, "Đã thanh toán");
				Ban_DAO.capNhatTrangThaiBan(tenBan, "CÒN TRỐNG");
				refreshBanList();
				panel_ChiTietBan.setVisible(false);
				panel_Ban.setVisible(true);
				qrDialog.dispose();
			} catch (SQLException ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(qrDialog, "Lỗi khi cập nhật dữ liệu: " + ex.getMessage());
			}
		});
		buttonPanel.add(confirmButton);

		JButton cancelButton = new JButton("HỦY");
		cancelButton.setFont(new Font("Roboto", Font.BOLD, 14));
		cancelButton.setBackground(new Color(239, 68, 68));
		cancelButton.setForeground(Color.WHITE);
		cancelButton.setFocusPainted(false);
		cancelButton.setBorderPainted(false);
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				cancelButton.setBackground(new Color(220, 38, 38));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				cancelButton.setBackground(new Color(239, 68, 68));
			}
		});
		cancelButton.addActionListener(e -> {
			qrDialog.dispose();
		});
		buttonPanel.add(cancelButton);

		qrDialog.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		qrDialog.setVisible(true);

		qrDialog.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosed(java.awt.event.WindowEvent e) {
				file.delete();
			}
		});
	}

	private BufferedImage resizeImage(BufferedImage originalImage, int newWidth, int newHeight) {
		BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = resizedImage.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
		g.dispose();
		return resizedImage;
	}

	private void showMenuPanel(String tenBan) throws Exception {
		if (panelMenu == null) {
			panelMenu = new JPanel();
			panelMenu.setLayout(null);
			panelMenu.setBackground(new Color(250, 251, 255));

			scrollPaneMenu = new JScrollPane(panelMenu);
			scrollPaneMenu.setBounds(10, 10, 1032, 755);
			scrollPaneMenu.setBorder(BorderFactory.createEmptyBorder());
			scrollPaneMenu.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			scrollPaneMenu.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			scrollPaneMenu.setVisible(false);
			scrollPaneMenu.getVerticalScrollBar().setUnitIncrement(16);
			scrollPaneMenu.getVerticalScrollBar().setBlockIncrement(50);
			add(scrollPaneMenu);

			JLabel lblTitle = new JLabel("DANH SÁCH MÓN ĂN");
			lblTitle.setBounds(0, 10, 1032, 40);
			lblTitle.setFont(new Font("Roboto", Font.BOLD, 24));
			lblTitle.setForeground(new Color(17, 24, 39));
			lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
			panelMenu.add(lblTitle);

			JLabel lblLoaiMon = new JLabel("LOẠI MÓN:");
			lblLoaiMon.setBounds(50, 60, 100, 30);
			lblLoaiMon.setFont(new Font("Roboto", Font.BOLD, 16));
			lblLoaiMon.setForeground(new Color(17, 24, 39));
			panelMenu.add(lblLoaiMon);

			monAn_DAO = new DAO_MonAn();
			List<MonAn> danhSachMonAn = monAn_DAO.getAllMonAn();

			comboLoaiMon = new JComboBox<String>();
			comboLoaiMon.setBounds(150, 60, 200, 30);
			comboLoaiMon.setFont(new Font("Roboto", Font.PLAIN, 14));
			comboLoaiMon.setBackground(Color.WHITE);
			comboLoaiMon.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235)));
			comboLoaiMon.addActionListener(e -> updateMenuDisplay(tenBan, danhSachMonAn));
			List<String> danhsachloaiMonAn = monAn_DAO.getLoaiMonAn();
			comboLoaiMon.addItem("Tất cả");
			for (String loaimonAn : danhsachloaiMonAn) {
				comboLoaiMon.addItem(loaimonAn);
			}
			panelMenu.add(comboLoaiMon);

			JButton btnBack = new JButton("QUAY LẠI");
			btnBack.setFont(new Font("Roboto", Font.BOLD, 15));
			btnBack.setBackground(new Color(239, 68, 68));
			btnBack.setForeground(Color.WHITE);
			btnBack.setFocusPainted(false);
			btnBack.setBorderPainted(false);
			panelMenu.add(btnBack);
			btnBack.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					btnBack.setBackground(new Color(220, 38, 38));
				}

				@Override
				public void mouseExited(MouseEvent e) {
					btnBack.setBackground(new Color(239, 68, 68));
				}
			});
			btnBack.addActionListener(e -> {
				panel_ChiTietBan.setVisible(true);
				scrollPaneMenu.setVisible(false);
				panel_Ban.setVisible(true);
			});

			updateMenuDisplay(tenBan, danhSachMonAn);
		} else {
			updateMenuDisplay(tenBan, monAn_DAO.getAllMonAn());
		}

		panel_Ban.setVisible(false);
		scrollPaneMenu.setVisible(true);
	}

	private void updateMenuDisplay(String tenBan, List<MonAn> danhSachMonAn) {
		Component[] components = panelMenu.getComponents();
		for (Component comp : components) {
			if (!(comp instanceof JLabel && ((JLabel) comp).getText().equals("DANH SÁCH MÓN ĂN"))
					&& !(comp instanceof JComboBox)
					&& !(comp instanceof JLabel && ((JLabel) comp).getText().equals("LOẠI MÓN:"))
					&& !(comp instanceof JButton)) {
				panelMenu.remove(comp);
			}
		}

		String selectedLoaiMon = (String) comboLoaiMon.getSelectedItem();
		List<MonAn> filteredMonAn = selectedLoaiMon == null || selectedLoaiMon.equals("Tất cả") ? danhSachMonAn
				: danhSachMonAn.stream()
						.filter(mon -> mon.getLoaiMonAn() != null && mon.getLoaiMonAn().equals(selectedLoaiMon))
						.collect(Collectors.toList());

		int x = 50, y = 100;
		int itemsPerRow = 4;
		int itemHeight = 260;
		int panelWidth = 1032;

		for (MonAn monAn : filteredMonAn) {
			RoundedPanelWithShadow dishPanel = new RoundedPanelWithShadow(30, Color.WHITE, 5, 3,
					new Color(0, 0, 0, 30));
			dishPanel.setBounds(x, y, 180, 250);
			dishPanel.setLayout(null);

			JLabel lblImage;
			try {
				String linkIMG = monAn.getLinkIMG();
				if (linkIMG != null && !linkIMG.isEmpty()) {
					ImageIcon originalIcon = new ImageIcon(getClass().getResource(linkIMG));
					Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
					lblImage = new JLabel(new ImageIcon(scaledImage));
				} else {
					throw new Exception("Hình ảnh không có");
				}
			} catch (Exception e) {
				lblImage = new JLabel("No Image", SwingConstants.CENTER);
				lblImage.setFont(new Font("Roboto", Font.PLAIN, 12));
			}
			lblImage.setBounds(40, 10, 100, 100);
			dishPanel.add(lblImage);

			JLabel lblName = new JLabel(monAn.getTenMonAn());
			lblName.setBounds(0, 120, 180, 30);
			lblName.setFont(new Font("Roboto", Font.BOLD, 16));
			lblName.setForeground(new Color(17, 24, 39));
			lblName.setHorizontalAlignment(SwingConstants.CENTER);
			dishPanel.add(lblName);

			JLabel lblPrice = new JLabel(String.format("%d VNĐ", (int) monAn.getGia()));
			lblPrice.setBounds(0, 150, 180, 20);
			lblPrice.setFont(new Font("Roboto", Font.PLAIN, 14));
			lblPrice.setForeground(new Color(55, 65, 81));
			lblPrice.setHorizontalAlignment(SwingConstants.CENTER);
			dishPanel.add(lblPrice);

			JTextField txtQuantity = new JTextField("1");
			txtQuantity.setBounds(40, 180, 100, 25);
			txtQuantity.setFont(new Font("Roboto", Font.PLAIN, 14));
			txtQuantity.setHorizontalAlignment(SwingConstants.CENTER);
			dishPanel.add(txtQuantity);

			JButton btnSelect = new JButton("CHỌN");
			btnSelect.setBounds(40, 210, 100, 30);
			btnSelect.setFont(new Font("Roboto", Font.BOLD, 12));
			btnSelect.setBackground(new Color(16, 185, 129));
			btnSelect.setForeground(Color.WHITE);
			btnSelect.setFocusPainted(false);
			btnSelect.setBorderPainted(false);
			btnSelect.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					btnSelect.setBackground(new Color(5, 150, 105));
				}

				@Override
				public void mouseExited(MouseEvent e) {
					btnSelect.setBackground(new Color(16, 185, 129));
				}
			});
			btnSelect.addActionListener(e -> {
				try {
					int quantity = Integer.parseInt(txtQuantity.getText());
					if (quantity <= 0) {
						JOptionPane.showMessageDialog(panelTang2.this, "Số lượng phải lớn hơn 0!");
						return;
					}
					int price = (int) monAn.getGia();
					int total = quantity * price;

					Ban_DAO = new DAO_Ban();
					String maBan = Ban_DAO.getMaBanByTenBan(tenBan);
					if (maBan == null) {
						JOptionPane.showMessageDialog(panelTang2.this, "Không tìm thấy mã bàn!");
						return;
					}

					String maKH = getMaKHFromPhieuDatBan(maBan);
					if (maKH == null) {
						JOptionPane.showMessageDialog(panelTang2.this, "Không tìm thấy khách hàng cho bàn này!");
						return;
					}

					hoaDon_DAO = new DAO_HoaDon();
					String maHD = hoaDon_DAO.getMaHDByMaKHAndStatus(maKH, "Chưa thanh toán");
					if (maHD == null) {
						LocalDateTime ngayLap = LocalDateTime.now();
						LocalDateTime ngayXuat = LocalDateTime.now();
						boolean isAdded = hoaDon_DAO.themHD("NV001", maKH, "PTTT01", "VAT01", "KM01", ngayLap, ngayXuat,
								"Chưa thanh toán");
						maHD = hoaDon_DAO.getMaHDByMaKHAndStatus(maKH, "Chưa thanh toán");
					}

					CTHD_DAO = new DAO_CTHoaDon();
					boolean isAddedCTHD = CTHD_DAO.themCTHD(maHD, monAn.getMaMonAn(), quantity, price);
					if (!isAddedCTHD) {
						JOptionPane.showMessageDialog(panelTang2.this, "Không thể thêm món vào hóa đơn!");
						return;
					}

					DefaultTableModel model = (DefaultTableModel) table.getModel();
					boolean found = false;
					for (int i = 0; i < model.getRowCount(); i++) {
						if (model.getValueAt(i, 0).equals(monAn.getTenMonAn())) {
							int oldQuantity = (int) model.getValueAt(i, 1);
							model.setValueAt(oldQuantity + quantity, i, 1);
							model.setValueAt((oldQuantity + quantity) * price, i, 3);
							found = true;
							break;
						}
					}
					if (!found) {
						model.addRow(new Object[] { monAn.getTenMonAn(), quantity, price, total, "Xóa" });
					}

					Ban_DAO.capNhatTrangThaiBan(tenBan, "ĐANG SỬ DỤNG");
					refreshBanList();
					btnHuyBan.setEnabled(false);
					btnHuyBan.setFocusPainted(false);
					btnHuyBan.setBorderPainted(false);

					updateTotal();
					JOptionPane.showMessageDialog(panelTang2.this,
							"Đã thêm " + quantity + " " + monAn.getTenMonAn() + " vào danh sách!");
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(panelTang2.this, "Vui lòng nhập số lượng hợp lệ!");
				} catch (SQLException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(panelTang2.this, "Lỗi cơ sở dữ liệu: " + ex.getMessage());
				}
			});
			dishPanel.add(btnSelect);

			panelMenu.add(dishPanel);
			x += 200;
			if (x > 850) {
				x = 50;
				y += itemHeight;
			}
		}

		int rows = (int) Math.ceil((double) filteredMonAn.size() / itemsPerRow);
		int contentHeight = 100 + rows * itemHeight;
		int btnHeight = 40;
		int padding = 20;
		int panelHeight = contentHeight + btnHeight + padding * 2;
		int btnX = (panelWidth - 150) / 2;
		for (Component comp : panelMenu.getComponents()) {
			if (comp instanceof JButton) {
				comp.setBounds(btnX, contentHeight + padding, 150, btnHeight);
				break;
			}
		}

		panelMenu.setPreferredSize(new Dimension(panelWidth, panelHeight));
		panelMenu.revalidate();
		panelMenu.repaint();
	}

	private String getMaKHFromPhieuDatBan(String maBan) throws SQLException {
		String maKH = null;
		String query = "SELECT TOP 1 MaKhachHang FROM PhieuDatBan WHERE MaBan = ? ORDER BY MaPDB DESC";
		try (Connection conn = new BaseDAO().getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, maBan);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				maKH = rs.getString("MaKhachHang");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return maKH;
	}

	private void updateTotal() {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		int tongCong = 0;
		if (model.getRowCount() > 0) {
			for (int i = 0; i < model.getRowCount(); i++) {
				tongCong += (int) model.getValueAt(i, 3);
			}
		}
		txt_tongCong.setText(String.valueOf(tongCong));

		double vatPercentage = 0.08;
		int vat = (int) (tongCong * vatPercentage);
		txt_VAT.setText(String.valueOf(vat));

		double khuyenMai = 0;
		String khuyenMaiText = (String) comboKM.getSelectedItem();
		if (khuyenMaiText != null && !khuyenMaiText.equals("Không có")) {
			khuyenMai = Double.parseDouble(khuyenMaiText.replace("%", "")) / 100;
		}

		int tienCanTra = (int) ((tongCong + vat) * (1 - khuyenMai));
		txt_tienCanTra.setText(String.valueOf(tienCanTra));
	}
	
	private void showMergeTableDialog(String mainTableName, String maKH) throws SQLException {
	    JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "CHỌN BÀN ĐỂ GỘP", Dialog.ModalityType.APPLICATION_MODAL);
	    dialog.setLayout(null);
	    dialog.setSize(400, 500);
	    dialog.setLocationRelativeTo(null);
	    
	    JLabel lblTitle = new JLabel("Chọn bàn để gộp với " + mainTableName);
	    lblTitle.setBounds(0, 10, 400, 30);
	    lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
	    lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
	    dialog.add(lblTitle);
	    
	    JList<String> tableList = new JList<>();
	    DefaultListModel<String> listModel = new DefaultListModel<>();
	    try {
	        Ban_DAO = new DAO_Ban();
	        PDB_DAO = new DAO_PhieuDatBan();
	        gopBan_DAO = new DAO_GopBan();
	        
	        // Lấy mã bàn chính
	        String mainMaBan = Ban_DAO.getMaBanByTenBan(mainTableName);
	        if (mainMaBan == null) {
	            JOptionPane.showMessageDialog(dialog, "Không tìm thấy mã bàn: " + mainTableName);
	            return;
	        }

	        // Lấy danh sách tất cả bàn từ cả 3 tầng, loại bỏ trùng lặp
	        Map<String, Ban> uniqueBans = new HashMap<>(); // Sử dụng Map để loại bỏ trùng lặp dựa trên maBan
	        List<Ban> allBans = new ArrayList<>();
	        allBans.addAll(Ban_DAO.getAllBansinTang1());
	        allBans.addAll(Ban_DAO.getAllBansinTang2());
	        allBans.addAll(Ban_DAO.getAllBansinTang3());

	        // Loại bỏ trùng lặp bằng cách lưu vào Map với key là maBan
	        for (Ban ban : allBans) {
	            uniqueBans.put(ban.getMaBan(), ban);
	        }

	        // Lấy danh sách các bàn đã được gộp với mainMaBan
	        List<String> mergedBans = gopBan_DAO.getMergedBans(mainMaBan); // Thêm phương thức này trong DAO_GopBan

	        // Lấy danh sách bàn khả dụng (không giới hạn tầng)
	        List<Ban> availableTables = uniqueBans.values().stream()
	                .filter(ban -> (ban.getTrangThai().equals("ĐANG SỬ DỤNG") || ban.getTrangThai().equals("CÒN TRỐNG") || ban.getTrangThai().equals("ĐÃ ĐẶT"))
	                        && !ban.getTenBan().equals(mainTableName)) // Loại bỏ chính bàn đang chọn
	                .filter(ban -> {
	                    try {
	                        String maBan = ban.getMaBan();
	                        // Loại bỏ các bàn đã được gộp với mainMaBan
	                        if (mergedBans.contains(maBan)) {
	                            return false;
	                        }
	                        String existingMaBanChinh = gopBan_DAO.getMaBanChinh(maBan);
	                        // Chỉ cho phép gộp nếu bàn chưa thuộc bàn chính nào khác
	                        return existingMaBanChinh == null || existingMaBanChinh.equals(mainMaBan);
	                    } catch (Exception e) {
	                        e.printStackTrace();
	                        return false;
	                    }
	                })
	                .collect(Collectors.toList());

	        // Nhóm và hiển thị danh sách bàn theo tầng
	        Map<String, List<Ban>> groupedByTang = new HashMap<>();
	        for (Ban ban : availableTables) {
	            String tang = getTang(ban);
	            groupedByTang.computeIfAbsent(tang, k -> new ArrayList<>()).add(ban);
	        }

	        // Thêm danh sách theo thứ tự Tầng 1, Tầng 2, Tầng 3
	        String[] tangs = {"Tầng 1", "Tầng 2", "Tầng 3"};
	        for (String tang : tangs) {
	            List<Ban> tangBans = groupedByTang.getOrDefault(tang, new ArrayList<>());
	            if (!tangBans.isEmpty()) {
	                listModel.addElement(tang + ":");
	                for (Ban ban : tangBans) {
	                    String maBan = ban.getMaBan();
	                    PhieuDatBan pdb = PDB_DAO.getLatestPDBByMaBan(maBan);
	                    String khInfo = (pdb != null && (ban.getTrangThai().equals("ĐANG SỬ DỤNG") || ban.getTrangThai().equals("ĐÃ ĐẶT")))
	                            ? " (KH: " + pdb.getMaKH() + ")" : " (Trống)";
	                    listModel.addElement(maBan + "-Bàn " + ban.getTenBan().replace("Bàn ", "") + "(Tầng " + tang.replace("Tầng ", "") + ", Số chỗ :" + ban.getSoCho() + ")" + khInfo);
	                }
	            }
	        }

	        if (listModel.isEmpty()) {
	            JOptionPane.showMessageDialog(dialog, "Không còn bàn nào để gộp với " + mainTableName + "!");
	            return;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(dialog, "Lỗi khi lấy danh sách bàn: " + e.getMessage());
	    }

	    tableList.setModel(listModel);
	    tableList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	    JScrollPane scrollPane = new JScrollPane(tableList);
	    scrollPane.setBounds(50, 50, 300, 300);
	    dialog.add(scrollPane);

	    JButton btnMerge = new JButton("GỘP BÀN");
	    btnMerge.setBounds(100, 360, 200, 40);
	    btnMerge.setFont(new Font("Segoe UI", Font.BOLD, 15));
	    btnMerge.setBackground(new Color(245, 158, 11));
	    btnMerge.setForeground(Color.WHITE);
	    btnMerge.setFocusPainted(false);
	    btnMerge.setBorderPainted(false);
	    btnMerge.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseEntered(MouseEvent e) {
	            btnMerge.setBackground(new Color(217, 119, 6));
	        }
	        @Override
	        public void mouseExited(MouseEvent e) {
	            btnMerge.setBackground(new Color(245, 158, 11));
	        }
	    });
	    
	    btnMerge.addActionListener(e -> {
	        List<String> selectedTables = tableList.getSelectedValuesList().stream()
	                .filter(s -> !s.startsWith("Tầng")) // Loại bỏ các dòng tiêu đề tầng
	                .map(s -> s.split("-")[1].split("\\(")[0].trim()) // Lấy tenBan từ chuỗi hiển thị
	                .collect(Collectors.toList());
	        if (selectedTables.isEmpty()) {
	            JOptionPane.showMessageDialog(dialog, "Vui lòng chọn ít nhất một bàn để gộp!");
	            return;
	        }

	        selectedTables.add(0, mainTableName);

	        // Kiểm tra trạng thái và khách hàng để hiển thị cảnh báo
	        String mainMaBan = Ban_DAO.getMaBanByTenBan(mainTableName);
	        PhieuDatBan mainPDB = null;
	        try {
	            mainPDB = PDB_DAO.getLatestPDBByMaBan(mainMaBan);
	        } catch (SQLException e1) {
	            e1.printStackTrace();
	            JOptionPane.showMessageDialog(dialog, "Lỗi khi lấy thông tin phiếu đặt bàn: " + e1.getMessage());
	            return;
	        }
	        String mainKhachHang = (mainPDB != null) ? mainPDB.getMaKH() : null;
	        boolean showWarning = false;
	        String warningMessage = "";

	        for (String tableName : selectedTables.subList(1, selectedTables.size())) {
	            String maBan = Ban_DAO.getMaBanByTenBan(tableName);
	            PhieuDatBan pdb = null;
	            try {
	                pdb = PDB_DAO.getLatestPDBByMaBan(maBan);
	            } catch (SQLException e1) {
	                e1.printStackTrace();
	                JOptionPane.showMessageDialog(dialog, "Lỗi khi lấy thông tin phiếu đặt bàn: " + e1.getMessage());
	                return;
	            }
	            String khachHang = (pdb != null) ? pdb.getMaKH() : null;
	            String trangThai = Ban_DAO.getBanbyTenBan(tableName).getTrangThai();

	            if (mainPDB != null && pdb != null && !mainKhachHang.equals(khachHang)) {
	                showWarning = true;
	                warningMessage = tableName + " thuộc về " + khachHang + ", có chắc chắn muốn gộp không?";
	                break;
	            }
	        }

	        if (showWarning) {
	            int confirm = JOptionPane.showConfirmDialog(dialog, warningMessage, "Xác Nhận Gộp Bàn", JOptionPane.YES_NO_OPTION);
	            if (confirm != JOptionPane.YES_OPTION) {
	                return;
	            }
	        }

	        mergeTables(selectedTables, maKH);
	        dialog.dispose();
	        refreshBanList();
	    });
	    dialog.add(btnMerge);
	    
	    JButton btnCancel = new JButton("HỦY");
	    btnCancel.setBounds(150, 410, 100, 30);
	    btnCancel.setFont(new Font("Roboto", Font.BOLD, 12));
	    btnCancel.setBackground(new Color(239, 68, 68));
	    btnCancel.setForeground(Color.WHITE);
	    btnCancel.setFocusPainted(false);
	    btnCancel.setBorderPainted(false);
	    btnCancel.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseEntered(MouseEvent e) {
	            btnCancel.setBackground(new Color(220, 38, 38));
	        }
	        @Override
	        public void mouseExited(MouseEvent e) {
	            btnCancel.setBackground(new Color(239, 68, 68));
	        }
	    });
	    btnCancel.addActionListener(e -> dialog.dispose());
	    dialog.add(btnCancel);
	    
	    dialog.setVisible(true);
	}
	
	// Phương thức hỗ trợ để xác định tầng của bàn (cải thiện logic)
	private String getTang(Ban ban) {
	    try {
	        String maBan = ban.getMaBan();
	        // Giả định maBan chứa thông tin tầng (ví dụ: B001-B006 là Tầng 1, B007-B010 là Tầng 2, v.v.)
	        // Bạn cần điều chỉnh logic này dựa trên cách bạn lưu tầng trong maBan hoặc cơ sở dữ liệu
	        if (maBan != null && !maBan.isEmpty()) {
	            String maBanPrefix = maBan.substring(0, 1); // Lấy ký tự đầu tiên của maBan (B)
	            String maBanNumber = maBan.substring(1); // Lấy số sau B
	            int banNumber = Integer.parseInt(maBanNumber);
	            if (banNumber >= 2 && banNumber <= 6) {
	                return "Tầng 1";
	            } else if (banNumber >= 7 && banNumber <= 10) {
	                return "Tầng 2";
	            } else if (banNumber >= 11 && banNumber <= 12) {
	                return "Tầng 3";
	            }
	        }
	        // Nếu không xác định được, kiểm tra bằng cách so sánh với danh sách tầng
	        if (Ban_DAO.getAllBansinTang1().stream().anyMatch(b -> b.getMaBan().equals(ban.getMaBan()))) {
	            return "Tầng 1";
	        } else if (Ban_DAO.getAllBansinTang2().stream().anyMatch(b -> b.getMaBan().equals(ban.getMaBan()))) {
	            return "Tầng 2";
	        } else if (Ban_DAO.getAllBansinTang3().stream().anyMatch(b -> b.getMaBan().equals(ban.getMaBan()))) {
	            return "Tầng 3";
	        }
	    } catch (NumberFormatException e) {
	        e.printStackTrace();
	    }
	    return "Tầng không xác định";
	}
	
	private void mergeTables(List<String> tableNames, String maKH) {
	    try {
	        Ban_DAO = new DAO_Ban();
	        PDB_DAO = new DAO_PhieuDatBan();
	        CTPDB_DAO = new DAO_CTPhieuDatBan();
	        hoaDon_DAO = new DAO_HoaDon();
	        CTHD_DAO = new DAO_CTHoaDon();
	        monAn_DAO = new DAO_MonAn();
	        gopBan_DAO = new DAO_GopBan();
	        
	        String mainTableName = tableNames.get(0);
	        String mainMaBan = Ban_DAO.getMaBanByTenBan(mainTableName);
	        if (mainMaBan == null) {
	            JOptionPane.showMessageDialog(this, "Không tìm thấy mã bàn chính: " + mainTableName + "!");
	            return;
	        }
	        Ban mainBan = Ban_DAO.getBanbyTenBan(mainTableName);
	        if (mainBan == null) {
	            JOptionPane.showMessageDialog(this, "Bàn chính không tồn tại: " + mainTableName + "!");
	            return;
	        }
	        
	        // Kiểm tra phiếu đặt bàn chính
	        PhieuDatBan mainPDB = PDB_DAO.getLatestPDBByMaBan(mainMaBan);
	        if (mainPDB == null || !mainPDB.getMaKH().equals(maKH)) {
	            JOptionPane.showMessageDialog(this, "Bạn chỉ có thể gộp bàn thuộc về khách hàng hiện tại!");
	            return;
	        }
	        
	        // Lấy thông tin chi tiết phiếu đặt bàn chính
	        CTPhieuDatBan mainCTPDB = CTPDB_DAO.getCTPDBByMa(mainPDB.getMaPDB());
	        if (mainCTPDB == null) {
	            throw new Exception("Không tìm thấy chi tiết phiếu đặt bàn cho bàn chính!");
	        }
	        LocalDateTime timeNhanBan = mainCTPDB.getTimeNhanBan();
	        LocalDateTime timeTraBan = mainCTPDB.getTimeTraBan();
	        
	        // Tính tổng số khách từ tất cả các bàn
	        int totalPeople = mainCTPDB.getSoNguoi(); // Số người của bàn chính
	        List<String> mergedTables = new ArrayList<>();
	        mergedTables.add(mainTableName);

	        // Lấy danh sách các bàn đã được gộp với mainMaBan
	        List<String> existingMergedBans = gopBan_DAO.getMergedBans(mainMaBan);

	        // Kiểm tra các bàn cần gộp và tính tổng số khách
	        for (int i = 1; i < tableNames.size(); i++) {
	            String tableName = tableNames.get(i);
	            Ban ban = Ban_DAO.getBanbyTenBan(tableName);
	            if (ban == null) {
	                JOptionPane.showMessageDialog(this, "Bàn " + tableName + " không tồn tại!");
	                return;
	            }
	            String maBan = Ban_DAO.getMaBanByTenBan(tableName);

	            // Kiểm tra xem bàn đã được gộp với mainMaBan chưa
	            if (existingMergedBans.contains(maBan)) {
	                JOptionPane.showMessageDialog(this, "Bàn " + tableName + " đã được gộp với " + mainTableName + "!");
	                return;
	            }

	            // Kiểm tra xem bàn có đang được gộp với bàn chính khác không
	            String existingMaBanChinh = gopBan_DAO.getMaBanChinh(maBan);
	            if (existingMaBanChinh != null && !existingMaBanChinh.equals(mainMaBan)) {
	                Ban banChinh = Ban_DAO.getBanbyMaBan(existingMaBanChinh);
	                JOptionPane.showMessageDialog(this, tableName + " đã được gộp với " + banChinh.getTenBan() + "!");
	                return;
	            }

	            PhieuDatBan otherPDB = PDB_DAO.getLatestPDBByMaBan(maBan);
	            if (otherPDB != null) {
	                CTPhieuDatBan otherCTPDB = CTPDB_DAO.getCTPDBByMa(otherPDB.getMaPDB());
	                if (otherCTPDB != null) {
	                    totalPeople += otherCTPDB.getSoNguoi(); // Cộng số người từ bàn phụ
	                }
	            }
	            mergedTables.add(tableName);
	        }
	        
	        // Xóa chi tiết phiếu đặt bàn trước, sau đó xóa phiếu đặt bàn chính
	        CTPDB_DAO.deleteCTPhieuDatBanByMaPDB(mainPDB.getMaPDB());
	        PDB_DAO.deletePhieuDatBanByMaBan(mainMaBan);
	        
	        // Tạo mã phiếu đặt bàn mới
	        String newMaPDB = PDB_DAO.getNextMaPDB();
	        // Tạo và chèn phiếu đặt bàn mới cho bàn chính
	        PhieuDatBan newPDB = new PhieuDatBan(newMaPDB, maKH, mainMaBan, "NV001");
	        if (!PDB_DAO.insertPhieuDatBan(newPDB)) {
	            throw new Exception("Không thể tạo phiếu đặt bàn mới!");
	        }
	        // Cập nhật chi tiết phiếu đặt bàn với tổng số người
	        CTPDB_DAO.insertCTPhieuDatBan(new CTPhieuDatBan(newMaPDB, timeNhanBan, timeTraBan, totalPeople));
	        
	        // Lưu thông tin gộp bàn
	        for (int i = 1; i < tableNames.size(); i++) {
	            String tableName = tableNames.get(i);
	            String maBanGop = Ban_DAO.getMaBanByTenBan(tableName);
	            if (gopBan_DAO.getMaBanChinh(maBanGop) == null) {
	                if (!gopBan_DAO.insertGopBan(mainMaBan, maBanGop)) {
	                    JOptionPane.showMessageDialog(this, "Lỗi khi lưu thông tin gộp bàn " + tableName + "!");
	                }
	            }
	        }
	        
	        // Gộp hóa đơn từ các bàn khác
	        String mainMaHD = hoaDon_DAO.getMaHDByMaKHAndStatus(maKH, "Chưa thanh toán");
	        if (mainMaHD == null) {
	            LocalDateTime ngayLap = LocalDateTime.now();
	            LocalDateTime ngayXuat = LocalDateTime.now();
	            hoaDon_DAO.themHD("NV001", maKH, "PTTT01", "VAT01", "KM01", ngayLap, ngayXuat, "Chưa thanh toán");
	            mainMaHD = hoaDon_DAO.getMaHDByMaKHAndStatus(maKH, "Chưa thanh toán");
	            if (mainMaHD == null) {
	                JOptionPane.showMessageDialog(this, "Không thể tạo hóa đơn mới!");
	                return;
	            }
	        }
	        
	        for (int i = 1; i < tableNames.size(); i++) {
	            String tableName = tableNames.get(i);
	            String maBan = Ban_DAO.getMaBanByTenBan(tableName);
	            Ban ban = Ban_DAO.getBanbyTenBan(tableName);
	            if (ban != null) {
	                PhieuDatBan otherPDB = PDB_DAO.getLatestPDBByMaBan(maBan);
	                if (otherPDB != null) {
	                    String otherMaKH = otherPDB.getMaKH();
	                    if (otherMaKH != null && !otherMaKH.equals(maKH)) {
	                        String otherMaHD = hoaDon_DAO.getMaHDByMaKHAndStatus(otherMaKH, "Chưa thanh toán");
	                        if (otherMaHD != null) {
	                            List<CTHoaDon> ctHds = CTHD_DAO.getCTHDByMaHD(otherMaHD);
	                            for (CTHoaDon ct : ctHds) {
	                                CTHD_DAO.themCTHD(mainMaHD, ct.getMaMonAn(), ct.getSoLuong(), (int) ct.getDonGia());
	                            }
	                            CTHD_DAO.deleteCTHDByMaHD(otherMaHD);
	                            String deleteHDQuery = "DELETE FROM HoaDon WHERE MaHD = ?";
	                            try (Connection conn = new BaseDAO().getConnection();
	                                 PreparedStatement ps = conn.prepareStatement(deleteHDQuery)) {
	                                ps.setString(1, otherMaHD);
	                                ps.executeUpdate();
	                            }
	                        }
	                    } else if (otherMaKH != null && otherMaKH.equals(maKH)) {
	                        String otherMaHD = hoaDon_DAO.getMaHDByMaKHAndStatus(maKH, "Chưa thanh toán");
	                        if (otherMaHD != null && !otherMaHD.equals(mainMaHD)) {
	                            List<CTHoaDon> ctHds = CTHD_DAO.getCTHDByMaHD(otherMaHD);
	                            for (CTHoaDon ct : ctHds) {
	                                CTHD_DAO.themCTHD(mainMaHD, ct.getMaMonAn(), ct.getSoLuong(), (int) ct.getDonGia());
	                            }
	                            CTHD_DAO.deleteCTHDByMaHD(otherMaHD);
	                            String deleteHDQuery = "DELETE FROM HoaDon WHERE MaHD = ?";
	                            try (Connection conn = new BaseDAO().getConnection();
	                                 PreparedStatement ps = conn.prepareStatement(deleteHDQuery)) {
	                                ps.setString(1, otherMaHD);
	                                ps.executeUpdate();
	                            }
	                        }
	                    }
	                    // Xóa phiếu đặt bàn của bàn phụ
	                    CTPDB_DAO.deleteCTPhieuDatBanByMaPDB(otherPDB.getMaPDB());
	                    PDB_DAO.deletePhieuDatBanByMaBan(maBan);
	                }
	            }
	        }
	        
	        // Cập nhật trạng thái tất cả bàn thành "ĐANG SỬ DỤNG"
	        for (String tableName : tableNames) {
	            if (!Ban_DAO.capNhatTrangThaiBan(tableName, "ĐANG SỬ DỤNG")) {
	                JOptionPane.showMessageDialog(this, "Không thể cập nhật trạng thái bàn " + tableName + "!");
	            }
	        }
	        
	        // Cập nhật giao diện với thông tin mới
	        KH_DAO = new DAO_KhachHang();
	        KhachHang kh = KH_DAO.getKhachHangbyMa(maKH);
	        if (kh == null) {
	            JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin khách hàng!");
	            return;
	        }
	        displayBanDetails(mainTableName, "ĐANG SỬ DỤNG", kh.getMaKH(), kh.getTenKH(), kh.getSdt(), kh.getGioiTinh(),
	                String.valueOf(totalPeople)); // Hiển thị tổng số người
	        panel_ChiTietBan.setVisible(true);

	        JOptionPane.showMessageDialog(this, "Gộp bàn thành công! Tổng số khách: " + totalPeople);
	        refreshBanList();
	    } catch (SQLException e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(this, "Lỗi khi gộp bàn: " + e.getMessage());
	    } catch (Exception e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(this, "Lỗi không xác định: " + e.getMessage());
	    }
	}
    private void showChuyenBanDialog(String tenBan, String maKH, String status) {
        if (!status.equals("ĐANG SỬ DỤNG") && !status.equals("ĐÃ ĐẶT")) {
            JOptionPane.showMessageDialog(this, "Bàn này chưa được sử dụng hoặc đặt, không thể chuyển!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String maBan = Ban_DAO.getMaBanByTenBan(tenBan);
        if (maBan == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy mã bàn hiện tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (maKH == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng cho bàn này!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Ban> banTrongList = Ban_DAO.getBanTrong();
        if (banTrongList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có bàn trống nào để chuyển!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Chọn bàn để chuyển", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(800, 500);
        dialog.setLocationRelativeTo(null);
        dialog.getContentPane().setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(new Color(240, 248, 255));

        // Title
        JLabel titleLabel = new JLabel("CHỌN BÀN ĐỂ CHUYỂN", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 20));
        titleLabel.setForeground(new Color(17, 24, 39));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        dialog.getContentPane().add(titleLabel, BorderLayout.NORTH);

        // Panel for table panels
        RoundedPanel tablePanel = new RoundedPanel(30);
        tablePanel.setLayout(new GridLayout(0, 5, 10, 10));
        tablePanel.setBackground(new Color(240, 248, 255));
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Track the selected table
        final String[] selectedMaBan = {null};
        final String[] selectedTenBan = {null};
        final JPanel[] selectedPanel = {null};

        for (Ban ban : banTrongList) {
            RoundedPanel tablePanelItem = new RoundedPanel(30) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(selectedPanel[0] == this ? new Color(30, 144, 255) : new Color(135, 206, 250));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.setColor(Color.BLACK);
                    g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            tablePanelItem.setBackground(new Color(135, 206, 250));
            tablePanelItem.setPreferredSize(new Dimension(120, 120));
            tablePanelItem.setLayout(new GridBagLayout());
            tablePanelItem.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            tablePanelItem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            ImageIcon originalIcon = new ImageIcon(panelTang2.class.getResource("/img/dining-table.png"));
            Image scaledImage = originalIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            JLabel lblImage = new JLabel(scaledIcon);
            lblImage.setHorizontalAlignment(SwingConstants.CENTER);

            JLabel lblBanTitle = new JLabel(ban.getTenBan());
            lblBanTitle.setFont(new Font("Roboto", Font.BOLD, 14));
            lblBanTitle.setForeground(Color.WHITE);
            lblBanTitle.setHorizontalAlignment(SwingConstants.CENTER);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(5, 0, 0, 0);
            tablePanelItem.add(lblImage, gbc);

            gbc.gridy = 1;
            gbc.insets = new Insets(0, 0, 0, 0);
            tablePanelItem.add(lblBanTitle, gbc);

            tablePanelItem.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (tablePanelItem != selectedPanel[0]) {
                        tablePanelItem.setBackground(new Color(100, 149, 237));
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (tablePanelItem != selectedPanel[0]) {
                        tablePanelItem.setBackground(new Color(135, 206, 250));
                    }
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (selectedPanel[0] != null) {
                        selectedPanel[0].setBackground(new Color(135, 206, 250));
                    }
                    tablePanelItem.setBackground(new Color(30, 144, 255));
                    selectedPanel[0] = tablePanelItem;
                    selectedMaBan[0] = ban.getMaBan();
                    selectedTenBan[0] = ban.getTenBan();
                    tablePanel.repaint();
                }
            });

            tablePanel.add(tablePanelItem);
        }

        JScrollPane scrollPane = new JScrollPane(tablePanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        dialog.getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Button panel for confirm and cancel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(240, 248, 255));

        JButton btnXacNhan = new JButton("XÁC NHẬN");
        btnXacNhan.setFont(new Font("Roboto", Font.BOLD, 14));
        btnXacNhan.setBackground(new Color(0, 120, 215));
        btnXacNhan.setForeground(Color.WHITE);
        btnXacNhan.setFocusPainted(false);
        btnXacNhan.setBorderPainted(false);
        btnXacNhan.setPreferredSize(new Dimension(120, 40));
        btnXacNhan.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnXacNhan.setBackground(new Color(0, 102, 204));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnXacNhan.setBackground(new Color(0, 120, 215));
            }
        });

        JButton btnHuy = new JButton("HỦY");
        btnHuy.setFont(new Font("Roboto", Font.BOLD, 14));
        btnHuy.setBackground(new Color(255, 99, 71));
        btnHuy.setForeground(Color.WHITE);
        btnHuy.setFocusPainted(false);
        btnHuy.setBorderPainted(false);
        btnHuy.setPreferredSize(new Dimension(120, 40));
        btnHuy.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnHuy.setBackground(new Color(220, 20, 60));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnHuy.setBackground(new Color(255, 99, 71));
            }
        });

        buttonPanel.add(btnXacNhan);
        buttonPanel.add(btnHuy);
        dialog.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        btnXacNhan.addActionListener(e -> {
            if (selectedMaBan[0] == null) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng chọn một bàn để chuyển!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String newMaBan = selectedMaBan[0];
            String newTenBan = selectedTenBan[0];

            // Update MaBan in ChiTietPhieuDatBan for both statuses
			boolean updatedPDB = CTPDB_DAO.capNhatMaBanChoPhieuDatBan(maBan, newMaBan);
			if (!updatedPDB) {
			    JOptionPane.showMessageDialog(dialog, "Lỗi khi cập nhật bàn cho phiếu đặt bàn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			    return;
			}

			// For ĐANG SỬ DỤNG, check and update invoice if exists
			if (status.equals("ĐANG SỬ DỤNG")) {
			    hoaDon_DAO = new DAO_HoaDon();
			    String maHD = hoaDon_DAO.getMaHDByMaKHAndStatus(maKH, "Chưa thanh toán");
			    if (maHD == null) {
			        JOptionPane.showMessageDialog(dialog, "Không tìm thấy hóa đơn cho bàn đang sử dụng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			        return;
			    }
			    // Note: If HoaDon stores MaBan, implement update here
			    // Example: hoaDon_DAO.updateMaBan(maHD, newMaBan);
			}

			// Update table statuses
			Ban_DAO.capNhatTrangThaiBan(tenBan, "CÒN TRỐNG");
			Ban_DAO.capNhatTrangThaiBan(newTenBan, status); // Preserve original status

			// Refresh UI
			refreshBanList();
			try {
				displayBanDetails(newTenBan, status, maKH, tenKH, sdt, gioiTinh, soLuong);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			panel_ChiTietBan.setVisible(true);
			JOptionPane.showMessageDialog(dialog, "Chuyển bàn thành công đến " + newTenBan + "!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

			dialog.dispose();
        });

        btnHuy.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
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

	private static class RoundedPanelWithShadow extends JPanel {
		private static final long serialVersionUID = 1L;
		private final int arc;
		private final int shadowSize;
		private final int shadowOffset;
		private final Color shadowColor;

		public RoundedPanelWithShadow(int arc, Color bgColor, int shadowSize, int shadowOffset, Color shadowColor) {
			this.arc = arc;
			this.shadowSize = shadowSize;
			this.shadowOffset = shadowOffset;
			this.shadowColor = shadowColor;
			setOpaque(false);
			setBackground(bgColor);
		}

		@Override
		protected void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(shadowColor);
			g2.fill(new RoundRectangle2D.Double(shadowOffset, shadowOffset, getWidth() - shadowSize,
					getHeight() - shadowSize, arc, arc));
			g2.setColor(getBackground());
			g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - shadowSize, getHeight() - shadowSize, arc, arc));
			g2.dispose();
		}
	}

	private static class ButtonRenderer extends JButton implements TableCellRenderer {
		public ButtonRenderer() {
			setOpaque(true);
			setFont(new Font("Roboto", Font.BOLD, 12));
			setBackground(new Color(239, 68, 68));
			setForeground(Color.WHITE);
			setFocusPainted(false);
			setBorderPainted(false);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			setText((value == null) ? "Xóa" : value.toString());
			return this;
		}
	}

	private static class ButtonEditor extends DefaultCellEditor {
		private JButton button;
		private String label;
		private boolean isPushed;
		private JTable table;
		private panelTang2 panel;
		private String tenBan;
		private int row;

		public ButtonEditor(JCheckBox checkBox, JTable table, String tenBan, panelTang2 panel) {
			super(checkBox);
			this.table = table;
			this.tenBan = tenBan;
			this.panel = panel;
			button = new JButton();
			button.setOpaque(true);
			button.setFont(new Font("Roboto", Font.BOLD, 12));
			button.setBackground(new Color(239, 68, 68));
			button.setForeground(Color.WHITE);
			button.setFocusPainted(false);
			button.setBorderPainted(false);
			button.addActionListener(e -> {
				isPushed = true;
				try {
					fireEditingStopped();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			});
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
				int column) {
			this.row = row;
			label = (value == null) ? "Xóa" : value.toString();
			button.setText(label);
			isPushed = false;
			return button;
		}

		@Override
		public Object getCellEditorValue() {
			if (isPushed && row >= 0 && row < table.getRowCount()) {
				int confirm = JOptionPane.showConfirmDialog(panel, "Bạn có muốn xóa món này khỏi danh sách?",
						"Xác Nhận Xóa", JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					DefaultTableModel model = (DefaultTableModel) table.getModel();
					String tenMonAn = (String) model.getValueAt(row, 0);
					try {
						if (table.isEditing()) {
							table.getCellEditor().stopCellEditing();
						}

						panel.Ban_DAO = new DAO_Ban();
						String maBan = panel.Ban_DAO.getMaBanByTenBan(tenBan);
						if (maBan == null) {
							JOptionPane.showMessageDialog(panel, "Không tìm thấy mã bàn!");
							return label;
						}

						String maKH = panel.getMaKHFromPhieuDatBan(maBan);
						if (maKH == null) {
							JOptionPane.showMessageDialog(panel, "Không tìm thấy khách hàng!");
							return label;
						}

						panel.hoaDon_DAO = new DAO_HoaDon();
						String maHD = panel.hoaDon_DAO.getMaHDByMaKHAndStatus(maKH, "Chưa thanh toán");
						if (maHD == null) {
							JOptionPane.showMessageDialog(panel, "Không tìm thấy hóa đơn!");
							return label;
						}

						panel.monAn_DAO = new DAO_MonAn();
						MonAn monAn = panel.monAn_DAO.getMonAnByTen(tenMonAn);
						if (monAn == null) {
							JOptionPane.showMessageDialog(panel, "Không tìm thấy món ăn!");
							return label;
						}

						panel.CTHD_DAO = new DAO_CTHoaDon();
						boolean isDeleted = panel.CTHD_DAO.xoaCTHD(maHD, monAn.getMaMonAn());
						if (!isDeleted) {
							JOptionPane.showMessageDialog(panel, "Không thể xóa món khỏi hóa đơn!");
							return label;
						}

						model.removeRow(row);
						panel.updateTotal();

						if (model.getRowCount() == 0) {
							panel.Ban_DAO.capNhatTrangThaiBan(tenBan, "ĐÃ ĐẶT");
							panel.btnHuyBan.setEnabled(true);
							panel.btnHuyBan.setBackground(new Color(239, 68, 68));
						}

						panel.refreshBanList();
						JOptionPane.showMessageDialog(panel, "Xóa món thành công!");
					} catch (SQLException ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(panel, "Lỗi cơ sở dữ liệu: " + ex.getMessage());
					}
				}
			} else if (isPushed) {
				JOptionPane.showMessageDialog(panel, "Hàng không hợp lệ hoặc không được chọn!");
			}
			isPushed = false;
			return label;
		}

		@Override
		public boolean stopCellEditing() {
			isPushed = false;
			return super.stopCellEditing();
		}

		@Override
		protected void fireEditingStopped() {
			super.fireEditingStopped();
		}
	}
}