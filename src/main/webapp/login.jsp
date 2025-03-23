<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Login</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <style>
    body {
      background-color: #f8f9fa;
      color: #212529;
      font-family: 'Segoe UI', Roboto, 'Helvetica Neue', sans-serif;
    }
    .login-container {
      max-width: 400px;
      background-color: white;
      border-radius: 10px;
      padding: 30px;
      box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.05);
    }
    .login-title {
      font-weight: 300;
      margin-bottom: 25px;
      color: #343a40;
    }
    .btn-login {
      background-color: #6c63ff;
      border-color: #6c63ff;
      border-radius: 5px;
      padding: 10px 20px;
      font-weight: 500;
      width: 100%;
    }
    .btn-login:hover, .btn-login:focus {
      background-color: #5a52e0;
      border-color: #5a52e0;
    }
    .register-link {
      display: block;
      text-align: center;
      margin-top: 15px;
      color: #6c757d;
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
  </style>
</head>
<body>
<div class="container d-flex justify-content-center align-items-center min-vh-100">
  <div class="login-container">
    <h2 class="login-title text-center">Welcome Back</h2>
    <% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-danger" role="alert">
      <%= request.getAttribute("error") %>
    </div>
    <% } %>
    <form action="login" method="post">
      <div class="form-group mb-4">
        <label for="username" class="small text-muted">Username</label>
        <input type="text" id="username" name="username" class="form-control" required autofocus>
      </div>
      <div class="form-group mb-4">
        <label for="password" class="small text-muted">Password</label>
        <input type="password" id="password" name="password" class="form-control" required>
      </div>
      <button type="submit" class="btn btn-primary btn-login">Log In</button>
      <a href="register.jsp" class="register-link">Need an account? Register</a>
    </form>
  </div>
</div>
</body>
</html>