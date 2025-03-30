<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <!-- Responsive Meta Tag -->
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Make a Reservation</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        /* Full background image */
        body {
            background: url('assets/res.jpeg') no-repeat center center fixed;
            background-size: cover;
            font-family: 'Segoe UI', Roboto, 'Helvetica Neue', sans-serif;
            color: #212529;
            margin: 0;
            padding: 0;
        }
        /* Overlay for content readability */
        .overlay {
            background: rgba(255, 255, 255, 0.95);
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.1);
            margin-top: 30px;
            margin-bottom: 30px;
        }
        .dashboard-header {
            text-align: center;
            margin-bottom: 30px;
            font-size: 2.5rem;
            color: #343a40;
            font-weight: bold;
        }
        .btn-custom {
            transition: all 0.3s ease;
        }
        .btn-custom:hover, .btn-custom:focus {
            transform: translateY(-2px);
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }
        /* Responsive adjustments */
        @media (max-width: 768px) {
            .dashboard-header {
                font-size: 2rem;
            }
            .overlay {
                padding: 20px;
            }
        }
        @media (max-width: 576px) {
            .dashboard-header {
                font-size: 1.8rem;
            }
            .overlay {
                padding: 15px;
            }
        }
    </style>
</head>
<body>
<div class="container overlay">
    <h1 class="dashboard-header">Make a Reservation</h1>
    <% if (request.getAttribute("error") != null) { %>
    <p class="text-danger text-center"><%= request.getAttribute("error") %></p>
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
        <button type="submit" class="btn btn-primary btn-custom">Make Reservation</button>
    </form>
</div>
<!-- Scripts -->
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.3/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
