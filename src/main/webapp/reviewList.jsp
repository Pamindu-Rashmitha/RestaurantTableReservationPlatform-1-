<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, model.Review" %>
<html>
<head>
    <title>Customer Reviews</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
</head>
<body>
<div class="container mt-4">
    <h2 class="mb-4">Customer Reviews</h2>

    <%-- Original form styling --%>
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
        <a href="customerDashboard.jsp" class="btn btn-info">Back</a>
    </form>
    <% } %>

    <% List<Review> reviews = (List<Review>) request.getAttribute("reviews"); %>
    <% if (reviews != null && !reviews.isEmpty()) { %>
    <div class="card shadow">
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-striped table-hover mb-0">
                    <thead class="thead-dark">
                    <tr>
                        <th>User</th>
                        <th>Rating</th>
                        <th>Comment</th>
                        <th>Date</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% for (Review review : reviews) { %>
                    <tr>
                        <td class="align-middle"><%= review.getUserId() %></td>
                        <td class="align-middle">
                            <% for (int i = 1; i <= 5; i++) { %>
                            <i class="<%= i <= review.getRating() ? "fas" : "far" %> fa-star text-warning"></i>
                            <% } %>
                        </td>
                        <td class="align-middle"><%= review.getComment() %></td>
                        <td class="align-middle"><%= review.getTimestamp() %></td>
                    </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <% } else { %>
    <div class="alert alert-info mt-3">No reviews found.</div>
    <% } %>
</div>
</body>
</html>