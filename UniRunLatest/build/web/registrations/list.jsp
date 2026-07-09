<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Registration" %>
<%@ page import="model.User" %>
<%@ include file="../includes/header.jsp" %>
<%
    User currentUser = (User) session.getAttribute("user");
%>
<div class="d-flex justify-content-between align-items-center mb-3">
    <h2><i class="fa-solid fa-clipboard-list"></i> <%= currentUser.isAdmin() ? "All Registrations" : "My Registrations" %></h2>
    <% if (!currentUser.isAdmin()) { %>
    <a class="btn btn-unirun" href="<%= request.getContextPath() %>/RegistrationServlet?action=new">
        <i class="fa-solid fa-plus"></i> Register for an Event
    </a>
    <% } %>
</div>

<% if (request.getParameter("msg") != null) { %>
<div class="alert alert-success"><%= request.getParameter("msg") %></div>
<% } %>
<% if (request.getAttribute("error") != null) { %>
<div class="alert alert-danger"><%= request.getAttribute("error") %></div>
<% } %>

<%
    List<Registration> regs = (List<Registration>) request.getAttribute("registrations");
%>

<% if (regs == null || regs.isEmpty()) { %>
    <div class="card shadow-sm">
        <div class="card-body text-center text-muted py-4">No registrations found.</div>
    </div>
<% } else {
    int currentEventId = -1;
    for (Registration r : regs) {
        // Whenever the event changes, close the previous event's card/table and start a new one.
        if (r.getEventId() != currentEventId) {
            if (currentEventId != -1) {
%>
            </tbody></table></div></div>
<%
            }
            currentEventId = r.getEventId();
%>
    <div class="card shadow-sm mb-4">
        <div class="card-header bg-white d-flex justify-content-between align-items-center">
            <div>
                <strong><i class="fa-solid fa-calendar-days"></i> <%= r.getEventName() %></strong>
                <span class="text-muted"> - <%= r.getEventDate() %></span>
            </div>
            <span class="badge status-badge-<%= r.getEventStatus() %>"><%= r.getEventStatus() %></span>
        </div>
        <div class="card-body p-0">
            <table class="table table-hover mb-0">
                <thead class="table-light">
                    <tr>
                        <th>#</th>
                        <% if (currentUser.isAdmin()) { %><th>Participant</th><% } %>
                        <th>Bib No.</th>
                        <th>Status</th>
                        <th>Finish Time</th>
                        <th class="text-end">Actions</th>
                    </tr>
                </thead>
                <tbody>
<%
        } // end new-event header

        boolean eventFinished = "COMPLETED".equals(r.getEventStatus());
%>
                <tr>
                    <td><%= r.getRegistrationId() %></td>
                    <% if (currentUser.isAdmin()) { %><td><%= r.getParticipantName() %></td><% } %>
                    <td><%= r.getBibNumber() %></td>
                    <td><span class="badge status-badge-<%= r.getStatus() %>"><%= r.getStatus() %></span></td>
                    <td>
                        <% if (currentUser.isAdmin() && eventFinished && "CONFIRMED".equals(r.getStatus())) { %>
                        <form class="d-flex gap-1" method="post"
                              action="<%= request.getContextPath() %>/RegistrationServlet">
                            <input type="hidden" name="action" value="updateTime">
                            <input type="hidden" name="id" value="<%= r.getRegistrationId() %>">
                            <input type="text" class="form-control form-control-sm" name="finishTime"
                                   style="max-width:120px;" placeholder="hh:mm:ss"
                                   pattern="^\d{1,2}:\d{2}:\d{2}$"
                                   value="<%= r.getFinishTime() != null ? r.getFinishTime() : "" %>">
                            <button type="submit" class="btn btn-sm btn-outline-primary">
                                <i class="fa-solid fa-floppy-disk"></i>
                            </button>
                        </form>
                        <% } else { %>
                            <%= r.getFinishTime() != null ? r.getFinishTime() : "-" %>
                        <% } %>
                    </td>
                    <td class="text-end">
                        <% if (currentUser.isAdmin() && "PENDING".equals(r.getStatus())) { %>
                        <a class="btn btn-sm btn-outline-success"
                           href="<%= request.getContextPath() %>/RegistrationServlet?action=confirm&id=<%= r.getRegistrationId() %>">
                           <i class="fa-solid fa-check"></i> Confirm
                        </a>
                        <% } %>
                        <% if (!"CANCELLED".equals(r.getStatus())) { %>
                        <a class="btn btn-sm btn-outline-warning"
                           href="<%= request.getContextPath() %>/RegistrationServlet?action=cancel&id=<%= r.getRegistrationId() %>"
                           onclick="return confirm('Cancel this registration?');">
                           <i class="fa-solid fa-ban"></i> Cancel
                        </a>
                        <% } %>
                        <% if (currentUser.isAdmin()) { %>
                        <a class="btn btn-sm btn-outline-danger"
                           href="<%= request.getContextPath() %>/RegistrationServlet?action=delete&id=<%= r.getRegistrationId() %>"
                           onclick="return confirm('Permanently delete this registration?');">
                           <i class="fa-solid fa-trash"></i> Delete
                        </a>
                        <% } %>
                    </td>
                </tr>
<%
    } // end for loop
%>
            </tbody></table></div></div>
<% } // end if regs empty/else %>

<%@ include file="../includes/footer.jsp" %>
