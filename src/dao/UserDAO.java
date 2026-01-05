package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import util.DBConnection;

public class UserDAO {
    public static String validateUser(String email, String password){
        String sql = "SELECT role FROM users WHERE email = ? AND password = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)){
            
            stmt.setString(1, email);
            stmt.setString(2, password);

            java.sql.ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString("role");
            } else {
                return "invalid";
            }

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return "error";
        }
        
    }
}
