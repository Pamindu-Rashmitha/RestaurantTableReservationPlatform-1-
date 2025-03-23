<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User" %>
<%
  User user = (User) session.getAttribute("user");
  if (user == null || !"customer".equals(user.getRole())) {
    response.sendRedirect("login.jsp");
    return;
  }
%>
<html>
<head>
  <title>Customer Dashboard</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-5">
  <h2>Welcome, <%= user.getName() %>!</h2>
  <p>This is your customer dashboard. You can make reservations here (to be implemented).</p>
  <a href="logout" class="btn btn-danger">Logout</a>
</div>
</body>
</html>

