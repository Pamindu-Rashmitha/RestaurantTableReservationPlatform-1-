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

        String filePath = getServletContext().getRealPath("/WEB-INF/reservations.txt");
        List<Reservation> allReservations = reservationManager.getAllReservations(filePath);
        request.setAttribute("allReservations", allReservations);

        String userFilePath = getServletContext().getRealPath("/WEB-INF/users.txt");
        List<User> allUsers = userManager.getAllUsers(userFilePath);
        request.setAttribute("allUsers", allUsers);

        request.getRequestDispatcher("adminDashboard.jsp").forward(request, response);
    }
}