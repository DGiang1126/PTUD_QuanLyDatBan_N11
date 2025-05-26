package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connectDatabase.BaseDAO;
import entity.Ban;

public class DAO_Ban extends BaseDAO {
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	List<Ban> list = new ArrayList<Ban>();

	public List<Ban> getAllBansinTang1() {
		String sql = "select * from Ban where Tang = 1";
		try {
			conn = new BaseDAO().getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				list.add(new Ban(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getString(5),
						rs.getString(6)));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return list;
	}
	public List<Ban> getAllBans() {
		String sql = "select * from Ban";
		try {
			conn = new BaseDAO().getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				list.add(new Ban(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getString(5),
						rs.getString(6)));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return list;
	}

	public List<Ban> getAllBansinTang2() {
		String sql = "select * from Ban where Tang = 2";
		try {
			conn = new BaseDAO().getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				list.add(new Ban(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getString(5),
						rs.getString(6)));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return list;
	}
	public List<Ban> getAllBansinTang3() {
		String sql = "select * from Ban where Tang = 3";
		try {
			conn = new BaseDAO().getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				list.add(new Ban(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getString(5),
						rs.getString(6)));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return list;
	}
	// Phương thức cập nhật trạng thái bàn
	public boolean capNhatTrangThaiBan(String tenBan, String trangThai) {
		// Kiểm tra trạng thái hợp lệ
		if (!isValidTrangThai(trangThai)) {
			System.out.println("Trạng thái không hợp lệ!");
			return false;
		}

		// Lấy maBan từ tenBan
		String maBan = getMaBanByTenBan(tenBan);
		if (maBan == null) {
			System.out.println("Số bàn không tồn tại!");
			return false;
		}

		String sqlUpdate = "UPDATE Ban SET TrangThai = ? WHERE MaBan = ?";
		boolean result = false;

		try {
			conn = new BaseDAO().getConnection();
			ps = conn.prepareStatement(sqlUpdate);
			ps.setString(1, trangThai);
			ps.setString(2, maBan);

			int rowsAffected = ps.executeUpdate();
			result = rowsAffected > 0;
			if (result) {
				System.out.println("Cập nhật trạng thái bàn thành công!");
			} else {
				System.out.println("Cập nhật trạng thái bàn thất bại!");
			}
		} catch (Exception e) {
			System.out.println("Lỗi khi cập nhật trạng thái bàn: " + e.getMessage());
		} finally {
			closeResources();
		}

		return result;
	}

	// Phương thức kiểm tra trạng thái hợp lệ
	private boolean isValidTrangThai(String trangThai) {
		return trangThai != null && (trangThai.equals("CÒN TRỐNG") || trangThai.equals("ĐÃ ĐẶT")
				|| trangThai.equals("ĐANG SỬ DỤNG") || trangThai.equals("BẢO TRÌ"));
	}

	public String getMaBanByTenBan(String tenBan) {
		String sql = "SELECT MaBan FROM Ban WHERE TenBan = ?";
		String maBan = null;
		try {
			conn = new BaseDAO().getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, tenBan);
			rs = ps.executeQuery();
			if (rs.next()) {
				maBan = rs.getString("MaBan");
			}
		} catch (Exception e) {
			System.out.println("Lỗi khi lấy maBan từ tenBan: " + e.getMessage());
		} finally {
			closeResources();
		}
		return maBan;
	}

	public Ban getBanbyTenBan(String tenBan) {
		String sql = "SELECT * FROM Ban WHERE TenBan = ?";
		try {
			conn = new BaseDAO().getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, tenBan);
			rs = ps.executeQuery();
			if (rs.next()) {
				return new Ban(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getString(5),
						rs.getString(6));
			}
		} catch (Exception e) {
			System.out.println("Lỗi khi lấy maBan từ tenBan: " + e.getMessage());
		} finally {
			closeResources();
		}
		return null;
	}

	public Ban getBanbyMaBan(String maBan) {
		String sql = "SELECT * FROM Ban WHERE MaBan = ?";
		try {
			conn = new BaseDAO().getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, maBan);
			rs = ps.executeQuery();
			if (rs.next()) {
				return new Ban(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getString(5),
						rs.getString(6));
			}
		} catch (Exception e) {
			System.out.println("Lỗi khi lấy thông tin bàn từ MaBan: " + e.getMessage());
		} finally {
			closeResources();
		}
		return null;
	}

	private void closeResources() {
		try {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
			if (conn != null)
				conn.close();
		} catch (Exception e) {
			System.out.println("Lỗi khi đóng tài nguyên: " + e.getMessage());
		}
	}

	public List<Ban> getBanTrong() {
		List<Ban> banTrongList = new ArrayList<>();
		String sql = "SELECT * FROM Ban WHERE TrangThai = ?";
		try (Connection conn = new BaseDAO().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, "CÒN TRỐNG"); // 👈 Gán tham số

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Ban ban = new Ban();
					ban.setMaBan(rs.getString("MaBan"));
					ban.setTenBan(rs.getString("TenBan"));
					ban.setTrangThai(rs.getString("TrangThai"));
					banTrongList.add(ban);
					System.out.println("Bàn trống tầng 1: " + ban.getTenBan() + " - Trạng thái: " + ban.getTrangThai());
				}
			}

			if (banTrongList.isEmpty()) {
				System.out.println("Không tìm thấy bàn trống ở tầng 1!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return banTrongList;
	}
}