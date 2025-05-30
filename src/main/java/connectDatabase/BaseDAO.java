package connectDatabase;

import java.sql.Connection;
import java.sql.DriverManager;

public class BaseDAO {
    public Connection getConnection() throws Exception {
        String url = "jdbc:sqlserver://DESKTOP-FH57LVM:1433;databaseName=QuanLyDatBan;encrypt=false;trustServerCertificate=true"; 
        String user = "sa";
        String password = "12";
        return DriverManager.getConnection(url, user, password);
    }

    public static void main(String[] args) {
        BaseDAO baseDAO = new BaseDAO();
        try {
            Connection conn = baseDAO.getConnection();
            if (conn != null) {
                System.out.println("Kết nối SQL thành công!");
                conn.close(); // Đóng kết nối khi kiểm tra xong
            }
        } catch (Exception e) {
            System.out.println("Kết nối SQL thất bại. Lỗi: " + e.getMessage());
        }
    }
}
