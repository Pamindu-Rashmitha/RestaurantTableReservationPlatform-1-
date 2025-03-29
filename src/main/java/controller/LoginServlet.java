package controller;



import jakarta.servlet.ServletContext;
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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UserManager userManager;

    @Override
    public void init() throws ServletException {
        userManager = new UserManager();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String filePath = getServletContext().getRealPath("/WEB-INF/users.txt");

        User user = userManager.getUserByUsername(username, filePath);
        if (user != null && user.getPassword().equals(password)) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            if ("admin".equals(user.getRole())) {
                response.sendRedirect("adminDashboard.jsp");
            } else {
                response.sendRedirect("customerDashboard.jsp");
            }
        } else {
            request.setAttribute("error", "Invalid username or password");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}