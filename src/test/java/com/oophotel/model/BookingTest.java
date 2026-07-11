package com.oophotel.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookingTest {

    @Test
    void gettersReturnConstructorArgs() {
        LocalDate checkIn = LocalDate.of(2026, 6, 1);
        LocalDate checkOut = LocalDate.of(2026, 6, 5);
        Booking booking = new Booking(7, 101, 42, checkIn, checkOut, "CONFIRMED");

        assertEquals(7, booking.getId());
        assertEquals(101, booking.getRoomId());
        assertEquals(42, booking.getUserId());
        assertEquals(checkIn, booking.getCheckIn());
        assertEquals(checkOut, booking.getCheckOut());
        assertEquals("CONFIRMED", booking.getStatus());
    }
}
