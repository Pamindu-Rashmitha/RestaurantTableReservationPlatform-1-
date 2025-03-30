<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User" %>
<%@ page import="model.Reservation" %>
<%@ page import="java.util.List" %>
<%
  User user = (User) session.getAttribute("user");
  if (user == null || !"customer".equals(user.getRole())) {
    response.sendRedirect("login.jsp");
    return;
  }
  List<Reservation> reservations = (List<Reservation>) request.getAttribute("reservations");
%>
<html>
<head>
  <meta charset="UTF-8">
  <!-- Responsive Meta Tag -->
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <title>Customer Dashboard</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <style>
    /* Full background image */
    body {
      background: url('assets/res.jpeg') no-repeat center center fixed;
      background-size: cover;
      font-family: 'Segoe UI', Roboto, 'Helvetica Neue', sans-serif;
      color: #212529;
      margin: 0;
      padding: 0;
    }
    /* Overlay for content readability */
    .overlay {
      background: rgba(255, 255, 255, 0.95);
      padding: 30px;
      border-radius: 10px;
      box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.1);
      margin-top: 30px;
      margin-bottom: 30px;
    }
    .dashboard-header {
      text-align: center;
      margin-bottom: 30px;
      font-size: 2.5rem;
      color: #343a40;
      font-weight: bold;
    }
    .card {
      border: none;
      margin-bottom: 30px;
    }
    .card-header {
      background: linear-gradient(135deg, #6c63ff, #4834d4);
      color: #fff;
      font-weight: 500;
      border-top-left-radius: 10px;
      border-top-right-radius: 10px;
    }
    .btn-custom {
      transition: all 0.3s ease;
    }
    .btn-custom:hover, .btn-custom:focus {
      transform: translateY(-2px);
      box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    }
    .table th, .table td {
      text-align: center;
    }
    .table th:first-child, .table td:first-child {
      width: 10%;
    }
    /* Responsive adjustments */
    @media (max-width: 768px) {
      .dashboard-header {
        font-size: 2rem;
      }
      .overlay {
        padding: 20px;
      }
      .card-header {
        font-size: 1.1rem;
      }
    }
    @media (max-width: 576px) {
      .dashboard-header {
        font-size: 1.8rem;
      }
      .overlay {
        padding: 15px;
      }
      .table-responsive {
        font-size: 0.9rem;
      }
    }
  </style>
</head>
<body>
<div class="container overlay">
  <h1 class="dashboard-header">Customer Dashboard</h1>
  <p class="text-center">Welcome, <%= user.getName() %>!</p>
  <br>
  <p class="text-center">
    "Welcome to your dashboard! Here, you can easily view and manage your upcoming reservations,
    check availability, and modify your bookings with just a few clicks. Enjoy a seamless dining
    experience by securing your table in advance. Need to make changes? No problem—your reservation
    details are always at your fingertips. Start planning your perfect meal now!"
  </p>
  <br>
  <div class="text-center">
    <a href="makeReservation.jsp" class="btn btn-primary btn-custom">Make a Reservation</a>
    <a href="logout" class="btn btn-danger btn-custom">Logout</a>
  </div>
  <br>
  <!-- Reservations Section -->
  <div class="card">
    <div class="card-header">Your Reservations</div>
    <div class="card-body">
      <a href="customerDashboard" class="btn btn-info mb-3 btn-custom">Refresh Reservations</a>
      <% if (reservations != null && !reservations.isEmpty()) { %>
      <div class="table-responsive">
        <table class="table table-striped table-bordered table-hover">
          <thead class="thead-dark">
          <tr>
            <th scope="col">ID</th>
            <th scope="col">Date</th>
            <th scope="col">Time</th>
            <th scope="col">Guests</th>
            <th scope="col">Status</th>
            <th scope="col">Action</th>
          </tr>
          </thead>
          <tbody>
          <% for (Reservation reservation : reservations) { %>
          <tr>
            <td><%= reservation.getReservationId() %></td>
            <td><%= reservation.getDate() %></td>
            <td><%= reservation.getTime() %></td>
            <td><%= reservation.getNumberOfGuests() %></td>
            <td><%= reservation.getStatus() %></td>
            <td>
              <% if ("Pending".equals(reservation.getStatus())) { %>
              <a href="payment.jsp?reservationId=<%= reservation.getReservationId() %>" class="btn btn-success btn-sm btn-custom" title="Make payment for this reservation">Pay Now</a>
              <a href="editReservation?reservationId=<%= reservation.getReservationId() %>" class="btn btn-primary btn-sm btn-custom">Edit</a>
              <form action="cancelReservation" method="post" style="display:inline;">
                <input type="hidden" name="reservationId" value="<%= reservation.getReservationId() %>">
                <button type="submit" class="btn btn-danger btn-sm btn-custom">Cancel</button>
              </form>
              <% } else if (!"Cancelled".equals(reservation.getStatus())) { %>
              <form action="cancelReservation" method="post" style="display:inline;">
                <input type="hidden" name="reservationId" value="<%= reservation.getReservationId() %>">
                <button type="submit" class="btn btn-danger btn-sm btn-custom">Cancel</button>
              </form>
              <% } %>
            </td>
          </tr>
          <% } %>
          </tbody>
        </table>
      </div>
      <% } else { %>
      <p class="text-center">No reservations found.</p>
      <% } %>
    </div>
  </div>
</div>
<!-- Scripts -->
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.3/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
