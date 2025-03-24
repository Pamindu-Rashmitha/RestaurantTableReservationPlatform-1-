<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User" %>
<%@ page import="model.Reservation" %>
<%
  User user = (User) session.getAttribute("user");
  if (user == null || !"customer".equals(user.getRole())) {
    response.sendRedirect("login.jsp");
    return;
  }
%>
<html>
<head>
  <title>Customer Dashboard</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
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
    <table class="table table-striped">
      <thead>
      <tr>
        <th>ID</th>
        <th>Date</th>
        <th>Time</th>
        <th>Guests</th>
        <th>Status</th>
        <th>Action</th>
      </tr>
      </thead>
      <tbody>
      <% Reservation[] reservations = new Reservation[0];
        for (Reservation reservation : reservations) { %>
      <tr>
        <td><%= reservation.getReservationId() %></td>
        <td><%= reservation.getDate() %></td>
        <td><%= reservation.getTime() %></td>
        <td><%= reservation.getNumberOfGuests() %></td>
        <td><%= reservation.getStatus() %></td>
        <td>
          <% if (!"Cancelled".equals(reservation.getStatus())) { %>
          <form action="cancelReservation" method="post">
            <input type="hidden" name="reservationId" value="<%= reservation.getReservationId() %>">
            <button type="submit" class="btn btn-danger btn-sm">Cancel</button>
          </form>
          <% } %>
        </td>
      </tr>
      <% } %>
      </tbody>
    </table>
  </div>
</body>
</html>

