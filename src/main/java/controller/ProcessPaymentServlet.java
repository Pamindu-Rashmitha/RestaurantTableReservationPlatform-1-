package controller;

import model.User;
import model.Reservation;
import model.Payment;
import util.ReservationManager;
import util.PaymentManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;

@WebServlet("/processPayment")
public class ProcessPaymentServlet extends HttpServlet {
    private ReservationManager reservationManager;
    private PaymentManager paymentManager;

    @Override
    public void init() throws ServletException {
        reservationManager = new ReservationManager();
        paymentManager = new PaymentManager();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String reservationId = request.getParameter("reservationId");
        String paymentMethod = request.getParameter("paymentMethod");
        int numberOfGuests = Integer.parseInt(request.getParameter("numberOfGuests"));
        double amount = numberOfGuests * 10.00; // Recalculate to ensure consistency

        // Validate card details if Credit or Debit Card is selected
        String cardNumber = request.getParameter("cardNumber");
        String expiryDate = request.getParameter("expiryDate");
        String cvv = request.getParameter("cvv");
        if ("Credit Card".equals(paymentMethod) || "Debit Card".equals(paymentMethod)) {
            if (cardNumber == null || cardNumber.isEmpty() || expiryDate == null || expiryDate.isEmpty() || cvv == null || cvv.isEmpty()) {
                request.setAttribute("error", "Please enter all card details.");
                request.getRequestDispatcher("payment.jsp").forward(request, response);
                return;
            }
            boolean isCardValid = validateCardDetails(cardNumber, expiryDate, cvv);
            if (!isCardValid) {
                request.setAttribute("error", "Invalid card details. Please check and try again.");
                request.getRequestDispatcher("payment.jsp").forward(request, response);
                return;
            }
        }

        String paymentFilePath = getServletContext().getRealPath("/data/payments.txt");
        String reservationFilePath = getServletContext().getRealPath("/data/reservations.txt");

        // Update the reservation with the new number of guests
        Reservation reservation = reservationManager.getReservationById(reservationId, reservationFilePath);
        if (reservation != null) {
            reservation.setNumberOfGuests(numberOfGuests);
            reservationManager.updateReservation(reservation, reservationFilePath);
        } else {
            request.setAttribute("error", "Reservation not found.");
            request.getRequestDispatcher("payment.jsp").forward(request, response);
            return;
        }

        // Process the payment
        String paymentId = UUID.randomUUID().toString();
        Payment payment = new Payment(paymentId, reservationId, user.getUsername(), amount, paymentMethod, "Pending");
        boolean paymentAdded = paymentManager.addPayment(payment, paymentFilePath);

        if (paymentAdded) {
            boolean paymentSuccessful = true; // Simulate payment processing
            if (paymentSuccessful) {
                paymentManager.updatePaymentStatus(paymentId, "Completed", paymentFilePath);
                reservation.setStatus("Paid");
                reservationManager.updateReservation(reservation, reservationFilePath);
                response.sendRedirect("customerDashboard.jsp?message=Payment successful");
            } else {
                paymentManager.updatePaymentStatus(paymentId, "Failed", paymentFilePath);
                request.setAttribute("error", "Payment failed. Please try again.");
                request.getRequestDispatcher("payment.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("error", "Failed to process payment. Please try again.");
            request.getRequestDispatcher("payment.jsp").forward(request, response);
        }
    }

    private boolean validateCardDetails(String cardNumber, String expiryDate, String cvv) {
        if (cardNumber.length() != 16 || !expiryDate.matches("\\d{2}/\\d{2}") || cvv.length() != 3) {
            return false;
        }
        return true;
    }
}
