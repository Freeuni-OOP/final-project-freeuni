package com.oophotel.dao;

import com.oophotel.db.DataSource;
import com.oophotel.model.Booking;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Database queries for bookings.
public class BookingDao {

    // Returns true if the room already has a CONFIRMED booking that overlap the given date range.
    public boolean hasOverlap(int roomId, LocalDate checkIn, LocalDate checkOut) throws SQLException {
        String sql = "SELECT 1 FROM bookings " +
                "WHERE room_id = ? AND status = 'CONFIRMED' " +
                "AND check_in < ? AND check_out > ? " +
                "LIMIT 1";
        try (Connection connection = DataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, roomId);
            statement.setDate(2, Date.valueOf(checkOut));
            statement.setDate(3, Date.valueOf(checkIn));
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        }
    }

    // Creates a new CONFIRMED booking and returns it with its generated id.
    // Throws IllegalStateException if the room is already booked for the range.
    public Booking create(int roomId, int userId, LocalDate checkIn, LocalDate checkOut) throws SQLException {
        if (!checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("check_out must be after check_in");
        }
        String overlapSql = "SELECT 1 FROM bookings " +
                "WHERE room_id = ? AND status = 'CONFIRMED' " +
                "AND check_in < ? AND check_out > ? " +
                "LIMIT 1 FOR UPDATE";
        String insertSql = "INSERT INTO bookings (room_id, user_id, check_in, check_out, status) " +
                "VALUES (?, ?, ?, ?, 'CONFIRMED')";
        try (Connection connection = DataSource.getConnection()) {
            connection.setAutoCommit(false);
            try {
                try (PreparedStatement check = connection.prepareStatement(overlapSql)) {
                    check.setInt(1, roomId);
                    check.setDate(2, Date.valueOf(checkOut));
                    check.setDate(3, Date.valueOf(checkIn));
                    try (ResultSet rs = check.executeQuery()) {
                        if (rs.next()) {
                            throw new IllegalStateException("Room is already booked for the selected dates");
                        }
                    }
                }
                try (PreparedStatement statement = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                    statement.setInt(1, roomId);
                    statement.setInt(2, userId);
                    statement.setDate(3, Date.valueOf(checkIn));
                    statement.setDate(4, Date.valueOf(checkOut));
                    statement.executeUpdate();
                    try (ResultSet keys = statement.getGeneratedKeys()) {
                        keys.next();
                        int id = keys.getInt(1);
                        Booking booking = new Booking(id, roomId, userId, checkIn, checkOut, "CONFIRMED");
                        connection.commit();
                        return booking;
                    }
                }
            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                throw e;
            }
        }
    }

    // Returns all bookings belonging to a user, most recent first.
    public List<Booking> findByUser(int userId) throws SQLException {
        String sql = "SELECT id, room_id, user_id, check_in, check_out, status " +
                "FROM bookings WHERE user_id = ? ORDER BY id DESC";
        List<Booking> bookings = new ArrayList<>();
        try (Connection connection = DataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    bookings.add(map(rs));
                }
            }
        }
        return bookings;
    }

    // Returns all bookings
    public List<Booking> findAll() throws SQLException {
        String sql = "SELECT id, room_id, user_id, check_in, check_out, status " +
                "FROM bookings ORDER BY id DESC";
        List<Booking> bookings = new ArrayList<>();
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                bookings.add(map(rs));
            }
        }
        return bookings;
    }

    private Booking map(ResultSet rs) throws SQLException {
        return new Booking(
                rs.getInt("id"),
                rs.getInt("room_id"),
                rs.getInt("user_id"),
                rs.getDate("check_in").toLocalDate(),
                rs.getDate("check_out").toLocalDate(),
                rs.getString("status")
        );
    }
}

