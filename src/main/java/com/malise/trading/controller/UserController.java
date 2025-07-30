package com.malise.trading.controller;

import com.malise.trading.ServiceImpl.UserServiceImpl;
import com.malise.trading.model.User;
import com.malise.trading.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

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
    public ResponseEntity<User> sendVerificationOtp(@PathVariable String verificationType, @RequestHeader("Authorization") String jwtToken) {
        User user = userService.findUserProfileByJwt(jwtToken);
        if (user == null) {
            return ResponseEntity.status(404).body(null); // User not found
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    //enable twofactor authentication
    @PatchMapping("enable-two-factor/verify-otp")
    public ResponseEntity<User> enableTwoFactorAuthentication(@RequestHeader("Authorization") String jwtToken) {
        User user = userService.findUserProfileByJwt(jwtToken);
        if (user == null) {
            return ResponseEntity.status(404).body(null); // User not found
        }
        return ResponseEntity.ok(user); // Return updated user profile
    }
}
