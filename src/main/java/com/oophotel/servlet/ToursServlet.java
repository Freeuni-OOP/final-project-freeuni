package com.oophotel.servlet;

import com.oophotel.dao.TourDao;
import com.oophotel.model.Tour;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/tours")
public class ToursServlet extends HttpServlet {

    private final TourDao tourDao = new TourDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {

            List<Tour> tours = tourDao.getAllTours();

            StringBuilder json = new StringBuilder();
            json.append("[");

            for (int i = 0; i < tours.size(); i++) {

                Tour tour = tours.get(i);

                if (i > 0) {
                    json.append(",");
                }

                json.append("{")
                        .append("\"id\":").append(tour.getId()).append(",")
                        .append("\"name\":\"").append(escape(tour.getName())).append("\",")
                        .append("\"description\":\"").append(escape(tour.getDescription())).append("\"")
                        .append("}");
            }

            json.append("]");

            response.getWriter().write(json.toString());

        } catch (SQLException e) {

            e.printStackTrace();

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("[]");
        }
    }

    private String escape(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }
}