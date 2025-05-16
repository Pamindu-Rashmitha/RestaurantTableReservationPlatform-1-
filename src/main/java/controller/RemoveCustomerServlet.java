package controller;

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

@WebServlet("/removeCustomer")
public class RemoveCustomerServlet extends HttpServlet {
    private UserManager userManager;
    private ReservationManager reservationManager;

    @Override
    public void init() throws ServletException {
        userManager = new UserManager();
        reservationManager = new ReservationManager();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            response.sendRedirect("login.jsp");
            return;
        }

        String username = request.getParameter("username");
        String userFilePath = getServletContext().getRealPath("/data/users.txt");
        String reservationFilePath = getServletContext().getRealPath("/data/reservations.txt");

        // Remove the user
        boolean userRemoved = userManager.removeUser(username, userFilePath);
        // Remove the user's reservations
        reservationManager.removeReservationsByUser(username, reservationFilePath);

        if (userRemoved) {
            // Refresh the dashboard data
            List<User> allUsers = userManager.getAllUsers(userFilePath);
            List<model.Reservation> allReservations = reservationManager.getAllReservations(reservationFilePath);
            request.setAttribute("allUsers", allUsers);
            request.setAttribute("allReservations", allReservations);
            request.getRequestDispatcher("adminDashboard.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Failed to remove customer");
            request.getRequestDispatcher("adminDashboard.jsp").forward(request, response);
        }
    }
}