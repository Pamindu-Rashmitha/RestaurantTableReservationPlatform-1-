package util;

import model.Payment;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentManager {

    // Read all payments from the file
    public List<Payment> getAllPayments(String filePath) {
        List<Payment> payments = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    Payment payment = new Payment(
                            parts[0], // paymentId
                            parts[1], // reservationId
                            parts[2], // userId
                            Double.parseDouble(parts[3]), // amount
                            parts[4], // paymentMethod
                            parts[5]  // status
                    );
                    payments.add(payment);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return payments;
    }

    // Add a new payment
    public boolean addPayment(Payment payment, String filePath) {
        List<Payment> payments = getAllPayments(filePath);
        // Check for duplicate paymentId
        for (Payment p : payments) {
            if (p.getPaymentId().equals(payment.getPaymentId())) {
                return false; // Payment ID already exists
            }
        }
        payments.add(payment);
        savePayments(payments, filePath);
        return true;
    }

    // Update payment status
    public boolean updatePaymentStatus(String paymentId, String newStatus, String filePath) {
        List<Payment> payments = getAllPayments(filePath);
        for (Payment payment : payments) {
            if (payment.getPaymentId().equals(paymentId)) {
                payment.setStatus(newStatus);
                savePayments(payments, filePath);
                return true;
            }
        }
        return false;
    }

    // Save payments to file
    private void savePayments(List<Payment> payments, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Payment payment : payments) {
                writer.write(payment.getPaymentId() + "," +
                        payment.getReservationId() + "," +
                        payment.getUserId() + "," +
                        payment.getAmount() + "," +
                        payment.getPaymentMethod() + "," +
                        payment.getStatus());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}