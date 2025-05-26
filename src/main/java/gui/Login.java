package gui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.util.UIScale;
import dao.DAO_TaiKhoan;
import entity.TaiKhoan;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.sql.SQLException;

public class Login extends JPanel {

    private static final long serialVersionUID = 1L;
    private Main mainFrame;
    private DAO_TaiKhoan taiKhoan;

    public Login(Main mainFrame) {
        this.mainFrame = mainFrame;
        init();
    }

    private void init() {
        setOpaque(false);
        setLayout(new MigLayout("wrap,fillx,insets 45 45 50 45", "[fill]"));
        JLabel title = new JLabel("ĐĂNG NHẬP", SwingConstants.CENTER);
        JTextField txtUsername = new JTextField();
        JPasswordField txtPassword = new JPasswordField();
        JCheckBox chRememberMe = new JCheckBox("Nhớ tài khoản");
        JButton buttonLogin = new JButton("Đăng nhập");

        title.putClientProperty(FlatClientProperties.STYLE, "font:bold +10");
        txtUsername.putClientProperty(FlatClientProperties.STYLE, "margin:5,10,5,10;focusWidth:1;innerFocusWidth:0");
        txtPassword.putClientProperty(FlatClientProperties.STYLE,
                "margin:5,10,5,10;focusWidth:1;innerFocusWidth:0;showRevealButton:true");
        buttonLogin.putClientProperty(FlatClientProperties.STYLE,
                "background:$Component.accentColor;borderWidth:0;focusWidth:0;innerFocusWidth:0");
        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Điền tài khoản của bạn");
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Điền mật khẩu của bạn");

        add(title);
        add(new JLabel("Tài khoản"), "gapy 20");
        add(txtUsername);
        add(new JLabel("Mật khẩu"), "gapy 10");
        add(txtPassword);
        add(chRememberMe);
        add(buttonLogin, "gapy 30");

        buttonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                taiKhoan = new DAO_TaiKhoan();
                String username = txtUsername.getText().trim();
                String password = new String(txtPassword.getPassword()).trim();

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(Login.this, "Vui lòng điền đầy đủ thông tin!", "Cảnh báo",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        TaiKhoan tk = taiKhoan.checkLogins(username, password);
                        if (tk != null) {
                            String maNV = tk.getNhanVien().getMaNV();
                            EventQueue.invokeLater(() -> {
                                mainFrame.dispose();
                                homeAll trangChu = null;
                                try {
                                    trangChu = new homeAll(mainFrame, maNV);
                                    JOptionPane.showMessageDialog(null, "Đăng nhập thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    JOptionPane.showMessageDialog(Login.this, "Lỗi khi mở trang chính: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                                }
                                if (trangChu != null) {
                                    trangChu.setVisible(true);
                                    trangChu.playVideo("video/2424767-uhd_3840_2160_24fps.mp4");
                                }
                            });
                        } else {
                            JOptionPane.showMessageDialog(Login.this, "Tài khoản hoặc mật khẩu không đúng!",
                                    "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(Login.this, "Lỗi đăng nhập: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                }
            }
        });

        txtPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonLogin.doClick();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int arc = UIScale.scale(20);
        g2.setColor(getBackground());
        g2.setComposite(AlphaComposite.SrcOver.derive(0.6f));
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), arc, arc));
        g2.dispose();
        super.paintComponent(g);
    }
}