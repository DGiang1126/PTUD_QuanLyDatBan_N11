package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

import dao.DAO_NhanVien;
import entity.NhanVien;
import gui.drawer.DrawerNV;
import gui.drawer.DrawerQL;
import gui.tabbed.WindowsTabbed;
import raven.drawer.Drawer;
import raven.popup.GlassPanePopup;
import java.awt.Font;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.BorderLayout;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;

public class homeAll extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel panelBody;
	@SuppressWarnings("unused")
	private Main loginFrame;
	private EmbeddedMediaPlayerComponent mediaPlayerComponent;
	private DAO_NhanVien nhanVien;
	private String maNV;
	public homeAll(Main loginFrame, String maNV) throws Exception {
		setUndecorated(true);
		getContentPane().setBackground(new Color(255, 255, 255));
		
		this.loginFrame = loginFrame;
		this.maNV = maNV;
		// Color theme
		FlatRobotoFont.install();
		FlatLaf.registerCustomDefaultsSource("themes");
		FlatIntelliJLaf.setup();
		UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
		// Drawer
		nhanVien = new DAO_NhanVien();
		NhanVien NV = nhanVien.getNVbyID(maNV);
		GlassPanePopup.install(this);
		if ("Nhân viên lễ tân".trim().equalsIgnoreCase(NV.getChucVu().trim())) {
			DrawerNV DrawerNV = new DrawerNV(this, loginFrame, maNV);
			DrawerNV.showWelcomeMessage(NV.getTenNV().toString().trim());
			Drawer.getInstance().setDrawerBuilder(DrawerNV);
		} else {
			DrawerQL DrawerQL = new DrawerQL(this, loginFrame, maNV);
			DrawerQL.showWelcomeMessage(NV.getTenNV().toString().trim());
			Drawer.getInstance().setDrawerBuilder(DrawerQL);
		}
		// Set size and properties
		java.net.URL iconURL = getClass().getResource("/img/bbqIcon.png");
		ImageIcon icon = new ImageIcon(iconURL);
		setIconImage(icon.getImage());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLocationRelativeTo(null);
		setResizable(false);
		getContentPane().setLayout(null);

		// PanelBody setup
		panelBody = new JPanel();
		panelBody.setBackground(new Color(255, 255, 255));
		panelBody.setBounds(0, 0, 1920, 1080);
		getContentPane().add(panelBody);
		panelBody.setLayout(new BorderLayout(0, 0));
		// Tabbed
		WindowsTabbed.getInstance().install(this, panelBody);

		// Khởi tạo VLCJ media player
		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
	}
	public String getNamebyID() throws Exception {
		nhanVien = new DAO_NhanVien();
		NhanVien nv = null;
		try {
			nv = nhanVien.getNVbyID(this.maNV);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nv.getTenNV().toString().trim();
	}
	// Phương thức để phát video
	public void playVideo(String mediaPath) {
		panelBody.removeAll(); // Xóa nội dung cũ trong panelBody
		panelBody.add(mediaPlayerComponent, BorderLayout.CENTER); // Thêm VLC player vào panelBody
		panelBody.revalidate();
		panelBody.repaint();
		// Phát video
		mediaPlayerComponent.mediaPlayer().media().play(mediaPath);
		mediaPlayerComponent.mediaPlayer().controls().setRepeat(true); // Tùy chọn: lặp lại video
	}

	// Phương thức để dừng video
	public void stopVideo() {
		if (mediaPlayerComponent != null) {
			mediaPlayerComponent.mediaPlayer().controls().stop();
			panelBody.remove(mediaPlayerComponent);
			panelBody.revalidate();
			panelBody.repaint();
		}
	}

	public String geturl() {
		String url = "/img/home.png";
		return url;
	}

	public String getName(String name) {
		return name;
	}

	public void setPanelBody(JPanel panel) {
		panelBody.removeAll();
		panelBody.add(panel, BorderLayout.CENTER);
		panelBody.revalidate();
		panelBody.repaint();
	}
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main loginFrame = new Main();
            homeAll frame = null;
			try {
				frame = new homeAll(loginFrame, "NV001");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            frame.setVisible(true); // Hiển thị JFrame sau khi constructor hoàn tất
            frame.playVideo("video/2424767-uhd_3840_2160_24fps.mp4"); // Thay bằng đường dẫn video của bạn
        });
    }
	

}