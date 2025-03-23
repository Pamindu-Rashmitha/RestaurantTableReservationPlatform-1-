<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Register</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <style>
    body {
      background-color: #f8f9fa;
      color: #212529;
      font-family: 'Segoe UI', Roboto, 'Helvetica Neue', sans-serif;
    }
    .register-container {
      max-width: 400px;
      background-color: white;
      border-radius: 10px;
      padding: 30px;
      box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.05);
    }
    .register-title {
      font-weight: 300;
      margin-bottom: 25px;
      color: #343a40;
    }
    .btn-register {
      background-color: #6c63ff;
      border-color: #6c63ff;
      border-radius: 5px;
      padding: 10px 20px;
      font-weight: 500;
      width: 100%;
    }
    .btn-register:hover, .btn-register:focus {
      background-color: #5a52e0;
      border-color: #5a52e0;
    }
    .login-link {
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
  <div class="register-container">
    <h2 class="register-title text-center">Create an Account</h2>
    <% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-danger" role="alert">
      <%= request.getAttribute("error") %>
    </div>
    <% } %>
    <form action="register" method="post">
      <div class="form-group mb-4">
        <label for="username" class="small text-muted">Username</label>
        <input type="text" id="username" name="username" class="form-control" required autofocus>
      </div>
      <div class="form-group mb-4">
        <label for="password" class="small text-muted">Password</label>
        <input type="password" id="password" name="password" class="form-control" required>
      </div>
      <div class="form-group mb-4">
        <label for="name" class="small text-muted">Full Name</label>
        <input type="text" id="name" name="name" class="form-control" required>
      </div>
      <div class="form-group mb-4">
        <label for="email" class="small text-muted">Email</label>
        <input type="email" id="email" name="email" class="form-control" required>
      </div>
      <button type="submit" class="btn btn-primary btn-register">Register</button>
      <a href="login.jsp" class="login-link">Already have an account? Log In</a>
    </form>
  </div>
</div>
</body>
</html>
