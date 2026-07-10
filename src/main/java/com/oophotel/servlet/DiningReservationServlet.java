package com.oophotel.servlet;

import com.oophotel.dao.DiningReservationDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

@WebServlet("/api/dining/reservations")
public class DiningReservationServlet extends HttpServlet {

    private final DiningReservationDao dao = new DiningReservationDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        String name = request.getParameter("name");
        String guests = request.getParameter("guests");
        String dateStr = request.getParameter("date");
        String timeStr = request.getParameter("time");
        String notes = request.getParameter("notes");

        if (isBlank(name) || isBlank(guests) || isBlank(dateStr) || isBlank(timeStr)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().print("{\"error\":\"Missing required fields\"}");
            return;
        }

        try {
            LocalDate date = LocalDate.parse(dateStr);
            LocalTime time = LocalTime.parse(timeStr);
            dao.create(name, guests, date, time, notes);
            response.getWriter().print("{\"success\":true}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().print("{\"error\":\"Failed to save reservation\"}");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
