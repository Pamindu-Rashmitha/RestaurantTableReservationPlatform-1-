package controller;

import model.Reservation;
import model.User;
import util.ReservationManager;
import util.UserManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

@WebServlet("/adminDashboard")
public class AdminDashboardServlet extends HttpServlet {
    private ReservationManager reservationManager;
    private UserManager userManager;

    @Override
    public void init() throws ServletException {
        reservationManager = new ReservationManager();
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

        // Load reservations from file
        String filePath = getServletContext().getRealPath("/WEB-INF/reservations.txt");
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

        // Load users from file
        String userFilePath = getServletContext().getRealPath("/WEB-INF/users.txt");
        List<User> allUsers = userManager.getAllUsers(userFilePath);
        request.setAttribute("allUsers", allUsers);

        // Forward to adminDashboard.jsp
        request.getRequestDispatcher("adminDashboard.jsp").forward(request, response);
    }
}
