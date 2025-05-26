package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import connectDatabase.BaseDAO;
import entity.HoaDon;

public class DAO_HoaDon extends BaseDAO {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    CallableStatement cs = null;

    private DAO_NhanVien daoNV = new DAO_NhanVien();
    private DAO_KhachHang daoKH = new DAO_KhachHang();
    private DAO_PhuongThucThanhToan daoPTTT = new DAO_PhuongThucThanhToan();
    private DAO_ThueVAT daoVAT = new DAO_ThueVAT();
    private DAO_KhuyenMai daoKM = new DAO_KhuyenMai();
    private DAO_Ban daoBan = new DAO_Ban();
    private DAO_PhieuDatBan daoPDB = new DAO_PhieuDatBan();
    

    public boolean themHD(String maNV, String maKH, String maPTTT, String maVAT, String maKM, LocalDateTime ngayLap,
            LocalDateTime ngayXuat, String trangThai) {
        boolean isSuccess = false;
        try {
            conn = new BaseDAO().getConnection();
            String sql = "{CALL ThemHoaDon(?, ?, ?, ?, ?, ?, ?, ?)}";
            cs = conn.prepareCall(sql);
            cs.setString(1, maNV);
            cs.setString(2, maKH);
            cs.setString(3, maPTTT);
            cs.setString(4, maVAT);
            cs.setString(5, maKM);
            
            cs.setTimestamp(6, Timestamp.valueOf(ngayLap));
            cs.setTimestamp(7, Timestamp.valueOf(ngayXuat));
            cs.setString(8, trangThai);
            int rowsAffected = cs.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return isSuccess;
    }

    public String getMaHDByMaKHAndStatus(String maKH, String trangThai) {
        String maHD = null;
        String query = "SELECT MaHD FROM HoaDon WHERE MaKH = ? AND TrangThai = ?";
        try (Connection conn = new BaseDAO().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, maKH);
            ps.setString(2, trangThai);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    maHD = rs.getString("MaHD");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maHD;
    }

    public List<HoaDon> getAllHoaDon() throws SQLException {
        List<HoaDon> hoaDonList = new ArrayList<>();
        String query = "{CALL sp_GetAllHoaDon}";
        try (Connection conn = new BaseDAO().getConnection();
             CallableStatement cs = conn.prepareCall(query);
             ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                HoaDon hoaDon = new HoaDon();
                hoaDon.setMaHD(rs.getString("MaHD"));
                hoaDon.setNhanVien(daoNV.getNVbyID(rs.getString("MaNV")));
                hoaDon.setKhachHang(daoKH.getKhachHangbyMa(rs.getString("MaKH")));
                hoaDon.setPTTT(daoPTTT.getPTTTByMaPTTT(rs.getString("MaPTTT")));
                hoaDon.setThueVAT(daoVAT.getThueVATByMaVAT(rs.getString("MaVAT")));
                hoaDon.setKhuyenMai(daoKM.getPromotionById(rs.getString("MaKM")));
                hoaDon.setNgayLap(rs.getTimestamp("NgayLap") != null ? rs.getTimestamp("NgayLap").toLocalDateTime() : null);
                hoaDon.setNgayXuat(rs.getTimestamp("NgayXuat") != null ? rs.getTimestamp("NgayXuat").toLocalDateTime() : null);
                hoaDon.setTrangThai(rs.getString("TrangThai"));
                hoaDon.setBan(daoPDB.getLatestPDBByMaBan(rs.getString("MaBan")));
                hoaDonList.add(hoaDon);
            }
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return hoaDonList;
    }

    public List<HoaDon> searchHoaDon(String maHD, String maKH, String maBan, String sdt, String trangThai,
            LocalDateTime startDate, LocalDateTime endDate, String sortByGia) throws SQLException {
		List<HoaDon> hoaDonList = new ArrayList<>();
		String query = "{CALL sp_SearchHoaDon(?, ?, ?, ?, ?, ?, ?, ?)}"; // Thêm tham số thứ 4 cho @SDT
		try (Connection conn = new BaseDAO().getConnection();
				CallableStatement cs = conn.prepareCall(query)) {
				cs.setString(1, maHD != null && !maHD.isEmpty() ? maHD : null);
				cs.setString(2, maKH != null && !maKH.isEmpty() ? maKH : null);
				cs.setString(3, maBan != null && !maBan.isEmpty() ? maBan : null);
				cs.setString(4, sdt != null && !sdt.isEmpty() ? sdt : null); // Thêm SDT
	            String dbTrangThai = trangThai.equals("Tất cả") ? null :
	                trangThai.equals("Chưa thanh toán") ? "Chưa thanh toán" :
	                trangThai.equals("Đã thanh toán") ? "Đã thanh toán" : null;
	            cs.setString(5, dbTrangThai);
				cs.setTimestamp(6, startDate != null ? Timestamp.valueOf(startDate) : null);
				cs.setTimestamp(7, endDate != null ? Timestamp.valueOf(endDate) : null);
				cs.setString(8, sortByGia != null && !sortByGia.isEmpty() ? sortByGia : null);
		
		try (ResultSet rs = cs.executeQuery()) {
		while (rs.next()) {
					HoaDon hoaDon = new HoaDon();
					hoaDon.setMaHD(rs.getString("MaHD"));
					hoaDon.setNhanVien(daoNV.getNVbyID(rs.getString("MaNV")));
					hoaDon.setKhachHang(daoKH.getKhachHangbyMa(rs.getString("MaKH")));
					hoaDon.setPTTT(daoPTTT.getPTTTByMaPTTT(rs.getString("MaPTTT")));
					hoaDon.setThueVAT(daoVAT.getThueVATByMaVAT(rs.getString("MaVAT")));
					hoaDon.setKhuyenMai(daoKM.getPromotionById(rs.getString("MaKM")));
					hoaDon.setNgayLap(rs.getTimestamp("NgayLap") != null ? rs.getTimestamp("NgayLap").toLocalDateTime() : null);
					hoaDon.setNgayXuat(rs.getTimestamp("NgayXuat") != null ? rs.getTimestamp("NgayXuat").toLocalDateTime() : null);
					hoaDon.setTrangThai(rs.getString("TrangThai"));
					String maBanFromRS = rs.getString("MaBan");
					System.out.println("MaBan from ResultSet: " + maBanFromRS); // Debug
					hoaDon.setBan(daoPDB.getLatestPDBByMaBan(maBanFromRS));
					hoaDon.setTongTien(rs.getDouble("TongTien"));
					hoaDonList.add(hoaDon);
		}
		}
		} catch (SQLException e) {
e.printStackTrace();
throw e;
} catch (Exception e1) {
	// TODO Auto-generated catch block
	e1.printStackTrace();
}
return hoaDonList;
}
	public boolean capNhatTrangThaiHoaDon(String maHD, String trangThai) throws SQLException {
	    String query = "UPDATE HoaDon SET TrangThai = ? WHERE MaHD = ?";
	    try (Connection conn = new BaseDAO().getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
	        ps.setString(1, trangThai);
	        ps.setString(2, maHD);
	        return ps.executeUpdate() > 0;
	    } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
    private void closeResources() {
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