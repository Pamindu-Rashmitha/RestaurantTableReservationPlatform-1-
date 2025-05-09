package util;

import model.Review;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewManager {
    public List<Review> getAllReviews(String filePath) {
        List<Review> reviews = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    Review review = new Review(parts[0], parts[1], parts[2],
                            Integer.parseInt(parts[3]), parts[4], parts[5]);
                    reviews.add(review);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    public void addReview(Review review, String filePath) {
        List<Review> reviews = getAllReviews(filePath);
        reviews.add(review);
        saveReviews(reviews, filePath);
    }


    private void saveReviews(List<Review> reviews, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Review review : reviews) {
                writer.write(review.getReviewId() + "," + review.getUserId() + "," +
                        review.getReservationId() + "," + review.getRating() + "," +
                        review.getComment() + "," + review.getTimestamp());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean deleteReview(String reviewId, String filePath) {
        List<Review> reviews = getAllReviews(filePath);
        boolean removed = reviews.removeIf(review -> review.getReviewId().equals(reviewId));
        if (removed) {
            saveReviews(reviews, filePath);
        }
        return removed;
    }


    public boolean updateReview(String reviewId, int rating, String comment, String timestamp, String filePath) {
        List<Review> reviews = getAllReviews(filePath);
        for (Review review : reviews) {
            if (review.getReviewId().equals(reviewId)) {
                review.setRating(rating);
                review.setComment(comment);
                review.setTimestamp(timestamp);
                saveReviews(reviews, filePath);
                return true;
            }
        }
        return false;
    }


}