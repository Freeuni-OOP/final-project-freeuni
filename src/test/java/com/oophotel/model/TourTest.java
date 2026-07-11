package com.oophotel.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TourTest {

    @Test
    void defaultsAreEmpty() {
        Tour tour = new Tour();
        assertEquals(0, tour.getId());
        assertNull(tour.getName());
        assertNull(tour.getDescription());
        assertEquals(0, tour.getMaxGuests());
    }

    @Test
    void constructorPopulatesFields() {
        Tour tour = new Tour(3, "Tusheti", "3 day mountain trip", 10);

        assertEquals(3, tour.getId());
        assertEquals("Tusheti", tour.getName());
        assertEquals("3 day mountain trip", tour.getDescription());
        assertEquals(10, tour.getMaxGuests());
    }

    @Test
    void settersRoundTrip() {
        Tour tour = new Tour();
        tour.setId(5);
        tour.setName("Mtatsminda");
        tour.setDescription("Half day");
        tour.setMaxGuests(30);

        assertEquals(5, tour.getId());
        assertEquals("Mtatsminda", tour.getName());
        assertEquals("Half day", tour.getDescription());
        assertEquals(30, tour.getMaxGuests());
    }
}
