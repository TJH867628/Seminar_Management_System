package model; 

public class EvaluationResult {

    private int submissionID;
    private int evaluatorID;
    private int problemClarifyScore;
    private int methodologyScore;
    private int resultScore;
    private int presentationScore;
    private int totalScore;
    private String comments;
    private String status;

    public EvaluationResult(int submissionID, int evaluatorID, int problemClarifyScore,
                            int methodologyScore, int resultScore, int presentationScore,
                            int totalScore, String comments, String status) {
        this.submissionID = submissionID;
        this.evaluatorID = evaluatorID;
        this.problemClarifyScore = problemClarifyScore;
        this.methodologyScore = methodologyScore;
        this.resultScore = resultScore;
        this.presentationScore = presentationScore;
        this.totalScore = totalScore;
        this.comments = comments;
        this.status = status;
    }

    public int getSubmissionID() { return submissionID; }
    public int getEvaluatorID() { return evaluatorID; }
    public int getProblemClarifyScore() { return problemClarifyScore; }
    public int getMethodologyScore() { return methodologyScore; }
    public int getResultScore() { return resultScore; }
    public int getPresentationScore() { return presentationScore; }
    public int getTotalScore() { return totalScore; }
    public String getComments() { return comments; }
    public String getStatus() { return status; }
}
