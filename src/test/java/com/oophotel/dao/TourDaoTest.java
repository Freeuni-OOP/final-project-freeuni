package com.oophotel.dao;

import com.oophotel.db.DataSource;
import com.oophotel.model.Tour;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TourDaoTest {

    private final TourDao dao = new TourDao();

    @Test
    void getMaxGuestsReturnsValueForKnownTour() throws Exception {
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt("max_guests")).thenReturn(20);

        try (MockedStatic<DataSource> ds = mockStatic(DataSource.class)) {
            ds.when(DataSource::getConnection).thenReturn(connection);

            assertEquals(20, dao.getMaxGuests(3));
        }

        verify(statement).setInt(1, 3);
    }

    @Test
    void getMaxGuestsReturnsNullForMissingTour() throws Exception {
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        try (MockedStatic<DataSource> ds = mockStatic(DataSource.class)) {
            ds.when(DataSource::getConnection).thenReturn(connection);

            assertNull(dao.getMaxGuests(999));
        }
    }

    @Test
    void getAllToursMapsRows() throws Exception {
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true, true, false);
        when(rs.getInt("id")).thenReturn(1, 2);
        when(rs.getString("name")).thenReturn("Kazbegi", "Gomis Mta");
        when(rs.getString("description")).thenReturn("mountain trip", "camping");
        when(rs.getInt("max_guests")).thenReturn(20, 12);

        try (MockedStatic<DataSource> ds = mockStatic(DataSource.class)) {
            ds.when(DataSource::getConnection).thenReturn(connection);

            List<Tour> tours = dao.getAllTours();

            assertEquals(2, tours.size());
            assertEquals("Kazbegi", tours.get(0).getName());
            assertEquals(12, tours.get(1).getMaxGuests());
        }
    }
}
