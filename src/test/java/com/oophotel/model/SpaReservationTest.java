package com.oophotel.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SpaReservationTest {

    @Test
    void gettersReturnConstructorArgs() {
        LocalDate date = LocalDate.of(2026, 9, 15);
        LocalTime time = LocalTime.of(11, 30);
        SpaReservation reservation = new SpaReservation(
                8, "Nora", "nora@example.com", "deep tissue",
                date, time, 1, "sensitive shoulders", "CONFIRMED");

        assertEquals(8, reservation.getId());
        assertEquals("Nora", reservation.getName());
        assertEquals("nora@example.com", reservation.getEmail());
        assertEquals("deep tissue", reservation.getTreatment());
        assertEquals(date, reservation.getDate());
        assertEquals(time, reservation.getTime());
        assertEquals(1, reservation.getGuests());
        assertEquals("sensitive shoulders", reservation.getNotes());
        assertEquals("CONFIRMED", reservation.getStatus());
    }
}
