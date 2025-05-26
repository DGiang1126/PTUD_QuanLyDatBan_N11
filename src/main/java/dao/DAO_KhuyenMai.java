package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import connectDatabase.BaseDAO;
import entity.KhuyenMai;

public class DAO_KhuyenMai extends BaseDAO {
    List<KhuyenMai> list = new ArrayList<KhuyenMai>();

    public List<KhuyenMai> getAllKMs() throws SQLException {
        list.clear();
        String sql = "SELECT * FROM KhuyenMai";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            LocalDateTime now = LocalDateTime.now();
            while (rs.next()) {
                LocalDateTime ngayKetThuc = rs.getTimestamp("Ngayketthuc") != null ?
                    rs.getTimestamp("Ngayketthuc").toLocalDateTime() : null;
                LocalDateTime ngayBatDau = rs.getTimestamp("Ngaybatdau") != null ?
                    rs.getTimestamp("Ngaybatdau").toLocalDateTime() : null;

                // Only include promotions that are still active (NgayKetThuc is null or after now)
                if (ngayKetThuc == null || ngayKetThuc.isAfter(now)) {
                    list.add(new KhuyenMai(
                        rs.getString("MaKM"),
                        rs.getString("DieuKien"),
                        ngayKetThuc,
                        ngayBatDau,
                        rs.getDouble("PhanTramGiamGia")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy danh sách khuyến mãi: " + e.getMessage());
            throw e;
        } catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        return list;
    }
    private boolean isValidMaKM(String maKM) {
        if (maKM == null || !maKM.matches("^KM\\d{3}$")) {
            JOptionPane.showMessageDialog(null, 
                "Invalid MaKM format: " + maKM + ". Mã Khuyến mãi phải có cấu trúc KMXXX (X là số).",
                "Lỗi định dạng", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public boolean addPromotion(KhuyenMai promotion) {
        if (promotion == null || !isValidMaKM(promotion.getMaKM()) || 
            promotion.getPhanTramGiamGia() < 0 || promotion.getPhanTramGiamGia() > 100) {
            return false;
        }
        String sql = "{CALL sp_AddPromotion(?, ?, ?, ?, ?)}";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, promotion.getMaKM());
            ps.setString(2, promotion.getDieuKien());
            ps.setTimestamp(3, promotion.getNgayBatDau() != null ? Timestamp.valueOf(promotion.getNgayBatDau()) : null);
            ps.setTimestamp(4, promotion.getngayKetThuc() != null ? Timestamp.valueOf(promotion.getngayKetThuc()) : null);
            ps.setDouble(5, promotion.getPhanTramGiamGia());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "Lỗi khi thêm khuyến mãi: " + e.getMessage(),
                "Lỗi cơ sở dữ liệu",
                JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return false;
    }

    public boolean updatePromotion(KhuyenMai promotion) {
        if (promotion == null || !isValidMaKM(promotion.getMaKM()) || 
            promotion.getPhanTramGiamGia() < 0 || promotion.getPhanTramGiamGia() > 100) {
            return false;
        }
        String sql = "{CALL sp_UpdatePromotion(?, ?, ?, ?, ?)}";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, promotion.getMaKM());
            ps.setString(2, promotion.getDieuKien());
            ps.setTimestamp(3, promotion.getNgayBatDau() != null ? Timestamp.valueOf(promotion.getNgayBatDau()) : null);
            ps.setTimestamp(4, promotion.getngayKetThuc() != null ? Timestamp.valueOf(promotion.getngayKetThuc()) : null);
            ps.setDouble(5, promotion.getPhanTramGiamGia());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "Lỗi khi cập nhật khuyến mãi: " + e.getMessage(),
                "Lỗi cơ sở dữ liệu",
                JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return false;
    }

    public boolean deletePromotion(String promotionId) {
        if (!isValidMaKM(promotionId)) {
            return false;
        }
        // Check if the promotion is referenced in HoaDon
        String checkSql = "SELECT COUNT(*) FROM HoaDon WHERE MaKM = ?";
        try (Connection conn = getConnection();
             PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
            checkPs.setString(1, promotionId);
            try (ResultSet rs = checkPs.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(null,
                        "Không thể xóa khuyến mãi " + promotionId + " vì đang được sử dụng trong hóa đơn.",
                        "Lỗi xóa khuyến mãi",
                        JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "Lỗi khi kiểm tra khuyến mãi: " + e.getMessage(),
                "Lỗi cơ sở dữ liệu",
                JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

        // Proceed with deletion if no references are found
        String deleteSql = "{CALL sp_DeletePromotion(?)}";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(deleteSql)) {
            ps.setString(1, promotionId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "Lỗi khi xóa khuyến mãi: " + e.getMessage(),
                "Lỗi cơ sở dữ liệu",
                JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return false;
    }

    public List<KhuyenMai> getAllPromotions() {
        List<KhuyenMai> promotions = new ArrayList<>();
        String sql = "{CALL sp_GetAllPromotions}";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                LocalDateTime ngayBatDau = rs.getTimestamp("NgayBatDau") != null ?
                    rs.getTimestamp("NgayBatDau").toLocalDateTime() : null;
                LocalDateTime ngayKetThuc = rs.getTimestamp("NgayKetThuc") != null ?
                    rs.getTimestamp("NgayKetThuc").toLocalDateTime() : null;
                KhuyenMai promotion = new KhuyenMai();
                promotion.setMaKM(rs.getString("MaKM"));
                promotion.setDieuKien(rs.getString("DieuKien"));
                promotion.setNgayBatDau(ngayBatDau);
                promotion.setngayKetThuc(ngayKetThuc);
                promotion.setPhanTramGiamGia(rs.getDouble("PhanTramGiamGia"));
                promotions.add(promotion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        return promotions;
    }

    public KhuyenMai getPromotionById(String promotionId) {
        if (!isValidMaKM(promotionId)) {
            return null;
        }
        String sql = "{CALL sp_GetPromotionById(?)}";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, promotionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LocalDateTime ngayBatDau = rs.getTimestamp("NgayBatDau") != null ?
                        rs.getTimestamp("NgayBatDau").toLocalDateTime() : null;
                    LocalDateTime ngayKetThuc = rs.getTimestamp("NgayKetThuc") != null ?
                        rs.getTimestamp("NgayKetThuc").toLocalDateTime() : null;
                    KhuyenMai promotion = new KhuyenMai();
                    promotion.setMaKM(rs.getString("MaKM"));
                    promotion.setDieuKien(rs.getString("DieuKien"));
                    promotion.setNgayBatDau(ngayBatDau);
                    promotion.setngayKetThuc(ngayKetThuc);
                    promotion.setPhanTramGiamGia(rs.getDouble("PhanTramGiamGia"));
                    return promotion;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        return null;
    }
}