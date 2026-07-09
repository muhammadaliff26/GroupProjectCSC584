<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.RunEvent" %>
<%@ include file="../includes/header.jsp" %>
<h2><i class="fa-solid fa-clipboard-check"></i> Register for an Event</h2>

<% if (request.getAttribute("error") != null) { %>
<div class="alert alert-danger"><%= request.getAttribute("error") %></div>
<% } %>

<div class="card shadow-sm">
    <div class="card-body">
        <form action="<%= request.getContextPath() %>/RegistrationServlet" method="post">
            <div class="mb-3">
                <label class="form-label">Choose Run Event</label>
                <select class="form-select" name="eventId" required>
                    <option value="" disabled selected>-- Select an event --</option>
                    <%
                        List<RunEvent> events = (List<RunEvent>) request.getAttribute("events");
                        if (events != null) {
                            for (RunEvent ev : events) {
                    %>
                    <option value="<%= ev.getEventId() %>">
                        <%= ev.getEventName() %> - <%= ev.getEventDate() %> (<%= ev.getCategoryName() %>, <%= ev.getDistanceKm() %> km)
                    </option>
                    <% } } %>
                </select>
            </div>
            <button type="submit" class="btn btn-unirun"><i class="fa-solid fa-check"></i> Submit Registration</button>
            <a href="<%= request.getContextPath() %>/RegistrationServlet" class="btn btn-outline-secondary">Cancel</a>
        </form>
    </div>
</div>

<%@ include file="../includes/footer.jsp" %>
