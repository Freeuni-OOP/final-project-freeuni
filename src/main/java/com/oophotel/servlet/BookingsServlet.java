package com.oophotel.servlet;

import com.oophotel.dao.BookingDao;
import com.oophotel.model.Booking;
import com.oophotel.util.JsonUtil;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@WebServlet("/api/bookings")
public class BookingsServlet extends HttpServlet {

    private final BookingDao bookingDao = new BookingDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            JsonUtil.write(response, 401, "You must be logged in to book a room");
            return;
        }
        int userId = (int) (long) session.getAttribute("userId");

        Dto body;
        try { body = JsonUtil.readBody(request, Dto.class); }
        catch (Exception e) { JsonUtil.write(response, 400, "Malformed JSON body"); return; }

        if (body == null || body.roomId == null || body.checkIn == null || body.checkOut == null) {
            JsonUtil.write(response, 400, "roomId, checkIn and checkOut are all required");
            return;
        }

        LocalDate checkIn, checkOut;
        try {
            checkIn = LocalDate.parse(body.checkIn);
            checkOut = LocalDate.parse(body.checkOut);
        } catch (DateTimeParseException e) {
            JsonUtil.write(response, 400, "checkIn/checkOut must be dates in YYYY-MM-DD format");
            return;
        }

        if (!checkOut.isAfter(checkIn)) {
            JsonUtil.write(response, 400, "checkOut must be after checkIn");
            return;
        }

        LocalDate today = LocalDate.now();
        if (checkIn.isBefore(today)) {
            JsonUtil.write(response, 400, "Check-in date cannot be in the past");
            return;
        }

        try {
            Booking booking = bookingDao.create(body.roomId, userId, checkIn, checkOut);

            String successJson = String.format(
                    "{\"id\": %d, \"status\": \"%s\", \"message\": \"Booking successful\"}",
                    booking.getId(), booking.getStatus()
            );

            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(successJson);

        } catch (IllegalStateException e) {
            JsonUtil.write(response, 409, e.getMessage());
        } catch (Exception e) {
            JsonUtil.write(response, 500, "Could not create booking: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            JsonUtil.write(response, 401, "You must be logged in to view bookings");
            return;
        }
        int userId = (int) (long) session.getAttribute("userId");

        try {
            List<Booking> bookings = bookingDao.findByUser(userId);
            JsonUtil.write(response, 200, bookings);
        } catch (Exception e) {
            JsonUtil.write(response, 500, "Could not load bookings: " + e.getMessage());
        }
    }

    private static class Dto {
        Integer roomId;
        String checkIn;
        String checkOut;
    }
}