package com.oophotel.model;

import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.Time;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class BarTableReservationTest {

    @Test
    void defaultsAreEmpty() {
        BarTableReservation reservation = new BarTableReservation();
        assertEquals(0, reservation.getId());
        assertEquals(0, reservation.getUserId());
        assertNull(reservation.getFullName());
        assertEquals(0, reservation.getGuests());
        assertNull(reservation.getReservationDate());
        assertNull(reservation.getReservationTime());
        assertNull(reservation.getNotes());
    }

    @Test
    void constructorPopulatesFields() {
        Date date = Date.valueOf("2026-10-10");
        Time time = Time.valueOf("21:00:00");
        BarTableReservation reservation = new BarTableReservation(
                7, "Grace", 2, date, time, "quiet corner");

        assertEquals(7, reservation.getUserId());
        assertEquals("Grace", reservation.getFullName());
        assertEquals(2, reservation.getGuests());
        assertEquals(date, reservation.getReservationDate());
        assertEquals(time, reservation.getReservationTime());
        assertEquals("quiet corner", reservation.getNotes());
    }

    @Test
    void settersRoundTrip() {
        BarTableReservation reservation = new BarTableReservation();
        Date date = Date.valueOf("2026-11-11");
        Time time = Time.valueOf("20:00:00");

        reservation.setId(3);
        reservation.setUserId(11);
        reservation.setFullName("Kim");
        reservation.setGuests(4);
        reservation.setReservationDate(date);
        reservation.setReservationTime(time);
        reservation.setNotes(null);

        assertEquals(3, reservation.getId());
        assertEquals(11, reservation.getUserId());
        assertEquals("Kim", reservation.getFullName());
        assertEquals(4, reservation.getGuests());
        assertEquals(date, reservation.getReservationDate());
        assertEquals(time, reservation.getReservationTime());
        assertNull(reservation.getNotes());
    }
}
