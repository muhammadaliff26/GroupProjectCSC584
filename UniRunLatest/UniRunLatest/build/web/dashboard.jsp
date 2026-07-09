<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.RunEvent" %>
<%@ page import="model.Registration" %>
<%@ page import="model.User" %>
<%@ include file="includes/header.jsp" %>
<%
    User currentUser = (User) session.getAttribute("user");
%>
<h2><i class="fa-solid fa-gauge-high"></i> Dashboard</h2>
<p class="text-muted">Welcome back, <%= currentUser.getFullName() %>! Here's an overview of Uni-Run.</p>

<div class="row g-3 mb-4">
    <div class="col-md-4">
        <div class="card card-stat shadow-sm">
            <div class="card-body">
                <h6 class="text-muted">Total Run Events</h6>
                <h2><%= request.getAttribute("totalEvents") %></h2>
                <i class="fa-solid fa-calendar-days text-warning"></i>
            </div>
        </div>
    </div>
    <div class="col-md-4">
        <div class="card card-stat shadow-sm">
            <div class="card-body">
                <h6 class="text-muted">Total Registrations</h6>
                <h2><%= request.getAttribute("totalRegistrations") %></h2>
                <i class="fa-solid fa-clipboard-list text-info"></i>
            </div>
        </div>
    </div>
    <div class="col-md-4">
        <div class="card card-stat shadow-sm">
            <div class="card-body">
                <h6 class="text-muted">Total Participants</h6>
                <h2><%= request.getAttribute("totalParticipants") %></h2>
                <i class="fa-solid fa-users text-success"></i>
            </div>
        </div>
    </div>
</div>

<div class="card shadow-sm mb-4">
    <div class="card-header bg-white"><strong><i class="fa-solid fa-calendar-check"></i> Upcoming Run Events</strong></div>
    <div class="card-body p-0">
        <table class="table table-hover mb-0">
            <thead class="table-light">
                <tr>
                    <th>Event Name</th>
                    <th>Date</th>
                    <th>Location</th>
                    <th>Category</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
            <%
                List<RunEvent> upcoming = (List<RunEvent>) request.getAttribute("upcomingEvents");
                if (upcoming != null && !upcoming.isEmpty()) {
                    for (RunEvent ev : upcoming) {
            %>
                <tr>
                    <td><%= ev.getEventName() %></td>
                    <td><%= ev.getEventDate() %></td>
                    <td><%= ev.getLocation() %></td>
                    <td><%= ev.getCategoryName() %> (<%= ev.getDistanceKm() %> km)</td>
                    <td><span class="badge status-badge-<%= ev.getStatus() %>"><%= ev.getStatus() %></span></td>
                </tr>
            <%
                    }
                } else {
            %>
                <tr><td colspan="5" class="text-center text-muted py-3">No upcoming events.</td></tr>
            <% } %>
            </tbody>
        </table>
    </div>
</div>

<% if (!currentUser.isAdmin()) { %>
<div class="card shadow-sm">
    <div class="card-header bg-white"><strong><i class="fa-solid fa-clipboard-list"></i> My Recent Registrations</strong></div>
    <div class="card-body p-0">
        <table class="table table-hover mb-0">
            <thead class="table-light">
                <tr><th>Event</th><th>Bib No.</th><th>Status</th></tr>
            </thead>
            <tbody>
            <%
                List<Registration> myRegs = (List<Registration>) request.getAttribute("myRegistrations");
                if (myRegs != null && !myRegs.isEmpty()) {
                    for (Registration r : myRegs) {
            %>
                <tr>
                    <td><%= r.getEventName() %></td>
                    <td><%= r.getBibNumber() %></td>
                    <td><span class="badge status-badge-<%= r.getStatus() %>"><%= r.getStatus() %></span></td>
                </tr>
            <%
                    }
                } else {
            %>
                <tr><td colspan="3" class="text-center text-muted py-3">You have not registered for any events yet.</td></tr>
            <% } %>
            </tbody>
        </table>
    </div>
</div>
<% } %>

<%@ include file="includes/footer.jsp" %>
