package com.path.tech.auth.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class UserSignupResponse {
    private UUID userId;
    private String phoneNumber;
    private boolean otpSent;
    private int otpExpirySeconds;
    private AuthStatus status;
    private String message;
    private LocalDateTime createdDateTime;
    private LocalDateTime updatedDateTime;
}

