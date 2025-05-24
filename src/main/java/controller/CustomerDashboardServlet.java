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

        /* ---------- 1. Auth check ---------- */
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || !"customer".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect("login.jsp");
            return;
        }

        /* ---------- 2. Load user reservations ---------- */
        String filePath = getServletContext().getRealPath("/data/reservations.txt");
        List<Reservation> userReservations =
                reservationManager.getReservationsByUser(user.getUsername(), filePath);

        request.setAttribute("reservations", userReservations);

        /* ---------- 3. Flash status message (if any) ---------- */
        String statusMessage = (String) session.getAttribute("statusMessage");
        if (statusMessage != null) {
            request.setAttribute("statusMessage", statusMessage);
            session.removeAttribute("statusMessage");            // show once
        }

        /* ---------- 4. Provide queue position if waiting ---------- */
        userReservations.stream()
                .filter(r -> "WAITING".equalsIgnoreCase(r.getStatus()))
                .findFirst()
                .ifPresent(waitingRes -> {
                    int pos = reservationManager.getWaitingListPosition(waitingRes);
                    request.setAttribute("queuePosition", pos);
                });

        /* ---------- 5. Forward to JSP ---------- */
        request.getRequestDispatcher("customerDashboard.jsp").forward(request, response);
    }
}

