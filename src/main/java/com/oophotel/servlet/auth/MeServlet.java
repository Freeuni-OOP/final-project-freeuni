package com.oophotel.servlet.auth;

import com.oophotel.dao.UserDao;
import com.oophotel.model.User;
import com.oophotel.util.JsonUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * GET /api/auth/me — returns the currently logged-in user's profile.
 * Requires an active session (AuthFilter blocks unauthenticated requests).
 */
@WebServlet("/api/auth/me")
public class MeServlet extends HttpServlet {

    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            JsonUtil.write(res, 401, "Unauthorized"); return;
        }

        long userId = (long) session.getAttribute("userId");

        try {
            Optional<User> opt = userDao.findById(userId);
            if (opt.isEmpty()) {
                session.invalidate();
                JsonUtil.write(res, 401, "Session invalid: user no longer exists"); return;
            }

            User u = opt.get();
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("id",          u.getId());
            data.put("firstName",   u.getFirstName());
            data.put("lastName",    u.getLastName());
            data.put("email",       u.getEmail());
            data.put("dateOfBirth", u.getDateOfBirth());
            data.put("role",        u.getRole());
            data.put("createdAt",   u.getCreatedAt());
            JsonUtil.write(res, 200, data);

        } catch (SQLException e) {
            JsonUtil.write(res, 500, "Database error: " + e.getMessage());
        }
    }
}
