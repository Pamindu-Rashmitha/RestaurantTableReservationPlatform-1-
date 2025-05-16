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

@WebServlet("/updateReservation")
public class UpdateReservationServlet extends HttpServlet {
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
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String reservationId = request.getParameter("reservationId");
        String date = request.getParameter("date");
        String time = request.getParameter("time");
        int numberOfGuests = Integer.parseInt(request.getParameter("numberOfGuests"));

        String filePath = getServletContext().getRealPath("/data/reservations.txt");
        Reservation reservation = reservationManager.getReservationById(reservationId, filePath);
        if (reservation == null || !reservation.getUserId().equals(user.getUsername())) {
            response.sendRedirect("customerDashboard.jsp");
            return;
        }

        // Update reservation details
        reservation.setDate(date);
        reservation.setTime(time);
        reservation.setNumberOfGuests(numberOfGuests);

        // Save the updated reservation
        if (reservationManager.updateReservation(reservation, filePath)) {
            // Fetch updated reservations for the user
            List<Reservation> userReservations =
                    reservationManager.getReservationsByUser(user.getUsername(), filePath);
            request.setAttribute("reservations", userReservations);
            request.getRequestDispatcher("customerDashboard.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Failed to update reservation");
            request.setAttribute("reservation", reservation);
            request.getRequestDispatcher("editReservation.jsp").forward(request, response);
        }
    }
}
