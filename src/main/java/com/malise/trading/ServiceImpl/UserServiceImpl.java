package com.malise.trading.ServiceImpl;

import com.malise.trading.config.JwtProvider;
import com.malise.trading.dto.AuthResponse;
import com.malise.trading.dto.CreateUserDTO;
import com.malise.trading.model.User;
import com.malise.trading.repository.UserRepository;
import com.malise.trading.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public AuthResponse createUser(CreateUserDTO dto) {

        // Check if email already exists
        if (userRepository.findByEmail(dto.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }

        // Create user entity
        User user = new User();
        user.setFullname(dto.getFullname());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword()); // ⚠️ Make sure to hash the password in production
        user.setRole(dto.getRole());

        // Save to DB
        User savedUser = userRepository.save(user);

        // Authenticate the user manually
        Authentication auth = new UsernamePasswordAuthenticationToken(
                savedUser.getEmail(),
                savedUser.getPassword()
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Generate JWT
        String jwt = JwtProvider.generateToken(auth);

        // Build response
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setStatus(true);
        authResponse.setMessage("User registered successfully");

        return authResponse;
    }
}
