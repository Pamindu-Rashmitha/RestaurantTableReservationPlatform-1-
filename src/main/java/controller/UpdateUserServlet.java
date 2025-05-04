package controller;

import model.User;
import util.UserManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/updateUser")
public class UpdateUserServlet extends HttpServlet {
    private UserManager userManager;

    @Override
    public void init() throws ServletException {
        userManager = new UserManager();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String username = request.getParameter("username");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Validate inputs
        if (name == null || name.isEmpty()) {
            request.setAttribute("error", "Name cannot be empty.");
            request.getRequestDispatcher("editProfile.jsp").forward(request, response);
            return;
        }
        if (email == null || email.isEmpty()) {
            request.setAttribute("error", "Email cannot be empty.");
            request.getRequestDispatcher("editProfile.jsp").forward(request, response);
            return;
        }

        // Check if email is already used by another user
        String userFilePath = getServletContext().getRealPath("/WEB-INF/users.txt");
        if (!email.equals(user.getEmail()) && userManager.emailExists(email, userFilePath)) {
            request.setAttribute("error", "Email is already in use by another user.");
            request.getRequestDispatcher("editProfile.jsp").forward(request, response);
            return;
        }

        // Create updated user object
        User updatedUser = new User(username, user.getPassword(), user.getRole(), name, email);
        if (password != null && !password.isEmpty()) {
            updatedUser.setPassword(password); // New password will be hashed in updateUser
        }

        boolean updated = userManager.updateUser(updatedUser, userFilePath);

        if (updated) {
            // Update session with new user details
            session.setAttribute("user", updatedUser);
            request.setAttribute("message", "Profile updated successfully.");
        } else {
            request.setAttribute("error", "Failed to update profile. Please try again.");
        }
        request.getRequestDispatcher("editProfile.jsp").forward(request, response);
    }
}