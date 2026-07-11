package com.oophotel.dao;

import com.oophotel.db.DataSource;
import com.oophotel.model.TourReservation;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TourReservationDao {

    public int getBookedGuests(int tourId, Date tourDate) throws SQLException {

        String query = "SELECT COALESCE(SUM(guests), 0) FROM tour_reservations WHERE tour_id = ? AND tour_date = ?";
        int bookedGuests = 0;

        try (Connection connection = DataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, tourId);
            statement.setDate(2, tourDate);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {
                    bookedGuests = result.getInt(1);
                }
            }
        }

        return bookedGuests;
    }

    public boolean hasCapacity(int tourId, Date tourDate, int requestedGuests, int maxGuests) throws SQLException {

        int currentGuests = getBookedGuests(tourId, tourDate);

        return currentGuests + requestedGuests <= maxGuests;
    }

    public void save(TourReservation reservation) throws SQLException {

        String query = "INSERT INTO tour_reservations (user_id, tour_id, full_name, email, guests, tour_date, special_requests) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, reservation.getUserId());
            statement.setInt(2, reservation.getTourId());
            statement.setString(3, reservation.getFullName());
            statement.setString(4, reservation.getEmail());
            statement.setInt(5, reservation.getGuests());
            statement.setDate(6, reservation.getTourDate());
            statement.setString(7, reservation.getSpecialRequests());

            statement.executeUpdate();
        }
    }
}