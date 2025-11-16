package com.rajat.quickpick.utils;

public class Secrets {

    public static final long JWT_EXPIRATION = 86400000;
    public static final long JWT_REFRESH_EXPIRATION = 604800000;
    public static final long EMAIL_VERIFICATION_TOKEN_EXPIRATION = 24 * 60 * 60 * 1000;
    public static final long PASSWORD_RESET_TOKEN_EXPIRATION = 60 * 60 * 1000;

    public static final String MONGODB_URI = System.getenv("MONGODB_URI");
    public static final String JWT_SECRET = System.getenv("APP_JWT_SECRET") != null
            ? System.getenv("APP_JWT_SECRET")
            : "quickpick-local-dev-secret";

    private Secrets() {
        throw new UnsupportedOperationException("Utility class");
    }
}