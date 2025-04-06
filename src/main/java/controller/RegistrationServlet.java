package controller;

import model.User;
import util.UserManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
    private UserManager userManager;

    @Override
    public void init() throws ServletException {
        userManager = new UserManager();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Trim input parameters to remove leading/trailing spaces
        String username = request.getParameter("username").trim();
        String password = request.getParameter("password").trim();
        String name = request.getParameter("name").trim();
        String email = request.getParameter("email").trim();

        String filePath = getServletContext().getRealPath("/WEB-INF/users.txt");

        // Validate password strength
        String passwordError = validatePassword(password);
        if (passwordError != null) {
            request.setAttribute("error", passwordError);
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // Check if email already exists
        if (userManager.emailExists(email, filePath)) {
            request.setAttribute("error", "Email already in use");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // Create new user and attempt to add it
        User newUser = new User(username, password, "customer", name, email);
        if (userManager.addUser(newUser, filePath)) {
            response.sendRedirect("login.jsp");
        } else {
            request.setAttribute("error", "Username already exists");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }

    // Password validation method
    private String validatePassword(String password) {
        if (password.length() < 6) {
            return "Password must be at least 6 characters long";
        }
        if (!Pattern.compile("[A-Z]").matcher(password).find()) {
            return "Password must contain at least one uppercase letter";
        }
        if (!Pattern.compile("[a-z]").matcher(password).find()) {
            return "Password must contain at least one lowercase letter";
        }
        if (!Pattern.compile("[0-9]").matcher(password).find()) {
            return "Password must contain at least one number";
        }
        if (!Pattern.compile("[!@#$%^&*]").matcher(password).find()) {
            return "Password must contain at least one special character (!@#$%^&*)";
        }
        return null; // Password is valid
    }
}