package com.malise.trading.dto;

import com.malise.trading.domain.USER_ROLE;
import lombok.Data;

@Data
public class CreateUserDTO {
    private Long id;
    private String fullname;
    private String email;
    private String password;
    private USER_ROLE role = USER_ROLE.ROLE_CUSTOMER;

}
