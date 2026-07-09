package controller;

import dao.RegistrationDAO;
import dao.RunEventDAO;
import dao.UserDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import model.RunEvent;
import model.User;

/** Module D: Dashboard - shows an overview of the system's data. */
@WebServlet(name = "DashboardServlet", urlPatterns = {"/DashboardServlet"})
public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        RunEventDAO eventDAO = new RunEventDAO();
        RegistrationDAO regDAO = new RegistrationDAO();
        UserDAO userDAO = new UserDAO();

        try {
            int totalEvents = eventDAO.countEvents();
            int totalRegistrations = regDAO.countRegistrations();
            int totalParticipants = userDAO.countParticipants();
            List<RunEvent> upcomingEvents = eventDAO.getUpcomingEvents(5);

            request.setAttribute("totalEvents", totalEvents);
            request.setAttribute("totalRegistrations", totalRegistrations);
            request.setAttribute("totalParticipants", totalParticipants);
            request.setAttribute("upcomingEvents", upcomingEvents);

            if (!user.isAdmin()) {
                request.setAttribute("myRegistrations", regDAO.getRegistrationsByUser(user.getUserId()));
            }

            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
