package controller;

import util.ReservationManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/cancelReservation")
public class CancelReservationServlet extends HttpServlet {

    private ReservationManager reservationManager;

    @Override
    public void init() throws ServletException {
        reservationManager = new ReservationManager();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String reservationId = request.getParameter("reservationId");
        String filePath      = getServletContext().getRealPath("/data/reservations.txt");


        boolean success = reservationManager.cancelReservationAndPromote(reservationId, filePath);


        HttpSession session = request.getSession();
        if (success) {
            session.setAttribute("statusMessage", "Reservation cancelled successfully.");
        } else {
            session.setAttribute("statusMessage", "Cancellation failed: reservation not found or already cancelled.");
        }


        response.sendRedirect("customerDashboard");
    }
}

