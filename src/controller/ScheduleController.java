package controller;

import dao.SessionDAO;
import model.Session;

import java.util.List;

public class ScheduleController {

    private SessionDAO sessionDAO;

    public ScheduleController() {
        this.sessionDAO = new SessionDAO();
    }

    //Generate schedule and load all sessions with evaluation results.
    //This will be called when "View Schedule & Report" is clicked.
    
    //@return List of all sessions with time slots and evaluation results loaded.
    
    public List<Session> generateScheduleAndReport() {
        // The DAO method already handles schedule generation and evaluation loading
        return sessionDAO.generateScheduleAndReport();
    }
}
