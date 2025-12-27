package com.path.tech.auth.exception;

import com.path.tech.auth.dto.AuthErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthControllerAdvice {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<AuthErrorResponse> handleAuthException(AuthException ex){
        AuthErrorResponse response = new AuthErrorResponse(ex.getStatus(), ex.getError(),ex.getMessage());

        return ResponseEntity.status(ex.getStatus()).body(response);
    }
}
