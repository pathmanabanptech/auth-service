package com.path.tech.auth.model;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class RefreshTokenEntity {
    private UUID jti;
    private UUID userId;
    private boolean revoked;
    private Instant createdAt;
    private Instant expiresAt;
}
