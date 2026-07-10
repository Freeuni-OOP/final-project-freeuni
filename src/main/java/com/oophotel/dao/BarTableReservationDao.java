package com.oophotel.dao;

import com.oophotel.db.DataSource;
import com.oophotel.model.BarTableReservation;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

public class BarTableReservationDao {

    private static final int MAX_BAR_CAPACITY = 20;


    public int getBookedSeats(Date date, Time time) throws SQLException {

        String sql =
                "SELECT COALESCE(SUM(guests), 0) " +
                        "FROM bar_table_reservations " +
                        "WHERE reservation_date = ? AND reservation_time = ?";

        try (Connection connection = DataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setDate(1, date);
            statement.setTime(2, time);

            try (ResultSet result = statement.executeQuery()) {
                result.next();
                return result.getInt(1);
            }
        }
    }


    public boolean hasCapacity(Date date, Time time, int numGuests)
            throws SQLException {

        int bookedSeats = getBookedSeats(date, time);
        return bookedSeats + numGuests <= MAX_BAR_CAPACITY;
    }


    public void save(BarTableReservation reservation) throws SQLException {

        String sql =
                "INSERT INTO bar_table_reservations " +
                        "(user_id, full_name, guests, reservation_date, reservation_time, notes) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, reservation.getUserId());
            statement.setString(2, reservation.getFullName());
            statement.setInt(3, reservation.getGuests());
            statement.setDate(4, reservation.getReservationDate());
            statement.setTime(5, reservation.getReservationTime());
            statement.setString(6, reservation.getNotes());

            statement.executeUpdate();
        }
    }
}