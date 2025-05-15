<%--
  Created by IntelliJ IDEA.
  User: pamid
  Date: 5/10/2025
  Time: 1:37 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Review" %>
<%
  Review review = (Review) request.getAttribute("review");
  if (review == null) {
    response.sendRedirect("reviews.jsp");
    return;
  }
%>
<html>
<head>
  <title>Delete Review</title>
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
      color: #c82333;
      margin-bottom: 20px;
      font-weight: bold;
    }

    p {
      text-align: center;
      font-size: 16px;
      color: #555;
    }

    ul {
      list-style-type: none;
      padding: 0;
      margin-bottom: 30px;
    }

    li {
      margin-bottom: 10px;
      font-size: 16px;
      color: #333;
    }

    .btn-danger {
      background-color: #dc3545;
      border: none;
      font-weight: bold;
      width: 150px;
    }

    .btn-danger:hover {
      background-color: #bd2130;
    }

    .btn-secondary {
      margin-left: 10px;
      width: 150px;
      font-weight: bold;
    }

    .text-center {
      text-align: center;
    }
  </style>

</head>
<body>
<div class="container mt-4">
  <h2>Confirm Delete</h2>
  <p>Are you sure you want to delete the following review?</p>
  <ul>
    <li><strong>User:</strong> <%= review.getUserId() %></li>
    <li><strong>Rating:</strong> <%= review.getRating() %></li>
    <li><strong>Comment:</strong> <%= review.getComment() %></li>
  </ul>
  <form action="deleteReview" method="post">
    <input type="hidden" name="action" value="delete">
    <input type="hidden" name="reviewId" value="<%= review.getReviewId() %>">
    <button type="submit" class="btn btn-danger">Confirm Delete</button>
    <a href="reviewList.jsp" class="btn btn-secondary">Cancel</a>
  </form>
</div>
</body>
</html>
