package com.oophotel.servlet;

import com.oophotel.dao.TourDao;
import com.oophotel.dao.TourReservationDao;
import com.oophotel.model.TourReservation;
import com.oophotel.util.JsonUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

@WebServlet("/api/reserve-tour")
public class TourReservationServlet extends HttpServlet {

    private final TourReservationDao reservationDao = new TourReservationDao();
    private final TourDao tourDao = new TourDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        Object userIdAttr = session == null ? null : session.getAttribute("userId");

        if (userIdAttr == null) {
            JsonUtil.write(response,
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Please sign in to book a tour.");
            return;
        }

        int userId = ((Number) userIdAttr).intValue();

        String fullName = request.getParameter("name");
        String email = request.getParameter("email");
        String guestValue = request.getParameter("guests");
        String dateValue = request.getParameter("date");
        String selectedTour = request.getParameter("tourId");
        String specialRequests = request.getParameter("notes");

        if (isBlank(fullName) || isBlank(email) || isBlank(guestValue)
                || isBlank(dateValue) || isBlank(selectedTour)) {
            JsonUtil.write(response,
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Please fill in all required fields.");
            return;
        }

        int guestCount;
        Date tourDate;

        try {
            guestValue = guestValue.trim();

            if (guestValue.startsWith("6+")) {
                guestCount = 6;
            } else {
                guestCount = Integer.parseInt(guestValue.split(" ")[0]);
            }

            tourDate = Date.valueOf(dateValue);

        } catch (IllegalArgumentException e) {
            JsonUtil.write(response,
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Please check the date and number of guests.");
            return;
        }

        if (guestCount < 1) {
            JsonUtil.write(response,
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Please choose at least one guest.");
            return;
        }

        String tourName;

        if (selectedTour.contains(" — GEL")) {
            tourName = selectedTour.substring(0, selectedTour.indexOf(" — GEL")).trim();
        } else {
            tourName = selectedTour.trim();
        }

        try {

            Integer tourId = tourDao.getTourIdByName(tourName);

            if (tourId == null) {
                JsonUtil.write(response,
                        HttpServletResponse.SC_BAD_REQUEST,
                        "That tour could not be found.");
                return;
            }

            Integer maxGuests = tourDao.getMaxGuests(tourId);

            if (maxGuests == null) {
                JsonUtil.write(response,
                        HttpServletResponse.SC_BAD_REQUEST,
                        "That tour could not be found.");
                return;
            }

            if (!reservationDao.hasCapacity(tourId, tourDate, guestCount, maxGuests)) {
                JsonUtil.write(response,
                        HttpServletResponse.SC_CONFLICT,
                        "Sorry, that tour is fully booked for this date. Please choose another date.");
                return;
            }

            TourReservation reservation = new TourReservation(
                    userId,
                    tourId,
                    fullName,
                    email,
                    guestCount,
                    tourDate,
                    specialRequests
            );

            reservationDao.save(reservation);

            response.setContentType("application/json");
            response.getWriter().write("{\"success\":true}");

        } catch (SQLException e) {

            e.printStackTrace();

            JsonUtil.write(response,
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Something went wrong. Please try again.");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
