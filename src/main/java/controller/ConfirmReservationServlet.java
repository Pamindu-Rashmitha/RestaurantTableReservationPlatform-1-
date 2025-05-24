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

@WebServlet("/confirmReservation")
public class ConfirmReservationServlet extends HttpServlet {

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

        Reservation res = reservationManager.getReservationById(reservationId, filePath);
        if (res == null) {
            session.setAttribute("statusMessage", "Reservation ID not found.");
            response.sendRedirect("adminDashboard");
            return;
        }


        reservationManager.cancelReservationAndPromote(reservationId, filePath);


        Reservation newCopy = new Reservation(
                res.getReservationId(),
                res.getUserId(),
                res.getDate(),
                res.getTime(),
                res.getNumberOfGuests(),
                "PENDING"
        );
        reservationManager.addReservation(newCopy, filePath);


        String msg = "Reservation " + reservationId + " ";
        if ("CONFIRMED".equalsIgnoreCase(newCopy.getStatus())) {
            msg += "is now CONFIRMED.";
        } else {
            int pos = reservationManager.getWaitingListPosition(newCopy);
            msg += "could not be confirmed (capacity full). It remains WAITING at position #" + pos + ".";
        }
        session.setAttribute("statusMessage", msg);


        response.sendRedirect("adminDashboard");
    }
}

