package dao;

import model.AssignedEvaluation;
import model.Evaluation;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSetMetaData;

public class EvaluatorDAO {

    // sessionId is passed in (NOT evaluator id)
    public List<AssignedEvaluation> getAssignedEvaluations(int evaluatorID) {
        System.out.println("Fetching assigned evaluations for session ID: " + evaluatorID);

        List<AssignedEvaluation> list = new ArrayList<>();

        String sql =
            "SELECT " +
            "  sub.researchTitle AS researchTitle, " +
            "  stu.id AS studentId, " +
            "  u.name AS studentName, " +
            "  ses.date, " +
            "  ses.venue, " +
            "  sub.filePath, " +
            "  sub.id AS submissionID, " +
            "  stu.sessionID, " +
            "  eva.totalScore, " +
            "  CASE " +
            "    WHEN eva.id IS NULL THEN 'NOT_STARTED' " +
            "    WHEN eva.status = 'COMPLETED' THEN 'COMPLETED' " +
            "    ELSE 'IN_PROGRESS' " +
            "  END AS status " +
            "FROM students stu " +
            "JOIN users u ON stu.userID = u.id " +
            "JOIN submissions sub ON sub.studentID = stu.id " +
            "JOIN sessions ses ON ses.id = stu.sessionID " +
            "LEFT JOIN evaluation eva ON eva.submissionID = sub.id " +
            "WHERE stu.sessionID IN ( " +
            "  SELECT sessionID " +
            "  FROM assigned_session " +
            "  WHERE evaluatorID = ? " +
            ")";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, evaluatorID);
            ResultSet rs = ps.executeQuery();

            System.out.println("=== ResultSet Debug ===");
            ResultSetMetaData meta = rs.getMetaData();
            int colCount = meta.getColumnCount();

            while (rs.next()) {
                list.add(new AssignedEvaluation(
                        rs.getString("studentName"),
                        rs.getString("sessionId"),
                        rs.getString("date"),
                        rs.getString("venue"),
                        rs.getString("filePath")
                        , rs.getInt("submissionID")
                        , rs.getInt("totalScore")
                        , (rs.getString("status"))
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
