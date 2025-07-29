package com.malise.trading.dto;

import com.malise.trading.domain.USER_ROLE;
import com.malise.trading.model.TwoFactorAuth;
import lombok.Data;

@Data
public class AuthResponse {
    private String jwt;
    private boolean status;
    private String message;
    private boolean isTwoFactorEnabled;
    private String session;
}
