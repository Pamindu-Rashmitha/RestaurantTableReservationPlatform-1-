<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Make a Reservation</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-5">
    <h2>Make a Reservation</h2>
    <% if (request.getAttribute("error") != null) { %>
    <p class="text-danger"><%= request.getAttribute("error") %></p>
    <% } %>
    <form action="reservation" method="post">
        <div class="form-group">
            <label>Date:</label>
            <input type="date" name="date" class="form-control" required>
        </div>
        <div class="form-group">
            <label>Time:</label>
            <input type="time" name="time" class="form-control" required>
        </div>
        <div class="form-group">
            <label>Number of Guests:</label>
            <input type="number" name="numberOfGuests" class="form-control" required min="1">
        </div>
        <button type="submit" class="btn btn-primary">Make Reservation</button>
    </form>
</div>
</body>
</html>
