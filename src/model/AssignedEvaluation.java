package model;

public class AssignedEvaluation {

    private String studentName;
    private String sessionID;
    private String date;
    private String venue;
    private String filePath;
    private int submissionID;
    private int totalScore;


    public AssignedEvaluation(String studentName, String sessionID,
                              String date, String venue, String filePath, int submissionID, int totalScore) {
        this.studentName = studentName;
        this.sessionID = sessionID;
        this.date = date;
        this.venue = venue;
        this.filePath = filePath;
        this.submissionID = submissionID;
        this.totalScore = totalScore;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getSessionID() {
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
    
    public Object[] toRow() {
        return new Object[]{
            submissionID,
            studentName,
            sessionID,
            date,
            venue,
            "Open File",
            totalScore > 0 ? totalScore : "No Marking",
            "Assigned"
        };
    }
}
