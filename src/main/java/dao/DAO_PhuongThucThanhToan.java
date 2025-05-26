package dao;

import connectDatabase.BaseDAO;
import entity.PhuongThucThanhToan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAO_PhuongThucThanhToan extends BaseDAO {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    public PhuongThucThanhToan getPTTTByMaPTTT(String maPTTT) {
        PhuongThucThanhToan pttt = null;
        String query = "SELECT * FROM PhuongThucThanhToan WHERE MaPTTT = ?";
        try (Connection conn = new BaseDAO().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, maPTTT);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    pttt = new PhuongThucThanhToan();
                    pttt.setMaPTTT(rs.getString("MaPTTT"));
                    pttt.setPhuongThuc(rs.getString("PhuongThuc"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        return pttt;
    }
}