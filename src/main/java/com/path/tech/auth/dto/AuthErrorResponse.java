package com.path.tech.auth.dto;

import com.path.tech.auth.exception.AuthErrorCode;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Data
public class AuthErrorResponse {
    private HttpStatus status;
    private AuthErrorCode error;
    private String message;
    private Instant timeStamp;
    public AuthErrorResponse(HttpStatus status, AuthErrorCode error, String message){
        this.status = status;
        this.error = error;
        this.message = message;
        this.timeStamp = Instant.now();
    }


}
