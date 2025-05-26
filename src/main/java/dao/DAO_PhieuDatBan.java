package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import connectDatabase.BaseDAO;
import entity.PhieuDatBan;

public class DAO_PhieuDatBan extends BaseDAO {
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	public void deletePhieuDatBanByMaBan(String maBan) throws SQLException {
	    String query = "{call sp_XoaPhieuDatTheoBan(?)}";
	    try (Connection conn = new BaseDAO().getConnection();
	         PreparedStatement ps = conn.prepareStatement(query)) {
	        ps.setString(1, maBan);
	        ps.executeUpdate();
	    } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public PhieuDatBan getLatestPDBByMaBan(String maBan) throws SQLException {
        String query = "SELECT TOP 1 * FROM PhieuDatBan WHERE MaBan = ? ORDER BY MaPDB DESC";
        try (Connection conn = new BaseDAO().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, maBan);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
            	return new PhieuDatBan(rs.getString(1),
            			rs.getString(2),
            			rs.getString(3),
            			rs.getString(4));
            }
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }
	public PhieuDatBan getPDBbyMa(String maPDB) {
	    String sql = "select * from PhieuDatBan where MaPDB = ?";
	    try (Connection conn = new BaseDAO().getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setString(1, maPDB);
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                return new PhieuDatBan(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
	public String getNextMaPDB() {
	    String sql = "SELECT MAX(MaPDB) AS MaxMaPDB FROM PhieuDatBan";
	    String prefix = "PDB";
	    String newMaPDB = prefix + "00001";
	    Connection conn = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    try {
	        try {
				conn = new BaseDAO().getConnection();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        ps = conn.prepareStatement(sql);
	        rs = ps.executeQuery();
	        if (rs.next()) {
	            String maxMaPDB = rs.getString("MaxMaPDB");
	            if (maxMaPDB != null && maxMaPDB.startsWith(prefix)) {
	                int number = Integer.parseInt(maxMaPDB.substring(prefix.length())) + 1;
	                newMaPDB = String.format("%s%05d", prefix, number); // Adjustable format
	            }
	        }
	    } catch (SQLException | NumberFormatException e) {
	        System.err.println("Error generating next MaPDB: " + e.getMessage());
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (ps != null) ps.close();
	            if (conn != null) conn.close();
	        } catch (SQLException e) {
	            System.err.println("Error closing resources: " + e.getMessage());
	        }
	    }
	    return newMaPDB;
	}

	public boolean insertPhieuDatBan(PhieuDatBan pdb) throws Exception {
		String sql = "INSERT INTO PhieuDatBan (MaPDB, MaKhachHang, MaBan, MaNV) VALUES (?, ?, ?, ?)";
		try {
			conn = new BaseDAO().getConnection();
			ps = conn.prepareStatement(sql);

			ps.setString(1, pdb.getMaPDB());
			ps.setString(2, pdb.getMaKH());
			ps.setString(3, pdb.getMaBan());
			ps.setString(4, pdb.getMaNV());

			int rowAffected = ps.executeUpdate();
			return rowAffected > 0;
		} catch (SQLException e) {
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

}