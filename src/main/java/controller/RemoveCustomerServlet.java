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

@WebServlet("/removeCustomer")
public class RemoveCustomerServlet extends HttpServlet {

    private UserManager         userManager;
    private ReservationManager  reservationManager;

    @Override
    public void init() throws ServletException {
        userManager        = new UserManager();
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


        String username           = request.getParameter("username");
        String userFilePath       = getServletContext().getRealPath("/data/users.txt");
        String reservationFilePath= getServletContext().getRealPath("/data/reservations.txt");


        boolean userRemoved = userManager.removeUser(username, userFilePath);
        reservationManager.removeReservationsByUser(username, reservationFilePath);


        String msg = userRemoved
                ? "Customer '" + username + "' and all their reservations removed."
                : "Failed to remove customer '" + username + "'.";
        session.setAttribute("statusMessage", msg);


        response.sendRedirect("adminDashboard");
    }
}

