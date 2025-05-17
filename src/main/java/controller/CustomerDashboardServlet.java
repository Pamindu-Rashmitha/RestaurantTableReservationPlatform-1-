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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/customerDashboard")
public class CustomerDashboardServlet extends HttpServlet {
    private ReservationManager reservationManager;

    @Override
    public void init() throws ServletException {
        reservationManager = new ReservationManager();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || !"customer".equals(user.getRole())) {
            response.sendRedirect("login.jsp");
            return;
        }

        String filePath = getServletContext().getRealPath("/WEB-INF/reservations.txt");

        // Get confirmed reservations from the file
        List<Reservation> confirmedReservations = reservationManager.getReservationsByUser(user.getUsername(), filePath);

        // Get all pending reservations from the queue and filter by current user
        Reservation[] allPending = reservationManager.getReservationQueue().toArray();
        List<Reservation> userPending = new ArrayList<>();
        for (Reservation res : allPending) {
            if (res.getUserId().equals(user.getUsername())) {
                userPending.add(res);
            }
        }

        // Combine confirmed and pending reservations
        List<Reservation> allUserReservations = new ArrayList<>();
        allUserReservations.addAll(confirmedReservations);
        allUserReservations.addAll(userPending);

        // Set the combined list in the request
        request.setAttribute("reservations", allUserReservations);
        request.getRequestDispatcher("customerDashboard.jsp").forward(request, response);
    }
}