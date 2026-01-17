package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Submission;
import util.DBConnection;

public class SubmissionDAO {

    // =========================
    // CREATE
    // =========================
    public static boolean createSubmission(Submission s) {

        String sql = """
            INSERT INTO submissions
            (studentID, researchTitle, abstract, supervisorName,
             presentationType, filePath, status, createdDate, updatedDate)
            VALUES (?, ?, ?, ?, ?, ?, 'submitted', NOW(), NOW())
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, s.getStudentID());
            ps.setString(2, s.getResearchTitle());
            ps.setString(3, s.getAbstracts());
            ps.setString(4, s.getSupervisorName());
            ps.setString(5, s.getPresentationType());
            ps.setString(6, s.getFilePath());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // =========================
    // READ BY STUDENT
    // =========================
    public static List<Submission> getSubmissionsByStudent(int studentID) {

        List<Submission> list = new ArrayList<>();

        String sql = """
            SELECT id, researchTitle, abstract, supervisorName,
                   presentationType, filePath, status
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
                        studentID,
                        rs.getString("abstract"),
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

    // =========================
    // UPDATE (LOCKED STATUS SAFE)
    // =========================
    public static boolean updateSubmission(Submission s) {

        String sql = """
            UPDATE submissions
            SET researchTitle = ?,
                abstract = ?,
                supervisorName = ?,
                presentationType = ?,
                filePath = ?,
                updatedDate = NOW()
            WHERE id = ?
              AND status = 'submitted'
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, s.getResearchTitle());
            ps.setString(2, s.getAbstracts());
            ps.setString(3, s.getSupervisorName());
            ps.setString(4, s.getPresentationType());
            ps.setString(5, s.getFilePath());
            ps.setInt(6, s.getSubmissionID());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // =========================
    // DELETE (ONLY IF SAFE)
    // =========================
    public static boolean deleteSubmission(int submissionID) {

        String sql = """
            DELETE FROM submissions
            WHERE id = ?
              AND status = 'submitted'
        """;

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
