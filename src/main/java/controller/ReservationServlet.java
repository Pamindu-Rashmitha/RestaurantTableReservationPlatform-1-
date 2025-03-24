package controller;

import model.Reservation;
import model.ReservationManager;
import model.User;

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String date = request.getParameter("date");
        String time = request.getParameter("time");
        int numberOfGuests = Integer.parseInt(request.getParameter("numberOfGuests"));
        String reservationId = "RES" + new Timestamp(System.currentTimeMillis()).getTime();

        Reservation reservation = new Reservation(reservationId, user.getUsername(), date, time, numberOfGuests, "Pending");
        String filePath = getServletContext().getRealPath("/WEB-INF/reservations.txt");

        if (reservationManager.addReservation(reservation, filePath)) {
            response.sendRedirect("customerDashboard.jsp");
        } else {
            request.setAttribute("error", "Failed to make reservation");
            request.getRequestDispatcher("makeReservation.jsp").forward(request, response);
        }
    }
}