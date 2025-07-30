package com.malise.trading.ServiceImpl;

import com.malise.trading.domain.VerificatonType;
import com.malise.trading.model.User;
import com.malise.trading.model.VerificationCode;
import com.malise.trading.repository.VerificationCodeRepository;
import com.malise.trading.service.VerificationCodeService;
import com.malise.trading.utils.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationCodeServiceImpl implements VerificationCodeService {

    private final VerificationCodeRepository verificationCodeRepository;
    @Override
    public VerificationCode sendVerificationCode(User user, VerificatonType verificationType) {
        VerificationCode verificationCodeSaved = new VerificationCode();
        verificationCodeSaved.setOtp(OtpUtil.generateOtp());
        verificationCodeSaved.setVerificationType(verificationType);
        verificationCodeSaved.setUser(user);

        return verificationCodeRepository.save(verificationCodeSaved);
    }

    @Override
    public VerificationCode getVerificationCodeById(Long id) {
        return verificationCodeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Verification code not found with id: " + id));

    }

    @Override
    public VerificationCode getVerificationCodeByUserId(Long userId) {
        return verificationCodeRepository.findByUserId(userId);
    }

    @Override
    public void deleteVerificationCodeById(VerificationCode verificationCode) {
        if (verificationCode == null) {
            throw new RuntimeException("Verification code cannot be null");
        }
        verificationCodeRepository.delete(verificationCode);
    }
}
