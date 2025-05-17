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

        String filePath = getServletContext().getRealPath("/data/reservations.txt");
        List<Reservation> userReservations = reservationManager.getReservationsByUser(user.getUsername(), filePath);
        request.setAttribute("reservations", userReservations);
        request.getRequestDispatcher("customerDashboard.jsp").forward(request, response);
    }
}