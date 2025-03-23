<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Register</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-5">
  <h2>Register</h2>
  <% if (request.getAttribute("error") != null) { %>
  <p class="text-danger"><%= request.getAttribute("error") %></p>
  <% } %>
  <form action="register" method="post">
    <div class="form-group">
      <label>Username:</label>
      <input type="text" name="username" class="form-control" required>
    </div>
    <div class="form-group">
      <label>Password:</label>
      <input type="password" name="password" class="form-control" required>
    </div>
    <div class="form-group">
      <label>Name:</label>
      <input type="text" name="name" class="form-control" required>
    </div>
    <div class="form-group">
      <label>Email:</label>
      <input type="email" name="email" class="form-control" required>
    </div>
    <button type="submit" class="btn btn-primary">Register</button>
    <a href="login.jsp" class="btn btn-link">Already have an account? Login</a>
  </form>
</div>
</body>
</html>