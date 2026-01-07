package dao;

import model.Evaluation;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EvaluationDAO {
    // ---------- LOAD DRAFT (IN_PROGRESS) ----------
    public Evaluation loadDraft(int submissionID, int evaluatorID) {
        String sql =
            "SELECT problemClarityScore, methodologyScore, resultScore, presentationScore, comments " +
            "FROM evaluation " +
            "WHERE submissionID = ? AND evaluatorID = ?";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, submissionID);
            ps.setInt(2, evaluatorID);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Evaluation(
                    rs.getInt("problemClarityScore"),
                    rs.getInt("methodologyScore"),
                    rs.getInt("resultScore"),
                    rs.getInt("presentationScore"),
                    rs.getString("comments")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // ---------- SAVE DRAFT (IN_PROGRESS) ----------
    public void saveDraft(Evaluation eval, int submissionID, int evaluatorID) {

        String sql =
            "INSERT INTO evaluation " +
            "(submissionID, evaluatorID, problemClarityScore, methodologyScore, resultScore, presentationScore, comments, totalScore, status) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'IN_PROGRESS') " +
            "ON DUPLICATE KEY UPDATE " +
            "problemClarityScore=?, methodologyScore=?, resultScore=?, presentationScore=?, comments=?, totalScore=?, status='IN_PROGRESS'";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, submissionID);
            ps.setInt(2, evaluatorID);
            ps.setInt(3, eval.getProblemClarity());
            ps.setInt(4, eval.getMethodology());
            ps.setInt(5, eval.getResults());
            ps.setInt(6, eval.getPresentation());
            ps.setString(7, eval.getComments());
            ps.setInt(8, eval.getTotalScore());

            ps.setInt(9, eval.getProblemClarity());
            ps.setInt(10, eval.getMethodology());
            ps.setInt(11, eval.getResults());
            ps.setInt(12, eval.getPresentation());
            ps.setString(13, eval.getComments());
            ps.setInt(14, eval.getTotalScore());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // ---------- SUBMIT FINAL (COMPLETED) ----------
    public boolean saveEvaluation(
            Evaluation evaluation,
            int submissionID,
            int evaluatorID
    ) {

        String sql =
            "INSERT INTO evaluation " +
            "(evaluatorID, submissionID, problemClarityScore, methodologyScore, resultScore, presentationScore, comments, totalScore, status) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'COMPLETED')";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, evaluatorID);
            ps.setInt(2, submissionID);
            ps.setInt(3, evaluation.getProblemClarity());
            ps.setInt(4, evaluation.getMethodology());
            ps.setInt(5, evaluation.getResults());
            ps.setInt(6, evaluation.getPresentation());
            ps.setString(7, evaluation.getComments());
            ps.setInt(8, evaluation.getTotalScore());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
