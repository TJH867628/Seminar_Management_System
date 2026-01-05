package model;

public class Evaluation {

    private int problemClarity;
    private int methodology;
    private int results;
    private int presentation;
    private String comments;
    private int totalScore;

    public Evaluation(int pc, int m, int r, int p, String comments) {
        this.problemClarity = pc;
        this.methodology = m;
        this.results = r;
        this.presentation = p;
        this.comments = comments;
        calculateTotalScore();
    }

    private void calculateTotalScore() {
        totalScore = problemClarity + methodology + results + presentation;
    }

    public int getTotalScore() {
        return totalScore;
    }
}
