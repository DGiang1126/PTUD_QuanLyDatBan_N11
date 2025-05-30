
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import connectDatabase.BaseDAO;

public class DAO_ThongKe extends BaseDAO {
	public Map<String, Object> getThongKeTheoNgay(Date ngay) throws Exception {
	    Map<String, Object> result = new HashMap<>();
	    Connection conn = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    try {
	        conn = getConnection();
	        // Tổng doanh thu: Tính tổng giá món trước, sau đó áp dụng VAT và khuyến mãi
	        String sqlDoanhThu = "WITH HoaDonTong AS (" +
	                             "    SELECT hd.MaHD, SUM(ct.SoLuong * ct.DonGia) AS TongGiaMon " +
	                             "    FROM HoaDon hd " +
	                             "    JOIN ChiTietHoaDon ct ON hd.MaHD = ct.MaHD " +
	                             "    WHERE CAST(hd.NgayLap AS DATE) = ? " +
	                             "    GROUP BY hd.MaHD " +
	                             ") " +
	                             "SELECT SUM(CASE " +
	                             "    WHEN km.MaKM IS NOT NULL AND km.NgayBatDau <= hd.NgayLap AND km.NgayKetThuc >= hd.NgayLap " +
	                             "    THEN (hdm.TongGiaMon * (1 + v.PhanTramThue / 100) * (1 - km.PhanTramGiamGia / 100)) " +
	                             "    ELSE (hdm.TongGiaMon * (1 + v.PhanTramThue / 100)) " +
	                             "    END) AS TongDoanhThu " +
	                             "FROM HoaDon hd " +
	                             "JOIN HoaDonTong hdm ON hd.MaHD = hdm.MaHD " +
	                             "LEFT JOIN ThueVAT v ON hd.MaVAT = v.MaVAT " +
	                             "LEFT JOIN KhuyenMai km ON hd.MaKM = km.MaKM " +
	                             "WHERE CAST(hd.NgayLap AS DATE) = ?";

	        ps = conn.prepareStatement(sqlDoanhThu);
	        ps.setDate(1, new java.sql.Date(ngay.getTime()));
	        ps.setDate(2, new java.sql.Date(ngay.getTime()));
	        rs = ps.executeQuery();
	        long tongDoanhThu = 0;
	        if (rs.next()) {
	            tongDoanhThu = rs.getLong("TongDoanhThu");
	        }
	        result.put("TongDoanhThu", tongDoanhThu);

	        // Tổng số hóa đơn
	        String sqlHoaDon = "SELECT COUNT(*) AS SoHoaDon " +
	                           "FROM HoaDon " +
	                           "WHERE CAST(NgayLap AS DATE) = ?";
	        ps = conn.prepareStatement(sqlHoaDon);
	        ps.setDate(1, new java.sql.Date(ngay.getTime()));
	        rs = ps.executeQuery();
	        int soHoaDon = 0;
	        if (rs.next()) {
	            soHoaDon = rs.getInt("SoHoaDon");
	        }
	        result.put("SoHoaDon", soHoaDon);

	        // Tổng số bàn được đặt
	        String sqlBan = "SELECT COUNT(DISTINCT pdb.MaBan) AS SoBan " +
	                        "FROM PhieuDatBan pdb " +
	                        "JOIN ChiTietPhieuDatBan ct ON pdb.MaPDB = ct.MaPDB " +
	                        "WHERE CAST(ct.TimeNhanBan AS DATE) = ?";
	        ps = conn.prepareStatement(sqlBan);
	        ps.setDate(1, new java.sql.Date(ngay.getTime()));
	        rs = ps.executeQuery();
	        int soBan = 0;
	        if (rs.next()) {
	            soBan = rs.getInt("SoBan");
	        }
	        result.put("SoBan", soBan);

	        // Doanh thu trung bình
	        result.put("DoanhThuTrungBinh", soHoaDon == 0 ? 0 : tongDoanhThu / soHoaDon);

	        return result;
	    } finally {
	        closeResources(conn, ps, rs, null);
	    }
	}

	public Map<Integer, Long> getDoanhThuTheoTang(Date ngay) throws Exception {
	    Map<Integer, Long> result = new HashMap<>();
	    Connection conn = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    try {
	        conn = getConnection();
	        String sql = "WITH HoaDonTong AS (" +
	                     "    SELECT hd.MaHD, SUM(ct.SoLuong * ct.DonGia) AS TongGiaMon " +
	                     "    FROM HoaDon hd " +
	                     "    JOIN ChiTietHoaDon ct ON hd.MaHD = ct.MaHD " +
	                     "    WHERE CAST(hd.NgayLap AS DATE) = ? " +
	                     "    GROUP BY hd.MaHD " +
	                     ") " +
	                     "SELECT b.Tang, SUM(CASE " +
	                     "    WHEN km.MaKM IS NOT NULL AND km.NgayBatDau <= hd.NgayLap AND km.NgayKetThuc >= hd.NgayLap " +
	                     "    THEN (hdm.TongGiaMon * (1 + v.PhanTramThue / 100) * (1 - km.PhanTramGiamGia / 100)) " +
	                     "    ELSE (hdm.TongGiaMon * (1 + v.PhanTramThue / 100)) " +
	                     "    END) AS DoanhThu " +
	                     "FROM HoaDon hd " +
	                     "JOIN HoaDonTong hdm ON hd.MaHD = hdm.MaHD " +
	                     "JOIN PhieuDatBan pdb ON hd.MaKH = pdb.MaKhachHang " +
	                     "JOIN ChiTietPhieuDatBan ctpd ON pdb.MaPDB = ctpd.MaPDB " +
	                     "JOIN Ban b ON pdb.MaBan = b.MaBan " + 
	                     "LEFT JOIN ThueVAT v ON hd.MaVAT = v.MaVAT " +
	                     "LEFT JOIN KhuyenMai km ON hd.MaKM = km.MaKM " +
	                     "WHERE CAST(hd.NgayLap AS DATE) = ? " +
	                     "AND hd.NgayLap BETWEEN ctpd.TimeNhanBan AND ctpd.TimeTraBan " +
	                     "GROUP BY b.Tang";
	        ps = conn.prepareStatement(sql);
	        ps.setDate(1, new java.sql.Date(ngay.getTime()));
	        ps.setDate(2, new java.sql.Date(ngay.getTime()));
	        rs = ps.executeQuery();
	        while (rs.next()) {
	            result.put(rs.getInt("Tang"), rs.getLong("DoanhThu"));
	        }
	        for (int tang = 1; tang <= 3; tang++) {
	            result.putIfAbsent(tang, 0L);
	        }
	        return result;
	    } catch (SQLException e) {
	        for (int tang = 1; tang <= 3; tang++) {
	            result.putIfAbsent(tang, 0L);
	        }
	        throw e;
	    } finally {
	        closeResources(conn, ps, rs, null);
	    }
	}

	public List<Object[]> getChiTietHoaDon(Date ngay) throws Exception {
	    List<Object[]> result = new ArrayList<>();
	    Connection conn = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    try {
	        conn = getConnection();
	        String sql = "WITH HoaDonTong AS (" +
	                     "    SELECT hd.MaHD, SUM(ct.SoLuong * ct.DonGia) AS TongGiaMon " +
	                     "    FROM HoaDon hd " +
	                     "    JOIN ChiTietHoaDon ct ON hd.MaHD = ct.MaHD " +
	                     "    WHERE CAST(hd.NgayLap AS DATE) = ? " +
	                     "    GROUP BY hd.MaHD " +
	                     ") " +
	                     "SELECT hd.MaHD, kh.SDT, kh.TenKH, nv.MaNV, nv.TenNV, " +
	                     "CASE " +
	                     "    WHEN km.MaKM IS NOT NULL AND km.NgayBatDau <= hd.NgayLap AND km.NgayKetThuc >= hd.NgayLap " +
	                     "    THEN (hdm.TongGiaMon * (1 + v.PhanTramThue / 100) * (1 - km.PhanTramGiamGia / 100)) " +
	                     "    ELSE (hdm.TongGiaMon * (1 + v.PhanTramThue / 100)) " +
	                     "END AS TongTien " +
	                     "FROM HoaDon hd " +
	                     "JOIN HoaDonTong hdm ON hd.MaHD = hdm.MaHD " +
	                     "JOIN KhachHang kh ON hd.MaKH = kh.MaKH " +
	                     "JOIN NhanVien nv ON hd.MaNV = nv.MaNV " +
	                     "LEFT JOIN ThueVAT v ON hd.MaVAT = v.MaVAT " +
	                     "LEFT JOIN KhuyenMai km ON hd.MaKM = km.MaKM " +
	                     "WHERE CAST(hd.NgayLap AS DATE) = ?";
	        ps = conn.prepareStatement(sql);
	        ps.setDate(1, new java.sql.Date(ngay.getTime()));
	        ps.setDate(2, new java.sql.Date(ngay.getTime()));
	        rs = ps.executeQuery();
	        while (rs.next()) {
	            result.add(new Object[]{
	                rs.getString("MaHD"),
	                rs.getString("SDT"),
	                rs.getString("TenKH"),
	                rs.getString("MaNV"),
	                rs.getString("TenNV"),
	                String.format("%,d", rs.getLong("TongTien"))
	            });
	        }
	        return result;
	    } finally {
	        closeResources(conn, ps, rs, null);
	    }
	}
	
	
	public Map<String, Object> getThongKeTheoThang(int thang, int nam) throws Exception {
	    Map<String, Object> result = new HashMap<>();
	    Connection conn = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    try {
	        conn = getConnection();
	        // Tổng doanh thu: Tính tổng giá món trước, sau đó áp dụng VAT và khuyến mãi
	        String sqlDoanhThu = "WITH HoaDonTong AS (" +
	                             "    SELECT hd.MaHD, SUM(ct.SoLuong * ct.DonGia) AS TongGiaMon " +
	                             "    FROM HoaDon hd " +
	                             "    JOIN ChiTietHoaDon ct ON hd.MaHD = ct.MaHD " +
	                             "    WHERE MONTH(hd.NgayLap) = ? AND YEAR(hd.NgayLap) = ? " +
	                             "    GROUP BY hd.MaHD " +
	                             ") " +
	                             "SELECT SUM(CASE " +
	                             "    WHEN km.MaKM IS NOT NULL AND km.NgayBatDau <= hd.NgayLap AND km.NgayKetThuc >= hd.NgayLap " +
	                             "    THEN (hdm.TongGiaMon * (1 + ISNULL(v.PhanTramThue, 0) / 100) * (1 - ISNULL(km.PhanTramGiamGia, 0) / 100)) " +
	                             "    ELSE (hdm.TongGiaMon * (1 + ISNULL(v.PhanTramThue, 0) / 100)) " +
	                             "    END) AS TongDoanhThu " +
	                             "FROM HoaDon hd " +
	                             "JOIN HoaDonTong hdm ON hd.MaHD = hdm.MaHD " +
	                             "LEFT JOIN ThueVAT v ON hd.MaVAT = v.MaVAT " +
	                             "LEFT JOIN KhuyenMai km ON hd.MaKM = km.MaKM " +
	                             "WHERE MONTH(hd.NgayLap) = ? AND YEAR(hd.NgayLap) = ?";

	        ps = conn.prepareStatement(sqlDoanhThu);
	        ps.setInt(1, thang);
	        ps.setInt(2, nam);
	        ps.setInt(3, thang);
	        ps.setInt(4, nam);
	        rs = ps.executeQuery();
	        long tongDoanhThu = 0;
	        if (rs.next()) {
	            tongDoanhThu = rs.getLong("TongDoanhThu");
	        }
	        result.put("TongDoanhThu", tongDoanhThu);

	        // Tổng số hóa đơn
	        String sqlHoaDon = "SELECT COUNT(*) AS SoHoaDon " +
	                           "FROM HoaDon " +
	                           "WHERE MONTH(NgayLap) = ? AND YEAR(NgayLap) = ?";
	        ps = conn.prepareStatement(sqlHoaDon);
	        ps.setInt(1, thang);
	        ps.setInt(2, nam);
	        rs = ps.executeQuery();
	        int soHoaDon = 0;
	        if (rs.next()) {
	            soHoaDon = rs.getInt("SoHoaDon");
	        }
	        result.put("SoHoaDon", soHoaDon);

	        // Tổng số bàn được đặt
	        String sqlBan = "SELECT COUNT(DISTINCT pdb.MaBan) AS SoBan " +
	                        "FROM PhieuDatBan pdb " +
	                        "JOIN ChiTietPhieuDatBan ct ON pdb.MaPDB = ct.MaPDB " +
	                        "WHERE MONTH(ct.TimeNhanBan) = ? AND YEAR(ct.TimeNhanBan) = ?";
	        ps = conn.prepareStatement(sqlBan);
	        ps.setInt(1, thang);
	        ps.setInt(2, nam);
	        rs = ps.executeQuery();
	        int soBan = 0;
	        if (rs.next()) {
	            soBan = rs.getInt("SoBan");
	        }
	        result.put("SoBan", soBan);

	        // Doanh thu trung bình
	        result.put("DoanhThuTrungBinh", soHoaDon == 0 ? 0 : tongDoanhThu / soHoaDon);

	        return result;
	    } finally {
	        closeResources(conn, ps, rs, null);
	    }
	}

	public Map<Integer, Long> getDoanhThuTheoTangThang(int thang, int nam) throws Exception {
	    Map<Integer, Long> result = new HashMap<>();
	    Connection conn = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    try {
	        conn = getConnection();
	        String sql = "WITH HoaDonTong AS (" +
	                     "    SELECT hd.MaHD, SUM(ct.SoLuong * ct.DonGia) AS TongGiaMon " +
	                     "    FROM HoaDon hd " +
	                     "    JOIN ChiTietHoaDon ct ON hd.MaHD = ct.MaHD " +
	                     "    WHERE MONTH(hd.NgayLap) = ? AND YEAR(hd.NgayLap) = ? " +
	                     "    GROUP BY hd.MaHD " +
	                     ") " +
	                     "SELECT b.Tang, SUM(CASE " +
	                     "    WHEN km.MaKM IS NOT NULL AND km.NgayBatDau <= hd.NgayLap AND km.NgayKetThuc >= hd.NgayLap " +
	                     "    THEN (hdm.TongGiaMon * (1 + ISNULL(v.PhanTramThue, 0) / 100) * (1 - ISNULL(km.PhanTramGiamGia, 0) / 100)) " +
	                     "    ELSE (hdm.TongGiaMon * (1 + ISNULL(v.PhanTramThue, 0) / 100)) " +
	                     "    END) AS DoanhThu " +
	                     "FROM HoaDon hd " +
	                     "JOIN HoaDonTong hdm ON hd.MaHD = hdm.MaHD " +
	                     "JOIN PhieuDatBan pdb ON hd.MaKH = pdb.MaKhachHang " +
	                     "JOIN ChiTietPhieuDatBan ctpd ON pdb.MaPDB = ctpd.MaPDB " +
	                     "JOIN Ban b ON pdb.MaBan = b.MaBan " +
	                     "LEFT JOIN ThueVAT v ON hd.MaVAT = v.MaVAT " +
	                     "LEFT JOIN KhuyenMai km ON hd.MaKM = km.MaKM " +
	                     "WHERE MONTH(hd.NgayLap) = ? AND YEAR(hd.NgayLap) = ? " +
	                     "AND hd.NgayLap BETWEEN ctpd.TimeNhanBan AND ctpd.TimeTraBan " +
	                     "GROUP BY b.Tang";
	        ps = conn.prepareStatement(sql);
	        ps.setInt(1, thang);
	        ps.setInt(2, nam);
	        ps.setInt(3, thang);
	        ps.setInt(4, nam);
	        rs = ps.executeQuery();
	        while (rs.next()) {
	            result.put(rs.getInt("Tang"), rs.getLong("DoanhThu"));
	        }
	        for (int tang = 1; tang <= 3; tang++) {
	            result.putIfAbsent(tang, 0L);
	        }
	        return result;
	    } finally {
	        closeResources(conn, ps, rs, null);
	    }
	}

	public List<Object[]> getChiTietHoaDonThang(int thang, int nam) throws Exception {
	    List<Object[]> result = new ArrayList<>();
	    Connection conn = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    try {
	        conn = getConnection();
	        String sql = "WITH HoaDonTong AS (" +
	                     "    SELECT hd.MaHD, SUM(ct.SoLuong * ct.DonGia) AS TongGiaMon " +
	                     "    FROM HoaDon hd " +
	                     "    JOIN ChiTietHoaDon ct ON hd.MaHD = ct.MaHD " +
	                     "    WHERE MONTH(hd.NgayLap) = ? AND YEAR(hd.NgayLap) = ? " +
	                     "    GROUP BY hd.MaHD " +
	                     ") " +
	                     "SELECT hd.MaHD, kh.SDT, kh.TenKH, nv.MaNV, nv.TenNV, " +
	                     "CASE " +
	                     "    WHEN km.MaKM IS NOT NULL AND km.NgayBatDau <= hd.NgayLap AND km.NgayKetThuc >= hd.NgayLap " +
	                     "    THEN (hdm.TongGiaMon * (1 + ISNULL(v.PhanTramThue, 0) / 100) * (1 - ISNULL(km.PhanTramGiamGia, 0) / 100)) " +
	                     "    ELSE (hdm.TongGiaMon * (1 + ISNULL(v.PhanTramThue, 0) / 100)) " +
	                     "END AS TongTien " +
	                     "FROM HoaDon hd " +
	                     "JOIN HoaDonTong hdm ON hd.MaHD = hdm.MaHD " +
	                     "JOIN KhachHang kh ON hd.MaKH = kh.MaKH " +
	                     "JOIN NhanVien nv ON hd.MaNV = nv.MaNV " +
	                     "LEFT JOIN ThueVAT v ON hd.MaVAT = v.MaVAT " +
	                     "LEFT JOIN KhuyenMai km ON hd.MaKM = km.MaKM " +
	                     "WHERE MONTH(hd.NgayLap) = ? AND YEAR(hd.NgayLap) = ?";
	        ps = conn.prepareStatement(sql);
	        ps.setInt(1, thang);
	        ps.setInt(2, nam);
	        ps.setInt(3, thang);
	        ps.setInt(4, nam);
	        rs = ps.executeQuery();
	        while (rs.next()) {
	            result.add(new Object[]{
	                rs.getString("MaHD"),
	                rs.getString("SDT"),
	                rs.getString("TenKH"),
	                rs.getString("MaNV"),
	                rs.getString("TenNV"),
	                String.format("%,d", rs.getLong("TongTien"))
	            });
	        }
	        return result;
	    } finally {
	        closeResources(conn, ps, rs, null);
	    }
	}

	public List<Object[]> getTongHopTheoNgay(int thang, int nam) throws Exception {
	    List<Object[]> result = new ArrayList<>();
	    Connection conn = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    try {
	        conn = getConnection();
	        String sql = "WITH HoaDonTong AS (" +
	                     "    SELECT hd.MaHD, DAY(hd.NgayLap) AS Ngay, SUM(ct.SoLuong * ct.DonGia) AS TongGiaMon " +
	                     "    FROM HoaDon hd " +
	                     "    JOIN ChiTietHoaDon ct ON hd.MaHD = ct.MaHD " +
	                     "    WHERE MONTH(hd.NgayLap) = ? AND YEAR(hd.NgayLap) = ? " +
	                     "    GROUP BY hd.MaHD, DAY(hd.NgayLap) " +
	                     ") " +
	                     "SELECT hdm.Ngay, COUNT(DISTINCT hd.MaHD) AS SoHoaDon, " +
	                     "SUM(CASE " +
	                     "    WHEN km.MaKM IS NOT NULL AND km.NgayBatDau <= hd.NgayLap AND km.NgayKetThuc >= hd.NgayLap " +
	                     "    THEN (hdm.TongGiaMon * (1 + ISNULL(v.PhanTramThue, 0) / 100) * (1 - ISNULL(km.PhanTramGiamGia, 0) / 100)) " +
	                     "    ELSE (hdm.TongGiaMon * (1 + ISNULL(v.PhanTramThue, 0) / 100)) " +
	                     "    END) AS TongTien " +
	                     "FROM HoaDon hd " +
	                     "JOIN HoaDonTong hdm ON hd.MaHD = hdm.MaHD " +
	                     "LEFT JOIN ThueVAT v ON hd.MaVAT = v.MaVAT " +
	                     "LEFT JOIN KhuyenMai km ON hd.MaKM = km.MaKM " +
	                     "WHERE MONTH(hd.NgayLap) = ? AND YEAR(hd.NgayLap) = ? " +
	                     "GROUP BY hdm.Ngay " +
	                     "ORDER BY hdm.Ngay";
	        ps = conn.prepareStatement(sql);
	        ps.setInt(1, thang);
	        ps.setInt(2, nam);
	        ps.setInt(3, thang);
	        ps.setInt(4, nam);
	        rs = ps.executeQuery();
	        while (rs.next()) {
	            result.add(new Object[]{
	                rs.getInt("Ngay"),
	                rs.getInt("SoHoaDon"),
	                String.format("%,d", rs.getLong("TongTien"))
	            });
	        }
	        return result;
	    } finally {
	        closeResources(conn, ps, rs, null);
	    }
	}

	public Map<String, Object> getThongKeTheoNam(int nam) throws Exception {
	    Map<String, Object> result = new HashMap<>();
	    Connection conn = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    try {
	        conn = getConnection();
	        // Tổng doanh thu: Tính tổng giá món trước, sau đó áp dụng VAT và khuyến mãi
	        String sqlDoanhThu = "WITH HoaDonTong AS (" +
	                             "    SELECT hd.MaHD, SUM(ct.SoLuong * ct.DonGia) AS TongGiaMon " +
	                             "    FROM HoaDon hd " +
	                             "    JOIN ChiTietHoaDon ct ON hd.MaHD = ct.MaHD " +
	                             "    WHERE YEAR(hd.NgayLap) = ? " +
	                             "    GROUP BY hd.MaHD " +
	                             ") " +
	                             "SELECT SUM(CASE " +
	                             "    WHEN km.MaKM IS NOT NULL AND km.NgayBatDau <= hd.NgayLap AND km.NgayKetThuc >= hd.NgayLap " +
	                             "    THEN (hdm.TongGiaMon * (1 + ISNULL(v.PhanTramThue, 0) / 100) * (1 - ISNULL(km.PhanTramGiamGia, 0) / 100)) " +
	                             "    ELSE (hdm.TongGiaMon * (1 + ISNULL(v.PhanTramThue, 0) / 100)) " +
	                             "    END) AS TongDoanhThu " +
	                             "FROM HoaDon hd " +
	                             "JOIN HoaDonTong hdm ON hd.MaHD = hdm.MaHD " +
	                             "LEFT JOIN ThueVAT v ON hd.MaVAT = v.MaVAT " +
	                             "LEFT JOIN KhuyenMai km ON hd.MaKM = km.MaKM " +
	                             "WHERE YEAR(hd.NgayLap) = ?";

	        ps = conn.prepareStatement(sqlDoanhThu);
	        ps.setInt(1, nam);
	        ps.setInt(2, nam);
	        rs = ps.executeQuery();
	        long tongDoanhThu = 0;
	        if (rs.next()) {
	            tongDoanhThu = rs.getLong("TongDoanhThu");
	        }
	        result.put("TongDoanhThu", tongDoanhThu);

	        // Tổng số hóa đơn
	        String sqlHoaDon = "SELECT COUNT(*) AS SoHoaDon " +
	                           "FROM HoaDon " +
	                           "WHERE YEAR(NgayLap) = ?";
	        ps = conn.prepareStatement(sqlHoaDon);
	        ps.setInt(1, nam);
	        rs = ps.executeQuery();
	        int soHoaDon = 0;
	        if (rs.next()) {
	            soHoaDon = rs.getInt("SoHoaDon");
	        }
	        result.put("SoHoaDon", soHoaDon);

	        // Tổng số bàn được đặt
	        String sqlBan = "SELECT COUNT(DISTINCT pdb.MaBan) AS SoBan " +
	                        "FROM PhieuDatBan pdb " +
	                        "JOIN ChiTietPhieuDatBan ct ON pdb.MaPDB = ct.MaPDB " +
	                        "WHERE YEAR(ct.TimeNhanBan) = ?";
	        ps = conn.prepareStatement(sqlBan);
	        ps.setInt(1, nam);
	        rs = ps.executeQuery();
	        int soBan = 0;
	        if (rs.next()) {
	            soBan = rs.getInt("SoBan");
	        }
	        result.put("SoBan", soBan);

	        // Doanh thu trung bình
	        result.put("DoanhThuTrungBinh", soHoaDon == 0 ? 0 : tongDoanhThu / soHoaDon);

	        return result;
	    } finally {
	        closeResources(conn, ps, rs, null);
	    }
	}

	public Map<Integer, Long> getDoanhThuTheoTangNam(int nam) throws Exception {
	    Map<Integer, Long> result = new HashMap<>();
	    Connection conn = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    try {
	        conn = getConnection();
	        String sql = "WITH HoaDonTong AS (" +
	                     "    SELECT hd.MaHD, SUM(ct.SoLuong * ct.DonGia) AS TongGiaMon " +
	                     "    FROM HoaDon hd " +
	                     "    JOIN ChiTietHoaDon ct ON hd.MaHD = ct.MaHD " +
	                     "    WHERE YEAR(hd.NgayLap) = ? " +
	                     "    GROUP BY hd.MaHD " +
	                     ") " +
	                     "SELECT b.Tang, SUM(CASE " +
	                     "    WHEN km.MaKM IS NOT NULL AND km.NgayBatDau <= hd.NgayLap AND km.NgayKetThuc >= hd.NgayLap " +
	                     "    THEN (hdm.TongGiaMon * (1 + ISNULL(v.PhanTramThue, 0) / 100) * (1 - ISNULL(km.PhanTramGiamGia, 0) / 100)) " +
	                     "    ELSE (hdm.TongGiaMon * (1 + ISNULL(v.PhanTramThue, 0) / 100)) " +
	                     "    END) AS DoanhThu " +
	                     "FROM HoaDon hd " +
	                     "JOIN HoaDonTong hdm ON hd.MaHD = hdm.MaHD " +
	                     "JOIN PhieuDatBan pdb ON hd.MaKH = pdb.MaKhachHang " +
	                     "JOIN ChiTietPhieuDatBan ctpd ON pdb.MaPDB = ctpd.MaPDB " +
	                     "JOIN Ban b ON pdb.MaBan = b.MaBan " +
	                     "LEFT JOIN ThueVAT v ON hd.MaVAT = v.MaVAT " +
	                     "LEFT JOIN KhuyenMai km ON hd.MaKM = km.MaKM " +
	                     "WHERE YEAR(hd.NgayLap) = ? " +
	                     "AND hd.NgayLap BETWEEN ctpd.TimeNhanBan AND ctpd.TimeTraBan " +
	                     "GROUP BY b.Tang";
	        ps = conn.prepareStatement(sql);
	        ps.setInt(1, nam);
	        ps.setInt(2, nam);
	        rs = ps.executeQuery();
	        while (rs.next()) {
	            result.put(rs.getInt("Tang"), rs.getLong("DoanhThu"));
	        }
	        for (int tang = 1; tang <= 3; tang++) {
	            result.putIfAbsent(tang, 0L);
	        }
	        return result;
	    } finally {
	        closeResources(conn, ps, rs, null);
	    }
	}

	public List<Object[]> getChiTietHoaDonNam(int nam) throws Exception {
	    List<Object[]> result = new ArrayList<>();
	    Connection conn = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    try {
	        conn = getConnection();
	        String sql = "WITH HoaDonTong AS (" +
	                     "    SELECT hd.MaHD, SUM(ct.SoLuong * ct.DonGia) AS TongGiaMon " +
	                     "    FROM HoaDon hd " +
	                     "    JOIN ChiTietHoaDon ct ON hd.MaHD = ct.MaHD " +
	                     "    WHERE YEAR(hd.NgayLap) = ? " +
	                     "    GROUP BY hd.MaHD " +
	                     ") " +
	                     "SELECT hd.MaHD, kh.SDT, kh.TenKH, nv.MaNV, nv.TenNV, " +
	                     "CASE " +
	                     "    WHEN km.MaKM IS NOT NULL AND km.NgayBatDau <= hd.NgayLap AND km.NgayKetThuc >= hd.NgayLap " +
	                     "    THEN (hdm.TongGiaMon * (1 + ISNULL(v.PhanTramThue, 0) / 100) * (1 - ISNULL(km.PhanTramGiamGia, 0) / 100)) " +
	                     "    ELSE (hdm.TongGiaMon * (1 + ISNULL(v.PhanTramThue, 0) / 100)) " +
	                     "END AS TongTien " +
	                     "FROM HoaDon hd " +
	                     "JOIN HoaDonTong hdm ON hd.MaHD = hdm.MaHD " +
	                     "JOIN KhachHang kh ON hd.MaKH = kh.MaKH " +
	                     "JOIN NhanVien nv ON hd.MaNV = nv.MaNV " +
	                     "LEFT JOIN ThueVAT v ON hd.MaVAT = v.MaVAT " +
	                     "LEFT JOIN KhuyenMai km ON hd.MaKM = km.MaKM " +
	                     "WHERE YEAR(hd.NgayLap) = ?";
	        ps = conn.prepareStatement(sql);
	        ps.setInt(1, nam);
	        ps.setInt(2, nam);
	        rs = ps.executeQuery();
	        while (rs.next()) {
	            result.add(new Object[]{
	                rs.getString("MaHD"),
	                rs.getString("SDT"),
	                rs.getString("TenKH"),
	                rs.getString("MaNV"),
	                rs.getString("TenNV"),
	                String.format("%,d", rs.getLong("TongTien"))
	            });
	        }
	        return result;
	    } finally {
	        closeResources(conn, ps, rs, null);
	    }
	}

	public List<Object[]> getTongHopTheoThang(int nam) throws Exception {
	    List<Object[]> result = new ArrayList<>();
	    Connection conn = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    try {
	        conn = getConnection();
	        String sql = "WITH HoaDonTong AS (" +
	                     "    SELECT hd.MaHD, MONTH(hd.NgayLap) AS Thang, SUM(ct.SoLuong * ct.DonGia) AS TongGiaMon " +
	                     "    FROM HoaDon hd " +
	                     "    JOIN ChiTietHoaDon ct ON hd.MaHD = ct.MaHD " +
	                     "    WHERE YEAR(hd.NgayLap) = ? " +
	                     "    GROUP BY hd.MaHD, MONTH(hd.NgayLap) " +
	                     ") " +
	                     "SELECT hdm.Thang, COUNT(DISTINCT hd.MaHD) AS SoHoaDon, " +
	                     "SUM(CASE " +
	                     "    WHEN km.MaKM IS NOT NULL AND km.NgayBatDau <= hd.NgayLap AND km.NgayKetThuc >= hd.NgayLap " +
	                     "    THEN (hdm.TongGiaMon * (1 + ISNULL(v.PhanTramThue, 0) / 100) * (1 - ISNULL(km.PhanTramGiamGia, 0) / 100)) " +
	                     "    ELSE (hdm.TongGiaMon * (1 + ISNULL(v.PhanTramThue, 0) / 100)) " +
	                     "    END) AS TongTien " +
	                     "FROM HoaDon hd " +
	                     "JOIN HoaDonTong hdm ON hd.MaHD = hdm.MaHD " +
	                     "LEFT JOIN ThueVAT v ON hd.MaVAT = v.MaVAT " +
	                     "LEFT JOIN KhuyenMai km ON hd.MaKM = km.MaKM " +
	                     "WHERE YEAR(hd.NgayLap) = ? " +
	                     "GROUP BY hdm.Thang " +
	                     "ORDER BY hdm.Thang";
	        ps = conn.prepareStatement(sql);
	        ps.setInt(1, nam);
	        ps.setInt(2, nam);
	        rs = ps.executeQuery();
	        while (rs.next()) {
	            result.add(new Object[]{
	                rs.getInt("Thang"),
	                rs.getInt("SoHoaDon"),
	                String.format("%,d", rs.getLong("TongTien"))
	            });
	        }
	        return result;
	    } finally {
	        closeResources(conn, ps, rs, null);
	    }
	}
    
	public List<Object[]> getThongKeKhachHangTheoNgay(Date ngay) throws Exception {
	    List<Object[]> result = new ArrayList<>();
	    Connection conn = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    try {
	        conn = getConnection();
	        String sql = "WITH HoaDonTong AS (" +
	                     "    SELECT hd.MaHD, hd.MaKH, SUM(ct.SoLuong * ct.DonGia) AS TongGiaMon " +
	                     "    FROM HoaDon hd " +
	                     "    LEFT JOIN ChiTietHoaDon ct ON hd.MaHD = ct.MaHD " +
	                     "    WHERE CAST(hd.NgayLap AS DATE) = ? " +
	                     "    GROUP BY hd.MaHD, hd.MaKH " +
	                     ") " +
	                     "SELECT k.MaKH, k.TenKH, k.SDT, " +
	                     "COALESCE(SUM(CASE " +
	                     "    WHEN km.MaKM IS NOT NULL AND km.NgayBatDau <= hd.NgayLap AND km.NgayKetThuc >= hd.NgayLap " +
	                     "    THEN (hdm.TongGiaMon * (1 + ISNULL(v.PhanTramThue, 0) / 100) * (1 - ISNULL(km.PhanTramGiamGia, 0) / 100)) " +
	                     "    ELSE (hdm.TongGiaMon * (1 + ISNULL(v.PhanTramThue, 0) / 100)) " +
	                     "    END), 0) AS TongTien " +
	                     "FROM KhachHang k " +
	                     "LEFT JOIN HoaDon hd ON k.MaKH = hd.MaKH " +
	                     "LEFT JOIN HoaDonTong hdm ON hd.MaHD = hdm.MaHD " +
	                     "LEFT JOIN ThueVAT v ON hd.MaVAT = v.MaVAT " +
	                     "LEFT JOIN KhuyenMai km ON hd.MaKM = km.MaKM " +
	                     "WHERE (CAST(hd.NgayLap AS DATE) = ? OR hd.MaHD IS NULL) " +
	                     "GROUP BY k.MaKH, k.TenKH, k.SDT " +
	                     "ORDER BY TongTien DESC";
	        ps = conn.prepareStatement(sql);
	        ps.setDate(1, new java.sql.Date(ngay.getTime()));
	        ps.setDate(2, new java.sql.Date(ngay.getTime()));
	        rs = ps.executeQuery();
	        while (rs.next()) {
	            Object[] row = new Object[4];
	            row[0] = rs.getString("MaKH");
	            row[1] = rs.getString("TenKH");
	            row[2] = rs.getString("SDT");
	            row[3] = rs.getLong("TongTien");
	            result.add(row);
	        }
	    } finally {
	        closeResources(conn, ps, rs, null);
	    }
	    return result;
	}

    public List<Object[]> getThongKeKhachHangTheoThang(int thang, int nam) throws Exception {
        List<Object[]> result = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            String sql = "WITH HoaDonTong AS (" +
                         "    SELECT hd.MaHD, hd.MaKH, SUM(ct.SoLuong * ct.DonGia) AS TongGiaMon " +
                         "    FROM HoaDon hd " +
                         "    LEFT JOIN ChiTietHoaDon ct ON hd.MaHD = ct.MaHD " +
                         "    WHERE MONTH(hd.NgayLap) = ? AND YEAR(hd.NgayLap) = ? " +
                         "    GROUP BY hd.MaHD, hd.MaKH " +
                         ") " +
                         "SELECT k.MaKH, k.TenKH, k.SDT, " +
                         "COALESCE(SUM(CASE " +
                         "    WHEN km.MaKM IS NOT NULL AND km.NgayBatDau <= hd.NgayLap AND km.NgayKetThuc >= hd.NgayLap " +
                         "    THEN (hdm.TongGiaMon * (1 + ISNULL(v.PhanTramThue, 0) / 100) * (1 - ISNULL(km.PhanTramGiamGia, 0) / 100)) " +
                         "    ELSE (hdm.TongGiaMon * (1 + ISNULL(v.PhanTramThue, 0) / 100)) " +
                         "    END), 0) AS TongTien " +
                         "FROM KhachHang k " +
                         "LEFT JOIN HoaDon hd ON k.MaKH = hd.MaKH " +
                         "LEFT JOIN HoaDonTong hdm ON hd.MaHD = hdm.MaHD " +
                         "LEFT JOIN ThueVAT v ON hd.MaVAT = v.MaVAT " +
                         "LEFT JOIN KhuyenMai km ON hd.MaKM = km.MaKM " +
                         "WHERE (MONTH(hd.NgayLap) = ? AND YEAR(hd.NgayLap) = ?) OR hd.MaHD IS NULL " +
                         "GROUP BY k.MaKH, k.TenKH, k.SDT " +
                         "ORDER BY TongTien DESC";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, thang);
            ps.setInt(2, nam);
            ps.setInt(3, thang);
            ps.setInt(4, nam);
            rs = ps.executeQuery();
            while (rs.next()) {
                Object[] row = new Object[4];
                row[0] = rs.getString("MaKH");
                row[1] = rs.getString("TenKH");
                row[2] = rs.getString("SDT");
                row[3] = rs.getLong("TongTien");
                result.add(row);
            }
        } finally {
            closeResources(conn, ps, rs, null);
        }
        return result;
    }

    public List<Object[]> getThongKeKhachHangTheoNam(int nam) throws Exception {
        List<Object[]> result = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            String sql = "WITH HoaDonTong AS (" +
                         "    SELECT hd.MaHD, hd.MaKH, SUM(ct.SoLuong * ct.DonGia) AS TongGiaMon " +
                         "    FROM HoaDon hd " +
                         "    LEFT JOIN ChiTietHoaDon ct ON hd.MaHD = ct.MaHD " +
                         "    WHERE YEAR(hd.NgayLap) = ? " +
                         "    GROUP BY hd.MaHD, hd.MaKH " +
                         ") " +
                         "SELECT k.MaKH, k.TenKH, k.SDT, " +
                         "COALESCE(SUM(CASE " +
                         "    WHEN km.MaKM IS NOT NULL AND km.NgayBatDau <= hd.NgayLap AND km.NgayKetThuc >= hd.NgayLap " +
                         "    THEN (hdm.TongGiaMon * (1 + ISNULL(v.PhanTramThue, 0) / 100) * (1 - ISNULL(km.PhanTramGiamGia, 0) / 100)) " +
                         "    ELSE (hdm.TongGiaMon * (1 + ISNULL(v.PhanTramThue, 0) / 100)) " +
                         "    END), 0) AS TongTien " +
                         "FROM KhachHang k " +
                         "LEFT JOIN HoaDon hd ON k.MaKH = hd.MaKH " +
                         "LEFT JOIN HoaDonTong hdm ON hd.MaHD = hdm.MaHD " +
                         "LEFT JOIN ThueVAT v ON hd.MaVAT = v.MaVAT " +
                         "LEFT JOIN KhuyenMai km ON hd.MaKM = km.MaKM " +
                         "WHERE YEAR(hd.NgayLap) = ? OR hd.MaHD IS NULL " +
                         "GROUP BY k.MaKH, k.TenKH, k.SDT " +
                         "ORDER BY TongTien DESC";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, nam);
            ps.setInt(2, nam);
            rs = ps.executeQuery();
            while (rs.next()) {
                Object[] row = new Object[4];
                row[0] = rs.getString("MaKH");
                row[1] = rs.getString("TenKH");
                row[2] = rs.getString("SDT");
                row[3] = rs.getLong("TongTien");
                result.add(row);
            }
        } finally {
            closeResources(conn, ps, rs, null);
        }
        return result;
    }
    
    protected void closeResources(Connection conn, PreparedStatement ps, ResultSet rs, Statement stmt) {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}