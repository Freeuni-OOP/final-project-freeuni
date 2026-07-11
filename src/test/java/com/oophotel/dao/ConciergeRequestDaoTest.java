package com.oophotel.dao;

import com.oophotel.db.DataSource;
import com.oophotel.model.ConciergeRequest;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ConciergeRequestDaoTest {

    private final ConciergeRequestDao dao = new ConciergeRequestDao();

    @Test
    void createInsertsWithDateAndReturnsRow() throws Exception {
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet keys = mock(ResultSet.class);
        LocalDate date = LocalDate.of(2026, 12, 24);

        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(statement);
        when(statement.getGeneratedKeys()).thenReturn(keys);
        when(keys.next()).thenReturn(true);
        when(keys.getInt(1)).thenReturn(5);

        try (MockedStatic<DataSource> ds = mockStatic(DataSource.class)) {
            ds.when(DataSource::getConnection).thenReturn(connection);

            ConciergeRequest saved = dao.create("Ada", "ada@x.com", "airport", date, "pickup");

            assertEquals(5, saved.getId());
            assertEquals("Ada", saved.getName());
            assertEquals("PENDING", saved.getStatus());
        }

        verify(statement).setString(1, "Ada");
        verify(statement).setDate(4, Date.valueOf(date));
        verify(statement).setString(5, "pickup");
    }

    @Test
    void createInsertsNullDateWhenAbsent() throws Exception {
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet keys = mock(ResultSet.class);

        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(statement);
        when(statement.getGeneratedKeys()).thenReturn(keys);
        when(keys.next()).thenReturn(true);
        when(keys.getInt(1)).thenReturn(1);

        try (MockedStatic<DataSource> ds = mockStatic(DataSource.class)) {
            ds.when(DataSource::getConnection).thenReturn(connection);

            dao.create("Ada", "ada@x.com", "other", null, "details");
        }

        verify(statement).setNull(4, Types.DATE);
    }

    @Test
    void findAllMapsRows() throws Exception {
        Connection connection = mock(Connection.class);
        Statement statement = mock(Statement.class);
        ResultSet rs = mock(ResultSet.class);

        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(rs);
        when(rs.next()).thenReturn(true, true, false);
        when(rs.getInt("id")).thenReturn(2, 1);
        when(rs.getString("name")).thenReturn("Ada", "Grace");
        when(rs.getString("email")).thenReturn("ada@x.com", "grace@x.com");
        when(rs.getString("type")).thenReturn("airport", "other");
        when(rs.getDate("request_date")).thenReturn(Date.valueOf("2026-01-05"), null);
        when(rs.getString("details")).thenReturn("pickup", "info");
        when(rs.getString("status")).thenReturn("PENDING", "PENDING");

        try (MockedStatic<DataSource> ds = mockStatic(DataSource.class)) {
            ds.when(DataSource::getConnection).thenReturn(connection);

            List<ConciergeRequest> results = dao.findAll();

            assertEquals(2, results.size());
            assertEquals("Ada", results.get(0).getName());
            assertEquals(LocalDate.of(2026, 1, 5), results.get(0).getRequestDate());
        }
    }
}
