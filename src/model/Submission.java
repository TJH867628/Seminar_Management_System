package model;

public class Submission {

    private int submissionID;
    private String researchTitle;
    private String filePath;
    private int studentID;
    private String abstracts;
    private String supervisorName;
    private String presentationType;
    private String status;

    // =========================
    // FULL CONSTRUCTOR (NEW)
    // =========================
    public Submission(
            int submissionID,
            String researchTitle,
            String filePath,
            int studentID,
            String abstracts,
            String supervisorName,
            String presentationType,
            String status
    ) {
        this.submissionID = submissionID;
        this.researchTitle = researchTitle;
        this.filePath = filePath;
        this.studentID = studentID;
        this.abstracts = abstracts;
        this.supervisorName = supervisorName;
        this.presentationType = presentationType;
        this.status = status;
    }

    // =========================
    // BACKWARD-COMPAT CONSTRUCTOR
    // (for old DAO/View code)
    // =========================
    public Submission(
            int submissionID,
            String researchTitle,
            String filePath,
            int studentID,
            String abstracts,
            String supervisorName,
            String status
    ) {
        this(
            submissionID,
            researchTitle,
            filePath,
            studentID,
            abstracts,
            supervisorName,
            "Oral",     // default type
            status
        );
    }

    // =========================
    // GETTERS
    // =========================
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

    public String getPresentationType() {
        return presentationType;
    }

    public String getStatus() {
        return status;
    }
}
