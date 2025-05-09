<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, model.Review" %>
<html>
<head>
  <title>Customer Reviews</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
  <style>
    body {
      background-image: url('assets/res.jpeg'); /* Update with your actual path */
      background-size: cover;
      background-repeat: no-repeat;
      background-attachment: fixed;
      background-position: center;
      color: #333;
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    }

    .container {
      background-color: rgba(255, 255, 255, 0.95);
      padding: 30px;
      border-radius: 12px;
      box-shadow: 0 0 20px rgba(0,0,0,0.2);
      margin-top: 50px;
    }

    h2 {
      text-align: center;
      margin-bottom: 30px;
      font-weight: bold;
      color: #444;
    }

    .btn-custom {
      background-color: #6c757d;
      color: white;
    }

    .table thead th {
      background-color: #343a40;
      color: white;
    }

    .table td {
      vertical-align: middle;
    }

    .card {
      margin-top: 30px;
      border-radius: 10px;
    }
  </style>

</head>
<body>
<div class="container mt-4">
  <h2>Customer Reviews</h2>

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

  <!-- Edit Review Form -->
  <form id="editForm" action="review" method="post" class="mb-4" style="display:none;">
    <input type="hidden" name="action" value="update">
    <input type="hidden" name="reviewId" id="editReviewId">
    <div class="form-group">
      <label>Rating (1-5)</label>
      <input type="number" name="rating" id="editRating" class="form-control" min="1" max="5" required>
    </div>
    <div class="form-group">
      <label>Comment</label>
      <textarea name="comment" id="editComment" class="form-control" required></textarea>
    </div>
    <button type="submit" class="btn btn-warning">Update Review</button>
    <button type="button" class="btn btn-secondary" onclick="document.getElementById('editForm').style.display='none'">Cancel</button>
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
            <th>Actions</th>
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
            <td class="align-middle">
              <a href="review?action=edit&reviewId=<%= review.getReviewId() %>" class="btn btn-secondary btn-sm">Edit</a>
              <a href="review?action=confirmDelete&reviewId=<%= review.getReviewId() %>" class="btn btn-danger btn-sm">Delete</a>
            </td>
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

<script>
  function prefillEditForm(reviewId, rating, comment) {
    document.getElementById("editReviewId").value = reviewId;
    document.getElementById("editRating").value = rating;
    document.getElementById("editComment").value = comment;
    document.getElementById("editForm").style.display = "block";
    window.scrollTo(0, document.getElementById("editForm").offsetTop);
  }
</script>
</body>
</html>
