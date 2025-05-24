package controller;

import model.Reservation;
import util.ReservationManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/editReservation")
public class EditReservationServlet extends HttpServlet {

    private ReservationManager reservationManager;

    @Override
    public void init() throws ServletException {
        reservationManager = new ReservationManager();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String reservationId = request.getParameter("reservationId");
        if (reservationId == null) {
            response.sendRedirect("customerDashboard");
            return;
        }

        String filePath   = getServletContext().getRealPath("/data/reservations.txt");
        Reservation res   = reservationManager.getReservationById(reservationId, filePath);

        if (res == null) {
            response.sendRedirect("customerDashboard");
            return;
        }

        request.setAttribute("reservation", res);
        request.getRequestDispatcher("editReservation.jsp").forward(request, response);
    }
}

