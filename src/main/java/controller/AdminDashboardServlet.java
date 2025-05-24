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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/adminDashboard")
public class AdminDashboardServlet extends HttpServlet {

    private ReservationManager reservationManager;
    private UserManager        userManager;

    @Override
    public void init() throws ServletException {
        reservationManager = new ReservationManager();
        userManager        = new UserManager();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        /* ---------- 1. Auth check ---------- */
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect("login.jsp");
            return;
        }

        /* ---------- 2. Load reservation data ---------- */
        String filePath = getServletContext().getRealPath("/data/reservations.txt");

        List<Reservation> confirmed =
                reservationManager.getConfirmedReservations(filePath);
        List<Reservation> waiting =
                reservationManager.getWaitingReservations(filePath);

        /* ---------- 3. Optional search filter ---------- */
        String term = request.getParameter("searchTerm");
        if (term != null && !term.trim().isEmpty()) {
            String q = term.toLowerCase();
            confirmed = filter(confirmed, q);
            waiting   = filter(waiting,   q);
        }

        /* ---------- 4. Push attributes for new JSP version ---------- */
        request.setAttribute("confirmedReservations", confirmed);
        request.setAttribute("waitingReservations",   waiting);

        /* ---------- 4-bis. Legacy combined list for old JSP ---------- */
        List<Reservation> all = new ArrayList<>(confirmed);
        all.addAll(waiting);
        request.setAttribute("allReservations", all);   // ← added line

        /* ---------- 5. Users list ---------- */
        String userFile = getServletContext().getRealPath("/data/users.txt");
        request.setAttribute("allUsers",
                userManager.getAllUsers(userFile));

        /* ---------- 6. Forward to JSP ---------- */
        request.getRequestDispatcher("adminDashboard.jsp")
                .forward(request, response);
    }

    /** Simple substring search across key fields */
    private List<Reservation> filter(List<Reservation> src, String q) {
        List<Reservation> out = new ArrayList<>();
        for (Reservation r : src) {
            if (r.getReservationId().toLowerCase().contains(q) ||
                    r.getUserId()        .toLowerCase().contains(q) ||
                    r.getDate()          .toLowerCase().contains(q) ||
                    r.getTime()          .toLowerCase().contains(q)) {
                out.add(r);
            }
        }
        return out;
    }
}

