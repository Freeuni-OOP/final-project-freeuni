package com.oophotel.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DiningReservationTest {

    @Test
    void gettersReturnConstructorArgs() {
        LocalDate date = LocalDate.of(2026, 12, 1);
        LocalTime time = LocalTime.of(19, 30);
        DiningReservation reservation = new DiningReservation(
                4, "John Doe", "3", date, time, "birthday", "CONFIRMED");

        assertEquals(4, reservation.getId());
        assertEquals("John Doe", reservation.getName());
        assertEquals("3", reservation.getGuests());
        assertEquals(date, reservation.getDate());
        assertEquals(time, reservation.getTime());
        assertEquals("birthday", reservation.getNotes());
        assertEquals("CONFIRMED", reservation.getStatus());
    }
}
