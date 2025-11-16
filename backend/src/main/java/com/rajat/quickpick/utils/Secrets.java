package com.rajat.quickpick.utils;

import io.github.cdimascio.dotenv.Dotenv;

public class Secrets {

    public static final long JWT_EXPIRATION = 86400000;
    public static final long JWT_REFRESH_EXPIRATION = 604800000;
    public static final long EMAIL_VERIFICATION_TOKEN_EXPIRATION = 24 * 60 * 60 * 1000;
    public static final long PASSWORD_RESET_TOKEN_EXPIRATION = 60 * 60 * 1000;
    private static final Dotenv dotenv = Dotenv.load();
    public static final String MONGODB_URI = dotenv.get("MONGODB_URI");
    public static final String JWT_SECRET = dotenv.get("APP_JWT_SECRET") != null
            ? dotenv.get("APP_JWT_SECRET")
            : "quickpick-jwt-secret-key-2024-secure-random-string";

    private Secrets() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}