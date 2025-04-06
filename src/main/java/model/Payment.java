package model;

public class Payment {
    private String paymentId;
    private String reservationId;
    private String userId;
    private double amount;
    private String paymentMethod;
    private String status;

    public Payment(String paymentId, String reservationId, String userId, double amount, String paymentMethod, String status) {
        this.paymentId = paymentId;
        this.reservationId = reservationId;
        this.userId = userId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = status;
    }

    // Getters
    public String getPaymentId() {
        return paymentId;
    }
    public String getReservationId() {
        return reservationId;
    }
    public String getUserId() {
        return userId;
    }
    public double getAmount() {
        return amount;
    }
    public String getPaymentMethod() {
        return paymentMethod;
    }
    public String getStatus() {
        return status;
    }

    // Setters
    public void setStatus(String status) {
        this.status = status;
    }
}