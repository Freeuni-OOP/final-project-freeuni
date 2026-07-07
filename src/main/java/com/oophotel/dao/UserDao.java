package com.oophotel.dao;

import com.oophotel.db.DataSource;
import com.oophotel.model.User;

import java.sql.*;
import java.util.Optional;

public class UserDao {

    private static final String COLS =
            "id, first_name, last_name, email, password_hash, date_of_birth, role, created_at";

    public Optional<User> findById(long id) throws SQLException {
        String sql = "SELECT " + COLS + " FROM users WHERE id = ?";
        try (Connection c = DataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            return first(ps);
        }
    }

    public Optional<User> findByEmail(String email) throws SQLException {
        String sql = "SELECT " + COLS + " FROM users WHERE email = ?";
        try (Connection c = DataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            return first(ps);
        }
    }

    public boolean existsByEmail(String email) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE email = ? LIMIT 1";
        try (Connection c = DataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public User create(String firstName, String lastName, String email,
                       String passwordHash, Date dateOfBirth) throws SQLException {
        String sql = "INSERT INTO users (first_name, last_name, email, password_hash, date_of_birth) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (Connection c = DataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, email);
            ps.setString(4, passwordHash);
            if (dateOfBirth != null) ps.setDate(5, dateOfBirth);
            else                     ps.setNull(5, Types.DATE);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return findById(keys.getLong(1))
                            .orElseThrow(() -> new SQLException("Created user not found"));
                }
            }
        }
        throw new SQLException("INSERT returned no generated key");
    }

    private Optional<User> first(PreparedStatement ps) throws SQLException {
        try (ResultSet rs = ps.executeQuery()) {
            return rs.next() ? Optional.of(map(rs)) : Optional.empty();
        }
    }

    private User map(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getLong("id"));
        u.setFirstName(rs.getString("first_name"));
        u.setLastName(rs.getString("last_name"));
        u.setEmail(rs.getString("email"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setDateOfBirth(rs.getDate("date_of_birth"));
        u.setRole(rs.getString("role"));
        u.setCreatedAt(rs.getTimestamp("created_at"));
        return u;
    }
}
