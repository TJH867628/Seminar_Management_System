package dao;

import model.Evaluation;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EvaluationDAO {
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

    // ---------- SAVE DRAFT (PENDING) ----------
    public void saveDraft(Evaluation eval, int submissionID, int evaluatorID) {

        String checkSql =
            "SELECT id FROM evaluation WHERE submissionID = ? AND evaluatorID = ?";
    
        String insertSql =
            "INSERT INTO evaluation " +
            "(submissionID, evaluatorID, problemClarityScore, methodologyScore, resultScore, presentationScore, comments, totalScore, status) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'PENDING')";
    
        String updateSql =
            "UPDATE evaluation SET " +
            "problemClarityScore=?, methodologyScore=?, resultScore=?, presentationScore=?, comments=?, totalScore=?, status='PENDING' " +
            "WHERE submissionID=? AND evaluatorID=?";
    
        try (Connection conn = DBConnection.getConnection()) {
    
            // 1️⃣ Check existence
            try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
                checkPs.setInt(1, submissionID);
                checkPs.setInt(2, evaluatorID);
    
                ResultSet rs = checkPs.executeQuery();
    
                if (rs.next()) {
                    // 2️⃣ UPDATE
                    try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                        ps.setInt(1, eval.getProblemClarity());
                        ps.setInt(2, eval.getMethodology());
                        ps.setInt(3, eval.getResults());
                        ps.setInt(4, eval.getPresentation());
                        ps.setString(5, eval.getComments());
                        ps.setInt(6, eval.getTotalScore());
                        ps.setInt(7, submissionID);
                        ps.setInt(8, evaluatorID);
                        ps.executeUpdate();
                    }
                } else {
                    // 3️⃣ INSERT
                    try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                        ps.setInt(1, submissionID);
                        ps.setInt(2, evaluatorID);
                        ps.setInt(3, eval.getProblemClarity());
                        ps.setInt(4, eval.getMethodology());
                        ps.setInt(5, eval.getResults());
                        ps.setInt(6, eval.getPresentation());
                        ps.setString(7, eval.getComments());
                        ps.setInt(8, eval.getTotalScore());
                        ps.executeUpdate();
                    }
                }
            }
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // ---------- SUBMIT FINAL (COMPLETED) ----------
    public boolean saveEvaluation(Evaluation evaluation, int submissionID, int evaluatorID) 
    {
        String checkSql =
            "SELECT id FROM evaluation WHERE submissionID = ? AND evaluatorID = ?";

        String updateSql =
            "UPDATE evaluation SET " +
            "problemClarityScore = ?, " +
            "methodologyScore = ?, " +
            "resultScore = ?, " +
            "presentationScore = ?, " +
            "comments = ?, " +
            "totalScore = ?, " +
            "status = 'COMPLETED' " +
            "WHERE submissionID = ? AND evaluatorID = ?";

        String insertSql =
            "INSERT INTO evaluation " +
            "(submissionID, evaluatorID, problemClarityScore, methodologyScore, resultScore, presentationScore, comments, totalScore, status) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'COMPLETED')";

        try (Connection conn = DBConnection.getConnection()) {

            // 1️⃣ Check if draft exists
            try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
                checkPs.setInt(1, submissionID);
                checkPs.setInt(2, evaluatorID);

                ResultSet rs = checkPs.executeQuery();

                if (rs.next()) {
                    // 2️⃣ UPDATE existing draft → COMPLETED
                    try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                        ps.setInt(1, evaluation.getProblemClarity());
                        ps.setInt(2, evaluation.getMethodology());
                        ps.setInt(3, evaluation.getResults());
                        ps.setInt(4, evaluation.getPresentation());
                        ps.setString(5, evaluation.getComments());
                        ps.setInt(6, evaluation.getTotalScore());
                        ps.setInt(7, submissionID);
                        ps.setInt(8, evaluatorID);
                        return ps.executeUpdate() > 0;
                    }
                } else {
                    // 3️⃣ INSERT new completed evaluation
                    try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                        ps.setInt(1, submissionID);
                        ps.setInt(2, evaluatorID);
                        ps.setInt(3, evaluation.getProblemClarity());
                        ps.setInt(4, evaluation.getMethodology());
                        ps.setInt(5, evaluation.getResults());
                        ps.setInt(6, evaluation.getPresentation());
                        ps.setString(7, evaluation.getComments());
                        ps.setInt(8, evaluation.getTotalScore());
                        return ps.executeUpdate() > 0;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
