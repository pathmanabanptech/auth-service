package com.path.tech.auth.model;

import com.path.tech.auth.dto.UserSignupRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserProfile extends UserSignupRequest {
    private UUID id;
    private Instant createDate;
    private Instant updatedDate;

}
