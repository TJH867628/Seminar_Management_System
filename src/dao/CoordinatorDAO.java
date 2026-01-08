package dao;

import model.*;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CoordinatorDAO {

    public List<Session> getAllSessions() {
        List<Session> list = new ArrayList<>();
        String sql = "SELECT * FROM sessions";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Session(
                        rs.getInt("id"),
                        rs.getString("venue"),
                        rs.getDate("date")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Evaluator> getAllEvaluators() {
        List<Evaluator> list = new ArrayList<>();
        String sql = "SELECT * FROM evaluators";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Evaluator(
                        rs.getInt("id"),
                        rs.getString("expertise")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Student> getAllSubmissionsWithoutSession() {
        List<Student> list = new ArrayList<>();
    
        String sql =
            "SELECT s.id, s.program " +
            "FROM students s " +
            "JOIN submissions sub ON sub.studentID = s.id " +
            "WHERE sub.sessionID IS NULL";
    
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
    
            while (rs.next()) {
                list.add(new Student(
                        rs.getInt("id"),
                        rs.getString("program")
                ));
            }
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return list;
    }

    public void assignEvaluator(int sessionID, int evaluatorID) {
        String sql = "INSERT INTO assigned_session (sessionID, evaluatorID) VALUES (?, ?)";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, sessionID);
            ps.setInt(2, evaluatorID);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
