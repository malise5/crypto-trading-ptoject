package com.malise.trading.ServiceImpl;

import com.malise.trading.config.JwtProvider;
import com.malise.trading.dto.AuthResponse;
import com.malise.trading.dto.CreateUserDTO;
import com.malise.trading.dto.UserResponseDTO;
import com.malise.trading.model.TwoFactorOTP;
import com.malise.trading.model.User;
import com.malise.trading.repository.UserRepository;
import com.malise.trading.service.TwoFactorOtpService;
import com.malise.trading.service.UserService;
import com.malise.trading.utils.OtpUtil;
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
    private final TwoFactorOtpService twoFactorOtpService;

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
        AuthResponse authResponse = getAuthResponse(savedUser);
        authResponse.setMessage("User registered successfully");

        return authResponse;
    }

    @Override
    public AuthResponse loginUser(String email, String password) {
        // Find user by email
        User user = userRepository.findByEmail(email);
        if (user == null || !user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid email or password");
        }

        if (user.getTwoFactorAuth().isEnabled()){
            AuthResponse authResponse = new AuthResponse();
//            authResponse.setMessage("Two factor authentication is enabled for this user. Please verify your OTP.");
//            authResponse.setTwoFactorEnabled(true);

            String otp = OtpUtil.generateOtp();

            TwoFactorOTP oldTwoFactorOTP = twoFactorOtpService.getTwoFactorOTPByUser(user);
            if (oldTwoFactorOTP != null) {
                twoFactorOtpService.deleteTwoFactorOTP(oldTwoFactorOTP);
            }

            TwoFactorOTP twoFactorOTP = twoFactorOtpService.createTwoFactorOTP(user, otp, null);

            // ✅ Return early with 2FA prompt
            authResponse.setMessage("Two factor authentication is enabled for this user. Please verify your OTP.");
            authResponse.setTwoFactorEnabled(true);
            authResponse.setSession(twoFactorOTP.getId());
            return authResponse;
        }

        AuthResponse authResponse = getAuthResponse(user);
        authResponse.setMessage("Login successful");

        return authResponse;
    }

    @Override
    public AuthResponse verifyOtp(String email, String otp) {
        return null;
    }

    private static AuthResponse getAuthResponse(User user) {
        // Authenticate the user
        Authentication auth = new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                user.getPassword()
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Generate JWT
        String jwt = JwtProvider.generateToken(auth);

        // Build response
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setStatus(true);
        return authResponse;
    }
}
