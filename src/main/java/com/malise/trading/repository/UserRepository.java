package com.malise.trading.repository;

import com.malise.trading.dto.CreateUserDTO;
import com.malise.trading.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
