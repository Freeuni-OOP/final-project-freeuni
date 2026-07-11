package com.oophotel.dao;

import com.oophotel.db.DataSource;
import com.oophotel.model.PoolReservation;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class PoolReservationDaoTest {

    private final PoolReservationDao dao = new PoolReservationDao();
    private final LocalDate date = LocalDate.of(2026, 8, 15);
    private final LocalTime time = LocalTime.of(15, 0);

    @Test
    void capacityForKnownPools() {
        assertEquals(40, PoolReservationDao.capacityFor("Leisure Pool"));
        assertEquals(32, PoolReservationDao.capacityFor("Olympic Lane Pool"));
        assertEquals(25, PoolReservationDao.capacityFor("Kids' Splash Pool"));
        assertEquals(8, PoolReservationDao.capacityFor("Private Pool Suite"));
    }

    @Test
    void capacityForUnknownPoolReturnsFallback() {
        assertEquals(20, PoolReservationDao.capacityFor("Rooftop"));
    }

    @Test
    void capacityForNullReturnsZero() {
        assertEquals(0, PoolReservationDao.capacityFor(null));
    }

    @Test
    void createInsertsRowWhenCapacityAvailable() throws Exception {
        Connection connection = mock(Connection.class);
        PreparedStatement countPs = mock(PreparedStatement.class);
        PreparedStatement insert = mock(PreparedStatement.class);
        ResultSet countRs = mock(ResultSet.class);
        ResultSet keys = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(countPs);
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(insert);
        when(countPs.executeQuery()).thenReturn(countRs);
        when(countRs.next()).thenReturn(true);
        when(countRs.getInt(1)).thenReturn(5);
        when(insert.getGeneratedKeys()).thenReturn(keys);
        when(keys.next()).thenReturn(true);
        when(keys.getInt(1)).thenReturn(11);

        try (MockedStatic<DataSource> ds = mockStatic(DataSource.class)) {
            ds.when(DataSource::getConnection).thenReturn(connection);

            PoolReservation saved = dao.create("Ada", "ada@x.com", "Leisure Pool",
                    "Swim", date, time, 2, "quiet");

            assertEquals(11, saved.getId());
            assertEquals("Leisure Pool", saved.getPool());
            assertEquals("CONFIRMED", saved.getStatus());
        }
    }

    @Test
    void createThrowsWhenSlotFullyBooked() throws Exception {
        Connection connection = mock(Connection.class);
        PreparedStatement countPs = mock(PreparedStatement.class);
        ResultSet countRs = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(countPs);
        when(countPs.executeQuery()).thenReturn(countRs);
        when(countRs.next()).thenReturn(true);
        when(countRs.getInt(1)).thenReturn(40);

        try (MockedStatic<DataSource> ds = mockStatic(DataSource.class)) {
            ds.when(DataSource::getConnection).thenReturn(connection);

            IllegalStateException ex = assertThrows(IllegalStateException.class,
                    () -> dao.create("Ada", "ada@x.com", "Leisure Pool",
                            null, date, time, 2, null));
            assertTrue(ex.getMessage().contains("fully booked"));
        }
    }

    @Test
    void createThrowsWithRemainingCountWhenPartialSpace() throws Exception {
        Connection connection = mock(Connection.class);
        PreparedStatement countPs = mock(PreparedStatement.class);
        ResultSet countRs = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(countPs);
        when(countPs.executeQuery()).thenReturn(countRs);
        when(countRs.next()).thenReturn(true);
        when(countRs.getInt(1)).thenReturn(38);

        try (MockedStatic<DataSource> ds = mockStatic(DataSource.class)) {
            ds.when(DataSource::getConnection).thenReturn(connection);

            IllegalStateException ex = assertThrows(IllegalStateException.class,
                    () -> dao.create("Ada", "ada@x.com", "Leisure Pool",
                            null, date, time, 5, null));
            assertTrue(ex.getMessage().contains("Only 2"));
        }
    }

    @Test
    void findByEmailMapsRows() throws Exception {
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true, false);
        when(rs.getInt("id")).thenReturn(1);
        when(rs.getString("name")).thenReturn("Ada");
        when(rs.getString("email")).thenReturn("ada@x.com");
        when(rs.getString("pool")).thenReturn("Leisure Pool");
        when(rs.getString("activity")).thenReturn("Swim");
        when(rs.getDate("reservation_date")).thenReturn(Date.valueOf(date));
        when(rs.getTime("reservation_time")).thenReturn(Time.valueOf(time));
        when(rs.getInt("guests")).thenReturn(2);
        when(rs.getString("notes")).thenReturn(null);
        when(rs.getString("status")).thenReturn("CONFIRMED");

        try (MockedStatic<DataSource> ds = mockStatic(DataSource.class)) {
            ds.when(DataSource::getConnection).thenReturn(connection);

            List<PoolReservation> results = dao.findByEmail("ada@x.com");

            assertEquals(1, results.size());
            assertEquals("Ada", results.get(0).getName());
        }
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
        when(rs.getString("name")).thenReturn("Ada");
        when(rs.getString("email")).thenReturn("ada@x.com");
        when(rs.getString("pool")).thenReturn("Leisure Pool");
        when(rs.getString("activity")).thenReturn(null);
        when(rs.getDate("reservation_date")).thenReturn(Date.valueOf(date));
        when(rs.getTime("reservation_time")).thenReturn(Time.valueOf(time));
        when(rs.getInt("guests")).thenReturn(2);
        when(rs.getString("notes")).thenReturn(null);
        when(rs.getString("status")).thenReturn("CONFIRMED");

        try (MockedStatic<DataSource> ds = mockStatic(DataSource.class)) {
            ds.when(DataSource::getConnection).thenReturn(connection);

            assertEquals(1, dao.findAll().size());
        }
    }
}
