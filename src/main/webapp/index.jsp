<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Restaurant Table Reservation Platform</title>
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        .hero {
            background: url('assets/res.jpeg') no-repeat center center;
            background-size: cover;
            height: 60vh;
            position: relative;
            color: #fff;
        }
        .hero-overlay {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0,0,0,0.5);
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
        }
        .hero h1 {
            font-size: 3rem;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>

<!-- Navigation Bar -->
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <a class="navbar-brand" href="index.jsp">Reservation Platform</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav"
            aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav ml-auto">
            <li class="nav-item">
                <a class="nav-link" href="login.jsp">Login</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="register.jsp">Register</a>
            </li>
        </ul>
    </div>
</nav>

<!-- Hero Section -->
<div class="hero">
    <div class="hero-overlay">
        <h1>Welcome to Our Reservation Platform</h1>
        <p>Reserve your table at your favorite restaurant with ease!</p>
        <a href="login.jsp" class="btn btn-primary btn-lg">Get Started</a>
    </div>
</div>

<!-- About Section -->
<div class="container mt-5">
    <h2>About Our Service</h2>
    <p>
        Our Restaurant Table Reservation Platform is designed to provide a seamless experience for diners and restaurant staff alike.
        Whether you are a first-time visitor or a regular, our user-friendly platform ensures you can book, update, and cancel your reservations
        in just a few clicks.
    </p>
    <p>
        Explore our site to register, log in, and manage your reservations.
    </p>
</div>

<!-- Footer -->
<footer class="bg-dark text-white text-center p-3 mt-5">
    <p class="mb-0">&copy; 2025 Restaurant Reservation Platform. All Rights Reserved.</p>
</footer>

<!-- Bootstrap JS and dependencies -->
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
