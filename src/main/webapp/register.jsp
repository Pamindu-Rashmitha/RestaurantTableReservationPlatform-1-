<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Register</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <style>
    body {
      color: #212529;
      font-family: 'Segoe UI', Roboto, 'Helvetica Neue', sans-serif;
      margin: 0; /* Reset default margin */
    }
    .background-image {
      position: fixed;
      width: 100%;
      height: 100%;
      background: linear-gradient(rgba(0, 0, 0, 0.3), rgba(0, 0, 0, 0.3)), url('assets/res.jpeg') no-repeat center center;
      background-size: cover;
      opacity: 0;
      animation: fadeIn 1s ease-in forwards;
      z-index: -1; /* Place behind content */
    }
    @keyframes fadeIn {
      to {
        opacity: 1;
      }
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
      transition: all 0.3s ease; /* Smooth transition for hover effects */
    }
    .btn-register:hover, .btn-register:focus {
      background-color: #5a52e0;
      border-color: #5a52e0;
      transform: translateY(-2px); /* Lift effect */
      box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); /* Subtle shadow */
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
      transition: all 0.3s ease; /* Smooth focus transition */
    }
    .form-control:focus {
      border-color: #6c63ff;
      box-shadow: 0 0 0 0.2rem rgba(108, 99, 255, 0.25);
    }
    label {
      font-weight: 500;
      color: #343a40; /* Darker color for better contrast */
    }
    .password-requirements {
      display: none; /* Hidden by default */
      background-color: #f8f9fa;
      border: 1px solid #ced4da;
      border-radius: 5px;
      padding: 10px;
      margin-top: 5px;
      font-size: 0.9em;
      color: #6c757d;
    }
  </style>
</head>
<body>
<div class="background-image"></div>
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
        <label for="username">Username</label>
        <input type="text" id="username" name="username" class="form-control" placeholder="Choose a username" required autofocus>
      </div>
      <div class="form-group mb-4">
        <label for="password">Password</label>
        <input type="password" id="password" name="password" class="form-control" placeholder="Enter a strong password" required aria-describedby="password-requirements">
        <div id="password-requirements" class="password-requirements">
          Password must be at least 8 characters, include uppercase, lowercase, number, and special character (!@#$%^&*).
        </div>
      </div>
      <div class="form-group mb-4">
        <label for="name">Full Name</label>
        <input type="text" id="name" name="name" class="form-control" placeholder="Your full name" required>
      </div>
      <div class="form-group mb-4">
        <label for="email">Email</label>
        <input type="email" id="email" name="email" class="form-control" placeholder="you@example.com" required>
      </div>
      <button type="submit" class="btn btn-primary btn-register">Register</button>
      <a href="login.jsp" class="login-link">Already have an account? Log In</a>
    </form>
  </div>
</div>
<script>
  document.addEventListener('DOMContentLoaded', function() {
    const passwordField = document.getElementById('password');
    const requirementsDiv = document.getElementById('password-requirements');

    passwordField.addEventListener('focus', function() {
      requirementsDiv.style.display = 'block';
    });

    passwordField.addEventListener('blur', function() {
      requirementsDiv.style.display = 'none';
    });
  });
</script>
</body>
</html>