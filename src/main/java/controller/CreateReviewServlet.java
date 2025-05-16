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

@WebServlet("/createReview")
public class CreateReviewServlet extends HttpServlet {
    private ReviewManager reviewManager = new ReviewManager();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User currentUser = (User) request.getSession().getAttribute("user");
        if (currentUser == null) {
            response.sendRedirect("login");
            return;
        }

        String filePath = getServletContext().getRealPath("/data/reviews.txt");
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
        response.sendRedirect("listReviews");
    }

    private String sanitizeInput(String input) {
        return input.replace(",", "&#44;").replace("\n", " ").trim();
    }
}
