package com.oophotel.dao;

import com.oophotel.db.DataSource;
import com.oophotel.model.Booking;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BookingDaoTest {

    private final BookingDao dao = new BookingDao();
    private final LocalDate checkIn = LocalDate.of(2026, 8, 1);
    private final LocalDate checkOut = LocalDate.of(2026, 8, 5);

    @Test
    void hasOverlapReturnsTrueWhenRowFound() throws Exception {
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);

        try (MockedStatic<DataSource> ds = mockStatic(DataSource.class)) {
            ds.when(DataSource::getConnection).thenReturn(connection);

            assertTrue(dao.hasOverlap(101, checkIn, checkOut));
        }
    }

    @Test
    void hasOverlapReturnsFalseWhenNoRow() throws Exception {
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        try (MockedStatic<DataSource> ds = mockStatic(DataSource.class)) {
            ds.when(DataSource::getConnection).thenReturn(connection);

            assertFalse(dao.hasOverlap(101, checkIn, checkOut));
        }
    }

    @Test
    void createRejectsCheckOutOnOrBeforeCheckIn() {
        assertThrows(IllegalArgumentException.class,
                () -> dao.create(101, 1, checkIn, checkIn));
        assertThrows(IllegalArgumentException.class,
                () -> dao.create(101, 1, checkOut, checkIn));
    }

    @Test
    void createCommitsAndReturnsBooking() throws Exception {
        Connection connection = mock(Connection.class);
        PreparedStatement overlap = mock(PreparedStatement.class);
        PreparedStatement insert = mock(PreparedStatement.class);
        ResultSet overlapRs = mock(ResultSet.class);
        ResultSet keys = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(overlap);
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(insert);
        when(overlap.executeQuery()).thenReturn(overlapRs);
        when(overlapRs.next()).thenReturn(false);
        when(insert.getGeneratedKeys()).thenReturn(keys);
        when(keys.next()).thenReturn(true);
        when(keys.getInt(1)).thenReturn(55);

        try (MockedStatic<DataSource> ds = mockStatic(DataSource.class)) {
            ds.when(DataSource::getConnection).thenReturn(connection);

            Booking booking = dao.create(101, 7, checkIn, checkOut);

            assertEquals(55, booking.getId());
            assertEquals(101, booking.getRoomId());
            assertEquals(7, booking.getUserId());
            assertEquals("CONFIRMED", booking.getStatus());
        }

        verify(connection).setAutoCommit(false);
        verify(connection).commit();
    }

    @Test
    void createRollsBackAndThrowsWhenOverlapExists() throws Exception {
        Connection connection = mock(Connection.class);
        PreparedStatement overlap = mock(PreparedStatement.class);
        ResultSet overlapRs = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(overlap);
        when(overlap.executeQuery()).thenReturn(overlapRs);
        when(overlapRs.next()).thenReturn(true);

        try (MockedStatic<DataSource> ds = mockStatic(DataSource.class)) {
            ds.when(DataSource::getConnection).thenReturn(connection);

            assertThrows(IllegalStateException.class,
                    () -> dao.create(101, 7, checkIn, checkOut));
        }

        verify(connection).rollback();
    }

    @Test
    void findByUserMapsRows() throws Exception {
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true, true, false);
        when(rs.getInt("id")).thenReturn(1, 2);
        when(rs.getInt("room_id")).thenReturn(101, 202);
        when(rs.getInt("user_id")).thenReturn(9, 9);
        when(rs.getDate("check_in")).thenReturn(java.sql.Date.valueOf(checkIn), java.sql.Date.valueOf(checkIn));
        when(rs.getDate("check_out")).thenReturn(java.sql.Date.valueOf(checkOut), java.sql.Date.valueOf(checkOut));
        when(rs.getString("status")).thenReturn("CONFIRMED", "CONFIRMED");

        try (MockedStatic<DataSource> ds = mockStatic(DataSource.class)) {
            ds.when(DataSource::getConnection).thenReturn(connection);

            List<Booking> bookings = dao.findByUser(9);

            assertEquals(2, bookings.size());
            assertEquals(101, bookings.get(0).getRoomId());
        }

        verify(statement).setInt(1, 9);
    }

    @Test
    void findAllMapsRows() throws Exception {
        Connection connection = mock(Connection.class);
        Statement statement = mock(Statement.class);
        ResultSet rs = mock(ResultSet.class);

        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(rs);
        when(rs.next()).thenReturn(true, false);
        when(rs.getInt("id")).thenReturn(1);
        when(rs.getInt("room_id")).thenReturn(101);
        when(rs.getInt("user_id")).thenReturn(9);
        when(rs.getDate("check_in")).thenReturn(java.sql.Date.valueOf(checkIn));
        when(rs.getDate("check_out")).thenReturn(java.sql.Date.valueOf(checkOut));
        when(rs.getString("status")).thenReturn("CONFIRMED");

        try (MockedStatic<DataSource> ds = mockStatic(DataSource.class)) {
            ds.when(DataSource::getConnection).thenReturn(connection);

            assertEquals(1, dao.findAll().size());
        }
    }
}
