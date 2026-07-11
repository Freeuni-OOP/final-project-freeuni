package com.oophotel.dao;

import com.oophotel.db.DataSource;
import com.oophotel.model.TourReservation;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TourReservationDaoTest {

    private final TourReservationDao dao = new TourReservationDao();

    @Test
    void getBookedGuestsReturnsSum() throws Exception {
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        Date date = Date.valueOf("2026-08-15");

        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt(1)).thenReturn(7);

        try (MockedStatic<DataSource> ds = mockStatic(DataSource.class)) {
            ds.when(DataSource::getConnection).thenReturn(connection);

            assertEquals(7, dao.getBookedGuests(3, date));
        }

        verify(statement).setInt(1, 3);
        verify(statement).setDate(2, date);
    }

    @Test
    void hasCapacityReturnsTrueWhenRoomLeft() throws Exception {
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt(1)).thenReturn(10);

        try (MockedStatic<DataSource> ds = mockStatic(DataSource.class)) {
            ds.when(DataSource::getConnection).thenReturn(connection);

            assertTrue(dao.hasCapacity(1, Date.valueOf("2026-08-15"), 5, 20));
        }
    }

    @Test
    void hasCapacityReturnsFalseWhenOverLimit() throws Exception {
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt(1)).thenReturn(18);

        try (MockedStatic<DataSource> ds = mockStatic(DataSource.class)) {
            ds.when(DataSource::getConnection).thenReturn(connection);

            assertFalse(dao.hasCapacity(1, Date.valueOf("2026-08-15"), 5, 20));
        }
    }

    @Test
    void saveExecutesInsertWithFieldsInOrder() throws Exception {
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        Date date = Date.valueOf("2026-09-01");

        when(connection.prepareStatement(anyString())).thenReturn(statement);

        try (MockedStatic<DataSource> ds = mockStatic(DataSource.class)) {
            ds.when(DataSource::getConnection).thenReturn(connection);

            TourReservation reservation = new TourReservation(
                    2, 3, "Jane", "jane@example.com", 4, date, "vegan");
            dao.save(reservation);
        }

        verify(statement).setInt(1, 2);
        verify(statement).setInt(2, 3);
        verify(statement).setString(3, "Jane");
        verify(statement).setString(4, "jane@example.com");
        verify(statement).setInt(5, 4);
        verify(statement).setDate(6, date);
        verify(statement).setString(7, "vegan");
        verify(statement).executeUpdate();
    }
}
