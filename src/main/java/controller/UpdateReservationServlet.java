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
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/updateReservation")
public class UpdateReservationServlet extends HttpServlet {
    private ReservationManager reservationManager;
    private static final Logger logger = Logger.getLogger(UpdateReservationServlet.class.getName());

    @Override
    public void init() throws ServletException {
        reservationManager = new ReservationManager();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
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

            String filePath = getServletContext().getRealPath("/WEB-INF/reservations.txt");
            Reservation reservation = reservationManager.getReservationById(reservationId, filePath);

            if (reservation == null || !reservation.getUserId().equals(user.getUsername())) {
                response.sendRedirect("customerDashboard.jsp");
                return;
            }

            // Update reservation details
            reservation.setDate(date);
            reservation.setTime(time);
            reservation.setNumberOfGuests(numberOfGuests);

            reservationManager.updateReservation(reservation, filePath);
            response.sendRedirect("customerDashboard.jsp");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error updating reservation", e);
            setErrorAndRedirect(request, response, "An error occurred while updating the reservation");
        }
    }

    private void setErrorAndRedirect(HttpServletRequest request, HttpServletResponse response, String errorMessage)
            throws ServletException, IOException {
        request.setAttribute("errorMessage", errorMessage);
        request.getRequestDispatcher("error.jsp").forward(request, response);
    }
}
