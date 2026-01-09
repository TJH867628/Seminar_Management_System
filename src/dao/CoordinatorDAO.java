package dao;

import model.*;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CoordinatorDAO {
    SeminarSessionDAO SeminarSessionDAO = new SeminarSessionDAO();

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

    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students";

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

    public List<SessionAssignment> getAllSessionAssignments() {
        List<SessionAssignment> list = new ArrayList<>();
        String sql = """
            SELECT
                s.id AS sessionID,
                s.venue,
                s.sessionType,
                s.timeSlot,
                COUNT(DISTINCT sub.id) AS submissionCount,
                COUNT(DISTINCT asg.evaluatorID) AS evaluatorCount
            FROM sessions s
            LEFT JOIN submissions sub ON s.id = sub.sessionID
            LEFT JOIN assigned_session asg ON s.id = asg.sessionID
            GROUP BY s.id, s.venue, s.sessionType, s.timeSlot
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new SessionAssignment(
                        rs.getInt("sessionID"),
                        rs.getString("venue"),
                        rs.getString("sessionType"),
                        rs.getTime("timeSlot"),
                        rs.getInt("submissionCount"),
                        rs.getInt("evaluatorCount")                        
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Submission> getSubmissionsBySession(int sessionID) {

        List<Submission> list = new ArrayList<>();

        String sql = "SELECT * FROM submissions WHERE sessionID = ?";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, sessionID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new Submission(
                    rs.getInt("id"),
                    rs.getString("researchTitle"),
                    rs.getString("filePath"),
                    rs.getInt("studentID"),
                    rs.getString("abstract"),
                    rs.getString("supervisorName"),
                    rs.getString("status")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Submission> getAvailableSubmissions(int sessionID) {

        List<Submission> list = new ArrayList<>();

        String sql = "SELECT * FROM submissions WHERE sessionID IS NULL";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new Submission(
                    rs.getInt("id"),
                    rs.getString("researchTitle"),
                    rs.getString("filePath"),
                    rs.getInt("studentID"),
                    rs.getString("abstract"),
                    rs.getString("supervisorName"),
                    rs.getString("status")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Evaluator> getEvaluatorsBySession(int sessionID) {

        List<Evaluator> list = new ArrayList<>();

        String sql = """
            SELECT e.id, e.expertise
            FROM evaluators e
            JOIN assigned_session asg ON e.id = asg.evaluatorID
            WHERE asg.sessionID = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, sessionID);
            ResultSet rs = stmt.executeQuery();

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

    public List<Evaluator> getAvailableEvaluators(int sessionID) {

        List<Evaluator> list = new ArrayList<>();

        String sql = """
            SELECT e.id, e.expertise
            FROM evaluators e
            WHERE e.id NOT IN (
                SELECT asg.evaluatorID
                FROM assigned_session asg
                WHERE asg.sessionID = ?
            )
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, sessionID);
            ResultSet rs = stmt.executeQuery();

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

    public SessionAssignmentDetail getSessionAssignmentDetails(int sessionID) {
        SessionAssignmentDetail detail = null;
        List<Submission> submissions = getSubmissionsBySession(sessionID);
        List<Evaluator> evaluators = getEvaluatorsBySession(sessionID);
        Session session = SeminarSessionDAO.getSeminarSessionById(sessionID);

        return detail = new SessionAssignmentDetail(
            session,
            submissions,
            evaluators
        );
    }

    public boolean removeSubmissionFromSession(int sessionId, int submissionId) {
        String sql = "UPDATE submissions SET sessionID = NULL WHERE id = ? AND sessionID = ?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, submissionId);
            ps.setInt(2, sessionId);
            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean assignSubmissionToSession(int sessionId, int submissionId) {
        String sql = "UPDATE submissions SET sessionID = ? WHERE id = ?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, sessionId);
            ps.setInt(2, submissionId);
            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean assignEvaluatorToSession(int sessionId, int evaluatorId) {
        String sql = "INSERT INTO assigned_session (sessionID, evaluatorID) VALUES (?, ?)";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, sessionId);
            ps.setInt(2, evaluatorId);
            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean removeEvaluatorFromSession(int sessionId, int evaluatorId) {
        String sql = "DELETE FROM assigned_session WHERE sessionID = ? AND evaluatorID = ?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, sessionId);
            ps.setInt(2, evaluatorId);
            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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
