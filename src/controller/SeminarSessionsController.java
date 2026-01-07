package controller;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import dao.SeminarSessionDAO;

import java.sql.Date;

import model.Session;
import dao.SeminarSessionDAO;

public class SeminarSessionsController {
    private SeminarSessionDAO dao;

    public SeminarSessionsController() {
        this.dao = new SeminarSessionDAO();
    }

    public List<Session> getAllSeminarSessions() {

        return dao.getAllSeminarSessions();
    }

    public boolean AddNewSeminarSession(Session session) {
        return dao.AddNewSeminarSession(session);
    }

    public boolean updateSeminarSession(Session session) {
        return dao.updateSeminarSession(session);
    }

}
