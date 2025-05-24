package controller;

import model.User;
import util.ReservationManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/cancelReservationAdmin")
public class CancelReservationAdminServlet extends HttpServlet {

    private ReservationManager reservationManager;

    @Override
    public void init() throws ServletException {
        reservationManager = new ReservationManager();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        HttpSession session = request.getSession();
        User admin = (User) session.getAttribute("user");
        if (admin == null || !"admin".equalsIgnoreCase(admin.getRole())) {
            response.sendRedirect("login.jsp");
            return;
        }


        String reservationId = request.getParameter("reservationId");
        String filePath      = getServletContext().getRealPath("/data/reservations.txt");

        boolean success = reservationManager.cancelReservationAndPromote(reservationId, filePath);


        if (success) {
            session.setAttribute("statusMessage", "Reservation " + reservationId + " cancelled; waiting list promoted if applicable.");
        } else {
            session.setAttribute("statusMessage", "Cancellation failed: reservation not found or already cancelled.");
        }


        response.sendRedirect("adminDashboard");
    }
}

