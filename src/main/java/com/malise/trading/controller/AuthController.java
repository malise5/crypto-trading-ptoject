package com.malise.trading.controller;

//import com.malise.trading.ServiceImpl.UserServiceImpl;
import com.malise.trading.ServiceImpl.UserServiceImpl;
import com.malise.trading.config.JwtProvider;
import com.malise.trading.dto.AuthResponse;
import com.malise.trading.dto.CreateUserDTO;
import com.malise.trading.model.User;
import com.malise.trading.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserServiceImpl userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> createUser(@RequestBody CreateUserDTO userDto) {
        // Create user
        AuthResponse authResponse = userService.createUser(userDto);

        // Return response
        return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);

    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody CreateUserDTO userDto) {
        // Login user
        AuthResponse authResponse = userService.loginUser(userDto.getEmail(), userDto.getPassword());
        // Return response
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<AuthResponse> verifyOtp(@PathVariable String otp, @RequestParam String Id) {
        // Verify OTP
        AuthResponse authResponse = userService.verifyOtp(otp, Id);

        // Return response
        return ResponseEntity.ok(authResponse);
    }
}
