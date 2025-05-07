<%--
  Created by IntelliJ IDEA.
  User: pamid
  Date: 5/7/2025
  Time: 1:03 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>How It works</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <style>
    body {
      background-color: #2c3034;           /* light gray */
      color: #343a40;                /* dark gray for text */
    }
    #how-it-works {
      padding: 60px 0;
      background: #ffffff;           /* white section */
      border-radius: 12px;
      box-shadow: 0 4px 12px rgba(0,0,0,0.05);
    }

    h2 {
      font-family: 'Segoe UI', sans-serif;
      font-weight: 600;
      color: #2c3e50;                /* deeper blue-gray */
      position: relative;
    }
    h2::after {
      content: '';
      display: block;
      width: 60px;
      height: 4px;
      background: #007bff;           /* primary blue */
      margin: 12px auto 0;
      border-radius: 2px;
    }

    .timeline {
      position: relative;
      margin: 40px 0;
    }
    .timeline::before {
      content: '';
      position: absolute;
      top: 0;
      left: 25px;
      bottom: 0;
      width: 4px;
      background: #e9ecef;
      border-radius: 2px;
    }

    /* Each step box */
    .timeline-step {
      position: relative;
      margin-bottom: 40px;
      padding-left: 60px;
    }
    .timeline-step:last-child {
      margin-bottom: 0;
    }
    .timeline-step .step-number {
      position: absolute;
      left: 0;
      top: 0;
      display: flex;
      align-items: center;
      justify-content: center;
      width: 50px; height: 50px;
      background: #007bff;
      color: #fff;
      border-radius: 50%;
      font-size: 1.25rem;
      box-shadow: 0 2px 6px rgba(0,0,0,0.1);
    }
    .timeline-step .card {
      border-radius: 8px;
      overflow: hidden;
      box-shadow: 0 4px 8px rgba(0,0,0,0.05);
      transition: transform 0.3s, box-shadow 0.3s;
    }
    .timeline-step .card:hover {
      transform: translateY(-4px);
      box-shadow: 0 8px 16px rgba(0,0,0,0.1);
    }
    .timeline-step {
      opacity: 4;
      transform: translateY(20px);
      transition: opacity 0.6s ease, transform 0.6s ease;
    }

  </style>
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
  <a class="navbar-brand" href="index.jsp">Gourmet Reservation</a>
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
      <li class="nav-item">
        <a href="review?action=list" class="nav-link">Reviews</a>
      </li>
    </ul>
  </div>
</nav>

<div id="how-it-works" class="container">
  <h2 class="text-center">How It Works</h2>
  <div class="timeline">

    <div class="timeline-step">
      <div class="step-number">1</div>
      <div class="card">
        <div class="card-body text-center">
          <h5>Create Account / Log In</h5>
          <p>Sign up or log in to start reserving tables.</p>
          <img src="assets/img.png" class="img-fluid mt-3" alt="">
        </div>
      </div>
    </div>

    <div class="timeline-step">
      <div class="step-number">2</div>
      <div class="card">
        <div class="card-body text-center">
          <h5>Make a Reservation</h5>
          <p>Browse tables, pick your date & time.</p>
          <img src="assets/img_1.png" class="img-fluid mt-3" alt="">
        </div>
      </div>
    </div>

    <div class="timeline-step">
      <div class="step-number">3</div>
      <div class="card">
        <div class="card-body text-center">
          <h5>Edit Reservation</h5>
          <p>Need changes? Edit booking details on the fly.</p>
          <img src="assets/img_2.png" class="img-fluid mt-3" alt="">
        </div>
      </div>
    </div>

    <div class="timeline-step">
      <div class="step-number">4</div>
      <div class="card">
        <div class="card-body text-center">
          <h5>Easy Payment</h5>
          <p>Securely pay with your preferred card.</p>
          <img src="assets/img_3.png" class="img-fluid mt-3" alt="">
      </div>
    </div>

  </div>
</div>
</div>


  <script>
    document.addEventListener('DOMContentLoaded', () => {
      const items = document.querySelectorAll('.timeline-step');
      const observer = new IntersectionObserver(entries => {
        entries.forEach(e => {
          if (e.isIntersecting) {
            e.target.classList.add('fade-in');
            observer.unobserve(e.target);
          }
        });
      }, { threshold: 0.2 });

      items.forEach(i => observer.observe(i));
    });
  </script>
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

</body>

</html>
