package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import connectDatabase.BaseDAO;
import entity.NhanVien;
import entity.TaiKhoan;

public class DAO_TaiKhoan extends BaseDAO {

    public TaiKhoan checkLogins(String username, String password) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            // Đăng xuất tất cả tài khoản Online trước khi đăng nhập mới
            updateAllAccountsToOffline(conn);
            String sql = "SELECT tk.MaNV, tk.TenDangNhap, tk.MatKhau, tk.TrangThai, tk.GioVaoLam, tk.GioNghi, tk.SoGioLam, " +
                         "nv.TenNV, nv.SDT, nv.GioiTinh, nv.ChucVu, nv.Tuoi, nv.Hesoluong, nv.LuongNV, nv.LinkIMG, nv.Email " +
                         "FROM TaiKhoan tk " +
                         "JOIN NhanVien nv ON tk.MaNV = nv.MaNV " +
                         "WHERE tk.TenDangNhap = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if (rs.next()) {
                if (password.equals(rs.getString("MatKhau").trim())) {
                    TaiKhoan tk = createTaiKhoanFromResultSet(rs);
                    updateLoginStatus(conn, tk.getNhanVien().getMaNV());
                    return tk;
                }
            }
        } finally {
            closeResources(conn, ps, rs, null);
        }
        return null;
    }

    private void updateAllAccountsToOffline(Connection conn) throws SQLException {
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE TaiKhoan SET TrangThai = 'Offline', GioNghi = GETDATE(), " +
                         "SoGioLam = SoGioLam + ROUND(DATEDIFF(MINUTE, GioVaoLam, GETDATE()) / 60.0, 1) " +
                         "WHERE TrangThai = 'Online' AND GioVaoLam IS NOT NULL";
            ps = conn.prepareStatement(sql);
            ps.executeUpdate();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void updateLoginStatus(Connection conn, String maNV) throws SQLException {
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE TaiKhoan SET TrangThai = 'Online', GioVaoLam = GETDATE(), GioNghi = NULL WHERE MaNV = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, maNV);
            ps.executeUpdate();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void updateLogoutStatus(String maNV) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            String sql = "UPDATE TaiKhoan SET TrangThai = 'Offline', GioNghi = GETDATE(), " +
                         "SoGioLam = SoGioLam + ROUND(DATEDIFF(MINUTE, GioVaoLam, GETDATE()) / 60.0, 1) " +
                         "WHERE MaNV = ? AND GioVaoLam IS NOT NULL";
            ps = conn.prepareStatement(sql);
            ps.setString(1, maNV);
            ps.executeUpdate();
        } finally {
            closeResources(conn, ps, null, null);
        }
    }

    public List<TaiKhoan> loadTaiKhoanData() throws Exception {
        Connection conn = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        List<TaiKhoan> taiKhoanList = new ArrayList<>();
        try {
            conn = getConnection();
            cs = conn.prepareCall("{CALL sp_GetAllTaiKhoan}");
            rs = cs.executeQuery();
            while (rs.next()) {
                taiKhoanList.add(createTaiKhoanFromResultSet(rs));
            }
        } finally {
            closeResources(conn, null, rs, cs);
        }
        return taiKhoanList;
    }

    public boolean addTaiKhoan(TaiKhoan tk) throws Exception {
        Connection conn = null;
        CallableStatement cs = null;
        try {
            conn = getConnection();
            cs = conn.prepareCall("{CALL sp_InsertTaiKhoan(?,?,?,?)}");
            cs.setString(1, tk.getNhanVien().getMaNV());
            cs.setString(2, tk.getTenDangNhap());
            cs.setString(3, tk.getMatKhau());
            cs.setString(4, tk.getTrangThai());
            return cs.executeUpdate() > 0;
        } finally {
            closeResources(conn, null, null, cs);
        }
    }

    public boolean deleteTaiKhoan(String maNV) throws Exception {
        Connection conn = null;
        CallableStatement cs = null;
        try {
            conn = getConnection();
            cs = conn.prepareCall("{CALL sp_DeleteTaiKhoan(?)}");
            cs.setString(1, maNV);
            return cs.executeUpdate() > 0;
        } finally {
            closeResources(conn, null, null, cs);
        }
    }

    public boolean updateTaiKhoan(TaiKhoan tk) throws Exception {
        Connection conn = null;
        CallableStatement cs = null;
        try {
            conn = getConnection();
            cs = conn.prepareCall("{CALL sp_UpdateTaiKhoan(?,?,?,?)}");
            cs.setString(1, tk.getNhanVien().getMaNV());
            cs.setString(2, tk.getTenDangNhap());
            cs.setString(3, tk.getMatKhau());
            cs.setString(4, tk.getTrangThai());
            return cs.executeUpdate() > 0;
        } finally {
            closeResources(conn, null, null, cs);
        }
    }

    public List<TaiKhoan> searchTaiKhoan(String keyword) throws Exception {
        Connection conn = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        List<TaiKhoan> taiKhoanList = new ArrayList<>();
        try {
            conn = getConnection();
            cs = conn.prepareCall("{CALL sp_SearchTaiKhoan(?)}");
            cs.setString(1, keyword);
            rs = cs.executeQuery();
            while (rs.next()) {
                taiKhoanList.add(createTaiKhoanFromResultSet(rs));
            }
        } finally {
            closeResources(conn, null, rs, cs);
        }
        return taiKhoanList;
    }

    private TaiKhoan createTaiKhoanFromResultSet(ResultSet rs) throws SQLException {
        NhanVien nhanVien = new NhanVien(
                rs.getString("MaNV"),
                rs.getString("TenNV"),
                rs.getString("SDT"),
                rs.getString("GioiTinh"),
                rs.getString("ChucVu"),
                rs.getInt("Tuoi"),
                rs.getDouble("Hesoluong"),
                rs.getDouble("LuongNV"),
                rs.getString("LinkIMG"),
                rs.getString("Email")
        );

        TaiKhoan taiKhoan = new TaiKhoan();
        taiKhoan.setTenDangNhap(rs.getString("TenDangNhap"));
        taiKhoan.setMatKhau(rs.getString("MatKhau"));
        taiKhoan.setTrangThai(rs.getString("TrangThai"));
        if (rs.getTimestamp("GioVaoLam") != null) {
            taiKhoan.setGioVaoLam(rs.getTimestamp("GioVaoLam").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        }
        if (rs.getTimestamp("GioNghi") != null) {
            taiKhoan.setGioNghi(rs.getTimestamp("GioNghi").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        }
        taiKhoan.setSoGioLam(rs.getDouble("SoGioLam"));
        taiKhoan.setNhanVien(nhanVien);
        return taiKhoan;
    }

    private void closeResources(Connection conn, PreparedStatement ps, ResultSet rs, CallableStatement cs) {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (cs != null) cs.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}