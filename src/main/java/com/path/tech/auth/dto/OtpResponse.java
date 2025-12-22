package com.path.tech.auth.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OtpResponse {
    private String phoneNumber;
    private boolean otpSent;
    private int otpExpirySeconds;
    private AuthStatus status;
    private String message;
    private LocalDateTime createdDateTime;
    private LocalDateTime updatedDateTime;
}
