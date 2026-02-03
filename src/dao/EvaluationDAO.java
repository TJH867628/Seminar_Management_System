package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.Evaluation;
import util.DBConnection;

public class EvaluationDAO {

    // ---------- LOAD DRAFT ----------
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

    // ---------- SAVE DRAFT (DRAFT) ----------
    public void saveDraft(Evaluation eval, int submissionID, int evaluatorID) {

        String checkSql =
            "SELECT id FROM evaluation WHERE submissionID = ? AND evaluatorID = ?";

        String insertSql =
            "INSERT INTO evaluation " +
            "(submissionID, evaluatorID, problemClarityScore, methodologyScore, resultScore, presentationScore, comments, totalScore, status) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'DRAFT')";

        String updateSql =
            "UPDATE evaluation SET " +
            "problemClarityScore=?, methodologyScore=?, resultScore=?, presentationScore=?, comments=?, totalScore=?, status='DRAFT' " +
            "WHERE submissionID=? AND evaluatorID=?";

        try (Connection conn = DBConnection.getConnection()) {

            try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
                checkPs.setInt(1, submissionID);
                checkPs.setInt(2, evaluatorID);

                ResultSet rs = checkPs.executeQuery();

                if (rs.next()) {
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
    public boolean saveEvaluation(Evaluation evaluation, int submissionID, int evaluatorID) {

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

            try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
                checkPs.setInt(1, submissionID);
                checkPs.setInt(2, evaluatorID);

                ResultSet rs = checkPs.executeQuery();

                boolean success;

                if (rs.next()) {
                    try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                        ps.setInt(1, evaluation.getProblemClarity());
                        ps.setInt(2, evaluation.getMethodology());
                        ps.setInt(3, evaluation.getResults());
                        ps.setInt(4, evaluation.getPresentation());
                        ps.setString(5, evaluation.getComments());
                        ps.setInt(6, evaluation.getTotalScore());
                        ps.setInt(7, submissionID);
                        ps.setInt(8, evaluatorID);
                        success = ps.executeUpdate() > 0;
                    }
                } else {
                    try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                        ps.setInt(1, submissionID);
                        ps.setInt(2, evaluatorID);
                        ps.setInt(3, evaluation.getProblemClarity());
                        ps.setInt(4, evaluation.getMethodology());
                        ps.setInt(5, evaluation.getResults());
                        ps.setInt(6, evaluation.getPresentation());
                        ps.setString(7, evaluation.getComments());
                        ps.setInt(8, evaluation.getTotalScore());
                        success = ps.executeUpdate() > 0;
                    }
                }

                if (success) {
                    try (PreparedStatement ps2 = conn.prepareStatement(
                            "UPDATE submissions SET status = 'evaluated', updatedDate = NOW() WHERE id = ?")) {
                        ps2.setInt(1, submissionID);
                        ps2.executeUpdate();
                    }
                }

                return success;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ================= STUDENT VIEW (READ-ONLY) =================
    public static Evaluation getFinalEvaluation(int submissionID) {

        String sql =
            "SELECT problemClarityScore, methodologyScore, resultScore, presentationScore, comments " +
            "FROM evaluation " +
            "WHERE submissionID = ? AND status = 'COMPLETED'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, submissionID);
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
}
