package com.oophotel.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PoolReservationTest {

    @Test
    void gettersReturnConstructorArgs() {
        LocalDate date = LocalDate.of(2026, 8, 1);
        LocalTime time = LocalTime.of(14, 0);
        PoolReservation reservation = new PoolReservation(
                6, "Ivan", "ivan@example.com", "outdoor", "swim lesson",
                date, time, 2, "beginner", "CONFIRMED");

        assertEquals(6, reservation.getId());
        assertEquals("Ivan", reservation.getName());
        assertEquals("ivan@example.com", reservation.getEmail());
        assertEquals("outdoor", reservation.getPool());
        assertEquals("swim lesson", reservation.getActivity());
        assertEquals(date, reservation.getDate());
        assertEquals(time, reservation.getTime());
        assertEquals(2, reservation.getGuests());
        assertEquals("beginner", reservation.getNotes());
        assertEquals("CONFIRMED", reservation.getStatus());
    }
}
