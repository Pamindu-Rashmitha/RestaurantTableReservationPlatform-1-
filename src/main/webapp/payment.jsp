<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Payment</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-5">
  <h2>Payment for Reservation: <%= request.getParameter("reservationId") %></h2>
  <form action="payment" method="post">
    <input type="hidden" name="reservationId" value="<%= request.getParameter("reservationId") %>">
    <div class="form-group">
      <label>Card Number:</label>
      <input type="text" name="cardNumber" class="form-control" required>
    </div>
    <div class="form-group">
      <label>Expiry Date:</label>
      <input type="text" name="expiryDate" class="form-control" required>
    </div>
    <div class="form-group">
      <label>CVV:</label>
      <input type="text" name="cvv" class="form-control" required>
    </div>
    <button type="submit" class="btn btn-success">Pay Now</button>
    <a href="customerDashboard.jsp" class="btn btn-secondary">Cancel</a>
  </form>
</div>
</body>
</html>