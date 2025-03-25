<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Reservation" %>
<%
    Reservation reservation = (Reservation) request.getAttribute("reservation");
    if (reservation == null) {
        response.sendRedirect("customerDashboard.jsp");
        return;
    }
%>
<html>
<head>
    <title>Edit Reservation</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-5">
    <h2>Edit Reservation: <%= reservation.getReservationId() %></h2>
    <p>Current Status: <%= reservation.getStatus() %></p>
    <form action="updateReservation" method="post">
        <input type="hidden" name="reservationId" value="<%= reservation.getReservationId() %>">
        <div class="form-group">
            <label>Date:</label>
            <input type="date" name="date" class="form-control" value="<%= reservation.getDate() %>" required>
        </div>
        <div class="form-group">
            <label>Time:</label>
            <input type="time" name="time" class="form-control" value="<%= reservation.getTime() %>" required>
        </div>
        <div class="form-group">
            <label>Number of Guests:</label>
            <input type="number" name="numberOfGuests" class="form-control"
                   value="<%= reservation.getNumberOfGuests() %>" required min="1">
        </div>
        <button type="submit" class="btn btn-primary">Update Reservation</button>
        <a href="customerDashboard.jsp" class="btn btn-secondary">Cancel</a>
    </form>
</div>
</body>
</html>