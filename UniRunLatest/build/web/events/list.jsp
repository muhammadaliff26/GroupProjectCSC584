<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.RunEvent" %>
<%@ include file="../includes/header.jsp" %>

<div class="d-flex justify-content-between align-items-center mb-3">
    <h2><i class="fa-solid fa-list-check"></i> Manage Run Events</h2>
    <a class="btn btn-unirun" href="<%= request.getContextPath() %>/EventServlet?action=new">
        <i class="fa-solid fa-plus"></i> Add New Event
    </a>
</div>

<% if (request.getParameter("msg") != null) { %>
<div class="alert alert-success"><%= request.getParameter("msg") %></div>
<% } %>

<div class="card shadow-sm">
    <div class="card-body p-0">
        <table class="table table-hover mb-0">
            <thead class="table-light">
                <tr>
                    <th>#</th>
                    <th>Event Name</th>
                    <th>Date</th>
                    <th>Location</th>
                    <th>Category</th>
                    <th>Status</th>
                    <th class="text-end">Actions</th>
                </tr>
            </thead>
            <tbody>
            <%
                List<RunEvent> events = (List<RunEvent>) request.getAttribute("events");
                if (events != null && !events.isEmpty()) {
                    for (RunEvent ev : events) {
            %>
                <tr>
                    <td><%= ev.getEventId() %></td>
                    <td><%= ev.getEventName() %></td>
                    <td><%= ev.getEventDate() %></td>
                    <td><%= ev.getLocation() %></td>
                    <td><%= ev.getCategoryName() %> (<%= ev.getDistanceKm() %> km)</td>
                    <td><span class="badge status-badge-<%= ev.getStatus() %>"><%= ev.getStatus() %></span></td>
                    <td class="text-end">
                        <% if (!"COMPLETED".equals(ev.getStatus()) && !"CANCELLED".equals(ev.getStatus())) { %>
                        <a class="btn btn-sm btn-outline-success"
                           href="<%= request.getContextPath() %>/EventServlet?action=finish&id=<%= ev.getEventId() %>"
                           onclick="return confirm('Mark this event as finished? You will then be able to record runners\' finish times.');">
                           <i class="fa-solid fa-flag-checkered"></i> Mark Finished
                        </a>
                        <% } %>
                        <a class="btn btn-sm btn-outline-secondary"
                           href="<%= request.getContextPath() %>/EventServlet?action=edit&id=<%= ev.getEventId() %>">
                           <i class="fa-solid fa-pen"></i> Edit
                        </a>
                        <a class="btn btn-sm btn-outline-danger"
                           href="<%= request.getContextPath() %>/EventServlet?action=delete&id=<%= ev.getEventId() %>"
                           onclick="return confirm('Delete this event? This cannot be undone.');">
                           <i class="fa-solid fa-trash"></i> Delete
                        </a>
                    </td>
                </tr>
            <%
                    }
                } else {
            %>
                <tr><td colspan="7" class="text-center text-muted py-3">No run events yet. Click "Add New Event" to create one.</td></tr>
            <% } %>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="../includes/footer.jsp" %>
