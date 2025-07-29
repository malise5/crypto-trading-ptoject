package com.malise.trading.dto;

import com.malise.trading.model.TwoFactorAuth;
import lombok.Data;

@Data
public class UserResponseDTO {
    private Long id;
    private String fullname;
    private String email;
    private String role;
    private TwoFactorAuth twoFactorAuth = new TwoFactorAuth();

}
