package com.oophotel.servlet;

import com.oophotel.dao.RoomDao;
import com.oophotel.model.Room;
import com.oophotel.util.JsonUtil;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/rooms")
public class RoomsServlet extends HttpServlet {

    private final RoomDao roomDao = new RoomDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            List<Room> rooms = roomDao.findAll();
            JsonUtil.write(response, 200, rooms);
        } catch (Exception e) {
            JsonUtil.write(response, 500, "Could not load rooms: " + e.getMessage());
        }
    }
}