package model;

public class AssignedEvaluation {

    private String studentName;
    private int sessionID;
    private String date;
    private String venue;
    private String filePath;
    private int submissionID;
    private int totalScore;
    private String status;

    public AssignedEvaluation(String studentName, int sessionID,
                              String date, String venue, String filePath, int submissionID, int totalScore, String status) {
        this.studentName = studentName;
        this.sessionID = sessionID;
        this.date = date;
        this.venue = venue;
        this.filePath = filePath;
        this.submissionID = submissionID;
        this.totalScore = totalScore;
        this.status = status;
    }

    public String getStudentName() {
        return studentName;
    }

    public int getSessionID() {
        return sessionID;
    }

    public String getDate() {
        return date;
    }

    public String getVenue() {
        return venue;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getSubmissionID() {
        return submissionID;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public String getStatus() {
        return status;
    }
    
    public Object[] toRow() {
        return new Object[]{
            submissionID,
            studentName,
            sessionID,
            date,
            venue,
            "Open File",
            totalScore > 0 ? totalScore : "No Marking",
            status
        };
    }
}
