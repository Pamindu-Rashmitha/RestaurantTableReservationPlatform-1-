package controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Reservation;
import util.ReservationManager;

import java.io.IOException;
import java.util.List;
import java.util.Arrays;

@WebServlet("/cancelReservation")
public class CancelReservationServlet extends HttpServlet {
    private ReservationManager reservationManager;

    @Override
    public void init() {
        reservationManager = new ReservationManager();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String reservationId = request.getParameter("reservationId");
        String filePath = getServletContext().getRealPath("/data/reservations.txt");

        // Load all reservations from the file
        List<Reservation> reservationsList = reservationManager.getAllReservations(filePath);
        Reservation[] reservations = new Reservation[reservationsList.size()];
        for (int i = 0; i < reservationsList.size(); i++) {
            reservations[i] = reservationsList.get(i);
        }

        // Update the status of the matching reservation
        boolean found = false;
        for (Reservation reservation : reservations) {
            if (reservation.getReservationId().equals(reservationId)) {
                reservation.setStatus("CANCELLED");
                found = true;
                break;
            }
        }

        // Save the updated reservations back to the file if found
        if (found) {
            reservationManager.saveReservations(Arrays.asList(reservations), filePath); //
        }

        // Redirect back to the customer dashboard servlet
        response.sendRedirect("customerDashboard");
    }
}
