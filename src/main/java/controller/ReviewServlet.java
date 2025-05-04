package controller;

import model.Review;
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
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String filePath = getServletContext().getRealPath("/WEB-INF/reviews.txt");

        if ("create".equals(action)) {
            String userId = request.getParameter("userId");
            String reservationId = request.getParameter("reservationId");
            int rating = Integer.parseInt(request.getParameter("rating"));
            String comment = request.getParameter("comment");
            String timestamp = LocalDateTime.now().toString();
            String reviewId = UUID.randomUUID().toString().substring(0, 8);

            Review review = new Review(reviewId, userId, reservationId, rating, comment, timestamp);
            reviewManager.addReview(review, filePath);
            response.sendRedirect("review?action=list");

        }
    }
}
