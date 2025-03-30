<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User" %>
<%@ page import="model.Reservation" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%
  User user = (User) session.getAttribute("user");
  if (user == null || !"admin".equals(user.getRole())) {
    response.sendRedirect("login.jsp");
    return;
  }
  List<Reservation> allReservations = (List<Reservation>) request.getAttribute("allReservations");
  List<User> allUsers = (List<User>) request.getAttribute("allUsers");
  List<User> customers = new ArrayList<>();
  if (allUsers != null) {
    for (User u : allUsers) {
      if ("customer".equals(u.getRole())) {
        customers.add(u);
      }
    }
  }
%>
<html>
<head>
  <meta charset="UTF-8">
  <!-- Responsive Meta Tag -->
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <title>Admin Dashboard</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <style>
    /* Full background image with fixed positioning */
    body {
      background: url('assets/res.jpeg') no-repeat center center fixed;
      background-size: cover;
      font-family: 'Segoe UI', Roboto, 'Helvetica Neue', sans-serif;
      color: #212529;
      margin: 0;
      padding: 0;
    }
    /* Overlay for readability */
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
    .table-hover tbody tr:hover {
      background-color: rgba(241, 241, 241, 0.8);
    }
    /* Modal enhancements */
    .modal-content {
      border-radius: 10px;
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
  <h1 class="dashboard-header">Admin Dashboard</h1>
  <p class="text-center">Welcome, <%= user.getName() %>!</p>
  <br>

  <!-- Registered Customers Section -->
  <div class="card">
    <div class="card-header">Registered Customers</div>
    <div class="card-body">
      <% if (customers != null && !customers.isEmpty()) { %>
      <div class="table-responsive">
        <table class="table table-striped table-bordered table-hover">
          <thead class="thead-dark">
          <tr>
            <th>Username</th>
            <th>Name</th>
            <th>Role</th>
            <th>Action</th>
          </tr>
          </thead>
          <tbody>
          <% for (User customer : customers) { %>
          <tr>
            <td><%= customer.getUsername() %></td>
            <td><%= customer.getName() %></td>
            <td><%= customer.getRole() %></td>
            <td>
              <button type="button" class="btn btn-danger btn-sm btn-custom" data-toggle="modal" data-target="#removeCustomerModal" data-username="<%= customer.getUsername() %>">
                Remove
              </button>
            </td>
          </tr>
          <% } %>
          </tbody>
        </table>
      </div>
      <% } else { %>
      <p>No registered customers found.</p>
      <% } %>
    </div>
  </div>

  <!-- Reservations Section -->
  <div class="card">
    <div class="card-header">All Reservations</div>
    <div class="card-body">
      <a href="adminDashboard" class="btn btn-info mb-3 btn-custom">Refresh Reservations</a>
      <% if (allReservations != null && !allReservations.isEmpty()) { %>
      <div class="table-responsive">
        <table class="table table-striped table-bordered table-hover">
          <thead class="thead-dark">
          <tr>
            <th>ID</th>
            <th>User</th>
            <th>Date</th>
            <th>Time</th>
            <th>Guests</th>
            <th>Status</th>
            <th>Action</th>
          </tr>
          </thead>
          <tbody>
          <% for (Reservation reservation : allReservations) { %>
          <tr>
            <td><%= reservation.getReservationId() %></td>
            <td><%= reservation.getUserId() %></td>
            <td><%= reservation.getDate() %></td>
            <td><%= reservation.getTime() %></td>
            <td><%= reservation.getNumberOfGuests() %></td>
            <td><%= reservation.getStatus() %></td>
            <td>
              <% if ("Paid".equals(reservation.getStatus())) { %>
              <form action="confirmReservation" method="post" style="display:inline;">
                <input type="hidden" name="reservationId" value="<%= reservation.getReservationId() %>">
                <button type="submit" class="btn btn-primary btn-sm btn-custom" title="Confirm this reservation (payment completed)">
                  Confirm
                </button>
              </form>
              <% } %>
              <% if (!"Cancelled".equals(reservation.getStatus())) { %>
              <button type="button" class="btn btn-danger btn-sm btn-custom" data-toggle="modal" data-target="#cancelReservationModal" data-reservationid="<%= reservation.getReservationId() %>">
                Cancel
              </button>
              <% } %>
            </td>
          </tr>
          <% } %>
          </tbody>
        </table>
      </div>
      <% } else { %>
      <p>No reservations found.</p>
      <% } %>
    </div>
  </div>
  <a href="logout" class="btn btn-danger mt-3 btn-custom">Logout</a>
</div>

<!-- Remove Customer Modal -->
<div class="modal fade" id="removeCustomerModal" tabindex="-1" role="dialog" aria-labelledby="removeCustomerModalLabel" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="removeCustomerModalLabel">Confirm Removal</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        Are you sure you want to remove this customer? This will also delete all their reservations.
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
        <form action="removeCustomer" method="post" style="display:inline;">
          <input type="hidden" name="username" id="removeUsername">
          <button type="submit" class="btn btn-danger">Remove</button>
        </form>
      </div>
    </div>
  </div>
</div>

<!-- Cancel Reservation Modal -->
<div class="modal fade" id="cancelReservationModal" tabindex="-1" role="dialog" aria-labelledby="cancelReservationModalLabel" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="cancelReservationModalLabel">Confirm Cancellation</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        Are you sure you want to cancel this reservation?
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
        <form action="cancelReservationAdmin" method="post" style="display:inline;">
          <input type="hidden" name="reservationId" id="cancelReservationId">
          <button type="submit" class="btn btn-danger">Cancel Reservation</button>
        </form>
      </div>
    </div>
  </div>
</div>

<!-- Scripts -->
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.3/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<script>
  $('#removeCustomerModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget);
    var username = button.data('username');
    var modal = $(this);
    modal.find('#removeUsername').val(username);
  });

  $('#cancelReservationModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget);
    var reservationId = button.data('reservationid');
    var modal = $(this);
    modal.find('#cancelReservationId').val(reservationId);
  });
</script>
</body>
</html>
