package com.oophotel.dao;

import com.oophotel.db.DataSource;
import com.oophotel.model.Tour;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TourDao {

    public Integer getTourIdByName(String tourName) throws SQLException {

        String query = "SELECT id FROM tours WHERE name = ?";

        try (Connection connection = DataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, tourName);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {
                    return result.getInt("id");
                }
            }
        }

        return null;
    }

    public Integer getMaxGuests(int tourId) throws SQLException {

        String query = "SELECT max_guests FROM tours WHERE id = ?";

        try (Connection connection = DataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, tourId);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {
                    return result.getInt("max_guests");
                }
            }
        }

        return null;
    }

    public List<Tour> getAllTours() throws SQLException {

        String query = "SELECT id, name, description, max_guests FROM tours ORDER BY id";
        List<Tour> tourList = new ArrayList<>();

        try (Connection connection = DataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {

                Tour tour = new Tour(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getString("description"),
                        result.getInt("max_guests")
                );

                tourList.add(tour);
            }
        }

        return tourList;
    }
}