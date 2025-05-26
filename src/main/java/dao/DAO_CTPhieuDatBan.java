package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import connectDatabase.BaseDAO;
import entity.CTPhieuDatBan;

public class DAO_CTPhieuDatBan extends BaseDAO {
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	public boolean insertCTPhieuDatBan(CTPhieuDatBan ctPDB) {
		String sql = "INSERT INTO ChiTietPhieuDatBan (MaPDB, TimeNhanBan, TimeTraBan, SoNguoi) VALUES (?, ?, ?, ?)";
		try {
			conn = new BaseDAO().getConnection(); // Giả sử BaseDAO có phương thức getConnection()
			ps = conn.prepareStatement(sql);

			ps.setString(1, ctPDB.getMaPDB()); // Lấy maPDB từ đối tượng PhieuDatBan
			ps.setTimestamp(2, Timestamp.valueOf(ctPDB.getTimeNhanBan())); // Chuyển LocalDateTime thành Timestamp
			ps.setTimestamp(3, Timestamp.valueOf(ctPDB.getTimeTraBan())); // Chuyển LocalDateTime thành Timestamp
			ps.setInt(4, ctPDB.getSoNguoi());

			int rowAffected = ps.executeUpdate();
			return rowAffected > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public CTPhieuDatBan getCTPDBByMa(String maPDB) {
		String sql = "select * from ChiTietPhieuDatBan where MaPDB =?";
		try (Connection conn = new BaseDAO().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, maPDB);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					LocalDateTime timeNhanBan = rs.getTimestamp(2) != null ? rs.getTimestamp(2).toLocalDateTime()
							: null;
					LocalDateTime timeTraBan = rs.getTimestamp(3) != null ? rs.getTimestamp(3).toLocalDateTime() : null;
					return new CTPhieuDatBan(rs.getString(1), // maPDB
							timeNhanBan, timeTraBan, rs.getInt(4) // soNguoi
					);
				}
			}
		} catch (

		Exception e) {
			e.printStackTrace();
		}
		return null;
	}

    public boolean capNhatMaBanChoPhieuDatBan(String oldMaBan, String newMaBan) {
        String sql = "UPDATE PhieuDatBan SET MaBan = ? WHERE MaBan = ?";
        try (Connection conn = new BaseDAO().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newMaBan);
            ps.setString(2, oldMaBan);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return false;
    }
    
    public boolean deleteCTPhieuDatBanByMaPDB(String maPDB) {
        String sql = "DELETE FROM ChiTietPhieuDatBan WHERE MaPDB = ?";
        try {
            conn = new BaseDAO().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, maPDB);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}