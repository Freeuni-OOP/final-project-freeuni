package com.oophotel.dao;

import com.oophotel.db.DataSource;
import com.oophotel.model.BarTableReservation;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BarTableReservationDaoTest {

    private final BarTableReservationDao dao = new BarTableReservationDao();
    private final Date date = Date.valueOf("2026-08-15");
    private final Time time = Time.valueOf("19:00:00");

    @Test
    void getBookedSeatsReturnsSum() throws Exception {
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt(1)).thenReturn(12);

        try (MockedStatic<DataSource> ds = mockStatic(DataSource.class)) {
            ds.when(DataSource::getConnection).thenReturn(connection);

            assertEquals(12, dao.getBookedSeats(date, time));
        }

        verify(statement).setDate(1, date);
        verify(statement).setTime(2, time);
    }

    @Test
    void hasCapacityReturnsTrueBelowCap() throws Exception {
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt(1)).thenReturn(15);

        try (MockedStatic<DataSource> ds = mockStatic(DataSource.class)) {
            ds.when(DataSource::getConnection).thenReturn(connection);

            assertTrue(dao.hasCapacity(date, time, 5));
        }
    }

    @Test
    void hasCapacityReturnsFalseAtCap() throws Exception {
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt(1)).thenReturn(18);

        try (MockedStatic<DataSource> ds = mockStatic(DataSource.class)) {
            ds.when(DataSource::getConnection).thenReturn(connection);

            assertFalse(dao.hasCapacity(date, time, 5));
        }
    }

    @Test
    void saveExecutesInsertWithFieldsInOrder() throws Exception {
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);

        when(connection.prepareStatement(anyString())).thenReturn(statement);

        BarTableReservation reservation = new BarTableReservation();
        reservation.setUserId(7);
        reservation.setFullName("Ada");
        reservation.setGuests(3);
        reservation.setReservationDate(date);
        reservation.setReservationTime(time);
        reservation.setNotes("window");

        try (MockedStatic<DataSource> ds = mockStatic(DataSource.class)) {
            ds.when(DataSource::getConnection).thenReturn(connection);

            dao.save(reservation);
        }

        verify(statement).setInt(1, 7);
        verify(statement).setString(2, "Ada");
        verify(statement).setInt(3, 3);
        verify(statement).setDate(4, date);
        verify(statement).setTime(5, time);
        verify(statement).setString(6, "window");
        verify(statement).executeUpdate();
    }
}
