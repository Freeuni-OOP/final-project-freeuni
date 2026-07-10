package com.oophotel.dao;

import com.oophotel.db.DataSource;
import com.oophotel.model.PoolReservation;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class PoolReservationDao {

    public static int capacityFor(String pool) {
        if (pool == null) return 0;
        if (pool.startsWith("Leisure Pool")) return 40;
        if (pool.startsWith("Olympic Lane Pool")) return 32;
        if (pool.startsWith("Kids' Splash Pool")) return 25;
        if (pool.startsWith("Private Pool Suite")) return 8;
        return 20;
    }

    private int bookedGuestsForSlot(String pool, LocalDate date, LocalTime time) throws SQLException {
        String sql = "SELECT COALESCE(SUM(guests), 0) FROM pool_reservations " +
                "WHERE pool = ? AND reservation_date = ? AND reservation_time = ? AND status = 'CONFIRMED'";
        try (Connection conn = DataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pool);
            ps.setDate(2, Date.valueOf(date));
            ps.setTime(3, Time.valueOf(time));
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    public PoolReservation create(String name, String email, String pool, String activity,
                                  LocalDate date, LocalTime time, int guests, String notes) throws SQLException {
        int capacity = capacityFor(pool);
        int alreadyBooked = bookedGuestsForSlot(pool, date, time);

        if (alreadyBooked + guests > capacity) {
            int remaining = capacity - alreadyBooked;
            if (remaining <= 0) {
                throw new IllegalStateException("This pool session is fully booked. Please choose a different time");
            }
            throw new IllegalStateException("Not enough spots left. Only " + remaining + " guest(s) can still be added to this session");
        }

        String sql = "INSERT INTO pool_reservations (name, email, pool, activity, reservation_date, reservation_time, guests, notes, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'CONFIRMED')";
        try (Connection conn = DataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, pool);
            ps.setString(4, activity == null || activity.isBlank() ? null : activity);
            ps.setDate(5, Date.valueOf(date));
            ps.setTime(6, Time.valueOf(time));
            ps.setInt(7, guests);
            ps.setString(8, notes);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return new PoolReservation(keys.getInt(1), name, email, pool, activity, date, time, guests, notes, "CONFIRMED");
            }
        }
    }

    public List<PoolReservation> findByEmail(String email) throws SQLException {
        String sql = "SELECT id, name, email, pool, activity, reservation_date, reservation_time, guests, notes, status " +
                "FROM pool_reservations WHERE email = ? ORDER BY reservation_date DESC, reservation_time DESC";
        List<PoolReservation> list = new ArrayList<>();
        try (Connection conn = DataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public List<PoolReservation> findAll() throws SQLException {
        String sql = "SELECT id, name, email, pool, activity, reservation_date, reservation_time, guests, notes, status " +
                "FROM pool_reservations ORDER BY id DESC";
        List<PoolReservation> list = new ArrayList<>();
        try (Connection conn = DataSource.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    private PoolReservation map(ResultSet rs) throws SQLException {
        return new PoolReservation(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("pool"),
                rs.getString("activity"),
                rs.getDate("reservation_date").toLocalDate(),
                rs.getTime("reservation_time").toLocalTime(),
                rs.getInt("guests"),
                rs.getString("notes"),
                rs.getString("status")
        );
    }
}
