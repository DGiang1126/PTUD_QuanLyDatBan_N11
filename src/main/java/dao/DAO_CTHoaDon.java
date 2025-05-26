package dao;

import connectDatabase.BaseDAO;
import entity.CTHoaDon;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAO_CTHoaDon {
    public boolean themCTHD(String maHD, String maMonAn, int soLuong, double gia) throws SQLException {
        String query = "INSERT INTO ChiTietHoaDon (MaHD, MaMonAn, SoLuong, DonGia) VALUES (?, ?, ?, ?)";
        try (Connection conn = new BaseDAO().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, maHD);
            ps.setString(2, maMonAn);
            ps.setInt(3, soLuong);
            ps.setDouble(4, gia);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
    }
    public boolean xoaCTHD(String maHD, String maMonAn) throws SQLException {
        String query = "DELETE FROM ChiTietHoaDon WHERE MaHD = ? AND MaMonAn = ?";
        try (Connection conn = new BaseDAO().getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, maHD);
            ps.setString(2, maMonAn);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
    }
    public List<CTHoaDon> getCTHDByMaHD(String maHD) {
        List<CTHoaDon> danhSachCTHD = new ArrayList<>();
        String query = "SELECT \r\n"
        		+ "    MaHD,\r\n"
        		+ "    MaMonAn, \r\n"
        		+ "    SUM(SoLuong) AS TongSoLuong,\r\n"
        		+ "    SUM(DonGia) AS TongDonGia\r\n"
        		+ "FROM \r\n"
        		+ "    ChiTietHoaDon \r\n"
        		+ "WHERE \r\n"
        		+ "    MaHD = ? \r\n"
        		+ "GROUP BY \r\n"
        		+ "    MaHD, MaMonAn;";
        try (Connection conn = new BaseDAO().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, maHD);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CTHoaDon cthd = new CTHoaDon();
                cthd.setMaHoaDon(rs.getString(1));
                cthd.setMaMonAn(rs.getString(2));
                cthd.setSoLuong(rs.getInt(3));
                cthd.setDonGia(rs.getInt(4));
                danhSachCTHD.add(cthd);
            }
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return danhSachCTHD;
    }
    public List<CTHoaDon> getChiTietHoaDonByMaHDFromSP(String maHD) throws SQLException {
        List<CTHoaDon> chiTietList = new ArrayList<>();
        String query = "{CALL sp_GetChiTietHoaDonByMaHD(?)}";

        try (Connection conn = new BaseDAO().getConnection();
             CallableStatement cs = conn.prepareCall(query)) {
            cs.setString(1, maHD);

            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    CTHoaDon ct = new CTHoaDon();
                    ct.setMaHoaDon(rs.getString("MaHD"));
                    ct.setMaMonAn(rs.getString("MaMonAn"));
                    ct.setSoLuong(rs.getInt("SoLuong"));
                    ct.setDonGia(rs.getDouble("DonGia"));
                    ct.setThanhTien(rs.getDouble("ThanhTien"));
                    chiTietList.add(ct);
                }
            }

            // Lấy tổng tiền từ result set thứ hai (nếu cần)
            try (ResultSet rsTongTien = cs.getMoreResults() ? cs.getResultSet() : null) {
                if (rsTongTien != null && rsTongTien.next()) {
                    double tongTien = rsTongTien.getDouble("TongTien");
                    // Có thể gán vào HoaDon hoặc xử lý trong giao diện
                    System.out.println("TongTien: " + tongTien);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chiTietList;
    }
    public boolean capNhatBanChoHoaDon(String maHD, String newMaBan) {
    	String sql = "UPDATE ChiTietHoaDon SET MaBan = ? WHERE MaHD = ?";
        try (Connection conn = new BaseDAO().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newMaBan);
            ps.setString(2, maHD);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return false;
    }
    public boolean deleteCTHDByMaHD(String maHD) throws Exception {
        String sql = "DELETE FROM ChiTietHoaDon WHERE MaHD = ?";
        try (Connection conn = new BaseDAO().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHD);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }
}