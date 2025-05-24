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

        /* ---------- 1. Authentication check ---------- */
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        /* ---------- 2. Build Reservation object ---------- */
        String reservationId = "RES" + System.currentTimeMillis();          // unique ID
        String date  = request.getParameter("date");
        String time  = request.getParameter("time");
        int guests   = Integer.parseInt(request.getParameter("numberOfGuests"));

        Reservation newRes = new Reservation(
                reservationId,
                user.getUsername(),
                date,
                time,
                guests,
                "PENDING"         // will be overwritten by manager
        );

        String filePath = getServletContext().getRealPath("/data/reservations.txt");

        /* ---------- 3. Add via ReservationManager ---------- */
        reservationManager.addReservation(newRes, filePath);

        /* ---------- 4. Build feedback message ---------- */
        String statusMessage;
        if ("CONFIRMED".equalsIgnoreCase(newRes.getStatus())) {
            statusMessage = "Table reserved successfully!";
        } else if ("WAITING".equalsIgnoreCase(newRes.getStatus())) {
            int pos = reservationManager.getWaitingListPosition(newRes);
            statusMessage = "All tables are booked. You were added to the waiting list (position #" + pos + ").";
        } else {
            statusMessage = "Reservation saved. Current status: " + newRes.getStatus();
        }

        /* ---------- 5. Flash message → session ---------- */
        session.setAttribute("statusMessage", statusMessage);

        /* ---------- 6. Redirect to dashboard servlet (not JSP) ---------- */
        response.sendRedirect("customerDashboard");   // triggers CustomerDashboardServlet
    }
}

