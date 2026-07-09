package controller;

import dao.CategoryDAO;
import dao.RunEventDAO;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import model.RunEvent;
import model.User;

/**
 * Module C: Information Management (Create / Read / Update / Delete)
 * Manages Run Events. Restricted to ADMIN users (see AdminFilter).
 */
@WebServlet(name = "EventServlet", urlPatterns = {"/EventServlet"})
public class EventServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";

        try {
            switch (action) {
                case "new":
                    showForm(request, response, null);
                    break;
                case "edit":
                    int editId = Integer.parseInt(request.getParameter("id"));
                    RunEvent event = new RunEventDAO().getEventById(editId);
                    showForm(request, response, event);
                    break;
                case "delete":
                    int deleteId = Integer.parseInt(request.getParameter("id"));
                    new RunEventDAO().deleteEvent(deleteId);
                    response.sendRedirect(request.getContextPath() + "/EventServlet");
                    break;
                case "finish":
                    int finishId = Integer.parseInt(request.getParameter("id"));
                    new RunEventDAO().markAsCompleted(finishId);
                    response.sendRedirect(request.getContextPath()
                            + "/RegistrationServlet?msg=Event marked as finished. You can now record finish times.");
                    break;
                default:
                    listEvents(request, response);
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
        User admin = (User) session.getAttribute("user");

        String action = request.getParameter("action");
        try {
            RunEvent e = new RunEvent();
            e.setEventName(request.getParameter("eventName"));
            e.setEventDate(Date.valueOf(request.getParameter("eventDate")));
            e.setLocation(request.getParameter("location"));
            e.setCategoryId(Integer.parseInt(request.getParameter("categoryId")));
            e.setDescription(request.getParameter("description"));
            e.setStatus(request.getParameter("status"));

            RunEventDAO dao = new RunEventDAO();

            if ("update".equals(action)) {
                e.setEventId(Integer.parseInt(request.getParameter("eventId")));
                dao.updateEvent(e);
            } else {
                e.setCreatedBy(admin.getUserId());
                dao.addEvent(e);
            }
            response.sendRedirect(request.getContextPath() + "/EventServlet");
        } catch (SQLException e) {
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void listEvents(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        request.setAttribute("events", new RunEventDAO().getAllEvents());
        request.getRequestDispatcher("events/list.jsp").forward(request, response);
    }

    private void showForm(HttpServletRequest request, HttpServletResponse response, RunEvent event)
            throws ServletException, IOException, SQLException {
        request.setAttribute("categories", new CategoryDAO().getAllCategories());
        request.setAttribute("event", event);
        request.getRequestDispatcher("events/form.jsp").forward(request, response);
    }
}
