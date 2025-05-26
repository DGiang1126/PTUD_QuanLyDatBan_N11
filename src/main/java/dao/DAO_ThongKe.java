
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
    // Thống kê theo ngày
    public Map<String, Object> getThongKeTheoNgay(Date ngay) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            // Tổng doanh thu
            String sqlDoanhThu = "SELECT SUM(ct.SoLuong * ct.DonGia) AS TongDoanhThu " +
                                 "FROM HoaDon hd " +
                                 "JOIN ChiTietHoaDon ct ON hd.MaHD = ct.MaHD " +
                                 "WHERE CAST(hd.NgayLap AS DATE) = ?";
            ps = conn.prepareStatement(sqlDoanhThu);
            ps.setDate(1, new java.sql.Date(ngay.getTime()));
            rs = ps.executeQuery();
            if (rs.next()) {
                result.put("TongDoanhThu", rs.getLong("TongDoanhThu"));
            } else {
                result.put("TongDoanhThu", 0L);
            }

            // Tổng số hóa đơn
            String sqlHoaDon = "SELECT COUNT(*) AS SoHoaDon " +
                               "FROM HoaDon " +
                               "WHERE CAST(NgayLap AS DATE) = ?";
            ps = conn.prepareStatement(sqlHoaDon);
            ps.setDate(1, new java.sql.Date(ngay.getTime()));
            rs = ps.executeQuery();
            if (rs.next()) {
                result.put("SoHoaDon", rs.getInt("SoHoaDon"));
            } else {
                result.put("SoHoaDon", 0);
            }

            // Tổng số bàn được đặt
            String sqlBan = "SELECT COUNT(DISTINCT pdb.MaBan) AS SoBan " +
                            "FROM PhieuDatBan pdb " +
                            "JOIN ChiTietPhieuDatBan ct ON pdb.MaPDB = ct.MaPDB " +
                            "WHERE CAST(ct.TimeNhanBan AS DATE) = ?";
            ps = conn.prepareStatement(sqlBan);
            ps.setDate(1, new java.sql.Date(ngay.getTime()));
            rs = ps.executeQuery();
            if (rs.next()) {
                result.put("SoBan", rs.getInt("SoBan"));
            } else {
                result.put("SoBan", 0);
            }

            // Doanh thu trung bình
            long tongDoanhThu = (Long) result.get("TongDoanhThu");
            int soHoaDon = (Integer) result.get("SoHoaDon");
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
            String sql = "SELECT b.Tang, SUM(ct.SoLuong * ct.DonGia) AS DoanhThu " +
                         "FROM HoaDon hd " +
                         "JOIN ChiTietHoaDon ct ON hd.MaHD = ct.MaHD " +
                         "JOIN PhieuDatBan pdb ON hd.MaKH = pdb.MaKhachHang " +
                         "JOIN ChiTietPhieuDatBan ctpd ON pdb.MaPDB = ctpd.MaPDB " +
                         "JOIN Ban b ON pdb.MaBan = b.MaBan " +
                         "WHERE CAST(hd.NgayLap AS DATE) = ? " +
                         "AND hd.NgayLap BETWEEN ctpd.TimeNhanBan AND ctpd.TimeTraBan " +
                         "GROUP BY b.Tang";
            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(ngay.getTime()));
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
            String sql = "SELECT hd.MaHD, kh.SDT, kh.TenKH, nv.MaNV, nv.TenNV, SUM(ct.SoLuong * ct.DonGia) AS TongTien " +
                         "FROM HoaDon hd " +
                         "JOIN KhachHang kh ON hd.MaKH = kh.MaKH " +
                         "JOIN NhanVien nv ON hd.MaNV = nv.MaNV " +
                         "JOIN ChiTietHoaDon ct ON hd.MaHD = ct.MaHD " +
                         "WHERE CAST(hd.NgayLap AS DATE) = ? " +
                         "GROUP BY hd.MaHD, kh.SDT, kh.TenKH, nv.MaNV, nv.TenNV";
            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(ngay.getTime()));
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

    // Thống kê theo tháng
    public Map<String, Object> getThongKeTheoThang(int thang, int nam) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            // Tổng doanh thu
            String sqlDoanhThu = "SELECT SUM(ct.SoLuong * ct.DonGia) AS TongDoanhThu " +
                                 "FROM HoaDon hd " +
                                 "JOIN ChiTietHoaDon ct ON hd.MaHD = ct.MaHD " +
                                 "WHERE MONTH(hd.NgayLap) = ? AND YEAR(hd.NgayLap) = ?";
            ps = conn.prepareStatement(sqlDoanhThu);
            ps.setInt(1, thang);
            ps.setInt(2, nam);
            rs = ps.executeQuery();
            if (rs.next()) {
                result.put("TongDoanhThu", rs.getLong("TongDoanhThu"));
            } else {
                result.put("TongDoanhThu", 0L);
            }

            // Tổng số hóa đơn
            String sqlHoaDon = "SELECT COUNT(*) AS SoHoaDon " +
                               "FROM HoaDon " +
                               "WHERE MONTH(NgayLap) = ? AND YEAR(NgayLap) = ?";
            ps = conn.prepareStatement(sqlHoaDon);
            ps.setInt(1, thang);
            ps.setInt(2, nam);
            rs = ps.executeQuery();
            if (rs.next()) {
                result.put("SoHoaDon", rs.getInt("SoHoaDon"));
            } else {
                result.put("SoHoaDon", 0);
            }

            // Tổng số bàn được đặt
            String sqlBan = "SELECT COUNT(DISTINCT pdb.MaBan) AS SoBan " +
                            "FROM PhieuDatBan pdb " +
                            "JOIN ChiTietPhieuDatBan ct ON pdb.MaPDB = ct.MaPDB " +
                            "WHERE MONTH(ct.TimeNhanBan) = ? AND YEAR(ct.TimeNhanBan) = ?";
            ps = conn.prepareStatement(sqlBan);
            ps.setInt(1, thang);
            ps.setInt(2, nam);
            rs = ps.executeQuery();
            if (rs.next()) {
                result.put("SoBan", rs.getInt("SoBan"));
            } else {
                result.put("SoBan", 0);
            }

            // Doanh thu trung bình
            long tongDoanhThu = (Long) result.get("TongDoanhThu");
            int soHoaDon = (Integer) result.get("SoHoaDon");
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
            String sql = "SELECT b.Tang, SUM(ct.SoLuong * ct.DonGia) AS DoanhThu " +
                         "FROM HoaDon hd " +
                         "JOIN ChiTietHoaDon ct ON hd.MaHD = ct.MaHD " +
                         "JOIN PhieuDatBan pdb ON hd.MaKH = pdb.MaKhachHang " +
                         "JOIN ChiTietPhieuDatBan ctpd ON pdb.MaPDB = ctpd.MaPDB " +
                         "JOIN Ban b ON pdb.MaBan = b.MaBan " +
                         "WHERE MONTH(hd.NgayLap) = ? AND YEAR(hd.NgayLap) = ? " +
                         "AND hd.NgayLap BETWEEN ctpd.TimeNhanBan AND ctpd.TimeTraBan " +
                         "GROUP BY b.Tang";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, thang);
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

    public List<Object[]> getChiTietHoaDonThang(int thang, int nam) throws Exception {
        List<Object[]> result = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            String sql = "SELECT hd.MaHD, kh.SDT, kh.TenKH, nv.MaNV, nv.TenNV, SUM(ct.SoLuong * ct.DonGia) AS TongTien " +
                         "FROM HoaDon hd " +
                         "JOIN KhachHang kh ON hd.MaKH = kh.MaKH " +
                         "JOIN NhanVien nv ON hd.MaNV = nv.MaNV " +
                         "JOIN ChiTietHoaDon ct ON hd.MaHD = ct.MaHD " +
                         "WHERE MONTH(hd.NgayLap) = ? AND YEAR(hd.NgayLap) = ? " +
                         "GROUP BY hd.MaHD, kh.SDT, kh.TenKH, nv.MaNV, nv.TenNV";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, thang);
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

    public List<Object[]> getTongHopTheoNgay(int thang, int nam) throws Exception {
        List<Object[]> result = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            String sql = "SELECT DAY(hd.NgayLap) AS Ngay, COUNT(DISTINCT hd.MaHD) AS SoHoaDon, SUM(ct.SoLuong * ct.DonGia) AS TongTien " +
                         "FROM HoaDon hd " +
                         "JOIN ChiTietHoaDon ct ON hd.MaHD = ct.MaHD " +
                         "WHERE MONTH(hd.NgayLap) = ? AND YEAR(hd.NgayLap) = ? " +
                         "GROUP BY DAY(hd.NgayLap) " +
                         "ORDER BY DAY(hd.NgayLap)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, thang);
            ps.setInt(2, nam);
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

    // Thống kê theo năm
    public Map<String, Object> getThongKeTheoNam(int nam) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            // Tổng doanh thu
            String sqlDoanhThu = "SELECT SUM(ct.SoLuong * ct.DonGia) AS TongDoanhThu " +
                                 "FROM HoaDon hd " +
                                 "JOIN ChiTietHoaDon ct ON hd.MaHD = ct.MaHD " +
                                 "WHERE YEAR(hd.NgayLap) = ?";
            ps = conn.prepareStatement(sqlDoanhThu);
            ps.setInt(1, nam);
            rs = ps.executeQuery();
            if (rs.next()) {
                result.put("TongDoanhThu", rs.getLong("TongDoanhThu"));
            } else {
                result.put("TongDoanhThu", 0L);
            }

            // Tổng số hóa đơn
            String sqlHoaDon = "SELECT COUNT(*) AS SoHoaDon " +
                               "FROM HoaDon " +
                               "WHERE YEAR(NgayLap) = ?";
            ps = conn.prepareStatement(sqlHoaDon);
            ps.setInt(1, nam);
            rs = ps.executeQuery();
            if (rs.next()) {
                result.put("SoHoaDon", rs.getInt("SoHoaDon"));
            } else {
                result.put("SoHoaDon", 0);
            }

            // Tổng số bàn được đặt
            String sqlBan = "SELECT COUNT(DISTINCT pdb.MaBan) AS SoBan " +
                            "FROM PhieuDatBan pdb " +
                            "JOIN ChiTietPhieuDatBan ct ON pdb.MaPDB = ct.MaPDB " +
                            "WHERE YEAR(ct.TimeNhanBan) = ?";
            ps = conn.prepareStatement(sqlBan);
            ps.setInt(1, nam);
            rs = ps.executeQuery();
            if (rs.next()) {
                result.put("SoBan", rs.getInt("SoBan"));
            } else {
                result.put("SoBan", 0);
            }

            // Doanh thu trung bình
            long tongDoanhThu = (Long) result.get("TongDoanhThu");
            int soHoaDon = (Integer) result.get("SoHoaDon");
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
            String sql = "SELECT b.Tang, SUM(ct.SoLuong * ct.DonGia) AS DoanhThu " +
                         "FROM HoaDon hd " +
                         "JOIN ChiTietHoaDon ct ON hd.MaHD = ct.MaHD " +
                         "JOIN PhieuDatBan pdb ON hd.MaKH = pdb.MaKhachHang " +
                         "JOIN ChiTietPhieuDatBan ctpd ON pdb.MaPDB = ctpd.MaPDB " +
                         "JOIN Ban b ON pdb.MaBan = b.MaBan " +
                         "WHERE YEAR(hd.NgayLap) = ? " +
                         "AND hd.NgayLap BETWEEN ctpd.TimeNhanBan AND ctpd.TimeTraBan " +
                         "GROUP BY b.Tang";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, nam);
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
            String sql = "SELECT hd.MaHD, kh.SDT, kh.TenKH, nv.MaNV, nv.TenNV, SUM(ct.SoLuong * ct.DonGia) AS TongTien " +
                         "FROM HoaDon hd " +
                         "JOIN KhachHang kh ON hd.MaKH = kh.MaKH " +
                         "JOIN NhanVien nv ON hd.MaNV = nv.MaNV " +
                         "JOIN ChiTietHoaDon ct ON hd.MaHD = ct.MaHD " +
                         "WHERE YEAR(hd.NgayLap) = ? " +
                         "GROUP BY hd.MaHD, kh.SDT, kh.TenKH, nv.MaNV, nv.TenNV";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, nam);
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
            String sql = "SELECT MONTH(hd.NgayLap) AS Thang, COUNT(DISTINCT hd.MaHD) AS SoHoaDon, SUM(ct.SoLuong * ct.DonGia) AS TongTien " +
                         "FROM HoaDon hd " +
                         "JOIN ChiTietHoaDon ct ON hd.MaHD = ct.MaHD " +
                         "WHERE YEAR(hd.NgayLap) = ? " +
                         "GROUP BY MONTH(hd.NgayLap) " +
                         "ORDER BY MONTH(hd.NgayLap)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, nam);
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
            String sql = "SELECT k.MaKH, k.TenKH, k.SDT, COALESCE(SUM(ct.SoLuong * ct.DonGia), 0) as TongTien " +
                         "FROM KhachHang k " +
                         "LEFT JOIN HoaDon h ON k.MaKH = h.MaKH " +
                         "LEFT JOIN ChiTietHoaDon ct ON h.MaHD = ct.MaHD " +
                         "WHERE CAST(h.NgayLap AS DATE) = ? " +
                         "GROUP BY k.MaKH, k.TenKH, k.SDT " +
                         "ORDER BY TongTien DESC";
            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(ngay.getTime()));
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

    // Thống kê khách hàng theo tháng
    public List<Object[]> getThongKeKhachHangTheoThang(int thang, int nam) throws Exception {
        List<Object[]> result = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            String sql = "SELECT k.MaKH, k.TenKH, k.SDT, COALESCE(SUM(ct.SoLuong * ct.DonGia), 0) as TongTien " +
                         "FROM KhachHang k " +
                         "LEFT JOIN HoaDon h ON k.MaKH = h.MaKH " +
                         "LEFT JOIN ChiTietHoaDon ct ON h.MaHD = ct.MaHD " +
                         "WHERE MONTH(h.NgayLap) = ? AND YEAR(h.NgayLap) = ? " +
                         "GROUP BY k.MaKH, k.TenKH, k.SDT " +
                         "ORDER BY TongTien DESC";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, thang);
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

    // Thống kê khách hàng theo năm
    public List<Object[]> getThongKeKhachHangTheoNam(int nam) throws Exception {
        List<Object[]> result = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            String sql = "SELECT k.MaKH, k.TenKH, k.SDT, COALESCE(SUM(ct.SoLuong * ct.DonGia), 0) as TongTien " +
                         "FROM KhachHang k " +
                         "LEFT JOIN HoaDon h ON k.MaKH = h.MaKH " +
                         "LEFT JOIN ChiTietHoaDon ct ON h.MaHD = ct.MaHD " +
                         "WHERE YEAR(h.NgayLap) = ? " +
                         "GROUP BY k.MaKH, k.TenKH, k.SDT " +
                         "ORDER BY TongTien DESC";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, nam);
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