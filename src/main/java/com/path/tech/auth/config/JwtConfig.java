package com.path.tech.auth.config;

import com.path.tech.auth.security.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.PrivateKey;
import java.security.PublicKey;

@Configuration
public class JwtConfig {

    @Value("${app.jwt.access-token.expiry:900}") // 15 mins = 15*60
    private long accessTokenExpiry;
    @Value("${app.jwt.access-token.expiry:604800}") // 7 days = 7*24*60*60
    private long refreshTokenExpiry;

    @Bean
    public JwtUtil jwtUtil() throws Exception {

        PrivateKey privateKey = RsaKeyLoader.loadPrivateKey("keys/private_key.pem");
        PublicKey publicKey = RsaKeyLoader.loadPublicKey("keys/public_key.pem");
        return new JwtUtil(privateKey,publicKey,"auth-service",accessTokenExpiry,refreshTokenExpiry);
    }
}