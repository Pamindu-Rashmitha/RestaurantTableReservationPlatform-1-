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
import java.util.List;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private ReservationManager reservationManager;

    @Override
    public void init() throws ServletException {
        reservationManager = new ReservationManager();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String filePath = getServletContext().getRealPath("/WEB-INF/reservations.txt");
        List<Reservation> userReservations = reservationManager.getReservationsByUser(user.getUsername(), filePath);
        request.setAttribute("reservations", userReservations);

        // Check for success parameter (optional)
        String success = request.getParameter("success");
        if ("true".equals(success)) {
            request.setAttribute("successMessage", "Reservation made successfully!");
        }

        request.getRequestDispatcher("customerDashboard.jsp").forward(request, response);
    }
}