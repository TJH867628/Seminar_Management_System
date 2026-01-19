package controller;

import dao.SessionDAO;
import model.Session;
import service.ReportService;

import java.util.List;

public class ScheduleController {

    private SessionDAO dao = new SessionDAO();

    public List<Session> generateScheduleAndReport() {

        List<Session> sessions = dao.getApprovedSessions();
        dao.generateSchedule(sessions);
        ReportService.generateReport(sessions);

        return sessions;
    }
}
