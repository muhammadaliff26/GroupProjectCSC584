<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Category" %>
<%@ page import="model.RunEvent" %>
<%@ include file="../includes/header.jsp" %>
<%
    RunEvent event = (RunEvent) request.getAttribute("event");
    boolean isEdit = (event != null);
    List<Category> categories = (List<Category>) request.getAttribute("categories");
%>
<h2><i class="fa-solid fa-<%= isEdit ? "pen" : "plus" %>"></i> <%= isEdit ? "Edit" : "Add New" %> Run Event</h2>

<% if (request.getAttribute("error") != null) { %>
<div class="alert alert-danger"><%= request.getAttribute("error") %></div>
<% } %>

<div class="card shadow-sm">
    <div class="card-body">
        <form action="<%= request.getContextPath() %>/EventServlet" method="post">
            <input type="hidden" name="action" value="<%= isEdit ? "update" : "insert" %>">
            <% if (isEdit) { %>
            <input type="hidden" name="eventId" value="<%= event.getEventId() %>">
            <% } %>

            <div class="mb-3">
                <label class="form-label">Event Name</label>
                <input type="text" class="form-control" name="eventName" required
                       value="<%= isEdit ? event.getEventName() : "" %>">
            </div>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label class="form-label">Event Date</label>
                    <input type="date" class="form-control" name="eventDate" required
                           value="<%= isEdit ? event.getEventDate().toString() : "" %>">
                </div>
                <div class="col-md-6 mb-3">
                    <label class="form-label">Location</label>
                    <input type="text" class="form-control" name="location" required
                           value="<%= isEdit ? event.getLocation() : "" %>">
                </div>
            </div>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label class="form-label">Category</label>
                    <select class="form-select" name="categoryId" required>
                        <% for (Category c : categories) { %>
                        <option value="<%= c.getCategoryId() %>"
                            <%= (isEdit && event.getCategoryId() == c.getCategoryId()) ? "selected" : "" %>>
                            <%= c.getCategoryName() %> (<%= c.getDistanceKm() %> km - RM <%= c.getFee() %>)
                        </option>
                        <% } %>
                    </select>
                </div>
                <div class="col-md-6 mb-3">
                    <label class="form-label">Status</label>
                    <select class="form-select" name="status">
                        <%
                            String[] statuses = {"UPCOMING", "ONGOING", "COMPLETED", "CANCELLED"};
                            for (String s : statuses) {
                        %>
                        <option value="<%= s %>" <%= (isEdit && s.equals(event.getStatus())) ? "selected" : "" %>><%= s %></option>
                        <% } %>
                    </select>
                </div>
            </div>

            <div class="mb-3">
                <label class="form-label">Description</label>
                <textarea class="form-control" name="description" rows="3"><%= isEdit ? event.getDescription() : "" %></textarea>
            </div>

            <button type="submit" class="btn btn-unirun"><i class="fa-solid fa-floppy-disk"></i> Save</button>
            <a href="<%= request.getContextPath() %>/EventServlet" class="btn btn-outline-secondary">Cancel</a>
        </form>
    </div>
</div>

<%@ include file="../includes/footer.jsp" %>
