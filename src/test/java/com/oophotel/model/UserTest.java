package com.oophotel.model;

import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserTest {

    @Test
    void defaultsAreEmpty() {
        User user = new User();
        assertEquals(0L, user.getId());
        assertNull(user.getFirstName());
        assertNull(user.getLastName());
        assertNull(user.getEmail());
        assertNull(user.getPasswordHash());
        assertNull(user.getDateOfBirth());
        assertNull(user.getRole());
        assertNull(user.getCreatedAt());
    }

    @Test
    void settersRoundTrip() {
        Date dob = Date.valueOf("1990-05-01");
        Timestamp created = Timestamp.valueOf("2026-01-15 10:00:00");
        User user = new User();

        user.setId(42L);
        user.setFirstName("Ada");
        user.setLastName("Lovelace");
        user.setEmail("ada@example.com");
        user.setPasswordHash("hash");
        user.setDateOfBirth(dob);
        user.setRole("USER");
        user.setCreatedAt(created);

        assertEquals(42L, user.getId());
        assertEquals("Ada", user.getFirstName());
        assertEquals("Lovelace", user.getLastName());
        assertEquals("ada@example.com", user.getEmail());
        assertEquals("hash", user.getPasswordHash());
        assertEquals(dob, user.getDateOfBirth());
        assertEquals("USER", user.getRole());
        assertEquals(created, user.getCreatedAt());
    }
}
