package com.oophotel.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConciergeRequestTest {

    @Test
    void gettersReturnConstructorArgs() {
        LocalDate date = LocalDate.of(2026, 7, 20);
        ConciergeRequest request = new ConciergeRequest(
                9, "Alice", "alice@example.com", "restaurant", date, "table for four", "PENDING");

        assertEquals(9, request.getId());
        assertEquals("Alice", request.getName());
        assertEquals("alice@example.com", request.getEmail());
        assertEquals("restaurant", request.getType());
        assertEquals(date, request.getRequestDate());
        assertEquals("table for four", request.getDetails());
        assertEquals("PENDING", request.getStatus());
    }
}
