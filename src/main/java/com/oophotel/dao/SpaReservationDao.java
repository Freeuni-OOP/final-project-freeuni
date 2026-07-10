package com.oophotel.dao;

import com.oophotel.db.DataSource;
import com.oophotel.model.SpaReservation;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class SpaReservationDao {

    private static final int MAX_GUESTS_PER_SLOT = 4;
    private static final int MAX_COUPLES_PER_SLOT = 1;

    private int bookedGuestsForSlot(LocalDate date, LocalTime time) throws SQLException {
        String sql = "SELECT COALESCE(SUM(guests), 0) FROM spa_reservations " +
                "WHERE reservation_date = ? AND reservation_time = ? AND status = 'CONFIRMED'";
        try (Connection conn = DataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(date));
            ps.setTime(2, Time.valueOf(time));
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    private int bookedCouplesForSlot(LocalDate date, LocalTime time) throws SQLException {
        String sql = "SELECT COUNT(*) FROM spa_reservations " +
                "WHERE reservation_date = ? AND reservation_time = ? AND status = 'CONFIRMED' " +
                "AND treatment LIKE 'Couples Package%'";
        try (Connection conn = DataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(date));
            ps.setTime(2, Time.valueOf(time));
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    public SpaReservation create(String name, String email, String treatment,
                                 LocalDate date, LocalTime time, int guests, String notes) throws SQLException {
        boolean isCouples = treatment != null && treatment.startsWith("Couples Package");

        if (isCouples && bookedCouplesForSlot(date, time) >= MAX_COUPLES_PER_SLOT) {
            throw new IllegalStateException("The Couples Package room is fully booked for this time slot");
        }

        if (bookedGuestsForSlot(date, time) + guests > MAX_GUESTS_PER_SLOT) {
            throw new IllegalStateException("This time slot is fully booked. Please choose a different time");
        }

        String sql = "INSERT INTO spa_reservations (name, email, treatment, reservation_date, reservation_time, guests, notes, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, 'CONFIRMED')";
        try (Connection conn = DataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, treatment);
            ps.setDate(4, Date.valueOf(date));
            ps.setTime(5, Time.valueOf(time));
            ps.setInt(6, guests);
            ps.setString(7, notes);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return new SpaReservation(keys.getInt(1), name, email, treatment, date, time, guests, notes, "CONFIRMED");
            }
        }
    }

    public List<SpaReservation> findByEmail(String email) throws SQLException {
        String sql = "SELECT id, name, email, treatment, reservation_date, reservation_time, guests, notes, status " +
                "FROM spa_reservations WHERE email = ? ORDER BY reservation_date DESC, reservation_time DESC";
        List<SpaReservation> list = new ArrayList<>();
        try (Connection conn = DataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public List<SpaReservation> findAll() throws SQLException {
        String sql = "SELECT id, name, email, treatment, reservation_date, reservation_time, guests, notes, status " +
                "FROM spa_reservations ORDER BY id DESC";
        List<SpaReservation> list = new ArrayList<>();
        try (Connection conn = DataSource.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    private SpaReservation map(ResultSet rs) throws SQLException {
        return new SpaReservation(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("treatment"),
                rs.getDate("reservation_date").toLocalDate(),
                rs.getTime("reservation_time").toLocalTime(),
                rs.getInt("guests"),
                rs.getString("notes"),
                rs.getString("status")
        );
    }
}
