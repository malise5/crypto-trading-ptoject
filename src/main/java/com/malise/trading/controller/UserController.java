package com.malise.trading.controller;

import com.malise.trading.ServiceImpl.UserServiceImpl;
import com.malise.trading.ServiceImpl.VerificationCodeServiceImpl;
import com.malise.trading.domain.VerificatonType;
import com.malise.trading.model.User;
import com.malise.trading.model.VerificationCode;
import com.malise.trading.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserServiceImpl userService;

    private VerificationCodeServiceImpl verificationCodeService;

    private EmailService emailService;


    @GetMapping("profile")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwtToken) {
        User user = userService.findUserProfileByJwt(jwtToken);
        if (user == null) {
            return ResponseEntity.status(404).body(null); // User not found
        }
        return ResponseEntity.ok(user); // Return user profile
    }

    @PatchMapping("verification/{verificationType}/send-otp")
    public ResponseEntity<String> sendVerificationOtp(@PathVariable VerificatonType verificationType, @RequestHeader("Authorization") String jwtToken) throws MessagingException {
        User user = userService.findUserProfileByJwt(jwtToken);
        if (user == null) {
            return ResponseEntity.status(404).body(null); // User not found
        }
        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUserId(user.getId());
        if (verificationCode == null) {
            verificationCode = verificationCodeService.sendVerificationCode(user, verificationType);
        } else {
            verificationCodeService.deleteVerificationCodeById(verificationCode);
            verificationCode = verificationCodeService.sendVerificationCode(user, verificationType);
        }
        if (verificationType.equals(VerificatonType.EMAIL)) {
            emailService.sendVerificationOtpEmail(user.getEmail(), verificationCode.getOtp());
        } else if (verificationType.equals(VerificatonType.MOBILE)) {
            // Assuming you have a method to send SMS
            // smsService.sendVerificationSms(user.getMobileNumber(), verificationCode.getOtp());
            log.info("SMS verification code sent to mobile number: {}", user.getMobileNumber());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Invalid verification type
        }

        return new ResponseEntity<>("Verification OTP sent", HttpStatus.OK);
    }

    //enable twofactor authentication
    @PatchMapping("enable-two-factor/verify-otp/{otp")
    public ResponseEntity<User> enableTwoFactorAuthentication(@RequestHeader("Authorization") String jwtToken, @PathVariable String otp ) {
        User user = userService.findUserProfileByJwt(jwtToken);
        if (user == null) {
            return ResponseEntity.status(404).body(null); // User not found
        }

        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUserId(user.getId());
        if (verificationCode == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // No verification code found
        }

        String sendTo = verificationCode.getVerificationType().equals(VerificatonType.EMAIL) ? user.getEmail() : user.getMobileNumber();

        boolean isVerified = verificationCode.getOtp().equals(otp);
        if (isVerified) {
            User updatedUser = userService.enableTwoFactorAuthentication(verificationCode.getVerificationType(), sendTo, user);

            verificationCodeService.deleteVerificationCodeById(verificationCode);

            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // OTP verification failed
    }
}
