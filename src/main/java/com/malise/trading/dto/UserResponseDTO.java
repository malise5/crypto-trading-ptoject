package com.malise.trading.dto;

import com.malise.trading.domain.USER_ROLE;
import com.malise.trading.model.TwoFactorAuth;
import lombok.Data;

@Data
public class UserResponseDTO {
    private Long id;
    private String fullname;
    private String email;
    private USER_ROLE role;
    private TwoFactorAuth twoFactorAuth;

}
