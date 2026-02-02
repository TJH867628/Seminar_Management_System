package controller;

import dao.CoordinatorDAO;
import java.util.*;

import model.Award;
import model.Coordinator;
import model.Evaluator;
import model.SessionAssignment;
import model.Submission;

public class CoordinatorController {
    private Coordinator coordinator;
    private static CoordinatorDAO dao = new CoordinatorDAO();

    public CoordinatorController(Coordinator coordinator) {
        this.coordinator = coordinator;
    }

    public List<SessionAssignment> getAllSessionAssignments() {
        return dao.getAllSessionAssignments();
    }

    public List<Evaluator> getAssignedEvaluators(int sessionId) {
        return dao.getEvaluatorsBySession(sessionId);
    }

    public List<Evaluator> getAvailableEvaluators(int sessionId) {
        return dao.getAvailableEvaluators(sessionId);
    }

    public boolean assignEvaluatorToSession(int sessionId, int evaluatorId) {
        return dao.assignEvaluatorToSession(sessionId, evaluatorId);
    }

    public boolean removeEvaluatorFromSession(int sessionId, int evaluatorId) {
        return dao.removeEvaluatorFromSession(sessionId, evaluatorId);
    }

    public List<Submission> getAssigedSubmissions(int sessionId) {
        return dao.getSubmissionsBySession(sessionId);
    }

    public List<Submission> getAvailableSubmissions(int sessionId) {
        return dao.getAvailableSubmissions(sessionId);
    }

    public boolean assignSubmissionToSession(int sessionId, int submissionId) {
        return dao.assignSubmissionToSession(sessionId, submissionId);
    }

    public boolean removeSubmissionFromSession(int sessionId, int submissionId) {
        return dao.removeSubmissionFromSession(sessionId, submissionId);
    }

    public List<Award> getAwardWinners() {
        CoordinatorDAO dao = new CoordinatorDAO();
        return dao.getAwardWinners();
    }
}
