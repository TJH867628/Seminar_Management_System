package dao;

import model.Submission;
import util.DBConnection;
import java.sql.*;

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
}
