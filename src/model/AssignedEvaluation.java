package model;

public class AssignedEvaluation {

    private String studentName;
    private String sessionID;
    private String date;
    private String venue;
    private String filePath;

    public AssignedEvaluation(String studentName, String sessionID,
                              String date, String venue, String filePath) {
        this.studentName = studentName;
        this.sessionID = sessionID;
        this.date = date;
        this.venue = venue;
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }
    
    public Object[] toRow() {
        return new Object[]{
            studentName,
            sessionID,
            date,
            venue,
            "Open File",
            "Assigned"
        };
    }
}
