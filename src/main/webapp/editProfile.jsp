<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Edit Profile</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        body {
            background: url('assets/res.jpeg') no-repeat center center fixed;
            background-size: cover;
            font-family: 'Segoe UI', Roboto, 'Helvetica Neue', sans-serif;
            color: #212529;
            margin: 0;
            padding: 0;
        }
        .overlay {
            background: rgba(255, 255, 255, 0.95);
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.1);
            margin-top: 30px;
            margin-bottom: 30px;
            max-width: 500px;
            margin-left: auto;
            margin-right: auto;
        }
        .form-header {
            text-align: center;
            margin-bottom: 30px;
            font-size: 2rem;
            color: #343a40;
            font-weight: bold;
        }
        .btn-custom {
            transition: all 0.3s ease;
        }
        .btn-custom:hover, .btn-custom:focus {
            transform: translateY(-2px);
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }
        .form-control {
            border: 1px solid #ced4da;
            border-radius: 5px;
            padding: 12px 15px;
        }
        .form-control:focus {
            border-color: #6c63ff;
            box-shadow: 0 0 0 0.2rem rgba(108, 99, 255, 0.25);
        }
        @media (max-width: 576px) {
            .form-header {
                font-size: 1.8rem;
            }
            .overlay {
                padding: 15px;
            }
        }
    </style>
</head>
<body>
<div class="container overlay">
    <h2 class="form-header">Edit Profile</h2>
    <% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-danger" role="alert">
        <%= request.getAttribute("error") %>
    </div>
    <% } %>
    <% if (request.getAttribute("message") != null) { %>
    <div class="alert alert-success" role="alert">
        <%= request.getAttribute("message") %>
    </div>
    <% } %>
    <form action="updateUser" method="post">
        <div class="form-group">
            <label for="username">Username (cannot be changed)</label>
            <input type="text" id="username" name="username" class="form-control" value="<%= user.getUsername() %>" readonly>
        </div>
        <div class="form-group">
            <label for="name">Name</label>
            <input type="text" id="name" name="name" class="form-control" value="<%= user.getName() %>" required>
        </div>
        <div class="form-group">
            <label for="email">Email</label>
            <input type="email" id="email" name="email" class="form-control" value="<%= user.getEmail() != null ? user.getEmail() : "" %>" required>
        </div>
        <div class="form-group">
            <label for="password">New Password (leave blank to keep current)</label>
            <input type="password" id="password" name="password" class="form-control" placeholder="Enter new password">
        </div>
        <button type="submit" class="btn btn-primary btn-custom btn-block">Update Profile</button>
    </form>
    <div class="text-center mt-3">
        <a href="customerDashboard.jsp" class="btn btn-secondary btn-custom">Back to Dashboard</a>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.3/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>