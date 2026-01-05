package app;

import util.DBConnection;
import java.sql.Connection;

public class DBTest {
    public static void main(String[] args) {
        Connection conn = DBConnection.getConnection();

        if (conn != null) {
            System.out.println("MySQL Connected Successfully");
        } else {
            System.out.println("Connection Failed");
        }
    }
}