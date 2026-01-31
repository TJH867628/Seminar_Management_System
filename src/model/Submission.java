package model;

import java.sql.Timestamp;

public class Submission {

    private int submissionID;
    private String researchTitle;
    private String filePath;
    private int studentID;
    private String abstracts;
    private String supervisorName;
    private String presentationType;
    private String status;
    private Timestamp createdDate;

    // ✅ FULL constructor (USED BY DAO)
    public Submission(
            int submissionID,
            String researchTitle,
            String filePath,
            int studentID,
            String abstracts,
            String supervisorName,
            String presentationType,
            String status,
            Timestamp createdDate
    ) {
        this.submissionID = submissionID;
        this.researchTitle = researchTitle;
        this.filePath = filePath;
        this.studentID = studentID;
        this.abstracts = abstracts;
        this.supervisorName = supervisorName;
        this.presentationType = presentationType;
        this.status = status;
        this.createdDate = createdDate;
    }

    //  LIGHT constructor (USED BY Create/Edit)
    public Submission(
            String researchTitle,
            String filePath,
            int studentID,
            String abstracts,
            String supervisorName,
            String presentationType
    ) {
        this(0, researchTitle, filePath, studentID,
             abstracts, supervisorName, presentationType,
             "submitted", null);
    }

    public int getSubmissionID() { return submissionID; }
    public String getResearchTitle() { return researchTitle; }
    public String getFilePath() { return filePath; }
    public int getStudentID() { return studentID; }
    public String getAbstracts() { return abstracts; }
    public String getSupervisorName() { return supervisorName; }
    public String getPresentationType() { return presentationType; }
    public String getStatus() { return status; }
    public Timestamp getCreatedDate() { return createdDate; }
}
