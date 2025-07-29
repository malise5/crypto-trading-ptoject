package com.malise.trading.service;

import com.malise.trading.dto.AuthResponse;
import com.malise.trading.model.TwoFactorOTP;
import com.malise.trading.model.User;

public interface TwoFactorOtpService {

    TwoFactorOTP createTwoFactorOTP(User user, String otp, String jwtToken);

    TwoFactorOTP getTwoFactorOTPByUser(User user);

    TwoFactorOTP getTwoFactorOTPById(String id);

    boolean verifyTwoFactorOTP(TwoFactorOTP twoFactorOTP, String otp);

    void deleteTwoFactorOTP(TwoFactorOTP twoFactorOTP);
}
