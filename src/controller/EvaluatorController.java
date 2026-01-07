package controller;

import dao.EvaluatorDAO;
import model.AssignedEvaluation;
import model.Evaluator;
import view.evaluator.EvaluationForm;
import view.evaluator.EvaluatorDashboard;

import javax.swing.*;
import java.util.List;

public class EvaluatorController {

    private final Evaluator evaluator;
    private final EvaluatorDAO dao;

    public EvaluatorController(Evaluator evaluator) {
        this.evaluator = evaluator;
        this.dao = new EvaluatorDAO();

        System.out.println("Controller created, evaluatorID = "
                + evaluator.getEvaluatorID());
    }

    public List<AssignedEvaluation> getAssignedEvaluations() {
        System.out.println("Controller method ENTERED");

        int evaluatorID = evaluator.getEvaluatorID();
        System.out.println("Controller fetching for evaluatorID = " + evaluatorID);

        List<AssignedEvaluation> list =
                dao.getAssignedEvaluations(evaluatorID);

        System.out.println("Controller list size = " + list.size());

        return list;
    }

    public void openEvaluationForm(JFrame parent, AssignedEvaluation ae) {
        new EvaluationForm(ae, evaluator);
    }

    public void backToDashboard() {
        new EvaluatorDashboard(evaluator);
    }
}
