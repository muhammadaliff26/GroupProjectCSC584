<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Uni-Run</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.3/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<div class="auth-wrapper">
    <div class="card auth-card shadow">
        <div class="card-body p-4">
            <div class="text-center mb-4">
                <i class="fa-solid fa-person-running fa-2x" style="color: var(--unirun-primary);"></i>
                <h3 class="mt-2">Uni-Run</h3>
                <p class="text-muted mb-0">Marathon &amp; Virtual Run Coordinator</p>
            </div>

            <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-danger"><%= request.getAttribute("error") %></div>
            <% } %>
            <% if (request.getParameter("msg") != null) { %>
            <div class="alert alert-success"><%= request.getParameter("msg") %></div>
            <% } %>

            <form action="<%= request.getContextPath() %>/LoginServlet" method="post">
                <div class="mb-3">
                    <label class="form-label">Username</label>
                    <input type="text" class="form-control" name="username" required autofocus>
                </div>
                <div class="mb-3">
                    <label class="form-label">Password</label>
                    <input type="password" class="form-control" name="password" required>
                </div>
                <button type="submit" class="btn btn-unirun w-100">
                    <i class="fa-solid fa-right-to-bracket"></i> Login
                </button>
            </form>

            <p class="text-center mt-3 mb-0">
                No account yet? <a href="<%= request.getContextPath() %>/register.jsp">Register here</a>
            </p>
            <p class="text-center text-muted mt-2" style="font-size:.8rem;">
            </p>
        </div>
    </div>
</div>
</body>
</html>
