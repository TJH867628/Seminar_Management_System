package model;

public class Award {
    private int awardID;
    private String awardName;
    private String category;
    private String criteria;
    private int submissionID;
    private String studentName;
    private String presentationType;
    private int scoreOrVotes;

    public Award(int awardID, String awardName, String category, String criteria,
                 int submissionID, String studentName, String presentationType, int scoreOrVotes) {
        this.awardID = awardID;
        this.awardName = awardName;
        this.category = category;
        this.criteria = criteria;
        this.submissionID = submissionID;
        this.studentName = studentName;
        this.presentationType = presentationType;
        this.scoreOrVotes = scoreOrVotes;
    }

    // Getters
    public int getAwardID() { return awardID; }
    public String getAwardName() { return awardName; }
    public String getCategory() { return category; }
    public String getCriteria() { return criteria; }
    public int getSubmissionID() { return submissionID; }
    public String getStudentName() { return studentName; }
    public String getPresentationType() { return presentationType; }
    public int getScoreOrVotes() { return scoreOrVotes; }
}
