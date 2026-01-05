package model;
import java.util.*;


public class Session {

    private int sessionID;
    private String venue;
    private String date;
    private String timeSlot;

    private List<Integer> studentIDs;
    private List<Integer> evaluatorIDs;

    public Session(int sessionID, String venue, String date, String timeSlot) {
        this.sessionID = sessionID;
        this.venue = venue;
        this.date = date;
        this.timeSlot = timeSlot;
        this.studentIDs = new ArrayList<>();
        this.evaluatorIDs = new ArrayList<>();
    }

    public void addStudent(int studentID) {
        studentIDs.add(studentID);
    }

    public void addEvaluator(int evaluatorID) {
        evaluatorIDs.add(evaluatorID);
    }
}
