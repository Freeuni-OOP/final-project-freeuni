package com.oophotel.dao;

import com.oophotel.db.DataSource;
import com.oophotel.model.DiningReservation;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DiningReservationDaoTest {

    private final DiningReservationDao dao = new DiningReservationDao();

    @Test
    void createInsertsRowAndReturnsSaved() throws Exception {
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet keys = mock(ResultSet.class);

        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(statement);
        when(statement.getGeneratedKeys()).thenReturn(keys);
        when(keys.next()).thenReturn(true);
        when(keys.getInt(1)).thenReturn(3);

        LocalDate date = LocalDate.of(2026, 8, 15);
        LocalTime time = LocalTime.of(19, 30);

        try (MockedStatic<DataSource> ds = mockStatic(DataSource.class)) {
            ds.when(DataSource::getConnection).thenReturn(connection);

            DiningReservation saved = dao.create("Ada", "2", date, time, "window");

            assertEquals(3, saved.getId());
            assertEquals("Ada", saved.getName());
            assertEquals("CONFIRMED", saved.getStatus());
        }

        verify(statement).setString(1, "Ada");
        verify(statement).setString(2, "2");
        verify(statement).setDate(3, Date.valueOf(date));
        verify(statement).setTime(4, Time.valueOf(time));
        verify(statement).setString(5, "window");
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
        when(rs.getString("guests")).thenReturn("4");
        when(rs.getDate("reservation_date")).thenReturn(Date.valueOf("2026-08-15"));
        when(rs.getTime("reservation_time")).thenReturn(Time.valueOf("19:30:00"));
        when(rs.getString("notes")).thenReturn(null);
        when(rs.getString("status")).thenReturn("CONFIRMED");

        try (MockedStatic<DataSource> ds = mockStatic(DataSource.class)) {
            ds.when(DataSource::getConnection).thenReturn(connection);

            List<DiningReservation> list = dao.findAll();

            assertEquals(1, list.size());
            assertEquals("Ada", list.get(0).getName());
            assertEquals(LocalTime.of(19, 30), list.get(0).getTime());
        }
    }
}
