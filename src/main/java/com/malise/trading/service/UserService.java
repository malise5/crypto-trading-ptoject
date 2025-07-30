package com.malise.trading.service;

import com.malise.trading.domain.VerificatonType;
import com.malise.trading.dto.AuthResponse;
import com.malise.trading.dto.CreateUserDTO;
import com.malise.trading.dto.UserResponseDTO;
import com.malise.trading.model.User;

public interface UserService {

    AuthResponse createUser(CreateUserDTO dto);

    //LOGIN METHOD
    AuthResponse loginUser(String email, String password);

    AuthResponse verifyOtp(String otp, String id);

    public User findUserProfileByJwt(String jwt);

    public User findUserByEmail(String email);

    public User findUserById(Long id);

    public User enableTwoFactorAuthentication(VerificatonType  verificatonType,String sendTo, User user);

    User updatePassword(User user, String newPassword);


}
