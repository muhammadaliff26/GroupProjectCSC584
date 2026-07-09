package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.RunEvent;
import util.DBConnection;

/** Handles Create / Read / Update / Delete for Run Events (Information Management module). */
public class RunEventDAO {

    private static final String JOIN_SELECT =
        "SELECT e.*, c.CATEGORY_NAME, c.DISTANCE_KM FROM RUN_EVENTS e "
      + "JOIN CATEGORIES c ON e.CATEGORY_ID = c.CATEGORY_ID ";

    public List<RunEvent> getAllEvents() throws SQLException {
        List<RunEvent> list = new ArrayList<>();
        String sql = JOIN_SELECT + "ORDER BY e.EVENT_DATE ASC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public List<RunEvent> getUpcomingEvents(int limit) throws SQLException {
        List<RunEvent> list = new ArrayList<>();
        String sql = JOIN_SELECT + "WHERE e.EVENT_DATE >= CURRENT_DATE ORDER BY e.EVENT_DATE ASC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            int count = 0;
            while (rs.next() && count < limit) {
                list.add(mapRow(rs));
                count++;
            }
        }
        return list;
    }

    public RunEvent getEventById(int eventId) throws SQLException {
        String sql = JOIN_SELECT + "WHERE e.EVENT_ID = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    /** CREATE */
    public boolean addEvent(RunEvent e) throws SQLException {
        String sql = "INSERT INTO RUN_EVENTS (EVENT_NAME, EVENT_DATE, LOCATION, CATEGORY_ID, "
                + "DESCRIPTION, STATUS, CREATED_BY) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, e.getEventName());
            ps.setDate(2, e.getEventDate());
            ps.setString(3, e.getLocation());
            ps.setInt(4, e.getCategoryId());
            ps.setString(5, e.getDescription());
            ps.setString(6, e.getStatus() == null ? "UPCOMING" : e.getStatus());
            ps.setInt(7, e.getCreatedBy());
            return ps.executeUpdate() > 0;
        }
    }

    /** UPDATE */
    public boolean updateEvent(RunEvent e) throws SQLException {
        String sql = "UPDATE RUN_EVENTS SET EVENT_NAME=?, EVENT_DATE=?, LOCATION=?, "
                + "CATEGORY_ID=?, DESCRIPTION=?, STATUS=? WHERE EVENT_ID=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, e.getEventName());
            ps.setDate(2, e.getEventDate());
            ps.setString(3, e.getLocation());
            ps.setInt(4, e.getCategoryId());
            ps.setString(5, e.getDescription());
            ps.setString(6, e.getStatus());
            ps.setInt(7, e.getEventId());
            return ps.executeUpdate() > 0;
        }
    }

    /** Quick action: admin marks an event as finished so finish times can be recorded. */
    public boolean markAsCompleted(int eventId) throws SQLException {
        String sql = "UPDATE RUN_EVENTS SET STATUS = 'COMPLETED' WHERE EVENT_ID = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, eventId);
            return ps.executeUpdate() > 0;
        }
    }

    /** DELETE */
    public boolean deleteEvent(int eventId) throws SQLException {
        String sql = "DELETE FROM RUN_EVENTS WHERE EVENT_ID = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, eventId);
            return ps.executeUpdate() > 0;
        }
    }

    public int countEvents() throws SQLException {
        String sql = "SELECT COUNT(*) FROM RUN_EVENTS";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    private RunEvent mapRow(ResultSet rs) throws SQLException {
        RunEvent e = new RunEvent();
        e.setEventId(rs.getInt("EVENT_ID"));
        e.setEventName(rs.getString("EVENT_NAME"));
        e.setEventDate(rs.getDate("EVENT_DATE"));
        e.setLocation(rs.getString("LOCATION"));
        e.setCategoryId(rs.getInt("CATEGORY_ID"));
        e.setCategoryName(rs.getString("CATEGORY_NAME"));
        e.setDistanceKm(rs.getDouble("DISTANCE_KM"));
        e.setDescription(rs.getString("DESCRIPTION"));
        e.setStatus(rs.getString("STATUS"));
        e.setCreatedBy(rs.getInt("CREATED_BY"));
        return e;
    }
}
