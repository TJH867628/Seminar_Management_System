package model;

public class Evaluation {

    private int problemClarity;
    private int methodology;
    private int results;
    private int presentation;
    private String comments;

    public Evaluation(int pc, int m, int r, int p, String comments) {
        this.problemClarity = pc;
        this.methodology = m;
        this.results = r;
        this.presentation = p;
        this.comments = comments;
    }

    public int getProblemClarity() { return problemClarity; }
    public int getMethodology() { return methodology; }
    public int getResults() { return results; }
    public int getPresentation() { return presentation; }
    public String getComments() { return comments; }

    public int getTotalScore() {
        return problemClarity + methodology + results + presentation;
    }
}

