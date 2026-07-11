package com.oophotel.dao;

import com.oophotel.db.DataSource;
import com.oophotel.model.SpaReservation;
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

class SpaReservationDaoTest {

    private final SpaReservationDao dao = new SpaReservationDao();
    private final LocalDate date = LocalDate.of(2026, 8, 15);
    private final LocalTime time = LocalTime.of(15, 0);

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
        when(countRs.getInt(1)).thenReturn(1);
        when(insert.getGeneratedKeys()).thenReturn(keys);
        when(keys.next()).thenReturn(true);
        when(keys.getInt(1)).thenReturn(7);

        try (MockedStatic<DataSource> ds = mockStatic(DataSource.class)) {
            ds.when(DataSource::getConnection).thenReturn(connection);

            SpaReservation saved = dao.create("Ada", "ada@x.com", "Aromatherapy",
                    date, time, 2, "quiet");

            assertEquals(7, saved.getId());
            assertEquals("Aromatherapy", saved.getTreatment());
            assertEquals("CONFIRMED", saved.getStatus());
        }
    }

    @Test
    void createThrowsWhenSlotFull() throws Exception {
        Connection connection = mock(Connection.class);
        PreparedStatement countPs = mock(PreparedStatement.class);
        ResultSet countRs = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(countPs);
        when(countPs.executeQuery()).thenReturn(countRs);
        when(countRs.next()).thenReturn(true);
        when(countRs.getInt(1)).thenReturn(4);

        try (MockedStatic<DataSource> ds = mockStatic(DataSource.class)) {
            ds.when(DataSource::getConnection).thenReturn(connection);

            IllegalStateException ex = assertThrows(IllegalStateException.class,
                    () -> dao.create("Ada", "ada@x.com", "Massage", date, time, 1, null));
            assertTrue(ex.getMessage().contains("fully booked"));
        }
    }

    @Test
    void createRejectsSecondCouplesInSameSlot() throws Exception {
        Connection connection = mock(Connection.class);
        PreparedStatement countPs = mock(PreparedStatement.class);
        ResultSet countRs = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(countPs);
        when(countPs.executeQuery()).thenReturn(countRs);
        when(countRs.next()).thenReturn(true);
        when(countRs.getInt(1)).thenReturn(1);

        try (MockedStatic<DataSource> ds = mockStatic(DataSource.class)) {
            ds.when(DataSource::getConnection).thenReturn(connection);

            IllegalStateException ex = assertThrows(IllegalStateException.class,
                    () -> dao.create("Ada", "ada@x.com", "Couples Package Deluxe",
                            date, time, 2, null));
            assertTrue(ex.getMessage().contains("Couples Package"));
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
        when(rs.getString("treatment")).thenReturn("Massage");
        when(rs.getDate("reservation_date")).thenReturn(Date.valueOf(date));
        when(rs.getTime("reservation_time")).thenReturn(Time.valueOf(time));
        when(rs.getInt("guests")).thenReturn(1);
        when(rs.getString("notes")).thenReturn(null);
        when(rs.getString("status")).thenReturn("CONFIRMED");

        try (MockedStatic<DataSource> ds = mockStatic(DataSource.class)) {
            ds.when(DataSource::getConnection).thenReturn(connection);

            List<SpaReservation> results = dao.findByEmail("ada@x.com");

            assertEquals(1, results.size());
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
        when(rs.getString("treatment")).thenReturn("Massage");
        when(rs.getDate("reservation_date")).thenReturn(Date.valueOf(date));
        when(rs.getTime("reservation_time")).thenReturn(Time.valueOf(time));
        when(rs.getInt("guests")).thenReturn(1);
        when(rs.getString("notes")).thenReturn(null);
        when(rs.getString("status")).thenReturn("CONFIRMED");

        try (MockedStatic<DataSource> ds = mockStatic(DataSource.class)) {
            ds.when(DataSource::getConnection).thenReturn(connection);

            assertEquals(1, dao.findAll().size());
        }
    }
}
