//package com.malise.trading.ServiceImpl;
//
//import com.malise.trading.dto.CreateUserDTO;
//import com.malise.trading.dto.UserResponseDTO;
//import com.malise.trading.model.User;
//import com.malise.trading.repository.UserRepository;
//import com.malise.trading.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class UserServiceImpl implements UserService {
//
//    private final UserRepository userRepository;
//
//    @Override
//    public UserResponseDTO createUser(CreateUserDTO dto) {
//
////        if(userRepository.findById(dto.getEmail()).isPresent()) {
////            throw new IllegalArgumentException("User with this email already exists");
////        }
//        User user = new User();
//        user.setFullname(dto.getFullname());
//        user.setEmail(dto.getEmail());
//        user.setPassword(dto.getPassword()); // Ensure to hash the password in a real application
//
//        User savedUser = userRepository.save(user);
//
//        UserResponseDTO responseDTO = new UserResponseDTO();
//        responseDTO.setId(savedUser.getId());
//        responseDTO.setFullname(savedUser.getFullname());
//        responseDTO.setEmail(savedUser.getEmail());
//        responseDTO.setRole(savedUser.getRole());
//        responseDTO.setTwoFactorAuth(savedUser.getTwoFactorAuth());
//
//        return responseDTO;
//    }
//}
