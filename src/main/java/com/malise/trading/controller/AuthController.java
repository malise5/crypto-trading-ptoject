package com.malise.trading.controller;

import com.malise.trading.ServiceImpl.UserServiceImpl;
import com.malise.trading.dto.CreateUserDTO;
import com.malise.trading.dto.UserResponseDTO;
import com.malise.trading.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserServiceImpl userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody CreateUserDTO userDto) {
        UserResponseDTO userResponse = userService.createUser(userDto);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }
}
