package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Reservation;
import model.User;
import util.ReservationManager;
import util.UserManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/adminDashboard")
public class AdminDashboardServlet extends HttpServlet {
    private UserManager userManager;

    @Override
    public void init() throws ServletException {
        userManager = new UserManager();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            response.sendRedirect("login.jsp");
            return;
        }

        ReservationManager reservationManager = (ReservationManager) session.getAttribute("reservationManager");
        if (reservationManager == null) {
            reservationManager = new ReservationManager();
            session.setAttribute("reservationManager", reservationManager);
        }

        String filePath = getServletContext().getRealPath("/WEB-INF/reservations.txt");
        List<Reservation> allReservations = reservationManager.getAllReservations(filePath);

        String searchTerm = request.getParameter("searchTerm");
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            String lowerCaseSearch = searchTerm.toLowerCase();
            List<Reservation> filteredReservations = new ArrayList<>();
            for (Reservation res : allReservations) {
                if (res.getReservationId().toLowerCase().contains(lowerCaseSearch) ||
                        res.getUserId().toLowerCase().contains(lowerCaseSearch) ||
                        res.getDate().toLowerCase().contains(lowerCaseSearch) ||
                        res.getTime().toLowerCase().contains(lowerCaseSearch)) {
                    filteredReservations.add(res);
                }
            }
            allReservations = filteredReservations;
        }
        request.setAttribute("allReservations", allReservations);

        Reservation[] pendingReservations = reservationManager.getReservationQueue().toArray();
        request.setAttribute("pendingReservations", pendingReservations);

        String userFilepath = getServletContext().getRealPath("/WEB-INF/users.txt");
        List<User> allUsers = userManager.getAllUsers(userFilepath);
        request.setAttribute("allUsers", allUsers);

        request.getRequestDispatcher("adminDashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        ReservationManager reservationManager = (ReservationManager) session.getAttribute("reservationManager");
        if (reservationManager == null) {
            reservationManager = new ReservationManager();
            session.setAttribute("reservationManager", reservationManager);
        }

        String action = request.getParameter("action");
        if ("processQueue".equals(action)) {
            boolean processed = reservationManager.processNextReservation();
            if (processed) {
                request.setAttribute("message", "Reservation processed successfully.");
            } else {
                request.setAttribute("message", "No reservations to process.");
            }
        }
        doGet(request, response); // Refresh the page
    }
}