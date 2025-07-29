package com.malise.trading.model;

import com.malise.trading.domain.VerificatonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TwoFactorAuth {
    private boolean isEnabled = false;
    private VerificatonType sendTo;
}
