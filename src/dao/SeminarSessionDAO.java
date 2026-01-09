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
            

            while(rs.next()) {
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

    public Session getSeminarSessionById(int sessionId) {
        String sql = "SELECT * FROM sessions WHERE id = ?";
        Session session = null;

        try(Connection conn = DBConnection.getConnection()){

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, sessionId);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                session = new Session(
                    rs.getInt("id"),
                    rs.getString("venue"),
                    rs.getString("sessionType"),
                    rs.getDate("date"),
                    rs.getTime("timeSlot")
                );
            }

            return session;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return session;
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

    public boolean deleteSeminarSession(int sessionId) {
        //Check if the sessionID is used in assigned_session or submissions table before deleting
        String checkSql1 = "SELECT COUNT(*) AS count FROM assigned_session WHERE sessionID = ?";
        String checkSql2 = "SELECT COUNT(*) AS count FROM submissions WHERE sessionID = ?";
        String sql = "DELETE FROM sessions WHERE id = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement checkPs1 = conn.prepareStatement(checkSql1); PreparedStatement checkPs2 = conn.prepareStatement(checkSql2); PreparedStatement ps = conn.prepareStatement(sql)) {
            checkPs1.setInt(1, sessionId);
            ResultSet rs1 = checkPs1.executeQuery();
            checkPs2.setInt(1, sessionId);
            ResultSet rs2 = checkPs2.executeQuery();
            if (rs1.next() && rs1.getInt("count") > 0 || rs2.next() && rs2.getInt("count") > 0) {
                return false;
            }

            ps.setInt(1, sessionId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
