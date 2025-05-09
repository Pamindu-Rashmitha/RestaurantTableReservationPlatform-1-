package model;

public class Review {
    private String reviewId;
    private String userName;
    private String reservationId;
    private int rating;
    private String comment;
    private String timestamp;

    public Review(String reviewId, String userName, String reservationId, int rating, String comment, String timestamp) {
        this.reviewId = reviewId;
        this.userName = userName;
        this.reservationId = reservationId;
        this.rating = rating;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    // Getters
    public String getReviewId() {
        return reviewId;
    }
    public String getUserId() {
        return userName;
    }
    public String getReservationId() {
        return reservationId;
    }
    public int getRating() {
        return rating;
    }
    public String getComment() {
        return comment;
    }
    public String getTimestamp() {
        return timestamp;
    }

    // Setters (for update operations)
    public void setRating(int rating) {
        this.rating = rating;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}