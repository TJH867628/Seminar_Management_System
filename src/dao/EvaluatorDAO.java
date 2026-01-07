package dao;

import model.AssignedEvaluation;
import model.Evaluation;
import model.Evaluator;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSetMetaData;

public class EvaluatorDAO {

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
        "  sub.sessionID, " +  
        "  eva.totalScore, " +
        "  CASE " +
        "    WHEN eva.id IS NULL THEN 'NOT_STARTED' " +
        "    WHEN eva.status = 'COMPLETED' THEN 'COMPLETED' " +
        "    ELSE 'IN_PROGRESS' " +
        "  END AS status " +
        "FROM assigned_session asg " +
        "JOIN submissions sub ON sub.sessionID = asg.sessionID " +
        "JOIN sessions ses ON ses.id = sub.sessionID " +
        "JOIN students stu ON stu.id = sub.studentID " +
        "JOIN users u ON stu.userID = u.id " +
        "LEFT JOIN evaluation eva ON eva.submissionID = sub.id " +
        "WHERE asg.evaluatorID = ?";    
    
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, evaluatorID);
            ResultSet rs = ps.executeQuery();

            System.out.println("=== ResultSet Debug ===");
            ResultSetMetaData meta = rs.getMetaData();
            int colCount = meta.getColumnCount();

            System.out.println("EvaluatorID passed = " + evaluatorID);

            int count = 0;

            while (rs.next()) {
                count++;
                System.out.println("ROW FOUND: " + rs.getString("studentName"));

                String studentName = rs.getString("studentName");
                int sessionID = rs.getInt("sessionID");
                String date = rs.getTimestamp("date").toString();
                String venue = rs.getString("venue");
                String filePath = rs.getString("filePath");
                int submissionID = rs.getInt("submissionID");
            
                int totalScore = rs.getObject("totalScore") != null
                        ? rs.getInt("totalScore")
                        : -1;
            
                String status = rs.getString("status");

                System.out.println("Created AE: " + studentName + ", " + sessionID + ", " + date + ", " + venue + ", " + filePath + ", " + submissionID + ", " + totalScore + ", " + status);
            
                list.add(new AssignedEvaluation(
                    studentName,
                    sessionID,   
                    date,
                    venue,
                    filePath,
                    submissionID,
                    totalScore,
                    status
                ));
            }
            System.out.println("TOTAL ROWS = " + count);

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("DAO returning list size = " + list.size());
        return list;
    }
}
