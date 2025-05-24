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

@WebServlet("/updateReservation")
public class UpdateReservationServlet extends HttpServlet {

    private ReservationManager reservationManager;

    @Override
    public void init() throws ServletException {
        reservationManager = new ReservationManager();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }


        String reservationId = request.getParameter("reservationId");
        String date          = request.getParameter("date");
        String time          = request.getParameter("time");
        int guests           = Integer.parseInt(request.getParameter("numberOfGuests"));

        String filePath = getServletContext().getRealPath("/data/reservations.txt");
        Reservation oldRes = reservationManager.getReservationById(reservationId, filePath);


        if (oldRes == null || !oldRes.getUserId().equals(user.getUsername())) {
            response.sendRedirect("customerDashboard");
            return;
        }


        Reservation updated = new Reservation(
                oldRes.getReservationId(),
                oldRes.getUserId(),
                date,
                time,
                guests,
                "PENDING"    // will be reassigned by manager
        );

        /* ---------- 5. Replace via manager ---------- */
        reservationManager.updateReservation(updated, filePath);


        String msg;
        if ("CONFIRMED".equalsIgnoreCase(updated.getStatus())) {
            msg = "Reservation updated and confirmed!";
        } else { // WAITING
            int pos = reservationManager.getWaitingListPosition(updated);
            msg = "Reservation updated, but all tables are booked. You are now #"
                    + pos + " in the waiting list.";
        }
        session.setAttribute("statusMessage", msg);


        response.sendRedirect("customerDashboard");
    }
}

