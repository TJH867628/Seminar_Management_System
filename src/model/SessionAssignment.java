package model;
import java.sql.Time;

public class SessionAssignment {
    private int sessionId;
    private String venue;
    private String sessionType;
    private Time timeSlot;
    private int submissionCount;
    private int evaluatorCount;

    public SessionAssignment(int sessionId, String venue, String sessionType, Time timeSlot, int submissionCount, int evaluatorCount) {
        this.sessionId = sessionId;
        this.venue = venue;
        this.sessionType = sessionType;
        this.timeSlot = timeSlot;
        this.submissionCount = submissionCount;
        this.evaluatorCount = evaluatorCount;
    }

    public int getSessionId() {
        return sessionId;
    }

    public String getVenue() {
        return venue;
    }

    public String getSessionType() {
        return sessionType;
    }

    public Time getTimeSlot() {
        return timeSlot;
    }

    public int getSubmissionCount() {
        return submissionCount;
    }

    public int getEvaluatorCount() {
        return evaluatorCount;
    }

    public Object[] toRow() {
        return new Object[] {
                sessionId,
                venue,
                sessionType,
                timeSlot,
                submissionCount,
                evaluatorCount
        };
    }
}