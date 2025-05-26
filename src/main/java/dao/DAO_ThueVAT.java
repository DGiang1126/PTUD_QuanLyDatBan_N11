package dao;

import connectDatabase.BaseDAO;
import entity.ThueVAT;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAO_ThueVAT extends BaseDAO {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    public ThueVAT getThueVATByMaVAT(String maVAT) {
        ThueVAT thueVAT = null;
        String query = "SELECT * FROM ThueVAT WHERE MaVAT = ?";
        try (Connection conn = new BaseDAO().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, maVAT);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    thueVAT = new ThueVAT();
                    thueVAT.setMaVAT(rs.getString("MaVAT"));
                    thueVAT.setPhanTramThue(rs.getDouble("PhanTramThue"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        return thueVAT;
    }
}