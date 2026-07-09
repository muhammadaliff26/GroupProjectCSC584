package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Registration;
import util.DBConnection;

public class RegistrationDAO {

    private static final String JOIN_SELECT =
        "SELECT r.*, u.FULL_NAME, e.EVENT_NAME, e.EVENT_DATE, e.STATUS AS EVENT_STATUS "
      + "FROM REGISTRATIONS r "
      + "JOIN USERS u ON r.USER_ID = u.USER_ID "
      + "JOIN RUN_EVENTS e ON r.EVENT_ID = e.EVENT_ID ";

    /**
     * Returns every registration grouped by event (all runners of one event
     * together, in event date order), and within each event ordered by
     * registration date - so the list is never "mixed" across events.
     */
    public List<Registration> getAllRegistrations() throws SQLException {
        List<Registration> list = new ArrayList<>();
        String sql = JOIN_SELECT + "ORDER BY e.EVENT_DATE ASC, e.EVENT_NAME ASC, r.REGISTRATION_DATE ASC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public List<Registration> getRegistrationsByUser(int userId) throws SQLException {
        List<Registration> list = new ArrayList<>();
        String sql = JOIN_SELECT + "WHERE r.USER_ID = ? ORDER BY e.EVENT_DATE ASC, e.EVENT_NAME ASC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    public boolean isAlreadyRegistered(int userId, int eventId) throws SQLException {
        String sql = "SELECT REGISTRATION_ID FROM REGISTRATIONS WHERE USER_ID=? AND EVENT_ID=? "
                + "AND STATUS <> 'CANCELLED'";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /** CREATE - participant registers for an event */
    public boolean addRegistration(Registration r) throws SQLException {
        String sql = "INSERT INTO REGISTRATIONS (USER_ID, EVENT_ID, BIB_NUMBER, STATUS) "
                + "VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, r.getUserId());
            ps.setInt(2, r.getEventId());
            ps.setString(3, r.getBibNumber());
            ps.setString(4, r.getStatus() == null ? "PENDING" : r.getStatus());
            return ps.executeUpdate() > 0;
        }
    }

    /** UPDATE - admin changes registration status (e.g. CONFIRMED) */
    public boolean updateStatus(int registrationId, String status) throws SQLException {
        String sql = "UPDATE REGISTRATIONS SET STATUS=? WHERE REGISTRATION_ID=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, registrationId);
            return ps.executeUpdate() > 0;
        }
    }

    /** Records a runner's finish time once the event is COMPLETED. */
    public boolean updateFinishTime(int registrationId, String finishTime) throws SQLException {
        String sql = "UPDATE REGISTRATIONS SET FINISH_TIME=? WHERE REGISTRATION_ID=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, finishTime);
            ps.setInt(2, registrationId);
            return ps.executeUpdate() > 0;
        }
    }

    /** DELETE - cancel / remove a registration */
    public boolean deleteRegistration(int registrationId) throws SQLException {
        String sql = "DELETE FROM REGISTRATIONS WHERE REGISTRATION_ID = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, registrationId);
            return ps.executeUpdate() > 0;
        }
    }

    public int countRegistrations() throws SQLException {
        String sql = "SELECT COUNT(*) FROM REGISTRATIONS";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    /** Generates a simple sequential bib number, e.g. BIB-0007 */
    public String generateBibNumber() throws SQLException {
        String sql = "SELECT COUNT(*) FROM REGISTRATIONS";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            int next = 1;
            if (rs.next()) next = rs.getInt(1) + 1;
            return String.format("BIB-%04d", next);
        }
    }

    private Registration mapRow(ResultSet rs) throws SQLException {
        Registration r = new Registration();
        r.setRegistrationId(rs.getInt("REGISTRATION_ID"));
        r.setUserId(rs.getInt("USER_ID"));
        r.setParticipantName(rs.getString("FULL_NAME"));
        r.setEventId(rs.getInt("EVENT_ID"));
        r.setEventName(rs.getString("EVENT_NAME"));
        r.setBibNumber(rs.getString("BIB_NUMBER"));
        r.setRegistrationDate(rs.getTimestamp("REGISTRATION_DATE"));
        r.setStatus(rs.getString("STATUS"));
        r.setFinishTime(rs.getString("FINISH_TIME"));
        r.setEventDate(rs.getDate("EVENT_DATE"));
        r.setEventStatus(rs.getString("EVENT_STATUS"));
        return r;
    }
}
