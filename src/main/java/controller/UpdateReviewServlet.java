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

@WebServlet("/updateReview")
public class UpdateReviewServlet extends HttpServlet {
    private ReviewManager reviewManager = new ReviewManager();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String reviewId = request.getParameter("reviewId");
        String filePath = getServletContext().getRealPath("/data/reviews.txt");
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
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User currentUser = (User) request.getSession().getAttribute("user");
        if (currentUser == null) {
            response.sendRedirect("login");
            return;
        }

        String filePath = getServletContext().getRealPath("/data/reviews.txt");
        String reviewId = request.getParameter("reviewId");
        int rating = Integer.parseInt(request.getParameter("rating"));
        String comment = sanitizeInput(request.getParameter("comment"));
        String timestamp = LocalDateTime.now().toString();

        Review existingReview = reviewManager.getReviewById(reviewId, filePath);
        if (existingReview == null || !existingReview.getUserId().equals(currentUser.getUsername())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Unauthorized");
            return;
        }

        if (reviewManager.updateReview(reviewId, rating, comment, timestamp, filePath)) {
            response.sendRedirect("listReviews");
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Update failed");
        }
    }

    private String sanitizeInput(String input) {
        return input.replace(",", "&#44;").replace("\n", " ").trim();
    }
}