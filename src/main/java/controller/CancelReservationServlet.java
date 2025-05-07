package controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Reservation;
import util.ReservationManager;

import java.io.IOException;
import java.util.List;

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
        String filePath = getServletContext().getRealPath("/WEB-INF/reservations.txt");

        // Fetch all reservations
        List<Reservation> reservations = reservationManager.getAllReservations(filePath);

        // Update the status of the matching reservation
        for (Reservation reservation : reservations) {
            if (reservation.getReservationId().equals(reservationId)) {
                reservation.setStatus("Cancelled");
                break;
            }
        }


        // Save the updated reservations back to the file
        reservationManager.saveReservations(reservations, filePath); // Assume this method exists

        // Redirect back to the dashboard
        response.sendRedirect("customerDashboard.jsp");
    }
}
