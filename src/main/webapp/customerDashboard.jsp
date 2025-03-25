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
%>
<%
  List<Reservation> reservations = (List<Reservation>) request.getAttribute("reservations");
%>
<html>
<head>
  <title>Customer Dashboard</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <style>
    .table th, .table td {
      text-align: center; /* Center-align text in all cells */
    }
    .table th:first-child, .table td:first-child {
      width: 10%; /* Make the ID column narrower */
    }
  </style>
</head>
<body>
<div class="container mt-5">
  <h2>Welcome, <%= user.getName() %>!</h2>
  <br><br><br>
  <p>"Welcome to your dashboard! Here, you can easily view and manage your upcoming reservations,
    check availability, and modify your bookings with just a few clicks. Enjoy a seamless dining
    experience by securing your table in advance. Need to make changes? No problem—your reservation
    details are always at your fingertips. Start planning your perfect meal now!"</p>
  <br><br><br>
  <a href="makeReservation.jsp" class="btn btn-primary">Make a Reservation</a>
  <a href="logout" class="btn btn-danger">Logout</a>
  <br><br><br>
  <div class="container">
    <h2>Your Reservations</h2>
    <% if (reservations != null && !reservations.isEmpty()) { %>
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
          <a href="editReservation?reservationId=<%= reservation.getReservationId() %>" class="btn btn-primary btn-sm">Edit</a>
          <form action="cancelReservation" method="post" style="display:inline;">
            <input type="hidden" name="reservationId" value="<%= reservation.getReservationId() %>">
            <button type="submit" class="btn btn-danger btn-sm">Cancel</button>
          </form>
          <% } else if (!"Cancelled".equals(reservation.getStatus())) { %>
          <form action="cancelReservation" method="post" style="display:inline;">
            <input type="hidden" name="reservationId" value="<%= reservation.getReservationId() %>">
            <button type="submit" class="btn btn-danger btn-sm">Cancel</button>
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
  </div>
</div>
</body>
</html>