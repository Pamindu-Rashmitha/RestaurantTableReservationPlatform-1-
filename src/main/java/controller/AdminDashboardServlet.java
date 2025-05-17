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
    private ReservationManager reservationManager;

    @Override
    public void init() throws ServletException {
        userManager = new UserManager();
        reservationManager = new ReservationManager();
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

        // Load reservations from reservations.txt
        String filePath = getServletContext().getRealPath("/data/reservations.txt");
        List<Reservation> allReservations = reservationManager.getAllReservations(filePath);

        // Retrieve search parameter and filter reservations if needed
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

        // Load pending reservations from queue
        Reservation[] pendingReservations = reservationManager.getReservationQueue().toArray();
        request.setAttribute("pendingReservations", pendingReservations);

        // Load users from users.txt
        String userFilepath = getServletContext().getRealPath("/data/users.txt");
        List<User> allUsers = userManager.getAllUsers(userFilepath);
        request.setAttribute("allUsers", allUsers);

        // Forward to admin dashboard
        request.getRequestDispatcher("adminDashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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