package model;

import java.util.List;

public class SessionAssignmentDetail {

    private Session session;
    private List<Submission> submissions;
    private List<Evaluator> evaluators;

    public SessionAssignmentDetail(Session session, List<Submission> submissions, List<Evaluator> evaluators) {
        this.session = session;
        this.submissions = submissions;
        this.evaluators = evaluators;
    }

    public Session getSession() {
        return session;
    }

    public List<Submission> getSubmissions() {
        return submissions;
    }

    public List<Evaluator> getEvaluators() {
        return evaluators;
    }
}