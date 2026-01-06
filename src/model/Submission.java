package model;

public class Submission {
    private int submissionID;
    private String researchTitle;
    private String filePath;
    private int studentID;
    private String abstracts;
    private String supervisorName;
    private String status;

    public Submission(int submissionID, String researchTitle, String filePath, int studentID, String abstracts, String supervisorName, String status) {
        this.submissionID = submissionID;
        this.researchTitle = researchTitle;
        this.filePath = filePath;
        this.studentID = studentID;
        this.abstracts = abstracts;
        this.supervisorName = supervisorName;
        this.status = status;
    }

    public int getSubmissionID() {
        return submissionID;
    }

    public String getResearchTitle() {
        return researchTitle;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getStudentID() {
        return studentID;
    }

    public String getAbstracts() {
        return abstracts;
    }

    public String getSupervisorName() {
        return supervisorName;
    }

    public String getStatus() {
        return status;
    }
}
