package controller;

import dao.EvaluatorDAO;
import model.AssignedEvaluation;
import model.Evaluator;
import view.evaluator.EvaluationForm;
import view.evaluator.EvaluatorDashboard;
import view.evaluator.VotingForm;

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

    // Fetch projects for voting
    public List<AssignedEvaluation> getStudentProjectVotes() {
        return dao.getStudentProjectVotes(evaluator.getEvaluatorID());
    }

    // Open VotingForm
    public void openVoteForm(JFrame parent, AssignedEvaluation project) {
        new VotingForm(project, evaluator); // VotingForm already accepts AssignedEvaluation
    }

    // EvaluatorController.java
    public boolean castVote(int submissionID) {
        return dao.castVote(evaluator.getEvaluatorID(), submissionID);
    }

    // In EvaluatorController.java
    // Check if evaluator has voted on a specific submission
    public boolean hasVoted(int submissionID) {
        return dao.hasVoted(evaluator.getEvaluatorID(), submissionID);
    }
}