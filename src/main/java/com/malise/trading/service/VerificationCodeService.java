package com.malise.trading.service;

import com.malise.trading.domain.VerificatonType;
import com.malise.trading.model.User;
import com.malise.trading.model.VerificationCode;

public interface VerificationCodeService {

    VerificationCode sendVerificationCode(User user, VerificatonType verificationType);

    VerificationCode getVerificationCodeById(Long id);

    VerificationCode getVerificationCodeByUserId(Long userId);

    void deleteVerificationCodeById(VerificationCode verificationCode);
}
