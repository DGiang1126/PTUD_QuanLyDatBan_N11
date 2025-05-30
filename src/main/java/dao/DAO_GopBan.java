package dao;

import connectDatabase.BaseDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DAO_GopBan {
    public boolean insertGopBan(String maBanChinh, String maBanGop) throws Exception {
        String sql = "INSERT INTO GopBan (MaBanChinh, MaBanGop, ThoiGianGop) VALUES (?, ?, ?)";
        try (Connection conn = new BaseDAO().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maBanChinh);
            ps.setString(2, maBanGop);
            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            return ps.executeUpdate() > 0;
        }
    }

    public String getMaBanChinh(String maBanGop) throws Exception {
        String sql = "SELECT MaBanChinh FROM GopBan WHERE MaBanGop = ?";
        try (Connection conn = new BaseDAO().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maBanGop);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("MaBanChinh");
            }
        }
        return null;
    }

    public void deleteGopBanByMaBanChinh(String maBanChinh) throws Exception {
        String sql = "DELETE FROM GopBan WHERE MaBanChinh = ?";
        try (Connection conn = new BaseDAO().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maBanChinh);
            ps.executeUpdate();
        }
    }
    
    public List<String> getMergedBans(String maBanChinh) throws Exception {
        List<String> mergedBans = new ArrayList<>();
        String sql = "SELECT MaBanGop FROM GopBan WHERE MaBanChinh = ?";
        try (Connection conn = new BaseDAO().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maBanChinh);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                mergedBans.add(rs.getString("MaBanGop"));
            }
        }
        return mergedBans;
    }
    
    public int countMergedTables(String maBanChinh) throws Exception {
        String sql = "SELECT COUNT(*) FROM GopBan WHERE MaBanChinh = ?";
        try (Connection conn = new BaseDAO().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maBanChinh);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
    
    public boolean deleteGopBanByMaBanPhu(String maBanPhu) throws Exception {
        String sql = "DELETE FROM GopBan WHERE MaBanGop = ?";
        try (Connection conn = new BaseDAO().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maBanPhu);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi khi xóa gộp bàn theo maBanPhu: " + e.getMessage());
            throw e;
        }
    }
    
}