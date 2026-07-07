package com.oophotel.servlet.auth;

import com.oophotel.dao.UserDao;
import com.oophotel.model.User;
import com.oophotel.util.JsonUtil;
import com.oophotel.util.PasswordUtil;
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
 * POST /api/auth/login
 *
 * Body: { "email", "password" }
 * Success 200: session created, returns public profile.
 * Failure 401: wrong credentials (deliberately vague).
 */
@WebServlet("/api/auth/login")
public class LoginServlet extends HttpServlet {

    private final UserDao userDao = new UserDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

        Dto body;
        try { body = JsonUtil.readBody(req, Dto.class); }
        catch (Exception e) { JsonUtil.write(res, 400, "Malformed JSON"); return; }

        if (body == null || blank(body.email) || blank(body.password)) {
            JsonUtil.write(res, 400, "email and password are required"); return;
        }

        try {
            Optional<User> opt = userDao.findByEmail(body.email.trim().toLowerCase());

            if (opt.isEmpty() || !PasswordUtil.verify(body.password, opt.get().getPasswordHash())) {
                JsonUtil.write(res, 401, "Invalid credentials"); return;
            }

            User user = opt.get();

            // Rotate session (prevents session-fixation)
            HttpSession old = req.getSession(false);
            if (old != null) old.invalidate();
            HttpSession session = req.getSession(true);
            session.setAttribute("userId",    user.getId());
            session.setAttribute("firstName", user.getFirstName());
            session.setAttribute("role",      user.getRole());

            Map<String, Object> data = new LinkedHashMap<>();
            data.put("id",        user.getId());
            data.put("firstName", user.getFirstName());
            data.put("lastName",  user.getLastName());
            data.put("email",     user.getEmail());
            data.put("role",      user.getRole());
            JsonUtil.write(res, 200, data);

        } catch (SQLException e) {
            JsonUtil.write(res, 500, "Database error: " + e.getMessage());
        }
    }

    private static boolean blank(String s) { return s == null || s.isBlank(); }

    private static class Dto { String email, password; }
}
