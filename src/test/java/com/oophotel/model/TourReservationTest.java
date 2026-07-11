package com.oophotel.model;

import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TourReservationTest {

    @Test
    void defaultsAreEmpty() {
        TourReservation reservation = new TourReservation();
        assertEquals(0, reservation.getId());
        assertEquals(0, reservation.getUserId());
        assertEquals(0, reservation.getTourId());
        assertNull(reservation.getFullName());
        assertNull(reservation.getEmail());
        assertEquals(0, reservation.getGuests());
        assertNull(reservation.getTourDate());
        assertNull(reservation.getSpecialRequests());
    }

    @Test
    void constructorPopulatesFields() {
        Date date = Date.valueOf("2026-08-15");
        TourReservation reservation = new TourReservation(
                42, 3, "Jane Doe", "jane@example.com", 4, date, "vegan lunch");

        assertEquals(42, reservation.getUserId());
        assertEquals(3, reservation.getTourId());
        assertEquals("Jane Doe", reservation.getFullName());
        assertEquals("jane@example.com", reservation.getEmail());
        assertEquals(4, reservation.getGuests());
        assertEquals(date, reservation.getTourDate());
        assertEquals("vegan lunch", reservation.getSpecialRequests());
    }

    @Test
    void settersRoundTrip() {
        Date date = Date.valueOf("2026-09-01");
        TourReservation reservation = new TourReservation();

        reservation.setId(11);
        reservation.setUserId(2);
        reservation.setTourId(5);
        reservation.setFullName("John");
        reservation.setEmail("john@example.com");
        reservation.setGuests(2);
        reservation.setTourDate(date);
        reservation.setSpecialRequests(null);

        assertEquals(11, reservation.getId());
        assertEquals(2, reservation.getUserId());
        assertEquals(5, reservation.getTourId());
        assertEquals("John", reservation.getFullName());
        assertEquals("john@example.com", reservation.getEmail());
        assertEquals(2, reservation.getGuests());
        assertEquals(date, reservation.getTourDate());
        assertNull(reservation.getSpecialRequests());
    }
}
