<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ include file="includes/header.jsp" %>
<div class="alert alert-danger mt-4">
    <h4><i class="fa-solid fa-triangle-exclamation"></i> Something went wrong</h4>
    <p><%= request.getAttribute("error") != null ? request.getAttribute("error") : (exception != null ? exception.getMessage() : "Unknown error") %></p>
    <a class="btn btn-unirun" href="<%= request.getContextPath() %>/DashboardServlet">Back to Dashboard</a>
</div>
<%@ include file="includes/footer.jsp" %>
