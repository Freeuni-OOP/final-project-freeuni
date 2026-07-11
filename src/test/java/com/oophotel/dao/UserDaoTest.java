package com.oophotel.dao;

import com.oophotel.db.DataSource;
import com.oophotel.model.User;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserDaoTest {

    private final UserDao dao = new UserDao();

    @Test
    void findByEmailReturnsUserWhenPresent() throws Exception {
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getLong("id")).thenReturn(42L);
        when(rs.getString("first_name")).thenReturn("Ada");
        when(rs.getString("last_name")).thenReturn("Lovelace");
        when(rs.getString("email")).thenReturn("ada@example.com");
        when(rs.getString("password_hash")).thenReturn("hashed");
        when(rs.getDate("date_of_birth")).thenReturn(Date.valueOf("1990-01-01"));
        when(rs.getString("role")).thenReturn("USER");
        when(rs.getTimestamp("created_at")).thenReturn(Timestamp.valueOf("2026-01-01 12:00:00"));

        try (MockedStatic<DataSource> ds = mockStatic(DataSource.class)) {
            ds.when(DataSource::getConnection).thenReturn(connection);

            Optional<User> result = dao.findByEmail("ada@example.com");

            assertTrue(result.isPresent());
            assertEquals(42L, result.get().getId());
            assertEquals("Ada", result.get().getFirstName());
            assertEquals("ada@example.com", result.get().getEmail());
        }

        verify(statement).setString(1, "ada@example.com");
    }

    @Test
    void findByEmailReturnsEmptyWhenAbsent() throws Exception {
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        try (MockedStatic<DataSource> ds = mockStatic(DataSource.class)) {
            ds.when(DataSource::getConnection).thenReturn(connection);

            assertTrue(dao.findByEmail("missing@example.com").isEmpty());
        }
    }

    @Test
    void findByIdReturnsUserWhenPresent() throws Exception {
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getLong("id")).thenReturn(7L);
        when(rs.getString("first_name")).thenReturn("Grace");

        try (MockedStatic<DataSource> ds = mockStatic(DataSource.class)) {
            ds.when(DataSource::getConnection).thenReturn(connection);

            Optional<User> result = dao.findById(7L);

            assertTrue(result.isPresent());
            assertEquals(7L, result.get().getId());
        }

        verify(statement).setLong(1, 7L);
    }

    @Test
    void existsByEmailReturnsTrueWhenRowFound() throws Exception {
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);

        try (MockedStatic<DataSource> ds = mockStatic(DataSource.class)) {
            ds.when(DataSource::getConnection).thenReturn(connection);

            assertTrue(dao.existsByEmail("ada@example.com"));
        }
    }

    @Test
    void existsByEmailReturnsFalseWhenAbsent() throws Exception {
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        try (MockedStatic<DataSource> ds = mockStatic(DataSource.class)) {
            ds.when(DataSource::getConnection).thenReturn(connection);

            assertFalse(dao.existsByEmail("missing@example.com"));
        }
    }

    @Test
    void createInsertsAndReturnsNewUser() throws Exception {
        Connection connection = mock(Connection.class);
        PreparedStatement insert = mock(PreparedStatement.class);
        PreparedStatement select = mock(PreparedStatement.class);
        ResultSet keys = mock(ResultSet.class);
        ResultSet lookup = mock(ResultSet.class);

        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(insert);
        when(connection.prepareStatement(anyString())).thenReturn(select);
        when(insert.getGeneratedKeys()).thenReturn(keys);
        when(keys.next()).thenReturn(true);
        when(keys.getLong(1)).thenReturn(99L);
        when(select.executeQuery()).thenReturn(lookup);
        when(lookup.next()).thenReturn(true);
        when(lookup.getLong("id")).thenReturn(99L);
        when(lookup.getString("first_name")).thenReturn("New");
        when(lookup.getString("email")).thenReturn("new@example.com");

        try (MockedStatic<DataSource> ds = mockStatic(DataSource.class)) {
            ds.when(DataSource::getConnection).thenReturn(connection);

            User created = dao.create("New", "User", "new@example.com", "hash", Date.valueOf("2000-05-05"));

            assertEquals(99L, created.getId());
            assertEquals("New", created.getFirstName());
        }

        verify(insert).setString(1, "New");
        verify(insert).setString(3, "new@example.com");
        verify(insert).setDate(eq(5), eq(Date.valueOf("2000-05-05")));
        verify(insert).executeUpdate();
    }

    @Test
    void createHandlesNullDateOfBirth() throws Exception {
        Connection connection = mock(Connection.class);
        PreparedStatement insert = mock(PreparedStatement.class);
        PreparedStatement select = mock(PreparedStatement.class);
        ResultSet keys = mock(ResultSet.class);
        ResultSet lookup = mock(ResultSet.class);

        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(insert);
        when(connection.prepareStatement(anyString())).thenReturn(select);
        when(insert.getGeneratedKeys()).thenReturn(keys);
        when(keys.next()).thenReturn(true);
        when(keys.getLong(1)).thenReturn(1L);
        when(select.executeQuery()).thenReturn(lookup);
        when(lookup.next()).thenReturn(true);
        when(lookup.getLong("id")).thenReturn(1L);

        try (MockedStatic<DataSource> ds = mockStatic(DataSource.class)) {
            ds.when(DataSource::getConnection).thenReturn(connection);

            dao.create("A", "B", "a@b.com", "hash", null);
        }

        verify(insert).setNull(5, Types.DATE);
    }

    @Test
    void createThrowsWhenNoGeneratedKey() throws Exception {
        Connection connection = mock(Connection.class);
        PreparedStatement insert = mock(PreparedStatement.class);
        ResultSet keys = mock(ResultSet.class);

        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(insert);
        when(insert.getGeneratedKeys()).thenReturn(keys);
        when(keys.next()).thenReturn(false);

        try (MockedStatic<DataSource> ds = mockStatic(DataSource.class)) {
            ds.when(DataSource::getConnection).thenReturn(connection);

            assertThrows(SQLException.class,
                    () -> dao.create("A", "B", "x@y.com", "hash", null));
        }
    }
}
