package com.path.tech.auth.service;

import com.path.tech.auth.dto.*;
import com.path.tech.auth.model.OtpEntry;
import com.path.tech.auth.model.UserProfile;
import com.path.tech.auth.security.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService{

    private final UserService userService;
    private final OtpService otpService;
    private final TokenService tokenService;
    private final String otpExpirySec;

    public AuthServiceImpl(UserService userService, OtpService otpService,  TokenService tokenService,
                           @Value("${app.auth.otp.expirySec}") String otpExpirySec) {
        this.userService = userService;
        this.otpService = otpService;
        this.tokenService = tokenService;
        this.otpExpirySec = otpExpirySec;
    }

    @Override
    public Mono<UserSignupResponse> userSignup(UserSignupRequest userSignupRequest) {
        // Store the user details to db
        UserProfile profile = userService.save(userSignupRequest);

        OtpResponse otp = requestOtp(profile.getPhoneNumber());
        return Mono.just(UserSignupResponse.builder()
                .userId(profile.getId())
                .phoneNumber(profile.getPhoneNumber())
                .otpSent(true)
                .otpExpirySeconds(otp.getOtpExpirySeconds())
                .status(AuthStatus.PENDING_VERIFICATION)
                .message("OTP sent to phone number. Please verify to complete signup.")
                .createdDateTime(LocalDateTime.now())
                .updatedDateTime(LocalDateTime.now())
                .build());

    }
    @Override
    public OtpResponse requestOtp(String phoneNumber) {
        OtpEntry entry = otpService.generateOtp(phoneNumber);
        // Persist to db for verification and tracking
        OtpResponse response = new OtpResponse();
        response.setOtpSent(true);
        response.setOtpExpirySeconds(Integer.parseInt(otpExpirySec));
        response.setPhoneNumber(entry.getPhoneNumber());
        response.setStatus(AuthStatus.OTP_SENT);
        response.setMessage("OTP sent to phone number.");
        response.setCreatedDateTime(LocalDateTime.now());
        response.setUpdatedDateTime(LocalDateTime.now());
        return response;
    }

    @Override
    public Mono<Object> verifyOtp(OtpRequest otpRequest) {
        if(!otpService.isOtpValid(otpRequest))
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED,"OTP Verification Failed!"));

        UUID uid =userService.getUserIdByPhoneNumber(otpRequest.getPhoneNumber());
        String token = tokenService.generateAccessToken(otpRequest.getPhoneNumber(),uid.toString(), List.of(UserRole.CUSTOMER.name()),null);
        String refreshToken = tokenService.generateRefreshToken(otpRequest.getPhoneNumber(),uid.toString(),null);

        AuthTokenResponse response = new AuthTokenResponse();
        response.setStatus(AuthStatus.OTP_VERIFIED);
        response.setMessage("OTP Verified Successfully!");
        response.setAccessToken(token);
        response.setRefreshToken(refreshToken);
        response.setExpiresIn(Long.parseLong(otpExpirySec));
        response.setTokenType("Bearer");
        response.setUserId(uid);
        response.setCreatedDateTime(LocalDateTime.now());
        response.setUpdatedDateTime(LocalDateTime.now());
        return Mono.just(response);
    }


}
