package gui.drawer;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import dao.DAO_TaiKhoan;
import gui.Main;
import gui.homeAll;
import gui.panelForm.PanelQLyHD;
import gui.panelForm.panelDatBan;
import gui.panelForm.panelKhuyenMai;
import gui.panelForm.panelPhieuDatBanTang1;
import gui.panelForm.panelQLNhanVien;
import gui.panelForm.panelQLyTK;
import gui.panelForm.panelQlyKhachhang;
import gui.panelForm.panelQlyMonAn;
import gui.panelForm.panelQlyban;
import gui.panelForm.panelSearchKH;
import gui.panelForm.panelTKNhanVien;
import gui.panelForm.panelThongKeKH;
import gui.panelForm.panelThongKeTheoNam;
import gui.panelForm.panelThongKeTheoNgay;
import gui.panelForm.panelThongKeTheoThang;
import gui.panelForm.panelTimKiemHD;
import gui.panelForm.panelTimKiemMonAn;
import gui.panelForm.panelTrangChu;
import raven.drawer.component.SimpleDrawerBuilder;
import raven.drawer.component.footer.SimpleFooterData;
import raven.drawer.component.header.SimpleHeaderData;
import raven.drawer.component.menu.MenuAction;
import raven.drawer.component.menu.MenuEvent;
import raven.drawer.component.menu.MenuValidation;
import raven.drawer.component.menu.SimpleMenuOption;
import raven.swing.AvatarIcon;

public class DrawerQL extends SimpleDrawerBuilder {

	private homeAll trangChinh;
	private Main trangDangNhap;
	private String maNV;
	private DAO_TaiKhoan taiKhoan;

//	public DrawerQL(homeAll trangChinh, Main trangDangNhap) {
//		this.trangChinh = trangChinh;
//		this.trangDangNhap = trangDangNhap;
//		this.maNV = maNV;
//        this.taiKhoan = new DAO_TaiKhoan();
//	}
	 public DrawerQL(homeAll trangChinh, Main trangDangNhap, String maNV) {
	        this.trangChinh = trangChinh;
	        this.trangDangNhap = trangDangNhap;
	        this.maNV = maNV;
	        this.taiKhoan = new DAO_TaiKhoan();
	    }

	public void showWelcomeMessage(String name) {
		JOptionPane.showMessageDialog(trangChinh, "Chào mừng " + name, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
	}

	@Override
	public SimpleHeaderData getSimpleHeaderData() {
		String chucVu = "Nhân viên quản lý";
		return new SimpleHeaderData()
				.setIcon(new AvatarIcon(getClass().getResource("/img/staffIcon.png"), 120, 120, 1200))
				.setTitle("Nhà hàng Cerbus BBQ").setDescription(chucVu);
	}

	@Override
	public SimpleFooterData getSimpleFooterData() {
		Date currentTime = new Date();
		// Định dạng ban đầu
		SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd/MM/yyyy" + " HH:mm              ");
		String formattedTime = formatter.format(currentTime);
		// Thay thế sang tiếng Việt
		formattedTime = formattedTime.replace("Monday", "Thứ Hai").replace("Tuesday", "Thứ Ba")
				.replace("Wednesday", "Thứ Tư").replace("Thursday", "Thứ Năm").replace("Friday", "Thứ Sáu")
				.replace("Saturday", "Thứ Bảy").replace("Sunday", "Chủ Nhật");
		return new SimpleFooterData().setTitle(formattedTime);
	}

	@Override
	public SimpleMenuOption getSimpleMenuOption() {
		String[][] menus = { { "~TRANG CHỦ~" }, { "Trang chủ" }, { "~BÀN~" }, { "Bàn", "Đặt bàn", "Quản lý bàn" },
				{ "~KHÁCH HÀNG~" }, { "Khách hàng", "Quản lý khách hàng", "Tìm kiếm khách hàng" }, 
				{ "~NHÂN VIÊN~" },{ "Nhân viên", "Quản lý nhân viên", "Tìm kiếm nhân viên" }, 
				{ "~HÓA ĐƠN~" },{ "Hóa đơn", "Quản lý hóa đơn", "Tìm kiếm hóa đơn" }, 
				{ "~MÓN ĂN~" }, { "Món ăn", "Quản lý món ăn", "Tìm kiếm món ăn" },
				 { "~THỐNG KÊ~" }, { "Doanh thu", "Theo ngày", "Theo tháng", "Theo năm", "Khách hàng" }, 
				{ "~KHUYẾN MÃI~" }, { "Khuyến mãi", "Quản lý khuyến mãi" },
				{ "~CÔNG CỤ~" }, { "Công cụ", "Trợ giúp", "Cài đặt" }, { "~TÀI KHOẢN~" },
				{ "Tài khoản", "Quản lý tài khoản", "Đăng xuất", "Thoát" } };
		String[] icons = { "home-svgrepo-com.svg", "table-dinner-svgrepo-com.svg", "users-svgrepo-com.svg",
				"staff-symbol-svgrepo-com.svg", "bill-svgrepo-com.svg", "fast-food-outline-svgrepo-com.svg",
				"statistics-graph-stats-analytics-business-data-svgrepo-com.svg","discount-percentage-svgrepo-com.svg",
				"tools-svgrepo-com.svg", "account-svgrepo-com.svg" };

		return new SimpleMenuOption().setMenus(menus).setIcons(icons).setBaseIconPath("img").setIconScale(0.03f)
				.addMenuEvent(new MenuEvent() {
					
					@Override
					public void selected(MenuAction action, int index, int subIndex) {
						System.out.println("Menu selected: " + index + " " + subIndex);
						// Trang chủ
						if (index == 0 && subIndex == 0) {

							panelTrangChu home = new panelTrangChu();
							trangChinh.setPanelBody(home);
							home.playVideo("video/2424767-uhd_3840_2160_24fps.mp4");
						}
						// Bàn
						if (index == 1) {
							if (subIndex == 1) {
								panelDatBan DatBan = new panelDatBan();
								trangChinh.setPanelBody(DatBan);
								DatBan.playVideo("video/88189-602915536_small.mp4");
							} else if (subIndex == 2) {
								panelQlyban QlyBan = new panelQlyban();
								trangChinh.setPanelBody(QlyBan);
							}
						}
						// Khách hàng
						if (index == 2) {
							if (subIndex == 1) {
								panelQlyKhachhang KhachHang = new panelQlyKhachhang();
								trangChinh.setPanelBody(KhachHang);
							} else if (subIndex == 2) {
								panelSearchKH TimKiemKH = new panelSearchKH();
								trangChinh.setPanelBody(TimKiemKH);
							}
						}
						// Nhân viên
						if (index == 3) {
							if (subIndex == 1) {
								panelQLNhanVien panelNV = null;
								try {
									panelNV = new panelQLNhanVien();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								trangChinh.setPanelBody(panelNV);
							}	else if (subIndex == 2) {
								panelTKNhanVien panelTKNV = null;
								try {
									panelTKNV = new panelTKNhanVien();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								trangChinh.setPanelBody(panelTKNV);
							}
							
						}
						// Hóa đơn
						if (index == 4) {
							if(subIndex == 1) {
								PanelQLyHD HoaDon = new PanelQLyHD();
								trangChinh.setPanelBody(HoaDon);
							}else if(subIndex == 2) {
								panelTimKiemHD TKHoaDon = new panelTimKiemHD();
								trangChinh.setPanelBody(TKHoaDon);
								
							}
						}
						// Món ăn
						if (index == 5) {
							if(subIndex == 1) {
								panelQlyMonAn MonAn = null;
								try {
									MonAn = new panelQlyMonAn();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								trangChinh.setPanelBody(MonAn);
							}else if(subIndex == 2) {
								panelTimKiemMonAn TkMonAn = null;
								try {
									TkMonAn = new panelTimKiemMonAn();
								}catch(Exception e) {
									e.printStackTrace();
								}
								trangChinh.setPanelBody(TkMonAn);
							}
						}
						// Thống kê
						if (index == 6) {
							if (subIndex == 1) {
								panelThongKeTheoNgay ThongKeTN = new panelThongKeTheoNgay();
								trangChinh.setPanelBody(ThongKeTN);
							}else if(subIndex == 2) {
								panelThongKeTheoThang ThongKeTT = new panelThongKeTheoThang();
								trangChinh.setPanelBody(ThongKeTT);
							}else if(subIndex == 3) {
								panelThongKeTheoNam ThongKeTY = new panelThongKeTheoNam();
								trangChinh.setPanelBody(ThongKeTY);
							}else if(subIndex == 4) {
								panelThongKeKH ThongKeKH = new panelThongKeKH();
								trangChinh.setPanelBody(ThongKeKH);
							}
						}
						// Khuyến mãi
						if (index == 7) {
							if (subIndex == 0) {
								panelKhuyenMai panelKM = new panelKhuyenMai();
								trangChinh.setPanelBody(panelKM);
							}
						}
						// Công cụ
						if (index == 8) {

						}
						// Tài khoản
						if (index == 9) {
							if (subIndex == 1) {
								panelQLyTK TaiKhoan = null;
								try {
									TaiKhoan = new panelQLyTK();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								trangChinh.setPanelBody(TaiKhoan);
							} else if (subIndex == 2) {
								int confirm = JOptionPane.showConfirmDialog(trangChinh, "Bạn có chắc muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                                if (confirm == JOptionPane.YES_OPTION) {
                                    try {
                                        taiKhoan.updateLogoutStatus(maNV);
                                        JOptionPane.showMessageDialog(trangChinh, "Đăng xuất thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                                        trangChinh.dispose();
                                        trangDangNhap.switchToLogin();
                                        trangDangNhap.setVisible(true);
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                        JOptionPane.showMessageDialog(trangChinh, "Lỗi khi đăng xuất: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                                    } catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
                                }
							} else if (subIndex == 3) {
								int confirm = JOptionPane.showConfirmDialog(trangChinh, "Bạn có chắc muốn thoát ứng dụng?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                                if (confirm == JOptionPane.YES_OPTION) {
                                    try {
                                        taiKhoan.updateLogoutStatus(maNV); // Đăng xuất trước khi thoát
                                        System.exit(0);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        JOptionPane.showMessageDialog(trangChinh, "Lỗi khi thoát: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                                    }
                                }
							}
						}
					}
				}).setMenuValidation(new MenuValidation() {
					@Override
					public boolean menuValidation(int index, int subIndex) {
						return true;
					}
				});
	}
}