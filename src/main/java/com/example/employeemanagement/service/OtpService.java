package com.example.employeemanagement.service;


import com.example.employeemanagement.entity.User;
import com.example.employeemanagement.repository.UserRepository;
import com.example.employeemanagement.ultil.Constants;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final Random random = new Random();

    @Transactional
    public void generateAndSendOtp(User user) {
        String otp = generateOtp();
        LocalDateTime expireTime = LocalDateTime.now().plus(Constants.OTP_VALIDITY_DURATION);

        userRepository.updateUserOtp(user.getId(), otp, expireTime);
        emailService.sendOtpEmail(user.getEmail(), otp);
    }

    @Transactional
    public boolean validateOtp(User user, String otp) {
        if (user.getOtp() == null || user.getOtpExpireTime() == null) {
            return false;
        }

        if (LocalDateTime.now().isAfter(user.getOtpExpireTime())) {
            return false;
        }

        if (!user.getOtp().equals(otp)) {
            userRepository.incrementFailedOtpAttempts(user.getId());
            return false;
        }

        return true;
    }

    private String generateOtp() {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < Constants.OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

}
