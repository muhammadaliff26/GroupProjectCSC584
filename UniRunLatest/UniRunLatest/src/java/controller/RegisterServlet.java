package controller;

import dao.UserDAO;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import model.User;

/** Module B: Registration - allows a new participant to create an account. */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/RegisterServlet"})
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        UserDAO dao = new UserDAO();

        try {
            if (username == null || username.trim().isEmpty()
                    || password == null || password.trim().isEmpty()
                    || fullName == null || fullName.trim().isEmpty()
                    || email == null || email.trim().isEmpty()) {
                request.setAttribute("error", "Please fill in all required fields.");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }

            if (!password.equals(confirmPassword)) {
                request.setAttribute("error", "Passwords do not match.");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }

            if (dao.isUsernameExists(username)) {
                request.setAttribute("error", "Username already taken. Please choose another.");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }

            User u = new User();
            u.setUsername(username);
            u.setPassword(password);
            u.setFullName(fullName);
            u.setEmail(email);
            u.setPhone(phone);
            u.setRole("PARTICIPANT");

            boolean success = dao.registerUser(u);
            if (success) {
                response.sendRedirect(request.getContextPath()
                        + "/login.jsp?msg=Registration successful. Please login.");
            } else {
                request.setAttribute("error", "Registration failed. Please try again.");
                request.getRequestDispatcher("register.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}
