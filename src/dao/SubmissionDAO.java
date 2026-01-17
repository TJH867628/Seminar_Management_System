package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Submission;
import util.DBConnection;

public class SubmissionDAO {

    // ========================
    // CREATE
    // ========================
    public static boolean createSubmission(Submission submission) {

    String sql = "INSERT INTO submissions " +
            "(studentID, researchTitle, abstract, supervisorName, filePath, status, createdDate, updatedDate) " +
            "VALUES (?, ?, ?, ?, ?, ?, NOW(), NOW())";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        int studentTableID =
                getStudentTableIDByUserID(submission.getStudentID());

        if (studentTableID == -1) {
            System.out.println("Student record not found.");
            return false;
        }

        ps.setInt(1, studentTableID);
        ps.setString(2, submission.getResearchTitle());
        ps.setString(3, submission.getAbstracts());
        ps.setString(4, submission.getSupervisorName());
        ps.setString(5, submission.getFilePath());
        ps.setString(6, submission.getStatus());

        return ps.executeUpdate() > 0;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}


    // ========================
    // READ (by ID)
    // ========================
    public static Submission getSubmissionByID(int id) {

        String sql = "SELECT * FROM submissions WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Submission(
                        rs.getInt("id"),
                        rs.getString("researchTitle"),
                        rs.getString("filePath"),
                        rs.getInt("studentID"),
                        rs.getString("abstract"),
                        rs.getString("supervisorName"),
                        rs.getString("status")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // ========================
    // READ (by student)
    // ========================
    public static List<Submission> getSubmissionsByStudent(int studentID) {

        List<Submission> list = new ArrayList<>();

        String sql = "SELECT * FROM submissions WHERE studentID = ?";

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
                        rs.getString("abstract"),
                        rs.getString("supervisorName"),
                        rs.getString("status")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // ========================
    // UPDATE
    // ========================
    public static boolean updateSubmission(Submission submission) {

        String sql = "UPDATE submissions SET " +
                "researchTitle = ?, abstract = ?, supervisorName = ?, " +
                "filePath = ?, status = ?, updatedDate = NOW() " +
                "WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, submission.getResearchTitle());
            ps.setString(2, submission.getAbstracts());
            ps.setString(3, submission.getSupervisorName());
            ps.setString(4, submission.getFilePath());
            ps.setString(5, submission.getStatus());
            ps.setInt(6, submission.getSubmissionID());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int getStudentTableIDByUserID(int userID) {

    String sql = "SELECT id FROM students WHERE userID = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, userID);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt("id");
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return -1; // not found
}

}
