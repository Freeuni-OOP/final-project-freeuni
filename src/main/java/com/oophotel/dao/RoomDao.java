package com.oophotel.dao;

import com.oophotel.db.DataSource;
import com.oophotel.model.Room;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// Database queries for rooms.
public class RoomDao {

    // Returns all rooms.
    public List<Room> findAll() throws SQLException {
        String sql = "SELECT id, type, name, description, price_per_night, capacity FROM rooms ORDER BY id";
        List<Room> rooms = new ArrayList<>();
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                rooms.add(new Room(
                        rs.getInt("id"),
                        rs.getString("type"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBigDecimal("price_per_night"),
                        rs.getInt("capacity")
                ));
            }
        }
        return rooms;
    }
}
