<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, model.Review" %>
<html>
<head>
    <title>Customer Reviews</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-4">
    <h2>Customer Reviews</h2>

    <%-- Hide the form if user not logged in (assumes session attribute "user" is set) --%>
    <% if (session.getAttribute("user") != null) { %>
    <!-- Create Review Form -->
    <form action="review" method="post" class="mb-4">
        <input type="hidden" name="action" value="create">
        <div class="form-group">
            <label>User Name</label>
            <input type="text" name="userId" class="form-control" required>
        </div>
        <div class="form-group">
            <label>Reservation ID</label>
            <input type="text" name="reservationId" class="form-control" required>
        </div>
        <div class="form-group">
            <label>Rating (1-5)</label>
            <input type="number" name="rating" class="form-control" min="1" max="5" required>
        </div>
        <div class="form-group">
            <label>Comment</label>
            <textarea name="comment" class="form-control" required></textarea>
        </div>
        <button type="submit" class="btn btn-primary">Submit Review</button>
        <a href="customerDashboard.jsp" class="btn btn-info btn-custom">Back</a>
    </form>
    <% } %>

    <% List<Review> reviews = (List<Review>) request.getAttribute("reviews"); %>
    <% if (reviews != null && !reviews.isEmpty()) { %>
    <table class="table table-striped">
        <thead>
        <tr>
            <th>Review ID</th>
            <th>User Name</th>
            <th>Reservation ID</th>
            <th>Rating</th>
            <th>Comment</th>
            <th>Timestamp</th>
        </tr>
        </thead>
        <tbody>
        <% for (Review review : reviews) { %>
        <tr>
            <td><%= review.getReviewId() %></td>
            <td><%= review.getUserId() %></td>
            <td><%= review.getReservationId() %></td>
            <td><%= review.getRating() %></td>
            <td><%= review.getComment() %></td>
            <td><%= review.getTimestamp() %></td>
        </tr>
        <% } %>
        </tbody>
    </table>
    <% } else { %>
    <p>No reviews found.</p>
    <% } %>
</div>
</body>
</html>
