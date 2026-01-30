package dao;

import model.EvaluationResult;
import model.Session;
import util.DBConnection;

import java.sql.*;
import java.util.*;

public class SessionDAO {

    // Get all sessions with students, evaluators, and evaluation results loaded
    public List<Session> getApprovedSessions() {
        List<Session> sessions = new ArrayList<>();

        String sql = """
            SELECT id, date, venue, sessionType, timeSlot
            FROM sessions
            ORDER BY date
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Session s = new Session(
                    rs.getInt("id"),
                    rs.getString("venue"),
                    rs.getString("sessionType"),
                    rs.getDate("date"),
                    rs.getTime("timeSlot")
                );

                // Load students, evaluators, and evaluation results
                loadParticipants(s);
                loadEvaluationResults(s);

                sessions.add(s);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sessions;
    }

    // Generate schedule (time slots) and save to DB
    public void generateSchedule(List<Session> sessions) {
        String updateSQL = "UPDATE sessions SET timeSlot = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(updateSQL)) {

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 9);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);

            for (Session s : sessions) {
                Time slot = new Time(cal.getTimeInMillis());

                ps.setTime(1, slot);
                ps.setInt(2, s.getSessionID());
                ps.executeUpdate();

                cal.add(Calendar.MINUTE, 30);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Load student IDs and evaluator IDs for a session
    private void loadParticipants(Session session) {
        // Load students
        String studentSQL = "SELECT id FROM submissions WHERE sessionID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(studentSQL)) {

            ps.setInt(1, session.getSessionID());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int studentID = rs.getInt("id");
                session.addStudent(studentID);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Load evaluators
        String evaluatorSQL = "SELECT DISTINCT evaluatorID FROM evaluation WHERE submissionID IN (SELECT id FROM submissions WHERE sessionID = ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(evaluatorSQL)) {

            ps.setInt(1, session.getSessionID());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int evaluatorID = rs.getInt("evaluatorID");
                if (!session.getEvaluatorIDs().contains(evaluatorID)) {
                    session.addEvaluator(evaluatorID);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Load evaluation results for a session
    public void loadEvaluationResults(Session session) {
        String sql = """
            SELECT evaluatorID, submissionID, problemClarityScore AS problemClarifyScore,
                   methodologyScore, resultScore, presentationScore, totalScore, comments, status
            FROM evaluation
            WHERE status = 'COMPLETED' AND submissionID IN (
                SELECT id FROM submissions WHERE sessionID = ?
            )
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, session.getSessionID());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                EvaluationResult er = new EvaluationResult(
                    rs.getInt("submissionID"),
                    rs.getInt("evaluatorID"),
                    rs.getInt("problemClarifyScore"),
                    rs.getInt("methodologyScore"),
                    rs.getInt("resultScore"),
                    rs.getInt("presentationScore"),
                    rs.getInt("totalScore"),
                    rs.getString("comments"),
                    rs.getString("status")
                );

                if (!session.getEvaluatorIDs().contains(er.getEvaluatorID())) {
                    session.addEvaluator(er.getEvaluatorID());
                }

                session.addEvaluationResult(er);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Generate schedule and load final evaluation report
    public List<Session> generateScheduleAndReport() {
        List<Session> sessions = getApprovedSessions(); // fetch all sessions
        generateSchedule(sessions);                     // assign time slots

        // Removed duplicate loadEvaluationResults call
        // for (Session s : sessions) {
        //     loadEvaluationResults(s);                  
        // }

        return sessions; 
    }
}
