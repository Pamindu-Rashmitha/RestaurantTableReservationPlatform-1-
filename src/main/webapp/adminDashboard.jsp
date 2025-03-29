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
  // Filter to show only customers
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
  <title>Admin Dashboard</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-5">
  <h2><a href="adminDashboard.jsp">Admin Dashboard</a></h2>
  <br>
  <h2>Welcome, <%= user.getName() %>!</h2>
  <br><br>
  <!-- Registered Customers Section -->
  <h3>Registered Customers</h3>
  <% if (customers != null && !customers.isEmpty()) { %>
  <table class="table table-striped table-bordered">
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
        <form action="removeCustomer" method="post" style="display:inline;">
          <input type="hidden" name="username" value="<%= customer.getUsername() %>">
          <button type="submit" class="btn btn-danger btn-sm"
                  title="Remove this customer and their reservations"
                  onclick="return confirm('Are you sure you want to remove this customer? This will also delete all their reservations.')">
            Remove
          </button>
        </form>
      </td>
    </tr>
    <% } %>
    </tbody>
  </table>
  <% } else { %>
  <p>No registered customers found.</p>
  <% } %>
  <br><br>
  <!-- Reservations Section -->
  <h3>All Reservations</h3>
  <a href="adminDashboard" class="btn btn-info mb-3">Refresh Reservations</a>
  <% if (allReservations != null && !allReservations.isEmpty()) { %>
  <table class="table table-striped table-bordered">
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
          <button type="submit" class="btn btn-primary btn-sm"
                  title="Confirm this reservation (payment completed)">
            Confirm
          </button>
        </form>
        <% } %>
        <% if (!"Cancelled".equals(reservation.getStatus())) { %>
        <form action="cancelReservationAdmin" method="post" style="display:inline;">
          <input type="hidden" name="reservationId" value="<%= reservation.getReservationId() %>">
          <button type="submit" class="btn btn-danger btn-sm"
                  title="Cancel this reservation">
            Cancel
          </button>
        </form>
        <% } %>
      </td>
    </tr>
    <% } %>
    </tbody>
  </table>
  <% } else { %>
  <p>No reservations found.</p>
  <% } %>
  <a href="logout" class="btn btn-danger">Logout</a>
</div>
</body>
</html>