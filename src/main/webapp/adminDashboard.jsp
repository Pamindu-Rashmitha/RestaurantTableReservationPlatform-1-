<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User" %>
<%@ page import="model.Reservation" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%
  User user = (User) session.getAttribute("user");
  if (user == null || !"admin".equals(user.getRole())) {
    response.sendRedirect("login.jsp");
    return;
  }
  List<Reservation> allReservations = (List<Reservation>) request.getAttribute("allReservations");
  Reservation[] pendingReservations = (Reservation[]) request.getAttribute("pendingReservations");
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
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <title>Admin Dashboard</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <link rel="apple-touch-icon" sizes="180x180" href="assets/assets/apple-touch-icon.png">
  <link rel="icon" type="image/png" sizes="32x32" href="assets/assets/favicon-32x32.png">
  <link rel="icon" type="image/png" sizes="16x16" href="assets/assets/favicon-16x16.png">
  <link rel="manifest" href="assets/assets/site.webmanifest">
  <link rel="stylesheet" href="assets/css/adminDashboard.css">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
  <a class="navbar-brand" >Admin Dashboard</a>
  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav"
          aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
    <span class="navbar-toggler-icon"></span>
  </button>
  <div class="collapse navbar-collapse" id="navbarNav">
    <ul class="navbar-nav ml-auto">
      <li class="nav-item">
        <a href="login.jsp" class="nav-link">Logout</a>
      </li>
    </ul>
  </div>
</nav>

<div class="container overlay">
  <h1 class="dashboard-header">Welcome, <%= user.getName() %>!</h1>
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

  <!-- Pending Reservations Section -->
  <div class="card">
    <div class="card-header">Pending Reservations</div>
    <div class="card-body">
      <% if (pendingReservations != null && pendingReservations.length > 0) { %>
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
          </tr>
          </thead>
          <tbody>
          <% for (Reservation reservation : pendingReservations) { %>
          <tr>
            <td><%= reservation.getReservationId() %></td>
            <td><%= reservation.getUserId() %></td>
            <td><%= reservation.getDate() %></td>
            <td><%= reservation.getTime() %></td>
            <td><%= reservation.getNumberOfGuests() %></td>
            <td><%= reservation.getStatus() %></td>
          </tr>
          <% } %>
          </tbody>
        </table>
      </div>
      <form action="adminDashboard" method="post">
        <input type="hidden" name="action" value="processQueue">
        <button type="submit" class="btn btn-success mt-3">Process Next Reservation</button>
      </form>
      <% } else { %>
      <p>No pending reservations.</p>
      <% } %>
      <% if (request.getAttribute("message") != null) { %>
      <p class="mt-3"><%= request.getAttribute("message") %></p>
      <% } %>
    </div>
  </div>

  <!-- All Reservations Section -->
  <div class="card">
    <div class="card-header">All Reservations</div>
    <div class="card-body">
      <div class="mb-3 d-flex flex-wrap gap-2">
        <a href="adminDashboard" class="btn btn-info btn-custom mr-2">Refresh Reservations</a>
      </div>
      <% if (allReservations != null && !allReservations.isEmpty()) { %>
      <form action="adminDashboard" method="get" class="mb-3">
        <div class="row">
          <div class="col-md-4">
            <input type="text" name="searchTerm" class="form-control" placeholder="Search by Reservation ID, User, Date, or Time">
          </div>
          <div class="col-md-2">
            <button type="submit" class="btn btn-primary">Search</button>
          </div>
        </div>
      </form>
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
</div>

<!-- Modals -->
<div class="modal fade" id="removeCustomerModal" tabindex="-1" role="dialog" aria-labelledby="removeCustomerModalLabel" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">Confirm Removal</h5>
        <button type="button" class="close" data-dismiss="modal"><span>&times;</span></button>
      </div>
      <div class="modal-body">
        Are you sure you want to remove this customer? This will also delete all their reservations.
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
        <form action="removeCustomer" method="post">
          <input type="hidden" name="username" id="removeUsername">
          <button type="submit" class="btn btn-danger">Remove</button>
        </form>
      </div>
    </div>
  </div>
</div>

<div class="modal fade" id="cancelReservationModal" tabindex="-1" role="dialog" aria-labelledby="cancelReservationModalLabel" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">Confirm Cancellation</h5>
        <button type="button" class="close" data-dismiss="modal"><span>&times;</span></button>
      </div>
      <div class="modal-body">
        Are you sure you want to cancel this reservation?
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
        <form action="cancelReservationAdmin" method="post">
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
    $(this).find('#removeUsername').val(username);
  });

  $('#cancelReservationModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget);
    var reservationId = button.data('reservationid');
    $(this).find('#cancelReservationId').val(reservationId);
  });
</script>
</body>
</html>