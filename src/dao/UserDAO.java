package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.*;
import util.DBConnection;

public class UserDAO {
    public static User validateUser(String email, String password){
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)){
            
            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if(!rs.next()) {
                return null;
            }

            int id = rs.getInt("id");
            String name = rs.getString("name");
            String role = rs.getString("role");

            switch(role) {
                case "Student":
                    String studentSql = "SELECT program, year, sessionID FROM students WHERE userID = ?";

                    try (PreparedStatement studentStmt = conn.prepareStatement(studentSql)){
                        studentStmt.setInt(1, id);
                        ResultSet studentRs = studentStmt.executeQuery();
                        if (studentRs.next()) {
                            String program = studentRs.getString("program");
                            int year = studentRs.getInt("year");
                            return new Student(id, email, name, program, year);
                        }
                    }

                    break;

                case "Evaluator":
                    String evaluatorSql = "SELECT id, expertise FROM evaluators WHERE userID = ?";

                    try (PreparedStatement evaluatorStmt = conn.prepareStatement(evaluatorSql)){
                        evaluatorStmt.setInt(1, id);
                        ResultSet evaluatorRs = evaluatorStmt.executeQuery();
                        if (evaluatorRs.next()) {
                            int evaluatorID = evaluatorRs.getInt("id");
                            String expertise = evaluatorRs.getString("expertise");
                            return new Evaluator(id, email, name, evaluatorID, expertise);
                        }
                    }

                    break;
                case "Coordinator":
                    String coordinatorSql = "SELECT id, department FROM coordinators WHERE userID = ?";

                    try(PreparedStatement coordinatorStmt = conn.prepareStatement(coordinatorSql)){
                        coordinatorStmt.setInt(1,id);

                        ResultSet coordinatorRs = coordinatorStmt.executeQuery();
                        if(coordinatorRs.next()){
                            int coordinatorID = coordinatorRs.getInt("id");
                            String department = coordinatorRs.getString("department");
                            return new Coordinator(id, email, name, coordinatorID, department);
                        }
                    }
                    break;
                default:
                    return null;
            }

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        return null;
        
    }

    public static boolean isEmailRegistered(String email) {
        String sql = "SELECT COUNT(*) AS count FROM users WHERE email = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt("count");
                return count > 0;
            }

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static int createUser(String name, String email, String password, String role) {
        String sql = "INSERT INTO users (name, email, password, role, createdAt, updatedAt) VALUES (?, ?, ?, ?, NOW(), NOW())";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setString(4, role);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new java.sql.SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new java.sql.SQLException("Creating user failed, no ID obtained.");
                }
            }

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public static boolean createStudent(Student student,String hashedPassword){
        int userID = createUser(student.getName(), student.getEmail(), hashedPassword, "Student");

        if(userID == -1) {
            return false;
        }

        String sql = "INSERT INTO students (userID, program, year, createdAt, updatedAt) VALUES (?, ?, ?, NOW(), NOW())";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            stmt.setString(2, student.getProgram());
            stmt.setInt(3, student.getYear());

            int affectedRows = stmt.executeUpdate();

            return affectedRows > 0;

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return false;
        
    }
}
