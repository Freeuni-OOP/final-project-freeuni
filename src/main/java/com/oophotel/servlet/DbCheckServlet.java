package com.oophotel.servlet;

import com.oophotel.dao.RoomDao;
import com.oophotel.model.Room;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

// Temporary endpoint that checks the DB connection works.
@WebServlet("/api/db-check")
public class DbCheckServlet extends HttpServlet {

    private final RoomDao roomDao = new RoomDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        try {
            List<Room> rooms = roomDao.findAll();
            out.println("DB OK - " + rooms.size() + " rooms:");
            for (Room room : rooms) {
                out.println(" - " + room.getName() + " (" + room.getType() + ", EUR " + room.getPricePerNight() + ")");
            }
        } catch (Exception e) {
            response.setStatus(500);
            out.println("DB ERROR: " + e.getMessage());
        }
    }
}
