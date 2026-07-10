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


        int userId = ((Number) session.getAttribute("userId")).intValue();

        String fullName = request.getParameter("name");
        String email = request.getParameter("email");

        String guestValue = request.getParameter("guests").trim();
        int guestCount;

        if (guestValue.startsWith("6+")) {
            guestCount = 6;
        } else {
            guestCount = Integer.parseInt(guestValue.split(" ")[0]);
        }

        Date tourDate = Date.valueOf(request.getParameter("date"));
        String specialRequests = request.getParameter("notes");


        String selectedTour = request.getParameter("tourId");
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

            if (!reservationDao.hasCapacity(tourId, tourDate, guestCount)) {
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
}