package com.oophotel.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Provides database connections.
public final class DataSource {

    private static final String URL = envOrDefault("DB_URL",
            "jdbc:mysql://localhost:3306/oophotel?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC");
    private static final String USER = envOrDefault("DB_USER", "oophotel");
    private static final String PASSWORD = envOrDefault("DB_PASSWORD", "oophotel");

    private DataSource() {
    }

    // Opens a new connection (caller closes it).
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Returns the env variable, or the default if it is not set.
    private static String envOrDefault(String key, String fallback) {
        String value = System.getenv(key);
        return (value == null || value.isBlank()) ? fallback : value;
    }
}
