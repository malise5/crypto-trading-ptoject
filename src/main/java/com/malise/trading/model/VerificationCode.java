package com.malise.trading.model;

import com.malise.trading.domain.VerificatonType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class VerificationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String otp; // One-Time Password

    @OneToOne
    private User user;

    private String email; // Email associated with the verification code

    private String mobileNumber; // Mobile number associated with the verification code

    private VerificatonType verificationType; // Type of verification (e.g., EMAIL, SMS)
}
