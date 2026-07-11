package com.oophotel.dao;

import com.oophotel.db.DataSource;
import com.oophotel.model.Room;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class RoomDaoTest {

    @Test
    void findAllReturnsEmptyList() throws Exception {
        Connection connection = mock(Connection.class);
        Statement statement = mock(Statement.class);
        ResultSet rs = mock(ResultSet.class);

        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(org.mockito.ArgumentMatchers.anyString())).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        try (MockedStatic<DataSource> ds = mockStatic(DataSource.class)) {
            ds.when(DataSource::getConnection).thenReturn(connection);

            List<Room> rooms = new RoomDao().findAll();

            assertTrue(rooms.isEmpty());
        }
    }

    @Test
    void findAllMapsRowsIntoRooms() throws Exception {
        Connection connection = mock(Connection.class);
        Statement statement = mock(Statement.class);
        ResultSet rs = mock(ResultSet.class);

        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(org.mockito.ArgumentMatchers.anyString())).thenReturn(rs);
        when(rs.next()).thenReturn(true, true, false);
        when(rs.getInt("id")).thenReturn(101, 201);
        when(rs.getString("type")).thenReturn("Deluxe", "Suite");
        when(rs.getString("name")).thenReturn("Deluxe Mountain View", "Junior Suite");
        when(rs.getString("description")).thenReturn("A", "B");
        when(rs.getBigDecimal("price_per_night")).thenReturn(new BigDecimal("220.00"), new BigDecimal("380.00"));
        when(rs.getInt("capacity")).thenReturn(2, 3);

        try (MockedStatic<DataSource> ds = mockStatic(DataSource.class)) {
            ds.when(DataSource::getConnection).thenReturn(connection);

            List<Room> rooms = new RoomDao().findAll();

            assertEquals(2, rooms.size());
            assertEquals(101, rooms.get(0).getId());
            assertEquals("Suite", rooms.get(1).getType());
            assertEquals(new BigDecimal("380.00"), rooms.get(1).getPricePerNight());
        }
    }
}
