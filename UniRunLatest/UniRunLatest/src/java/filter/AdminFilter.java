package filter;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import model.User;

/** Restricts the Information Management (Run Event CRUD) module to ADMIN users only. */
@WebFilter(urlPatterns = {"/EventServlet"})
public class AdminFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException { }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user != null && user.isAdmin()) {
            chain.doFilter(request, response);
        } else {
            res.sendRedirect(req.getContextPath() + "/DashboardServlet?msg=Admin access only");
        }
    }

    @Override
    public void destroy() { }
}
