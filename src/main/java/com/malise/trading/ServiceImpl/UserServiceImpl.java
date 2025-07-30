package com.malise.trading.ServiceImpl;

import com.malise.trading.config.JwtProvider;
import com.malise.trading.domain.VerificatonType;
import com.malise.trading.dto.AuthResponse;
import com.malise.trading.dto.CreateUserDTO;
import com.malise.trading.dto.UserResponseDTO;
import com.malise.trading.model.TwoFactorAuth;
import com.malise.trading.model.TwoFactorOTP;
import com.malise.trading.model.User;
import com.malise.trading.repository.UserRepository;
import com.malise.trading.service.EmailService;
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
    private final EmailService emailService;

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
        user.setMobileNumber(dto.getMobileNumber());
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

            try {
                emailService.sendVerificationOtpEmail(user.getEmail(), otp);
            } catch (Exception e) {
                throw new RuntimeException("Failed to send OTP email", e);
            }

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
    public AuthResponse verifyOtp(String otp, String id) {
        TwoFactorOTP twoFactorOTP = twoFactorOtpService.getTwoFactorOTPById(id);
        if (twoFactorOTP == null) {
            throw new RuntimeException("Invalid OTP session ID");
        }
        if (!twoFactorOTP.getOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }
        if (twoFactorOtpService.verifyTwoFactorOTP(twoFactorOTP, otp)) {
            AuthResponse authResponse = new AuthResponse();
            authResponse.setMessage("OTP verified successfully");
            authResponse.setTwoFactorEnabled(true);
            authResponse.setJwt( twoFactorOTP.getJwtToken());
            return authResponse;
        }
        throw new RuntimeException("OTP verification failed");
    }

    @Override
    public User findUserProfileByJwt(String jwt) {
        String email = JwtProvider.getEmailFromToken(jwt);
        if (email == null) {
            throw new RuntimeException("Invalid JWT token");
        }
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return user;
    }

    @Override
    public User findUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found with email: " + email);
        }
        return user;
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

    }

    @Override
    public User enableTwoFactorAuthentication(VerificatonType verificatonType,String sendTo, User user) {
        TwoFactorAuth twoFactorAuth = user.getTwoFactorAuth();
        twoFactorAuth.setEnabled(true);
        twoFactorAuth.setSendTo(verificatonType);
        user.setTwoFactorAuth(twoFactorAuth);
        return userRepository.save(user);

    }

    @Override
    public User updatePassword(User user, String newPassword) {
        // Update the user's password
        user.setPassword(newPassword); // ⚠️ Make sure to hash the password in production
        return userRepository.save(user);
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
