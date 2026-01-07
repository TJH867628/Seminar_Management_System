package dao;

import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import app.UserSession;

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
                case "Admin":
                    String adminSql = "SELECT id FROM admins WHERE userID = ?";
                    try(PreparedStatement adminStmt = conn.prepareStatement(adminSql)){
                        adminStmt.setInt(1,id);

                        ResultSet adminRs = adminStmt.executeQuery();
                        if(adminRs.next()){
                            int adminID = adminRs.getInt("id");
                            return new Admin(adminID, id, email, name);
                        }
                    }
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

    public static boolean createEvaluator(Evaluator evaluator, String hashedPassword) {
        int userID = createUser(evaluator.getName(), evaluator.getEmail(), hashedPassword, "Evaluator");

        if (userID == -1) {
            return false;
        }

        String sql = "INSERT INTO evaluators (userID, expertise, createdAt, updatedAt) VALUES (?, ?, NOW(), NOW())";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            stmt.setString(2, evaluator.getExpertise());

            int affectedRows = stmt.executeUpdate();

            return affectedRows > 0;

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean createCoordinator(Coordinator coordinator, String hashedPassword) {
        int userID = createUser(coordinator.getName(), coordinator.getEmail(), hashedPassword, "Coordinator");

        if (userID == -1) {
            return false;
        }

        String sql = "INSERT INTO coordinators (userID, department, createdAt, updatedAt) VALUES (?, ?, NOW(), NOW())";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            stmt.setString(2, coordinator.getDepartment());

            int affectedRows = stmt.executeUpdate();

            return affectedRows > 0;

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<User> getAllUsers() {
        
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();
        try(Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)){
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String role = rs.getString("role");

                if(id == UserSession.getCurrentUser().getUserID()) {
                    continue; 
                }
                
                users.add(new User(id, email, name, role) {});
            }
            return users;
            
        }catch(java.sql.SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateUser(int userID, String name, String email, String role) {
        String sql = "UPDATE users SET name = ?, email = ?, role = ?, updatedAt = NOW() WHERE id = ?";
        System.out.println("Update with user id: " + userID);

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, role);
            stmt.setInt(4, userID);

            int affectedRows = stmt.executeUpdate();

            return affectedRows > 0;

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
