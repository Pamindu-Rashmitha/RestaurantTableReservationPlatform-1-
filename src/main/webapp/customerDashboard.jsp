<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User,model.Reservation,java.util.List" %>

<%
  /* ---------- Security check ---------- */
  User user = (User) session.getAttribute("user");
  if (user == null || !"customer".equalsIgnoreCase(user.getRole())) {
    response.sendRedirect("login.jsp");
    return;
  }

  /* ---------- Data from servlet ---------- */
  @SuppressWarnings("unchecked")
  List<Reservation> reservations =
          (List<Reservation>) request.getAttribute("reservations");

  String statusMessage = (String) request.getAttribute("statusMessage");
  Integer queuePosition = (Integer) request.getAttribute("queuePosition");
%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Customer Dashboard</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
  <style>
    /* General styles */
    body {
      font-family: 'Inter', 'Segoe UI', Roboto, sans-serif;
      background: url('assets/res.jpeg') no-repeat center/cover fixed;
      color: #2d3436;
      margin: 0;
    }

    /* Navbar styling */
    .navbar {
      background-color: #1a202c;
      padding: 1rem 2rem;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    }
    .navbar-brand {
      font-size: 1.5rem;
      font-weight: 600;
    }
    .nav-link {
      color: #ffffff !important;
      transition: color 0.3s;
      padding: 0.5rem 1rem;
    }
    .nav-link:hover {
      color: #cbd5e0 !important;
    }

    /* Container overlay */
    .overlay {
      background: rgba(255, 255, 255, 0.97);
      padding: 2.5rem;
      margin: 2rem auto;
      border-radius: 12px;
      box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
      max-width: 1200px;
    }

    /* Headings */
    h2 {
      font-size: 2rem;
      font-weight: 700;
      color: #2d3436;
      margin-bottom: 1.5rem;
    }
    .card-header {
      font-size: 1.5rem;
      font-weight: 600;
      color: #4a5568;
      background-color: #edf2f7;
      border-bottom: 1px solid #e2e8f0;
    }

    /* Buttons */
    .btn {
      border-radius: 8px;
      padding: 0.6rem 1.2rem;
      font-weight: 500;
      transition: all 0.3s ease;
    }
    .btn-primary {
      background-color: #2b6cb0;
      border-color: #2b6cb0;
    }
    .btn-primary:hover {
      background-color: #2c5282;
      border-color: #2c5282;
      transform: translateY(-2px);
    }
    .btn-danger {
      background-color: #c53030;
      border-color: #c53030;
    }
    .btn-danger:hover {
      background-color: #9b2c2c;
      border-color: #9b2c2c;
      transform: translateY(-2px);
    }
    .btn-info {
      background-color: #3182ce;
      border-color: #3182ce;
    }
    .btn-info:hover {
      background-color: #2b6cb0;
      border-color: #2b6cb0;
      transform: translateY(-2px);
    }
    .btn-secondary {
      background-color: #718096;
      border-color: #718096;
    }
    .btn-secondary:hover {
      background-color: #5a6b7d;
      border-color: #5a6b7d;
      transform: translateY(-2px);
    }
    .btn-sm {
      padding: 0.4rem 0.8rem;
      font-size: 0.875rem;
      margin-right: 0.5rem;
    }

    /* Badges */
    .badge-success {
      background-color: #38a169;
      font-size: 0.9rem;
      padding: 0.4rem 0.8rem;
    }
    .badge-warning {
      background-color: #dd6b20;
      color: #fff;
      font-size: 0.9rem;
      padding: 0.4rem 0.8rem;
    }

    /* Tables */
    .table {
      background-color: #fff;
      border-radius: 8px;
      overflow: hidden;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
    }
    .table thead th {
      background-color: #1a202c;
      color: #fff;
      font-weight: 600;
      padding: 1rem;
      text-align: center;
    }
    .table td {
      padding: 1rem;
      vertical-align: middle;
      border-color: #e2e8f0;
      text-align: center;
    }
    .table-hover tbody tr:hover {
      background-color: #f7fafc;
    }
    .table-responsive {
      margin-bottom: 1.5rem;
    }

    /* Alerts */
    .alert-info {
      background-color: #e6f7fa;
      border-color: #bee3f8;
      color: #2b6cb0;
      border-radius: 8px;
      padding: 1rem;
      margin-bottom: 1.5rem;
    }
    .alert-warning {
      background-color: #fffaf0;
      border-color: #f6e05e;
      color: #dd6b20;
      border-radius: 8px;
      padding: 1rem;
      margin-bottom: 1.5rem;
    }

    /* Card */
    .card {
      border: none;
      border-radius: 8px;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
      margin-bottom: 1.5rem;
    }
    .card-body {
      padding: 1.5rem;
    }

    /* Text */
    .text-muted {
      color: #718096 !important;
    }
    p.text-center {
      font-size: 1.1rem;
      color: #4a5568;
      margin-bottom: 2rem;
    }

    /* Responsive adjustments */
    @media (max-width: 768px) {
      .overlay {
        padding: 1.5rem;
        margin: 1rem;
      }
      h2 {
        font-size: 1.75rem;
      }
      .card-header {
        font-size: 1.25rem;
      }
      .table th, .table td {
        padding: 0.75rem;
        font-size: 0.9rem;
      }
      .btn-sm {
        margin-bottom: 0.5rem;
      }
    }

    @media (max-width: 576px) {
      .navbar {
        padding: 0.75rem 1rem;
      }
      .navbar-nav {
        text-align: center;
      }
      .nav-link {
        padding: 0.5rem;
      }
      .btn-sm {
        display: block;
        width: 100%;
        text-align: center;
        margin-right: 0;
      }
      .table-responsive {
        -webkit-overflow-scrolling: touch;
      }
    }
  </style>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
  <a class="navbar-brand"><i class="fas fa-home mr-2"></i>Customer Dashboard</a>
  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
    <span class="navbar-toggler-icon"></span>
  </button>
  <div class="collapse navbar-collapse" id="navbarNav">
    <ul class="navbar-nav ml-auto">
      <li class="nav-item"><a href="reviewList.jsp" class="nav-link"><i class="fas fa-star mr-1"></i>Add a Review</a></li>
      <li class="nav-item"><a href="editProfile.jsp" class="nav-link"><i class="fas fa-user-edit mr-1"></i>Edit Profile</a></li>
      <li class="nav-item"><a href="login.jsp" class="nav-link"><i class="fas fa-sign-out-alt mr-1"></i>Logout</a></li>
    </ul>
  </div>
</nav>

<div class="container overlay">
  <h2 class="text-center mb-4"><i class="fas fa-user mr-2"></i>Hi, <%= user.getName() %>!</h2>

  <%-- Flash message --%>
  <% if (statusMessage != null) { %>
  <div class="alert alert-info text-center"><i class="fas fa-info-circle mr-2"></i><%= statusMessage %></div>
  <% } %>

  <%-- Waiting-list position --%>
  <% if (queuePosition != null && queuePosition.intValue() > 0) { %>
  <div class="alert alert-warning text-center">
    <i class="fas fa-hourglass-half mr-2"></i>You are currently #<%= queuePosition %> in the waiting list.
  </div>
  <% } %>

  <p class="text-center mb-4">
    <i class="fas fa-utensils mr-2"></i>"Welcome to your dashboard! Here you can manage reservations, check status, and plan your perfect meal."
  </p>

  <div class="text-center mb-4">
    <a href="makeReservation.jsp" class="btn btn-primary"><i class="fas fa-calendar-plus mr-2"></i>Make a Reservation</a>
  </div>

  <div class="card">
    <div class="card-header"><i class="fas fa-list-alt mr-2"></i>Your Reservations</div>
    <div class="card-body">
      <a href="customerDashboard" class="btn btn-info mb-3"><i class="fas fa-sync-alt mr-2"></i>Refresh Reservations</a>

      <% if (reservations != null && !reservations.isEmpty()) { %>
      <div class="table-responsive">
        <table class="table table-striped table-bordered table-hover">
          <thead class="thead-dark">
          <tr>
            <th>ID</th><th>Date</th><th>Time</th><th>Guests</th><th>Status / Info</th><th>Action</th>
          </tr>
          </thead>
          <tbody>
          <% for (Reservation r : reservations) { %>
          <tr>
            <td><%= r.getReservationId() %></td>
            <td><%= r.getDate() %></td>
            <td><%= r.getTime() %></td>
            <td><%= r.getNumberOfGuests() %></td>
            <td>
              <% if ("CONFIRMED".equalsIgnoreCase(r.getStatus())) { %>
              <span class="badge badge-success"><i class="fas fa-check mr-1"></i>Confirmed</span>
              <% } else if ("WAITING".equalsIgnoreCase(r.getStatus())) { %>
              <span class="badge badge-warning"><i class="fas fa-hourglass mr-1"></i>Waiting</span>
              <% } else { %>
              <%= r.getStatus() %>
              <% } %>
            </td>
            <td>
              <%-- Show Pay / Edit / Cancel actions based on status --%>
              <% if ("PENDING".equalsIgnoreCase(r.getStatus()) || "WAITING".equalsIgnoreCase(r.getStatus())) { %>
              <a href="payment.jsp?reservationId=<%= r.getReservationId() %>" class="btn btn-primary btn-sm"><i class="fas fa-credit-card mr-1"></i>Pay Now</a>
              <a href="editReservation?reservationId=<%= r.getReservationId() %>" class="btn btn-secondary btn-sm"><i class="fas fa-edit mr-1"></i>Edit</a>
              <form action="cancelReservation" method="post" style="display:inline;">
                <input type="hidden" name="reservationId" value="<%= r.getReservationId() %>">
                <button class="btn btn-danger btn-sm"><i class="fas fa-times mr-1"></i>Cancel</button>
              </form>
              <% } else if (!"CANCELLED".equalsIgnoreCase(r.getStatus())) { %>
              <form action="cancelReservation" method="post" style="display:inline;">
                <input type="hidden" name="reservationId" value="<%= r.getReservationId() %>">
                <button class="btn btn-danger btn-sm"><i class="fas fa-times mr-1"></i>Cancel</button>
              </form>
              <% } %>
            </td>
          </tr>
          <% } %>
          </tbody>
        </table>
      </div>
      <% } else { %>
      <p class="text-center text-muted"><i class="fas fa-info-circle mr-2"></i>No reservations found.</p>
      <% } %>
    </div>
  </div>
</div>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>