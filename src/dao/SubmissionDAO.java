package dao;

import java.sql.*;
import model.Submission;
import util.DBConnection;

public class SubmissionDAO {
    public static Submission getSubmissionByID(int submissionID) {

        String sql = "SELECT * FROM submissions WHERE submissionID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, submissionID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Submission(
                        rs.getInt("submissionID"),
                        rs.getString("researchTitle"),
                        rs.getString("filePath"),
                        rs.getInt("studentID"),
                        rs.getString("abstracts"),
                        rs.getString("supervisorName"),
                        rs.getString("status")
                );
            }

            return null;

        } catch (SQLException e) {
            e.printStackTrace();
        return null;
        }
    }

    public static boolean createSubmission(Submission submission) {

    String sql = "INSERT INTO submissions " +
                 "(researchTitle, filePath, studentID, abstracts, supervisorName, status) " +
                 "VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, submission.getResearchTitle());
        ps.setString(2, submission.getFilePath());
        ps.setInt(3, submission.getStudentID());
        ps.setString(4, submission.getAbstracts());
        ps.setString(5, submission.getSupervisorName());
        ps.setString(6, submission.getStatus());

        return ps.executeUpdate() > 0;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

}
