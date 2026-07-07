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
import java.sql.Date;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * POST /api/auth/register
 *
 * Body: { "firstName", "lastName", "email", "password", "dateOfBirth" (YYYY-MM-DD, optional) }
 * Success 201: user created + session started (auto-login).
 */
@WebServlet("/api/auth/register")
public class RegisterServlet extends HttpServlet {

    private final UserDao userDao = new UserDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

        Dto body;
        try { body = JsonUtil.readBody(req, Dto.class); }
        catch (Exception e) { JsonUtil.write(res, 400, "Malformed JSON"); return; }

        if (body == null || blank(body.firstName) || blank(body.lastName)
                || blank(body.email) || blank(body.password)) {
            JsonUtil.write(res, 400, "firstName, lastName, email and password are required");
            return;
        }

        body.firstName = body.firstName.trim();
        body.lastName  = body.lastName.trim();
        body.email     = body.email.trim().toLowerCase();

        if (!body.email.contains("@")) {
            JsonUtil.write(res, 400, "Invalid email address"); return;
        }
        if (!PasswordUtil.isStrong(body.password)) {
            JsonUtil.write(res, 400, "Password must be at least " + PasswordUtil.MIN_LENGTH + " characters"); return;
        }

        Date dob = null;
        if (!blank(body.dateOfBirth)) {
            try { dob = Date.valueOf(body.dateOfBirth.trim()); }
            catch (IllegalArgumentException e) {
                JsonUtil.write(res, 400, "dateOfBirth must be YYYY-MM-DD"); return;
            }
        }

        try {
            if (userDao.existsByEmail(body.email)) {
                JsonUtil.write(res, 409, "Email address is already registered"); return;
            }
            User user = userDao.create(body.firstName, body.lastName, body.email,
                    PasswordUtil.hash(body.password), dob);

            HttpSession session = req.getSession(true);
            session.setAttribute("userId",    user.getId());
            session.setAttribute("firstName", user.getFirstName());
            session.setAttribute("role",      user.getRole());

            JsonUtil.write(res, 201, publicMap(user));

        } catch (SQLException e) {
            JsonUtil.write(res, 500, "Database error: " + e.getMessage());
        }
    }

    private static Map<String, Object> publicMap(User u) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id",          u.getId());
        m.put("firstName",   u.getFirstName());
        m.put("lastName",    u.getLastName());
        m.put("email",       u.getEmail());
        m.put("dateOfBirth", u.getDateOfBirth());
        m.put("role",        u.getRole());
        m.put("createdAt",   u.getCreatedAt());
        return m;
    }

    private static boolean blank(String s) { return s == null || s.isBlank(); }

    private static class Dto {
        String firstName, lastName, email, password, dateOfBirth;
    }
}
