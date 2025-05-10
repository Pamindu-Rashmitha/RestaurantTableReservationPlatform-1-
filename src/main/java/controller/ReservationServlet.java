package controller;

import model.Reservation;
import util.ReservationManager;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

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
        // Check if the user is logged in
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
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

        // Define the file path for storing reservations
        String filePath = getServletContext().getRealPath("/data/reservations.txt");

        // Attempt to add the reservation
        if (reservationManager.addReservation(reservation, filePath)) {
            // Fetch the user's reservations
            List<Reservation> userReservations = reservationManager.getReservationsByUser(user.getUsername(), filePath);
            // Set the reservations as a request attribute
            request.setAttribute("reservations", userReservations);
            // Forward to the customer dashboard
            request.getRequestDispatcher("customerDashboard.jsp").forward(request, response);
        } else {
            // Set an error attribute and forward back to the reservation page
            request.setAttribute("error", "Failed to make reservation");
            request.getRequestDispatcher("makeReservation.jsp").forward(request, response);
        }


    }
}