package model;

public class Evaluator extends User{
    protected int evaluatorID;
    protected String expertise;
    protected int sessionID;

    public Evaluator(int userID, String email, String name, int evaluatorID, String expertise) {
        super(userID, email, name, "Evaluator");

        this.evaluatorID = evaluatorID;
        this.expertise = expertise;
    }

    public int getEvaluatorID() {
        return evaluatorID;
    }

    public String getExpertise() {
        return expertise;
    }
}
