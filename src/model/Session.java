package model;
import java.sql.Time;
import java.util.*;

public class Session {

    private int sessionID;
    private String venue;
    private Date date;
    private String sessionType;
    private Time timeSlot;

    private List<Integer> studentIDs;
    private List<Integer> evaluatorIDs;

    public Session(int sessionID, String venue, String sessionType, Date date,Time timeSlot) {
        this.sessionID = sessionID;
        this.venue = venue;
        this.date = date;
        this.timeSlot = timeSlot;
        this.sessionType = sessionType;
        this.studentIDs = new ArrayList<>();
        this.evaluatorIDs = new ArrayList<>();
    }

    public Session(int sessionID, String venue, Date date) {
        this.sessionID = sessionID;
        this.venue = venue;
        this.date = date;
    
        // optional defaults
        this.sessionType = "";
        this.timeSlot = null;
    
        this.studentIDs = new ArrayList<>();
        this.evaluatorIDs = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Session " + sessionID + " | " + venue + " | " + date;
    }

    public int getSessionID() {
        return sessionID;
    }

    public String getVenue() {
        return venue;
    }

    public Date getDate() {
        return date;
    }

    public String getSessionType() {
        return sessionType;
    }

    public Time getTimeSlot() {
        return timeSlot;
    }

    public void addStudent(int studentID) {
        studentIDs.add(studentID);
    }

    public void addEvaluator(int evaluatorID) {
        evaluatorIDs.add(evaluatorID);
    }

    public Object[] toRow() {
        return new Object[]{
            sessionID,
            date.toString(),
            venue,
            sessionType,
            timeSlot.toString(),
    
        };
    }
}
