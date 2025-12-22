package com.path.tech.auth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.path.tech.auth.security.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserSignupRequest {
    @NotBlank(message = "First name is required")
    private String firstName;
    @NotBlank(message = "Last name is required")
    private String lastName;
    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^\\d{10}$",
            message = "Phone number must be exactly 10 digits"
    )
    private String phoneNumber;
    @NotNull(message = "Sex is required")
    private String sex;
    @NotNull(message = "DOB is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;
    @NotNull(message = "Role is required")
    private UserRole role;
}
