package controller;

import model.Reservation;
import util.ReservationManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/editReservation")
public class EditReservationServlet extends HttpServlet {
    private ReservationManager reservationManager;

    @Override
    public void init() throws ServletException {
        reservationManager = new ReservationManager();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String reservationId = request.getParameter("reservationId");
        if (reservationId == null) {
            response.sendRedirect("customerDashboard.jsp");
            return;
        }

        String filePath = getServletContext().getRealPath("/data/reservations.txt");
        Reservation reservation = reservationManager.getReservationById(reservationId, filePath);
        if (reservation == null) {
            response.sendRedirect("customerDashboard.jsp");
            return;
        }

        request.setAttribute("reservation", reservation);
        request.getRequestDispatcher("editReservation.jsp").forward(request, response);
    }
}