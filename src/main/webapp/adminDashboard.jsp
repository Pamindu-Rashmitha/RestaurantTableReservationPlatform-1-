<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*,model.User,model.Reservation" %>

<%

  User user = (User) session.getAttribute("user");
  if (user == null || !"admin".equalsIgnoreCase(user.getRole())) {
    response.sendRedirect("login.jsp");
    return;
  }

  /* ---------- Data from servlet ---------- */
  @SuppressWarnings("unchecked")
  List<Reservation> confirmedReservations =
          (List<Reservation>) request.getAttribute("confirmedReservations");

  @SuppressWarnings("unchecked")
  List<Reservation> waitingReservations =
          (List<Reservation>) request.getAttribute("waitingReservations");

  @SuppressWarnings("unchecked")
  List<User> allUsers = (List<User>) request.getAttribute("allUsers");

  List<User> customers = new ArrayList<>();
  if (allUsers != null) {
    for (User u : allUsers) if ("customer".equals(u.getRole())) customers.add(u);
  }

  /* ---------- Flash message ---------- */
  String flash = (String) session.getAttribute("statusMessage");
  if (flash != null) session.removeAttribute("statusMessage");
%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Admin Dashboard</title>
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
    .navbar-text a {
      color: #ffffff !important;
      transition: color 0.3s;
    }
    .navbar-text a:hover {
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
    h4 {
      font-size: 1.5rem;
      font-weight: 600;
      color: #4a5568;
      margin-top: 2rem;
      margin-bottom: 1rem;
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
    .btn-sm {
      padding: 0.4rem 0.8rem;
      font-size: 0.875rem;
    }
    .btn-lg {
      padding: 0.8rem 1.5rem;
      font-size: 1.1rem;
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
      background-color: 1a202c;
      color: #fff;
      font-weight: 600;
      padding: 1rem;
    }
    .table td {
      padding: 1rem;
      vertical-align: middle;
      border-color: #e2e8f0;
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

    /* Responsive adjustments */
    @media (max-width: 768px) {
      .overlay {
        padding: 1.5rem;
        margin: 1rem;
      }
      h2 {
        font-size: 1.75rem;
      }
      h4 {
        font-size: 1.25rem;
      }
      .table th, .table td {
        padding: 0.75rem;
        font-size: 0.9rem;
      }
      .btn-lg {
        padding: 0.6rem 1rem;
        font-size: 1rem;
      }
    }

    @media (max-width: 576px) {
      .navbar {
        padding: 0.75rem 1rem;
      }
      .btn-sm {
        display: block;
        margin-bottom: 0.5rem;
        width: 100%;
        text-align: center;
      }
      .table-responsive {
        -webkit-overflow-scrolling: touch;
      }
    }
  </style>
</head>
<body>
<nav class="navbar navbar-dark bg-dark">
  <a class="navbar-brand"><i class="fas fa-tachometer-alt mr-2"></i>Admin Dashboard</a>
  <span class="navbar-text ml-auto"><a href="login.jsp"><i class="fas fa-sign-out-alt mr-1"></i>Logout</a></span>
</nav>

<div class="container overlay">
  <h2 class="mb-4">Welcome, <%= user.getName() %>!</h2>

  <a href="adminDashboard" class="btn btn-lg btn-primary">
    <i class="fas fa-sync-alt mr-2"></i>
  </a>

  <% if (flash != null) { %>
  <div class="alert alert-info"><i class="fas fa-info-circle mr-2"></i><%= flash %></div>
  <% } %>

  <!-- Registered customers -->
  <h4><i class="fas fa-users mr-2"></i>Registered Customers</h4>
  <% if (customers.isEmpty()) { %>
  <p class="text-muted">No registered customers.</p>
  <% } else { %>
  <div class="table-responsive">
    <table class="table table-bordered table-hover">
      <thead class="thead-dark"><tr><th>Username</th><th>Name</th><th>Role</th><th>Action</th></tr></thead>
      <tbody>
      <% for (User c : customers) { %>
      <tr>
        <td><%= c.getUsername() %></td>
        <td><%= c.getName() %></td>
        <td><%= c.getRole() %></td>
        <td>
          <form action="removeCustomer" method="post" class="d-inline">
            <input type="hidden" name="username" value="<%= c.getUsername() %>">
            <button class="btn btn-danger btn-sm"><i class="fas fa-trash-alt mr-1"></i>Remove</button>
          </form>
        </td>
      </tr>
      <% } %>
      </tbody>
    </table>
  </div>
  <% } %>

  <!-- Confirmed queue -->
  <h4 class="mt-4"><i class="fas fa-check-circle mr-2"></i>Active Reservations (Confirmed)</h4>
  <% if (confirmedReservations == null || confirmedReservations.isEmpty()) { %>
  <p class="text-muted">No confirmed reservations.</p>
  <% } else { %>
  <div class="table-responsive">
    <table class="table table-bordered table-hover">
      <thead class="thead-dark"><tr>
        <th>ID</th><th>User</th><th>Date</th><th>Time</th><th>Guests</th><th>Status</th><th>Action</th>
      </tr></thead>
      <tbody>
      <% for (Reservation r : confirmedReservations) { %>
      <tr>
        <td><%= r.getReservationId() %></td>
        <td><%= r.getUserId() %></td>
        <td><%= r.getDate() %></td>
        <td><%= r.getTime() %></td>
        <td><%= r.getNumberOfGuests() %></td>
        <td><span class="badge badge-success"><i class="fas fa-check mr-1"></i>Confirmed</span></td>
        <td>
          <form action="cancelReservationAdmin" method="post" class="d-inline">
            <input type="hidden" name="reservationId" value="<%= r.getReservationId() %>">
            <button class="btn btn-danger btn-sm"><i class="fas fa-times mr-1"></i>Cancel</button>
          </form>
        </td>
      </tr>
      <% } %>
      </tbody>
    </table>
  </div>
  <% } %>

  <!-- Waiting list -->
  <h4 class="mt-4"><i class="fas fa-hourglass-half mr-2"></i>Waiting List</h4>
  <% if (waitingReservations == null || waitingReservations.isEmpty()) { %>
  <p class="text-muted">No entries in waiting list.</p>
  <% } else { %>
  <div class="table-responsive">
    <table class="table table-bordered table-hover">
      <thead class="thead-dark"><tr>
        <th>Pos</th><th>ID</th><th>User</th><th>Date</th><th>Time</th><th>Guests</th><th>Status</th><th>Action</th>
      </tr></thead>
      <tbody>
      <% int pos = 1;
        for (Reservation r : waitingReservations) { %>
      <tr>
        <td><%= pos++ %></td>
        <td><%= r.getReservationId() %></td>
        <td><%= r.getUserId() %></td>
        <td><%= r.getDate() %></td>
        <td><%= r.getTime() %></td>
        <td><%= r.getNumberOfGuests() %></td>
        <td><span class="badge badge-warning"><i class="fas fa-hourglass mr-1"></i>Waiting</span></td>
        <td>
          <form action="cancelReservationAdmin" method="post" class="d-inline">
            <input type="hidden" name="reservationId" value="<%= r.getReservationId() %>">
            <button class="btn btn-danger btn-sm"><i class="fas fa-times mr-1"></i>Cancel</button>
          </form>
        </td>
      </tr>
      <% } %>
      </tbody>
    </table>
  </div>
  <% } %>
</div>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>