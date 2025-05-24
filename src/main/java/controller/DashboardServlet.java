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

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private ReservationManager reservationManager;

    @Override
    public void init() throws ServletException {
        reservationManager = new ReservationManager();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {

        /* ---------- 1. Auth check ---------- */
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        /* ---------- 2. Load reservations ---------- */
        String filePath = getServletContext().getRealPath("/data/reservations.txt");
        List<Reservation> userReservations =
                reservationManager.getReservationsByUser(user.getUsername(), filePath);

        request.setAttribute("reservations", userReservations);

        /* ---------- 3. Flash status or success messages ---------- */
        String statusMsg = (String) session.getAttribute("statusMessage");
        if (statusMsg != null) {
            request.setAttribute("statusMessage", statusMsg);
            session.removeAttribute("statusMessage");
        }
        // legacy “success” param still supported
        if ("true".equals(request.getParameter("success"))) {
            request.setAttribute("successMessage", "Reservation made successfully!");
        }

        /* ---------- 4. Queue position if waiting ---------- */
        userReservations.stream()
                .filter(r -> "WAITING".equalsIgnoreCase(r.getStatus()))
                .findFirst()
                .ifPresent(waiting -> {
                    int pos = reservationManager.getWaitingListPosition(waiting);
                    request.setAttribute("queuePosition", pos);
                });

        /* ---------- 5. Forward to same JSP as CustomerDashboardServlet ---------- */
        request.getRequestDispatcher("customerDashboard.jsp").forward(request, response);
    }
}

