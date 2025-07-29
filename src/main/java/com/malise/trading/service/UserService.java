package com.malise.trading.service;

import com.malise.trading.dto.CreateUserDTO;
import com.malise.trading.dto.UserResponseDTO;
import com.malise.trading.model.User;

public interface UserService {

    UserResponseDTO createUser(CreateUserDTO dto);

}
