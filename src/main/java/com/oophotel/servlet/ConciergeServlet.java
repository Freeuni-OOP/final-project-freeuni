package com.oophotel.servlet;

import com.oophotel.dao.ConciergeRequestDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/api/concierge")
public class ConciergeServlet extends HttpServlet {

    private final ConciergeRequestDao dao = new ConciergeRequestDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String type = request.getParameter("type");
        String dateStr = request.getParameter("date");
        String details = request.getParameter("details");

        if (isBlank(name) || isBlank(email) || isBlank(type) || isBlank(details)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().print("{\"error\":\"Missing required fields\"}");
            return;
        }

        try {
            LocalDate date = isBlank(dateStr) ? null : LocalDate.parse(dateStr);
            dao.create(name, email, type, date, details);
            response.getWriter().print("{\"success\":true}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().print("{\"error\":\"Failed to save request\"}");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
