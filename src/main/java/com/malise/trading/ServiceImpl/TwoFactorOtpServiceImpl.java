package com.malise.trading.ServiceImpl;

import com.malise.trading.model.TwoFactorOTP;
import com.malise.trading.model.User;
import com.malise.trading.repository.TwoFactorOtpRepository;
import com.malise.trading.service.TwoFactorOtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TwoFactorOtpServiceImpl implements TwoFactorOtpService {

    private final TwoFactorOtpRepository twoFactorOtpRepository;
    @Override
    public TwoFactorOTP createTwoFactorOTP(User user, String otp, String jwtToken) {
        UUID uuId = UUID.randomUUID();

        String id = uuId.toString();

        TwoFactorOTP twoFactorOTP = new TwoFactorOTP();
        twoFactorOTP.setId(id);
        twoFactorOTP.setOtp(otp);
        twoFactorOTP.setUser(user);
        twoFactorOTP.setJwtToken(jwtToken);
        return twoFactorOtpRepository.save(twoFactorOTP);
    }

    @Override
    public TwoFactorOTP getTwoFactorOTPByUser(User user) {

        return twoFactorOtpRepository.findByUserId(user.getId());
    }

    @Override
    public TwoFactorOTP getTwoFactorOTPById(String id) {
        return twoFactorOtpRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TwoFactorOTP not found with id: " + id));
    }

    @Override
    public boolean verifyTwoFactorOTP(TwoFactorOTP twoFactorOTP, String otp) {
        if (twoFactorOTP == null) {
            throw new RuntimeException("TwoFactorOTP cannot be null");
        }
        return twoFactorOTP.getOtp().equals(otp);
    }

    @Override
    public void deleteTwoFactorOTP(TwoFactorOTP twoFactorOTP) {
        if (twoFactorOTP == null) {
            throw new RuntimeException("TwoFactorOTP cannot be null");
        }
        twoFactorOtpRepository.delete(twoFactorOTP);
    }
}
