package com.malise.trading.utils;

public class OtpUtil {
    public static String generateOtp() {
        // Simple OTP generation logic (for demonstration purposes)
        int otp = (int) (Math.random() * 900000) + 100000; // Generates a 6-digit OTP
        return String.valueOf(otp);
    }

    public static boolean isValidOtp(String otp) {
        // Check if the OTP is a valid 6-digit number
        return otp != null && otp.matches("\\d{6}");
    }
}
