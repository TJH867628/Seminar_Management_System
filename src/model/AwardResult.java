package model;

public class AwardResult {

    private int awardID;
    private String awardName;
    private String category;
    private String criteria;

    private int submissionID;
    private String studentName;
    private String presentationType;

    private int totalScore;
    private int totalVotes;

    public AwardResult(int submissionID,
                       String studentName,
                       String presentationType,
                       int totalScore,
                       int totalVotes) {
        this.submissionID = submissionID;
        this.studentName = studentName;
        this.presentationType = presentationType;
        this.totalScore = totalScore;
        this.totalVotes = totalVotes;
    }

    public int getAwardID() {
        return awardID;
    }

    public String getAwardName() {
        return awardName;
    }

    public String getCategory() {
        return category;
    }

    public String getCriteria() {
        return criteria;
    }

    public int getSubmissionID() {
        return submissionID;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getPresentationType() {
        return presentationType;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getTotalVotes() {
        return totalVotes;
    }
}
