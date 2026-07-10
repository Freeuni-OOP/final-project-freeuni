package com.oophotel.dao;

import com.oophotel.db.DataSource;
import com.oophotel.model.ConciergeRequest;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Database queries for concierge requests.
public class ConciergeRequestDao {

    // Saves a new request and returns it with its generated id.
    public ConciergeRequest create(String name, String email, String type, LocalDate requestDate, String details) throws SQLException {
        String sql = "INSERT INTO concierge_requests (name, email, type, request_date, details, status) " +
                "VALUES (?, ?, ?, ?, ?, 'PENDING')";
        try (Connection connection = DataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, type);
            if (requestDate != null) {
                statement.setDate(4, Date.valueOf(requestDate));
            } else {
                statement.setNull(4, Types.DATE);
            }
            statement.setString(5, details);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                keys.next();
                int id = keys.getInt(1);
                return new ConciergeRequest(id, name, email, type, requestDate, details, "PENDING");
            }
        }
    }

    // Returns all requests, newest first.
    public List<ConciergeRequest> findAll() throws SQLException {
        String sql = "SELECT id, name, email, type, request_date, details, status " +
                "FROM concierge_requests ORDER BY id DESC";
        List<ConciergeRequest> requests = new ArrayList<>();
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                requests.add(map(rs));
            }
        }
        return requests;
    }

    private ConciergeRequest map(ResultSet rs) throws SQLException {
        Date date = rs.getDate("request_date");
        return new ConciergeRequest(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("type"),
                date != null ? date.toLocalDate() : null,
                rs.getString("details"),
                rs.getString("status")
        );
    }
}
