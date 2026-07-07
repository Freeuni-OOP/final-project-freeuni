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
        return password != null && password.length() >= MIN_LENGTH;
    }
}
