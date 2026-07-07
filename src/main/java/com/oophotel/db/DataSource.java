package com.oophotel.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Provides database connections using env vars with local-dev fallback defaults.
public final class DataSource {

    // Explicitly load the driver once so Tomcat's classloader hierarchy
    // doesn't prevent DriverManager from finding it.
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC driver not found on classpath", e);
        }
    }

    private static final String URL = envOrDefault("DB_URL",
            "jdbc:mysql://localhost:3306/oophotel?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC");
    private static final String USER     = envOrDefault("DB_USER",     "root");
    private static final String PASSWORD = envOrDefault("DB_PASSWORD", "");

    private DataSource() {}

    // Opens a new connection — caller must close it.
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private static String envOrDefault(String key, String fallback) {
        String value = System.getenv(key);
        return (value == null || value.isBlank()) ? fallback : value;
    }
}
