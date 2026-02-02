package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.*;
import util.DBConnection;

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
                        rs.getDate("date")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Evaluator> getAllEvaluators() {
        List<Evaluator> list = new ArrayList<>();
        String sql = "SELECT * FROM evaluators join users on evaluators.userID = users.id";

        try (Connection c = DBConnection.getConnection();
                PreparedStatement ps = c.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Evaluator(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("name"),
                        rs.getInt("evaluatorID"),
                        rs.getString("expertise")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Student> getAllSubmissionsWithoutSession() {
        List<Student> list = new ArrayList<>();

        String sql = "SELECT s.id, s.program " +
                "FROM students s " +
                "JOIN submissions sub ON sub.studentID = s.id " +
                "WHERE sub.sessionID IS NULL";

        try (Connection c = DBConnection.getConnection();
                PreparedStatement ps = c.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Student(
                        rs.getInt("id"),
                        rs.getString("program")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<SessionAssignment> getAllSessionAssignments() {
        List<SessionAssignment> list = new ArrayList<>();
        String sql = "SELECT " +
                "  s.id AS sessionID, " +
                "  s.venue, " +
                "  s.sessionType, " +
                "  s.timeSlot, " +
                "  COUNT(DISTINCT sub.id) AS submissionCount, " +
                "  COUNT(DISTINCT asg.evaluatorID) AS evaluatorCount " +
                "FROM sessions s " +
                "LEFT JOIN submissions sub ON s.id = sub.sessionID " +
                "LEFT JOIN assigned_session asg ON s.id = asg.sessionID " +
                "GROUP BY s.id, s.venue, s.sessionType, s.timeSlot";

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
                        rs.getInt("evaluatorCount")));
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
                        rs.getString("abstracts"),
                        rs.getString("supervisorName"),
                        rs.getString("presentationType"),
                        rs.getString("status"),
                        rs.getTimestamp("createdDate")));
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
                        rs.getString("abstracts"),
                        rs.getString("supervisorName"),
                        rs.getString("presentationType"),
                        rs.getString("status"),
                        rs.getTimestamp("createdDate")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Evaluator> getEvaluatorsBySession(int sessionID) {

        List<Evaluator> list = new ArrayList<>();

        String sql = "SELECT u.id AS userID, u.email, u.name, e.id AS evaluatorID, e.expertise " +
                "FROM evaluators e " +
                "JOIN users u ON u.id = e.userID " +
                "JOIN assigned_session asg ON e.id = asg.evaluatorID " +
                "WHERE asg.sessionID = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, sessionID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new Evaluator(
                        rs.getInt("userID"),
                        rs.getString("email"),
                        rs.getString("name"),
                        rs.getInt("evaluatorID"),
                        rs.getString("expertise")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Evaluator> getAvailableEvaluators(int sessionID) {

        List<Evaluator> list = new ArrayList<>();

        String sql = "SELECT u.id AS userID, u.email, u.name, e.id AS evaluatorID, e.expertise " +
                "FROM evaluators e " +
                "JOIN users u ON u.id = e.userID " +
                "WHERE e.id NOT IN ( " +
                "    SELECT asg.evaluatorID " +
                "    FROM assigned_session asg " +
                "    WHERE asg.sessionID = ? " +
                ")";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, sessionID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new Evaluator(
                        rs.getInt("userID"),
                        rs.getString("email"),
                        rs.getString("name"),
                        rs.getInt("evaluatorID"),
                        rs.getString("expertise")));
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
                evaluators);
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

        public List<Award> getAwardWinners() {
    List<Award> list = new ArrayList<>();
    String sql = """
        SELECT * FROM (
            SELECT 1 AS awardID,
                   'Best Oral' AS awardName,
                   'Oral Presentation' AS category,
                   'Highest total score' AS criteria,
                   sub.id AS submissionID,
                   u.name AS studentName,
                   sub.presentationType,
                   eva.totalScore AS scoreOrVotes
            FROM submissions sub
            JOIN evaluation eva ON eva.submissionID = sub.id
            JOIN students s ON s.id = sub.studentID
            JOIN users u ON u.id = s.userID
            WHERE sub.presentationType = 'Oral'
            ORDER BY eva.totalScore DESC
            LIMIT 1
        ) AS oral

        UNION ALL

        SELECT * FROM (
            SELECT 2 AS awardID,
                   'Best Poster' AS awardName,
                   'Poster Presentation' AS category,
                   'Highest total score' AS criteria,
                   sub.id AS submissionID,
                   u.name AS studentName,
                   sub.presentationType,
                   eva.totalScore AS scoreOrVotes
            FROM submissions sub
            JOIN evaluation eva ON eva.submissionID = sub.id
            JOIN students s ON s.id = sub.studentID
            JOIN users u ON u.id = s.userID
            WHERE sub.presentationType = 'Poster'
            ORDER BY eva.totalScore DESC
            LIMIT 1
        ) AS poster

        UNION ALL

        SELECT * FROM (
            SELECT 3 AS awardID,
                   'People''s Choice' AS awardName,
                   'All Presentations' AS category,
                   'Most votes by evaluators' AS criteria,
                   sub.id AS submissionID,
                   u.name AS studentName,
                   sub.presentationType,
                   COUNT(pcv.voteID) AS scoreOrVotes
            FROM submissions sub
            JOIN students s ON s.id = sub.studentID
            JOIN users u ON u.id = s.userID
            LEFT JOIN peoples_choice_votes pcv ON pcv.submissionID = sub.id
            GROUP BY sub.id
            ORDER BY scoreOrVotes DESC
            LIMIT 1
        ) AS people_choice;
        """;

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            list.add(new Award(
                rs.getInt("awardID"),
                rs.getString("awardName"),
                rs.getString("category"),
                rs.getString("criteria"),
                rs.getInt("submissionID"),
                rs.getString("studentName"),
                rs.getString("presentationType"),
                rs.getInt("scoreOrVotes")
            ));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}

// Fetch all awards for Award Agenda
public List<Award> getAllAwards() {
    List<Award> awards = new ArrayList<>();
    String sql = "SELECT * FROM awards";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {  // Fixed typo 'wwhile' -> 'while'
            // Use constructor since setters may not exist
            Award award = new Award(
                rs.getInt("id"),
                rs.getString("awardName"),
                rs.getString("category"),
                rs.getString("criteria")
            );
            awards.add(award);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return awards;
}
}
