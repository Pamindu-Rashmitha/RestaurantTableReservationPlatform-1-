package controller;

import model.User;
import model.UserManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
    private UserManager userManager;

    @Override
    public void init() throws ServletException {
        userManager = new UserManager();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String email = request.getParameter("email");

        User newUser = new User(username, password, "customer", name, email);
        String filePath = getServletContext().getRealPath("/WEB-INF/users.txt");

        if (userManager.addUser(newUser, filePath)) {
            response.sendRedirect("login.jsp");
        } else {
            request.setAttribute("error", "Username already exists");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}
