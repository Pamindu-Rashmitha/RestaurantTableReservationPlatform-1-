package controller;

import jakarta.servlet.http.HttpServletRequest;
import model.Reservation;
import model.User;
import util.ReservationManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/customerDashboard")
public class CustomerDashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || !"customer".equals(user.getRole())) {
            response.sendRedirect("login.jsp");
            return;
        }

        ReservationManager reservationManager = (ReservationManager) session.getAttribute("reservationManager");
        if (reservationManager == null) {
            reservationManager = new ReservationManager();
            session.setAttribute("reservationManager", reservationManager);
        }

        String filePath = getServletContext().getRealPath("/data/reservations.txt");

        List<Reservation> confirmedReservations = reservationManager.getReservationsByUser(user.getUsername(), filePath);
        Reservation[] allPending = reservationManager.getReservationQueue().toArray();
        List<Reservation> userPending = new ArrayList<>();
        for (Reservation res : allPending) {
            if (res.getUserId().equals(user.getUsername())) {
                userPending.add(res);
            }
        }

        List<Reservation> allUserReservations = new ArrayList<>();
        allUserReservations.addAll(confirmedReservations);
        allUserReservations.addAll(userPending);

        request.setAttribute("reservations", allUserReservations);
        request.getRequestDispatcher("customerDashboard.jsp").forward(request, response);
    }
}