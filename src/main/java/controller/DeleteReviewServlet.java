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

@WebServlet("/deleteReview")
public class DeleteReviewServlet extends HttpServlet {
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
        request.getRequestDispatcher("/deleteReview.jsp").forward(request, response);
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

        if (reviewManager.deleteReview(reviewId, filePath)) {
            response.sendRedirect("listReviews");
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Deletion failed");
        }
    }
}