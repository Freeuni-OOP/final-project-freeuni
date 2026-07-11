package com.oophotel.util;

import org.mindrot.jbcrypt.BCrypt;

public final class PasswordUtil {

    public static final int MIN_LENGTH = 8;

    private PasswordUtil() {}

    public static String hash(String plaintext) {
        return BCrypt.hashpw(plaintext, BCrypt.gensalt(12));
    }

    public static boolean verify(String plaintext, String hashed) {
        return BCrypt.checkpw(plaintext, hashed);
    }

    public static boolean isStrong(String password) {
        if (password == null || password.length() < MIN_LENGTH) return false;
        boolean hasLetter = false, hasDigit = false;
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isLetter(c)) hasLetter = true;
            else if (Character.isDigit(c)) hasDigit = true;
            if (hasLetter && hasDigit) return true;
        }
        return false;
    }
}
