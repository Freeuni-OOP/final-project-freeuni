package com.oophotel.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoomTest {

    @Test
    void gettersReturnConstructorArgs() {
        BigDecimal price = new BigDecimal("220.00");
        Room room = new Room(101, "Deluxe", "Deluxe Mountain View", "Stone floors, king bed.", price, 2);

        assertEquals(101, room.getId());
        assertEquals("Deluxe", room.getType());
        assertEquals("Deluxe Mountain View", room.getName());
        assertEquals("Stone floors, king bed.", room.getDescription());
        assertEquals(price, room.getPricePerNight());
        assertEquals(2, room.getCapacity());
    }
}
