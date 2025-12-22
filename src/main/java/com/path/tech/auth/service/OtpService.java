package com.path.tech.auth.service;

import com.path.tech.auth.dto.OtpRequest;
import com.path.tech.auth.model.OtpEntry;
import com.path.tech.auth.repository.OtpRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;

@Slf4j
@Service
public class OtpService {
    @Autowired
    private OtpRepository otpRepository;

    @Value("${app.auth.otp.expirySec}")
    private String otpExpirySec;

    public OtpEntry generateOtp(String phoneNumber) {
        SecureRandom secureRandom = null;
        try {
            secureRandom = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        int randomInteger = secureRandom.nextInt(1000000);
        String otp = String.format("%06d", randomInteger);
        OtpEntry otpEntry = new OtpEntry();
        otpEntry.setPhoneNumber(phoneNumber);
        otpEntry.setOtp(otp);
        otpEntry.setCreatedAt(Instant.now());

        log.info("OTP :{}",otp);
        return otpRepository.save(otpEntry);
    }

    public boolean isOtpValid(OtpRequest otpRequest) {
        OtpEntry otpEntry = otpRepository.findByPhoneNumber(otpRequest.getPhoneNumber());
        // otp not found
        if(otpEntry==null)
            return false;
        // otp already verified
        if(otpEntry.isVerified())
            return false;
        Instant now = Instant.now();
        // otp expired
        if(now.isAfter(otpEntry.getCreatedAt().plusSeconds(Long.parseLong(otpExpirySec)))){
            return false;
        }
        if (!otpEntry.getOtp().equals(otpRequest.getOtp())) {
            return false; // Incorrect OTP
        }

        // Mark OTP as used
        otpEntry.setVerified(true);
        otpRepository.save(otpEntry);

        return true;
    }
}
