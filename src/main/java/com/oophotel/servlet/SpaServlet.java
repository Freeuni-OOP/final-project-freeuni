package com.oophotel.servlet;

import com.oophotel.dao.SpaReservationDao;
import com.oophotel.model.SpaReservation;
import com.oophotel.util.JsonUtil;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@WebServlet("/api/spa/reservations")
public class SpaServlet extends HttpServlet {

    private final SpaReservationDao dao = new SpaReservationDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Dto body;
        try {
            body = JsonUtil.readBody(request, Dto.class);
        } catch (Exception e) {
            JsonUtil.write(response, 400, "Malformed request body");
            return;
        }

        if (body == null || isBlank(body.name) || isBlank(body.email)
                || isBlank(body.treatment) || isBlank(body.date) || isBlank(body.time)) {
            JsonUtil.write(response, 400, "Name, email, treatment, date and time are required");
            return;
        }

        if (!body.email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            JsonUtil.write(response, 400, "Invalid email address");
            return;
        }

        LocalDate date;
        LocalTime time;
        try {
            date = LocalDate.parse(body.date);
            time = LocalTime.parse(body.time);
        } catch (DateTimeParseException e) {
            JsonUtil.write(response, 400, "Date must be YYYY-MM-DD and time HH:MM");
            return;
        }

        if (date.isBefore(LocalDate.now())) {
            JsonUtil.write(response, 400, "Reservation date cannot be in the past");
            return;
        }

        int guests = body.guests > 0 ? body.guests : 1;

        try {
            SpaReservation reservation = dao.create(body.name.trim(), body.email.trim(),
                    body.treatment, date, time, guests, body.notes);
            JsonUtil.write(response, 200, reservation);
        } catch (IllegalStateException e) {
            JsonUtil.write(response, 409, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtil.write(response, 500, "Could not save reservation");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");

        try {
            List<SpaReservation> list = isBlank(email) ? dao.findAll() : dao.findByEmail(email.trim());
            JsonUtil.write(response, 200, list);
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtil.write(response, 500, "Could not load reservations");
        }
    }

    private boolean isBlank(String v) {
        return v == null || v.isBlank();
    }

    private static class Dto {
        String name;
        String email;
        String treatment;
        String date;
        String time;
        int guests;
        String notes;
    }
}
