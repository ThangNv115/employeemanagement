package com.example.employeemanagement.ultil;

import java.time.Duration;

public class Constants {
    // OTP Configuration
    public static final int OTP_LENGTH = 6;
    public static final Duration OTP_VALIDITY_DURATION = Duration.ofMinutes(5);
    public static final int MAX_OTP_ATTEMPTS = 3;

    // Session Configuration
    public static final Duration SESSION_TIMEOUT = Duration.ofMinutes(10);

    // Password Validation Regex
    public static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

    // URL Patterns
    public static final String ADMIN_URL_PATTERN = "/admin/**";
    public static final String IT_URL_PATTERN = "/it/**";
    public static final String ACCOUNTANT_URL_PATTERN = "/accountant/**";
    public static final String MARKETING_URL_PATTERN = "/marketing/**";

    private Constants() {
        // Private constructor to prevent instantiation
    }
}
