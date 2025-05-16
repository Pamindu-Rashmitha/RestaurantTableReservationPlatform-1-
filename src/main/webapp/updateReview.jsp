<%--
  Created by IntelliJ IDEA.
  User: pamid
  Date: 5/10/2025
  Time: 1:35 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Review" %>
<%
  Review review = (Review) request.getAttribute("review");
  if (review == null) {
    response.sendRedirect("reviews.jsp"); // fallback
    return;
  }
%>
<html>
<head>
  <title>Update Review</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <style>
    body {
      background-image: url('assets/res.jpeg');
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    }

    .container {
      background-color: #fff;
      padding: 40px;
      border-radius: 12px;
      box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
      margin-top: 60px;
      max-width: 600px;
    }

    h2 {
      text-align: center;
      color: #333;
      margin-bottom: 30px;
      font-weight: bold;
    }

    .form-group label {
      font-weight: 500;
      color: #555;
    }

    .form-control {
      border-radius: 6px;
      border: 1px solid #ccc;
      transition: border-color 0.3s;
    }

    .form-control:focus {
      border-color: #007bff;
      box-shadow: none;
    }

    .btn-warning {
      background-color: #ffc107;
      border: none;
      font-weight: bold;
      width: 120px;
    }

    .btn-warning:hover {
      background-color: #e0a800;
    }

    .btn-secondary {
      margin-left: 10px;
      font-weight: bold;
      width: 120px;
    }

    .text-center {
      text-align: center;
    }
  </style>

</head>
<body>
<div class="container mt-4">
  <h2>Edit Review</h2>
  <form action="updateReview" method="post">
    <input type="hidden" name="action" value="update">
    <input type="hidden" name="reviewId" value="<%= review.getReviewId() %>">
    <div class="form-group">
      <label>Rating</label>
      <input type="number" name="rating" class="form-control" min="1" max="5" value="<%= review.getRating() %>" required>
    </div>
    <div class="form-group">
      <label>Comment</label>
      <textarea name="comment" class="form-control" required><%= review.getComment() %></textarea>
    </div>
    <button type="submit" class="btn btn-warning">Update</button>
    <a href="reviewList.jsp" class="btn btn-secondary">Cancel</a>
  </form>
</div>
</body>
</html>

