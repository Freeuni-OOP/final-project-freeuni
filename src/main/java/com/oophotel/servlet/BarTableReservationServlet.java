package com.oophotel.servlet;

import com.oophotel.dao.BarTableReservationDao;
import com.oophotel.model.BarTableReservation;
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
import java.sql.Time;

@WebServlet("/api/reserve-bar-table")
public class BarTableReservationServlet extends HttpServlet {

    private final BarTableReservationDao dao = new BarTableReservationDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // logged in user's id
        HttpSession session = request.getSession(false);
        int userId = ((Number) session.getAttribute("userId")).intValue();

        String fullName = request.getParameter("name");

        String guestsText = request.getParameter("guests");
        int guests = guestsText.equals("6+") ? 6 : Integer.parseInt(guestsText);

        Date reservationDate = Date.valueOf(request.getParameter("date"));
        Time reservationTime = Time.valueOf(request.getParameter("time") + ":00");
        String notes = request.getParameter("notes");

        try {

            if (!dao.hasCapacity(reservationDate, reservationTime, guests)) {

                JsonUtil.write(
                        response,
                        HttpServletResponse.SC_CONFLICT,
                        "Sorry, that time slot is fully booked. Please choose another time."
                );
                return;
            }

            BarTableReservation reservation = new BarTableReservation(
                    userId,
                    fullName,
                    guests,
                    reservationDate,
                    reservationTime,
                    notes
            );

            dao.save(reservation);

            response.setContentType("application/json");
            response.getWriter().write("{\"success\":true}");

        } catch (SQLException e) {

            e.printStackTrace();

            JsonUtil.write(
                    response,
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Something went wrong. Please try again."
            );
        }
    }
}