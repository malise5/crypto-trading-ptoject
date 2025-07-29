package com.malise.trading.controller;

import com.malise.trading.ServiceImpl.UserServiceImpl;
import com.malise.trading.dto.AuthResponse;
import com.malise.trading.dto.CreateUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserServiceImpl userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody CreateUserDTO userDto) {
        AuthResponse response = userService.createUser(userDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
