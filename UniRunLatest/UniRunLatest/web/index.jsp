<%
    if (session.getAttribute("user") != null) {
        response.sendRedirect(request.getContextPath() + "/DashboardServlet");
    } else {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }
%>
