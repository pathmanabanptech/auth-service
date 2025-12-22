package com.path.tech.auth.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AuthTokenResponse{
    private String accessToken,refreshToken,tokenType;
    private long expiresIn; // 3600 seconds
    private UUID userId;
    private AuthStatus status;
    private String message;
    private LocalDateTime createdDateTime;
    private LocalDateTime updatedDateTime;
}
