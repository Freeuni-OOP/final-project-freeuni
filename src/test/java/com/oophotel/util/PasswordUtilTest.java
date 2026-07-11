package com.oophotel.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordUtilTest {

    @Test
    void hashDiffersFromPlaintext() {
        String plaintext = "hunter2A";
        String hashed = PasswordUtil.hash(plaintext);

        assertNotEquals(plaintext, hashed);
        assertTrue(hashed.startsWith("$2"));
    }

    @Test
    void verifyMatchesHash() {
        String plaintext = "hunter2A";
        String hashed = PasswordUtil.hash(plaintext);

        assertTrue(PasswordUtil.verify(plaintext, hashed));
        assertFalse(PasswordUtil.verify("wrong-password9", hashed));
    }

    @Test
    void isStrongRejectsNull() {
        assertFalse(PasswordUtil.isStrong(null));
    }

    @Test
    void isStrongRejectsShort() {
        assertFalse(PasswordUtil.isStrong("ab1"));
        assertFalse(PasswordUtil.isStrong("abc12"));
    }

    @Test
    void isStrongRejectsLettersOnly() {
        assertFalse(PasswordUtil.isStrong("abcdefgh"));
    }

    @Test
    void isStrongRejectsDigitsOnly() {
        assertFalse(PasswordUtil.isStrong("12345678"));
    }

    @Test
    void isStrongAcceptsLetterAndDigit() {
        assertTrue(PasswordUtil.isStrong("hunter2A"));
        assertTrue(PasswordUtil.isStrong("password1"));
        assertTrue(PasswordUtil.isStrong("1passwor"));
    }
}
