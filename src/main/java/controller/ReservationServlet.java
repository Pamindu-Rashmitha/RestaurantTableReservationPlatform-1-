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
import java.sql.Timestamp;

@WebServlet("/reservation")
public class ReservationServlet extends HttpServlet {
    private ReservationManager reservationManager;

    @Override
    public void init() throws ServletException {
        reservationManager = new ReservationManager();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Ensure the user is logged in
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Generate unique reservation ID
        String reservationId = "RES" + new Timestamp(System.currentTimeMillis()).getTime();

        // Get form data
        String date = request.getParameter("date");
        String time = request.getParameter("time");
        int numberOfGuests = Integer.parseInt(request.getParameter("numberOfGuests"));

        // Create reservation (initial status will be set inside manager)
        Reservation newReservation = new Reservation(
                reservationId,
                user.getUsername(),
                date,
                time,
                numberOfGuests,
                "Pending" // Default, will be overridden inside addReservation()
        );

        String filePath = getServletContext().getRealPath("/data/reservations.txt");

        // Add the reservation (status set and stored inside the manager)
        reservationManager.addReservation(newReservation, filePath);

        // Status message
        String statusMessage = "CONFIRMED".equals(newReservation.getStatus())
                ? "Table reserved successfully!"
                : "Added to waiting list. Position: " + reservationManager.getWaitingListPosition(newReservation);

        // Store message in session for display
        session.setAttribute("statusMessage", statusMessage);

        // Redirect to dashboard
        response.sendRedirect("customerDashboard.jsp");
    }
}
