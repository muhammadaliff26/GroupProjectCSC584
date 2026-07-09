package model;

import java.sql.Timestamp;

public class Registration {
    private int registrationId;
    private int userId;
    private String participantName;  // populated by JOIN for display
    private int eventId;
    private String eventName;        // populated by JOIN for display
    private String bibNumber;
    private Timestamp registrationDate;
    private String status;           // PENDING, CONFIRMED, CANCELLED
    private String finishTime;       // e.g. "01:52:30" - filled in once the event is COMPLETED
    private java.sql.Date eventDate; // populated by JOIN, used to group/sort by event
    private String eventStatus;      // populated by JOIN (UPCOMING/ONGOING/COMPLETED/CANCELLED)

    public Registration() { }

    public int getRegistrationId() { return registrationId; }
    public void setRegistrationId(int registrationId) { this.registrationId = registrationId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getParticipantName() { return participantName; }
    public void setParticipantName(String participantName) { this.participantName = participantName; }

    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }

    public String getBibNumber() { return bibNumber; }
    public void setBibNumber(String bibNumber) { this.bibNumber = bibNumber; }

    public Timestamp getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(Timestamp registrationDate) { this.registrationDate = registrationDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getFinishTime() { return finishTime; }
    public void setFinishTime(String finishTime) { this.finishTime = finishTime; }

    public java.sql.Date getEventDate() { return eventDate; }
    public void setEventDate(java.sql.Date eventDate) { this.eventDate = eventDate; }

    public String getEventStatus() { return eventStatus; }
    public void setEventStatus(String eventStatus) { this.eventStatus = eventStatus; }
}
