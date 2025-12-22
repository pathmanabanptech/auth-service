package com.path.tech.auth.model;

import lombok.Data;

import java.time.Instant;

@Data
public class OtpEntry {
    private String phoneNumber;
    private String otp;
    private Instant createdAt;
    private boolean verified;
}
