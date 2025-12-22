package com.path.tech.auth.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class TestJwtUtil {
    private JwtUtil jwtUtil;

    @BeforeEach
    void setup(){
        jwtUtil = TestJwtFactory.jwtUtil();
    }
    @Test
    void generateValidToken(){
        String token = jwtUtil.generateToken(TokenType.ACCESS,"9876543210","user123", List.of("CUSTOMER"),null);
        Claims claims = jwtUtil.validateAndParse(token);
        assertEquals("9876543210",claims.get("phone"));
        assertEquals(List.of("CUSTOMER"),claims.get("roles"));
        assertEquals("user123",claims.getSubject());
    }
}
