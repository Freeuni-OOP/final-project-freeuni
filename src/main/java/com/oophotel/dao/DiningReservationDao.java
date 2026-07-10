package com.oophotel.dao;

import com.oophotel.db.DataSource;
import com.oophotel.model.DiningReservation;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

// Database queries for dining reservations.
public class DiningReservationDao {

    // Saves a new reservation and returns it with its generated id.
    public DiningReservation create(String name, String guests, LocalDate date, LocalTime time, String notes) throws SQLException {
        String sql = "INSERT INTO dining_reservations (name, guests, reservation_date, reservation_time, notes, status) " +
                "VALUES (?, ?, ?, ?, ?, 'CONFIRMED')";
        try (Connection connection = DataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name);
            statement.setString(2, guests);
            statement.setDate(3, Date.valueOf(date));
            statement.setTime(4, Time.valueOf(time));
            statement.setString(5, notes);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                keys.next();
                int id = keys.getInt(1);
                return new DiningReservation(id, name, guests, date, time, notes, "CONFIRMED");
            }
        }
    }

    // Returns all reservations, newest first.
    public List<DiningReservation> findAll() throws SQLException {
        String sql = "SELECT id, name, guests, reservation_date, reservation_time, notes, status " +
                "FROM dining_reservations ORDER BY id DESC";
        List<DiningReservation> reservations = new ArrayList<>();
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                reservations.add(map(rs));
            }
        }
        return reservations;
    }

    private DiningReservation map(ResultSet rs) throws SQLException {
        return new DiningReservation(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("guests"),
                rs.getDate("reservation_date").toLocalDate(),
                rs.getTime("reservation_time").toLocalTime(),
                rs.getString("notes"),
                rs.getString("status")
        );
    }
}
