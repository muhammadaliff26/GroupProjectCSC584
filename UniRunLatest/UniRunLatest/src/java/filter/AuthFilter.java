package filter;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;

/**
 * Session management filter (supports Module A - Login/Logout).
 * Any request that is not the login page, register page, or a static
 * resource (css/js) will be blocked unless the user has an active session.
 */
@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException { }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        String uri = req.getRequestURI();
        boolean isPublic = uri.endsWith("login.jsp")
                || uri.endsWith("LoginServlet")
                || uri.endsWith("register.jsp")
                || uri.endsWith("RegisterServlet")
                || uri.contains("/css/")
                || uri.contains("/js/")
                || uri.endsWith("index.jsp")
                || uri.endsWith("/");

        boolean loggedIn = (session != null && session.getAttribute("user") != null);

        if (isPublic || loggedIn) {
            chain.doFilter(request, response);
        } else {
            res.sendRedirect(req.getContextPath() + "/login.jsp?msg=Please login first");
        }
    }

    @Override
    public void destroy() { }
}
