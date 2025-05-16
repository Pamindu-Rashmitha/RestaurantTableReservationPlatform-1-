package controller;

import model.Reservation;
import model.User;
import util.ReservationManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/cancelReservationAdmin")
public class CancelReservationAdminServlet extends HttpServlet {
    private ReservationManager reservationManager;

    @Override
    public void init() throws ServletException {
        reservationManager = new ReservationManager();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            response.sendRedirect("login.jsp");
            return;
        }

        String reservationId = request.getParameter("reservationId");
        String filePath = getServletContext().getRealPath("/data/reservations.txt");
        Reservation reservation = reservationManager.getReservationById(reservationId, filePath);
        if (reservation == null || "Cancelled".equals(reservation.getStatus())) {
            response.sendRedirect("adminDashboard");
            return;
        }

        reservation.setStatus("Cancelled");
        if (reservationManager.updateReservation(reservation, filePath)) {
            List<Reservation> allReservations = reservationManager.getAllReservations(filePath);
            request.setAttribute("allReservations", allReservations);
            request.getRequestDispatcher("adminDashboard.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Cancellation failed");
            request.getRequestDispatcher("adminDashboard.jsp").forward(request, response);
        }
    }
}