<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Uni-Run : Marathon &amp; Virtual Run Coordinator</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.3/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<% if (session.getAttribute("user") != null) { %>
<nav class="navbar navbar-expand-lg navbar-unirun">
    <div class="container">
        <a class="navbar-brand" href="<%= request.getContextPath() %>/DashboardServlet">
            <i class="fa-solid fa-person-running"></i> Uni-Run
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navMain">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navMain">
            <ul class="navbar-nav me-auto">
                <li class="nav-item">
                    <a class="nav-link" href="<%= request.getContextPath() %>/DashboardServlet">Dashboard</a>
                </li>
                <% model.User navUser = (model.User) session.getAttribute("user"); %>
                <% if (navUser != null && navUser.isAdmin()) { %>
                <li class="nav-item">
                    <a class="nav-link" href="<%= request.getContextPath() %>/EventServlet">Manage Run Events</a>
                </li>
                <% } %>
                <li class="nav-item">
                    <a class="nav-link" href="<%= request.getContextPath() %>/RegistrationServlet">
                        <% if (navUser != null && navUser.isAdmin()) { %>All Registrations<% } else { %>My Registrations<% } %>
                    </a>
                </li>
            </ul>
            <span class="navbar-text text-white me-3">
                <i class="fa-solid fa-user"></i> <%= navUser != null ? navUser.getFullName() : "" %>
                (<%= navUser != null ? navUser.getRole() : "" %>)
            </span>
            <a class="btn btn-sm btn-unirun" href="<%= request.getContextPath() %>/LogoutServlet">
                <i class="fa-solid fa-right-from-bracket"></i> Logout
            </a>
        </div>
    </div>
</nav>
<% } %>
<div class="container my-4">
