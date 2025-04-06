<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User" %>
<%@ page import="model.Reservation" %>
<%@ page import="util.ReservationManager" %>
<%
  User user = (User) session.getAttribute("user");
  if (user == null) {
    response.sendRedirect("login.jsp");
    return;
  }
  String reservationId = request.getParameter("reservationId");
  ReservationManager reservationManager = new ReservationManager();
  String reservationFilePath = application.getRealPath("/WEB-INF/reservations.txt");
  Reservation reservation = reservationManager.getReservationById(reservationId, reservationFilePath);
  if (reservation == null || !reservation.getUserId().equals(user.getUsername())) {
    response.sendRedirect("customerDashboard.jsp");
    return;
  }
  int initialGuests = reservation.getNumberOfGuests();
  double initialAmount = 10.00 * initialGuests; // $10 per guest
%>
<html>
<head>
  <title>Payment</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <style>
    body {
      background-color: #f8f9fa;
      color: #212529;
      font-family: 'Segoe UI', Roboto, 'Helvetica Neue', sans-serif;
    }
    .payment-container {
      max-width: 500px;
      background-color: white;
      border-radius: 10px;
      padding: 30px;
      box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.05);
    }
    .payment-title {
      font-weight: 300;
      margin-bottom: 25px;
      color: #343a40;
    }
    .btn-pay {
      background-color: #6c63ff;
      border-color: #6c63ff;
      border-radius: 5px;
      padding: 10px 20px;
      font-weight: 500;
      width: 100%;
      transition: all 0.3s ease;
    }
    .btn-pay:hover, .btn-pay:focus {
      background-color: #5a52e0;
      border-color: #5a52e0;
      transform: translateY(-2px);
      box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    }
    .form-control {
      border: 1px solid #ced4da;
      border-radius: 5px;
      padding: 12px 15px;
    }
    .form-control:focus {
      border-color: #6c63ff;
      box-shadow: 0 0 0 0.2rem rgba(108, 99, 255, 0.25);
    }
    .card-details {
      display: none;
    }
  </style>
</head>
<body>
<div class="container d-flex justify-content-center align-items-center min-vh-100">
  <div class="payment-container">
    <h2 class="payment-title text-center">Payment for Reservation</h2>
    <% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-danger" role="alert">
      <%= request.getAttribute("error") %>
    </div>
    <% } %>
    <form action="processPayment" method="post">
      <input type="hidden" name="reservationId" value="<%= reservationId %>">
      <div class="form-group mb-4">
        <label for="numberOfGuests">Number of Guests</label>
        <input type="number" id="numberOfGuests" name="numberOfGuests" class="form-control" value="<%= initialGuests %>" min="1" required oninput="updateAmount()">
      </div>
      <div class="form-group mb-4">
        <label for="amount">Amount ($10 per guest)</label>
        <input type="text" id="amount" name="amount" class="form-control" value="<%= initialAmount %>" readonly>
      </div>
      <div class="form-group mb-4">
        <label for="paymentMethod">Payment Method</label>
        <select id="paymentMethod" name="paymentMethod" class="form-control" required onchange="toggleCardDetails()">
          <option value="Credit Card">Credit Card</option>
          <option value="Debit Card">Debit Card</option>
        </select>
      </div>
      <div id="cardDetails" class="card-details">
        <div class="form-group mb-4">
          <label for="cardNumber">Card Number</label>
          <input type="text" id="cardNumber" name="cardNumber" class="form-control" placeholder="1234 5678 9012 3456">
        </div>
        <div class="form-group mb-4">
          <label for="expiryDate">Expiry Date</label>
          <input type="text" id="expiryDate" name="expiryDate" class="form-control" placeholder="MM/YY">
        </div>
        <div class="form-group mb-4">
          <label for="cvv">CVV</label>
          <input type="text" id="cvv" name="cvv" class="form-control" placeholder="123">
        </div>
      </div>
      <button type="submit" class="btn btn-primary btn-pay">Pay Now</button>
    </form>
  </div>
</div>
<script>
  function updateAmount() {
    var guests = document.getElementById("numberOfGuests").value;
    var amount = guests * 10.00; // $10 per guest
    document.getElementById("amount").value = amount.toFixed(2);
  }

  function toggleCardDetails() {
    var method = document.getElementById("paymentMethod").value;
    var cardDetails = document.getElementById("cardDetails");
    if (method === "Credit Card" || method === "Debit Card") {
      cardDetails.style.display = "block";
    } else {
      cardDetails.style.display = "none";
    }
  }

  // Set initial visibility on page load
  toggleCardDetails();
</script>
</body>
</html>