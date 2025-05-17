package controller;

import model.Reservation;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import util.ReservationManager;

import java.io.IOException;
import java.sql.Timestamp;

@WebServlet("/reservation")
public class ReservationServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Retrieve or create ReservationManager from session
        ReservationManager reservationManager = (ReservationManager) session.getAttribute("reservationManager");
        if (reservationManager == null) {
            reservationManager = new ReservationManager();
            session.setAttribute("reservationManager", reservationManager);
        }

        // Generate a unique reservation ID server-side
        String reservationId = "RES" + new Timestamp(System.currentTimeMillis()).getTime();

        // Retrieve reservation details from the request
        String date = request.getParameter("date");
        String time = request.getParameter("time");
        int numberOfGuests = Integer.parseInt(request.getParameter("numberOfGuests"));

        // Create the reservation with "Pending" status
        Reservation reservation = new Reservation(
                reservationId,
                user.getUsername(),
                date,
                time,
                numberOfGuests,
                "Pending"
        );

        // Attempt to add the reservation to the queue
        String filePath = getServletContext().getRealPath("/WEB-INF/reservations.txt");
        if (reservationManager.addReservation(reservation, filePath)) {
            // Redirect to customer dashboard to refresh with queue data
            response.sendRedirect("customerDashboard");
        } else {
            request.setAttribute("error", "Failed to make reservation");
            request.getRequestDispatcher("makeReservation.jsp").forward(request, response);
        }
    }
}