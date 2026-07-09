package controller;

import dao.RegistrationDAO;
import dao.RunEventDAO;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import model.Registration;
import model.User;

/**
 * Handles participants registering for Run Events, and admin management
 * of those registrations (part of Module C - Information Management, and
 * supports the participant-facing flow of the app).
 */
@WebServlet(name = "RegistrationServlet", urlPatterns = {"/RegistrationServlet"})
public class RegistrationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        String action = request.getParameter("action");
        if (action == null) action = "list";

        RegistrationDAO regDAO = new RegistrationDAO();

        try {
            switch (action) {
                case "new":
                    request.setAttribute("events", new RunEventDAO().getAllEvents());
                    request.getRequestDispatcher("registrations/form.jsp").forward(request, response);
                    break;
                case "cancel":
                    int cancelId = Integer.parseInt(request.getParameter("id"));
                    regDAO.updateStatus(cancelId, "CANCELLED");
                    response.sendRedirect(request.getContextPath() + "/RegistrationServlet");
                    break;
                case "confirm":
                    int confirmId = Integer.parseInt(request.getParameter("id"));
                    regDAO.updateStatus(confirmId, "CONFIRMED");
                    response.sendRedirect(request.getContextPath() + "/RegistrationServlet");
                    break;
                case "delete":
                    int deleteId = Integer.parseInt(request.getParameter("id"));
                    regDAO.deleteRegistration(deleteId);
                    response.sendRedirect(request.getContextPath() + "/RegistrationServlet");
                    break;
                default:
                    if (user.isAdmin()) {
                        request.setAttribute("registrations", regDAO.getAllRegistrations());
                    } else {
                        request.setAttribute("registrations", regDAO.getRegistrationsByUser(user.getUserId()));
                    }
                    request.getRequestDispatcher("registrations/list.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        RegistrationDAO regDAO = new RegistrationDAO();

        String action = request.getParameter("action");
        if ("updateTime".equals(action)) {
            try {
                if (user.isAdmin()) {
                    int timeId = Integer.parseInt(request.getParameter("id"));
                    String finishTime = request.getParameter("finishTime");
                    regDAO.updateFinishTime(timeId, finishTime);
                }
                response.sendRedirect(request.getContextPath() + "/RegistrationServlet");
            } catch (SQLException e) {
                request.setAttribute("error", "Database error: " + e.getMessage());
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
            return;
        }

        try {
            int eventId = Integer.parseInt(request.getParameter("eventId"));

            if (regDAO.isAlreadyRegistered(user.getUserId(), eventId)) {
                request.setAttribute("error", "You are already registered for this event.");
                request.setAttribute("events", new RunEventDAO().getAllEvents());
                request.getRequestDispatcher("registrations/form.jsp").forward(request, response);
                return;
            }

            Registration r = new Registration();
            r.setUserId(user.getUserId());
            r.setEventId(eventId);
            r.setBibNumber(regDAO.generateBibNumber());
            r.setStatus("PENDING");

            regDAO.addRegistration(r);
            response.sendRedirect(request.getContextPath() + "/RegistrationServlet");
        } catch (SQLException e) {
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
