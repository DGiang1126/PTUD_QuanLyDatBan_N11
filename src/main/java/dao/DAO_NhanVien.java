package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connectDatabase.BaseDAO;
import entity.NhanVien;

public class DAO_NhanVien extends BaseDAO {

    // Get NhanVien by ID
    public NhanVien getNVbyID(String idNV) throws Exception {
        String sql = "SELECT * FROM NhanVien WHERE MaNV = ?";
        try (Connection conn = new BaseDAO().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idNV);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new NhanVien(
                        rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getInt(6), rs.getDouble(7), rs.getDouble(8),
                        rs.getString(9), rs.getString(10)
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<String> getAllMaNhanVien() throws Exception {
        List<String> maNhanVienList = new ArrayList<>();
        String sql = "SELECT MaNV FROM NhanVien";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                maNhanVienList.add(rs.getString("MaNV"));
            }
        }
        return maNhanVienList;
    }

    // Get all NhanVien
    public List<NhanVien> getAllNhanVien() throws Exception {
        List<NhanVien> list = new ArrayList<>();
        String sql = "{call GetAllNhanVien()}";
        try (Connection conn = new BaseDAO().getConnection();
             CallableStatement cs = conn.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setMaNV(rs.getString("MaNV"));
                nv.setTenNV(rs.getString("TenNV"));
                nv.setSdt(rs.getString("SDT"));
                nv.setGioiTinh(rs.getString("GioiTinh"));
                nv.setChucVu(rs.getString("ChucVu"));
                nv.setTuoi(rs.getInt("Tuoi"));
                nv.setHeSoLuong(rs.getDouble("Hesoluong"));
                nv.setLuongNV(rs.getDouble("LuongNV"));
                nv.setLinkIMG(rs.getString("LinkIMG"));
                nv.setEmail(rs.getString("Email"));
                list.add(nv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Search for NhanVien based on a keyword
    public List<NhanVien> searchNhanVien(String keyword) throws Exception {
        List<NhanVien> list = new ArrayList<>();
        String sql = "{call SearchNhanVien(?)}";
        try (Connection conn = new BaseDAO().getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, keyword);
            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    NhanVien nv = new NhanVien();
                    nv.setMaNV(rs.getString("MaNV"));
                    nv.setTenNV(rs.getString("TenNV"));
                    nv.setSdt(rs.getString("SDT"));
                    nv.setGioiTinh(rs.getString("GioiTinh"));
                    nv.setChucVu(rs.getString("ChucVu"));
                    nv.setTuoi(rs.getInt("Tuoi"));
                    nv.setHeSoLuong(rs.getDouble("Hesoluong"));
                    nv.setLuongNV(rs.getDouble("LuongNV"));
                    nv.setLinkIMG(rs.getString("LinkIMG"));
                    nv.setEmail(rs.getString("Email"));
                    list.add(nv);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Validate NhanVien data
    private String validateNhanVienData(NhanVien nv) {
        if (!nv.getMaNV().matches("NV\\d{3}")) {
            return "Lỗi: Mã NV phải có định dạng NVXXX (XXX là 3 số).";
        }
        if (!nv.getTenNV().matches("^[A-Z].*$")) {
            return "Lỗi: Tên NV phải bắt đầu bằng chữ hoa.";
        }
        if (!nv.getSdt().matches("\\d{10,11}")) {
            return "Lỗi: SĐT phải có 10 hoặc 11 số.";
        }
        if (!nv.getGioiTinh().equals("Nam") && !nv.getGioiTinh().equals("Nữ")) {
            return "Lỗi: Giới tính phải là 'Nam' hoặc 'Nữ'.";
        }
        if (!nv.getChucVu().equals("Nhân viên lễ tân") && !nv.getChucVu().equals("Nhân viên quản lý")) {
            return "Lỗi: Chức vụ phải là 'Nhân viên lễ tân' hoặc 'Nhân viên quản lý'.";
        }
        if (nv.getTuoi() < 18) {
            return "Lỗi: Tuổi phải trên 18.";
        }
        if (!nv.getEmail().matches(".*@gmail\\.com")) {
            return "Lỗi: Email phải có định dạng xxx@gmail.com.";
        }
        return null;
    }

    // Insert NhanVien
    public String insertNhanVien(NhanVien nv) throws Exception {
        String sql = "{call InsertNhanVien(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        String validationError = validateNhanVienData(nv);
        if (validationError != null) {
            return validationError;
        }
        try (Connection conn = new BaseDAO().getConnection(); CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, nv.getMaNV());
            cs.setString(2, nv.getTenNV());
            cs.setString(3, nv.getSdt());
            cs.setString(4, nv.getGioiTinh());
            cs.setString(5, nv.getChucVu());
            cs.setInt(6, nv.getTuoi());
            cs.setDouble(7, nv.getHeSoLuong());
            cs.setDouble(8, nv.getLuongNV());
            cs.setString(9, nv.getLinkIMG());
            cs.setString(10, nv.getEmail());
            cs.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return "Lỗi SQL: " + e.getMessage();
        }
        return null;
    }

    // Update NhanVien
    public String updateNhanVien(NhanVien nv) throws Exception {
        String sql = "{call UpdateNhanVien(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        String validationError = validateNhanVienData(nv);
        if (validationError != null) {
            return validationError;
        }
        try (Connection conn = new BaseDAO().getConnection(); CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, nv.getTenNV());
            cs.setString(2, nv.getSdt());
            cs.setString(3, nv.getGioiTinh());
            cs.setString(4, nv.getChucVu());
            cs.setInt(5, nv.getTuoi());
            cs.setDouble(6, nv.getHeSoLuong());
            cs.setDouble(7, nv.getLuongNV());
            cs.setString(8, nv.getLinkIMG());
            cs.setString(9, nv.getEmail());
            cs.setString(10, nv.getMaNV());
            cs.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return "Lỗi SQL: " + e.getMessage();
        }
        return null;
    }

    // Delete NhanVien
    public void deleteNhanVien(String maNV) throws Exception {
        String sql = "{call DeleteNhanVien(?)}";
        try (Connection conn = new BaseDAO().getConnection(); CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, maNV);
            cs.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}