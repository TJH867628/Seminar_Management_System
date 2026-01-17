package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Submission;
import util.DBConnection;

public class SubmissionDAO {

    // ================= CREATE =================
    public static boolean createSubmission(Submission submission) {

        String sql = """
            INSERT INTO submissions
            (studentID, researchTitle, abstracts, supervisorName,
             presentationType, filePath, status, createdDate, updatedDate)
            VALUES (?, ?, ?, ?, ?, ?, 'submitted', NOW(), NOW())
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, submission.getStudentID());
            ps.setString(2, submission.getResearchTitle());
            ps.setString(3, submission.getAbstracts());
            ps.setString(4, submission.getSupervisorName());
            ps.setString(5, submission.getPresentationType());
            ps.setString(6, submission.getFilePath());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ================= READ (BY STUDENT) =================
    public static List<Submission> getSubmissionsByStudent(int studentID) {

        List<Submission> list = new ArrayList<>();

        String sql = """
            SELECT *
            FROM submissions
            WHERE studentID = ?
            ORDER BY createdDate DESC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Submission(
                        rs.getInt("id"),
                        rs.getString("researchTitle"),
                        rs.getString("filePath"),
                        rs.getInt("studentID"),
                        rs.getString("abstracts"),
                        rs.getString("supervisorName"),
                        rs.getString("presentationType"),
                        rs.getString("status")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // ================= READ (SINGLE) =================
    public static Submission getSubmissionById(int submissionID) {

        String sql = "SELECT * FROM submissions WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, submissionID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Submission(
                        rs.getInt("id"),
                        rs.getString("researchTitle"),
                        rs.getString("filePath"),
                        rs.getInt("studentID"),
                        rs.getString("abstracts"),
                        rs.getString("supervisorName"),
                        rs.getString("presentationType"),
                        rs.getString("status")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // ================= UPDATE =================
    public static boolean updateSubmission(Submission submission) {

        String sql = """
            UPDATE submissions
            SET researchTitle = ?,
                abstracts = ?,
                supervisorName = ?,
                presentationType = ?,
                filePath = ?,
                updatedDate = NOW()
            WHERE id = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, submission.getResearchTitle());
            ps.setString(2, submission.getAbstracts());
            ps.setString(3, submission.getSupervisorName());
            ps.setString(4, submission.getPresentationType());
            ps.setString(5, submission.getFilePath());
            ps.setInt(6, submission.getSubmissionID());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ================= DELETE =================
    public static boolean deleteSubmission(int submissionID) {

        String sql = "DELETE FROM submissions WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, submissionID);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
