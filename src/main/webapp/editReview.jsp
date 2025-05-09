<%--
  Created by IntelliJ IDEA.
  User: pamid
  Date: 5/9/2025
  Time: 9:34 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit Review</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-4">
  <h2>Edit Review</h2>
  <form action="review" method="post">
    <input type="hidden" name="action" value="update">
    <input type="hidden" name="reviewId" value="${review.reviewId}">
    <div class="form-group">
      <label>Rating (1-5)</label>
      <input type="number" name="rating" class="form-control" min="1" max="5" value="${review.rating}" required>
    </div>
    <div class="form-group">
      <label>Comment</label>
      <textarea name="comment" class="form-control" required>${review.comment}</textarea>
    </div>
    <button type="submit" class="btn btn-primary">Update Review</button>
    <a href="review?action=list" class="btn btn-secondary">Cancel</a>
  </form>
</div>
</body>
</html>
