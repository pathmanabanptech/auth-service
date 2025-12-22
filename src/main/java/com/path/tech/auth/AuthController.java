package com.path.tech.auth;

import com.path.tech.auth.dto.UserSignupRequest;
import com.path.tech.auth.dto.OtpRequest;
import com.path.tech.auth.dto.UserSignupResponse;
import com.path.tech.auth.service.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Slf4j
@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/auth/signup")
    public Mono<UserSignupResponse> userSignup(@Valid @RequestBody UserSignupRequest userSignupRequest){
        log.info(userSignupRequest.toString());
        return  validateUserSignupRequest(userSignupRequest)
                .then(Mono.defer(()->authService.userSignup(userSignupRequest)));
    }


    @PostMapping("/auth/request-otp")
    public Mono<Object> requestOTP(@NotBlank @RequestParam String phoneNumber){
        // request otp for phone number
        return Mono.just(authService.requestOtp(phoneNumber)) ;
    }

    @PostMapping("/auth/verify-otp")
    public Mono<Object> verifyOTP(@Valid @RequestBody OtpRequest otpRequest){
        // verify otp user provided
        return authService.verifyOtp(otpRequest) ;
    }

    private Mono<Void> validateUserSignupRequest(UserSignupRequest userSignupRequest) {

        if(!isValidDob(userSignupRequest.getDob()))
            return Mono.error(new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "User must be at least 13 years old"
            ));

        return Mono.empty();
    }

    private boolean isValidDob(LocalDate dob) {
        return dob.isBefore(LocalDate.now().minusYears(13));
    }

}
