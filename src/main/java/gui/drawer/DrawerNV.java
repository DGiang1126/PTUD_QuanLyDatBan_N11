package gui.drawer;


import java.sql.SQLException;
import java.text.SimpleDateFormat;	
import java.util.Date;
import javax.swing.JOptionPane;

import dao.DAO_TaiKhoan;
import gui.Main;
import gui.homeAll;
import gui.panelForm.panelDatBan;
import gui.panelForm.panelQLyTK;
import gui.panelForm.panelTrangChu;
import raven.drawer.component.SimpleDrawerBuilder;
import raven.drawer.component.footer.SimpleFooterData;
import raven.drawer.component.header.SimpleHeaderData;
import raven.drawer.component.menu.MenuAction;
import raven.drawer.component.menu.MenuEvent;
import raven.drawer.component.menu.MenuValidation;
import raven.drawer.component.menu.SimpleMenuOption;
import raven.swing.AvatarIcon;

public class DrawerNV extends SimpleDrawerBuilder {

	private homeAll trangChinh;
	private Main trangDangNhap;
	private String maNV;
	private DAO_TaiKhoan taiKhoan;
	public DrawerNV(homeAll trangChinh, Main trangDangNhap, String maNV) {
		this.trangChinh = trangChinh;
		this.trangDangNhap = trangDangNhap;
		this.maNV = maNV;
		this.taiKhoan = new DAO_TaiKhoan();
		this.getSimpleHeaderData().setTitle("NTan");
	}
	public String getName() throws Exception {
		return trangChinh.getNamebyID();
	}
	public void showWelcomeMessage(String name) {
		JOptionPane.showMessageDialog(trangChinh, "Chào mừng " + name, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
	}
	@Override
	public SimpleHeaderData getSimpleHeaderData() {
	    String chucVu = "Nhân viên lễ tân";
	    return new SimpleHeaderData()
	        .setIcon(new AvatarIcon(getClass().getResource("/img/staffIcon.png"), 120, 120, 1500))
	        .setTitle("Nhà hàng Cerbus BBQ")
	        .setDescription(chucVu);
	}
	@Override
	public SimpleFooterData getSimpleFooterData() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd/MM/yyyy" + " HH:mm         ");
		String formattedTime = formatter.format(currentTime);
		formattedTime = formattedTime.replace("Monday", "Thứ Hai").replace("Tuesday", "Thứ Ba")
				.replace("Wednesday", "Thứ Tư").replace("Thursday", "Thứ Năm").replace("Friday", "Thứ Sáu")
				.replace("Saturday", "Thứ Bảy").replace("Sunday", "Chủ Nhật");
		return new SimpleFooterData().setTitle(formattedTime);
	}

	@Override
	public SimpleMenuOption getSimpleMenuOption() {
		String[][] menus = { { "~TRANG CHỦ~" }, { "Trang chủ" }, { "~BÀN~" }, { "Bàn", "Đặt bàn", "Quản lý bàn" },
				{ "~KHÁCH HÀNG~" }, { "Khách hàng", "Quản lý khách hàng", "Tìm kiếm khách hàng" }, { "~HÓA ĐƠN~" },
				{ "Hóa đơn", "Quản lý hóa đơn", "Tìm kiếm hóa đơn" }, { "~CÔNG CỤ~" },
				{ "Công cụ", "Trợ giúp", "Cài đặt" }, { "~TÀI KHOẢN~" },
				{ "Tài khoản", "Thông tin cá nhân", "Đăng xuất", "Thoát" } };
		String[] icons = { "home-svgrepo-com.svg", "table-dinner-svgrepo-com.svg", "users-svgrepo-com.svg",
				"bill-svgrepo-com.svg", "tools-svgrepo-com.svg", "account-svgrepo-com.svg" };

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

							}
						}
						// Khách hàng
						if (index == 2) {
							if (subIndex == 1) {
							} else if (subIndex == 2) {
							}
						}
						// Hóa đơn
						if (index == 3) {

						}
						// Công cụ
						if (index == 4) {

						}
						// Tài khoản
						if (index == 5) {
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