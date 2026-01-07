package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import util.DBConnection;

import model.Session;

public class SeminarSessionDAO {
    public List<Session> getAllSeminarSessions() {
        String sql = "SELECT * FROM sessions";
        List<Session> list = new ArrayList<>();


        try(Connection conn = DBConnection.getConnection()){

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                list.add(new Session(
                    rs.getInt("id"),
                    rs.getString("venue"),
                    rs.getString("sessionType"),
                    rs.getDate("date"),
                    rs.getTime("timeSlot")
                ));
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean AddNewSeminarSession(Session session) {
        String sql = "INSERT INTO sessions (venue, sessionType, date, timeSlot, createdAt, updatedAt) VALUES (?, ?, ?, ?, NOW(), NOW())";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, session.getVenue());
            ps.setString(2, session.getSessionType());
            ps.setDate(3, new java.sql.Date(session.getDate().getTime()));
            ps.setTime(4, session.getTimeSlot());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateSeminarSession(Session session) {
        String sql = "UPDATE sessions SET venue = ?, sessionType = ?, date = ?, timeSlot = ?, updatedAt = NOW() WHERE id = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, session.getVenue());
            ps.setString(2, session.getSessionType());
            ps.setDate(3, new java.sql.Date(session.getDate().getTime()));
            ps.setTime(4, session.getTimeSlot());
            ps.setInt(5, session.getSessionID());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
