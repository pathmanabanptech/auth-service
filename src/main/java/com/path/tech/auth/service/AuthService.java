package com.path.tech.auth.service;

import com.path.tech.auth.dto.*;
import reactor.core.publisher.Mono;

public interface AuthService {

    Mono<UserSignupResponse> userSignup(UserSignupRequest userSignupRequest);
    OtpResponse requestOtp(String phoneNumber);
    Mono<Object> verifyOtp(OtpRequest otpRequest);
   RefreshTokenResponse refreshToken(String refreshToken);
    void logout(RefreshTokenRequest request);
}
