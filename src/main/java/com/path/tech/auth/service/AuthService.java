package com.path.tech.auth.service;

import com.path.tech.auth.dto.OtpResponse;
import com.path.tech.auth.dto.UserSignupRequest;
import com.path.tech.auth.dto.UserSignupResponse;
import com.path.tech.auth.dto.OtpRequest;
import reactor.core.publisher.Mono;

public interface AuthService {

    Mono<UserSignupResponse> userSignup(UserSignupRequest userSignupRequest);
    OtpResponse requestOtp(String phoneNumber);
    Mono<Object> verifyOtp(OtpRequest otpRequest);
}
