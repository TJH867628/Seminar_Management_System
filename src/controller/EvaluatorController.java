package controller;

import dao.EvaluatorDAO;
import model.AssignedEvaluation;
import model.Evaluator;
import app.AppNavigator;

import java.util.List;

import javax.swing.JFrame;

public class EvaluatorController {
    private Evaluator evaluator;
    private EvaluatorDAO dao;

    public EvaluatorController(Evaluator evaluator) {
        this.evaluator = evaluator;
        this.dao = new EvaluatorDAO();
    }

    public List<AssignedEvaluation> getAssignedEvaluations() {
        return dao.getAssignedEvaluations(evaluator.getEvaluatorID());
    }

    public void openEvaluationForm(JFrame parentFrame,AssignedEvaluation evaluation) {
        AppNavigator.openEvaluationForm(parentFrame, evaluation, evaluator);
    }

    public void backToDashboard() {
        AppNavigator.openDashboard(evaluator);
    }
}
