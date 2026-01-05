package dao;

import model.AssignedEvaluation;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EvaluatorDAO {

    // sessionId is passed in (NOT evaluator id)
    public List<AssignedEvaluation> getAssignedEvaluations(int sessionId) {

        List<AssignedEvaluation> list = new ArrayList<>();

        String sql =
            "SELECT u.name AS studentName, " +
            "sess.id AS sessionId, " +
            "sess.date, " +
            "sess.venue, " +
            "sub.filePath " +
            "FROM sessions sess " +
            "JOIN students s ON sess.id = s.id " +
            "JOIN users u ON s.userID = u.id " +
            "JOIN submissions sub ON u.id = sub.userID " +
            "WHERE sess.id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, sessionId);
            ResultSet rs = ps.executeQuery();

            System.out.println("RS: " + rs);
            while (rs.next()) {
                list.add(new AssignedEvaluation(
                        rs.getString("studentName"),
                        rs.getString("sessionId"),
                        rs.getString("date"),
                        rs.getString("venue"),
                        rs.getString("filePath")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
