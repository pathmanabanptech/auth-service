package com.path.tech.auth.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
public class AuthException extends RuntimeException {

    private final HttpStatus status;
    private final AuthErrorCode error;


    public AuthException(HttpStatus status, AuthErrorCode error, String message) {
       super(message);
        this.status = status;
        this.error = error;
    }
}
