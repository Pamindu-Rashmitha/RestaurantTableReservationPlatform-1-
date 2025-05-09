package controller;

import model.Review;
import model.User;
import util.ReviewManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@WebServlet("/review")
public class ReviewServlet extends HttpServlet {
    private ReviewManager reviewManager = new ReviewManager();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String filePath = getServletContext().getRealPath("/WEB-INF/reviews.txt");

        if ("list".equals(action)) {
            request.setAttribute("reviews", reviewManager.getAllReviews(filePath));
            request.getRequestDispatcher("/reviewList.jsp").forward(request, response);

        } else if ("edit".equals(action)) {
            String reviewId = request.getParameter("reviewId");
            Review review = reviewManager.getReviewById(reviewId, filePath);

            if (review == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Review not found");
                return;
            }

            User currentUser = (User) request.getSession().getAttribute("user");
            if (currentUser == null || !review.getUserId().equals(currentUser.getUsername())) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Unauthorized");
                return;
            }

            request.setAttribute("review", review);
            request.getRequestDispatcher("/updateReview.jsp").forward(request, response);

        } else if ("confirmDelete".equals(action)) {
            String reviewId = request.getParameter("reviewId");
            Review review = reviewManager.getReviewById(reviewId, filePath);

            if (review == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Review not found");
                return;
            }

            User currentUser = (User) request.getSession().getAttribute("user");
            if (currentUser == null || !review.getUserId().equals(currentUser.getUsername())) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Unauthorized");
                return;
            }

            request.setAttribute("review", review);
            request.getRequestDispatcher("/deleteReview.jsp").forward(request, response);
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String filePath = getServletContext().getRealPath("/WEB-INF/reviews.txt");
        User currentUser = (User) request.getSession().getAttribute("user");

        // Check authentication
        if (currentUser == null) {
            response.sendRedirect("login");
            return;
        }

        try {
            switch (action) {
                case "create":
                    handleCreateReview(request, response, currentUser, filePath);
                    break;
                case "update":
                    handleUpdateReview(request, response, currentUser, filePath);
                    break;
                case "delete":
                    handleDeleteReview(request, response, currentUser, filePath);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid numeric input");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error");
        }
    }

    private void handleCreateReview(HttpServletRequest request, HttpServletResponse response,
                                    User currentUser, String filePath) throws IOException {
        String reservationId = request.getParameter("reservationId");
        int rating = Integer.parseInt(request.getParameter("rating"));
        String comment = sanitizeInput(request.getParameter("comment"));
        String timestamp = LocalDateTime.now().toString();
        String reviewId = UUID.randomUUID().toString().substring(0, 8);

        Review review = new Review(
                reviewId,
                currentUser.getUsername(),
                reservationId,
                rating,
                comment,
                timestamp
        );

        reviewManager.addReview(review, filePath);
        response.sendRedirect("review?action=list");
    }

    private void handleUpdateReview(HttpServletRequest request, HttpServletResponse response,
                                    User currentUser, String filePath) throws IOException {
        String reviewId = request.getParameter("reviewId");
        int rating = Integer.parseInt(request.getParameter("rating"));
        String comment = sanitizeInput(request.getParameter("comment"));
        String timestamp = LocalDateTime.now().toString();

        Review existingReview = reviewManager.getReviewById(reviewId, filePath);
        if (existingReview == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Review not found");
            return;
        }

        if (!existingReview.getUserId().equals(currentUser.getUsername())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Can only update your own reviews");
            return;
        }

        if (reviewManager.updateReview(reviewId, rating, comment, timestamp, filePath)) {
            response.sendRedirect("review?action=list");
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Update failed");
        }
    }

    private void handleDeleteReview(HttpServletRequest request, HttpServletResponse response,
                                    User currentUser, String filePath) throws IOException {
        String reviewId = request.getParameter("reviewId");

        Review targetReview = reviewManager.getReviewById(reviewId, filePath);
        if (targetReview == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Review not found");
            return;
        }

        if (!targetReview.getUserId().equals(currentUser.getUsername())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Can only delete your own reviews");
            return;
        }

        if (reviewManager.deleteReview(reviewId, filePath)) {
            response.sendRedirect("review?action=list");
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Deletion failed");
        }
    }

    private String sanitizeInput(String input) {
        return input.replace(",", "&#44;").replace("\n", " ").trim();
    }
}