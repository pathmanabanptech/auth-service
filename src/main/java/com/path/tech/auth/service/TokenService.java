package com.path.tech.auth.service;

import com.path.tech.auth.security.JwtUtil;
import com.path.tech.auth.security.TokenType;
import org.springframework.stereotype.Service;

import java.security.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class TokenService {

  private JwtUtil jwtUtil;

  public TokenService(JwtUtil jwtUtil){
      this.jwtUtil = jwtUtil;
  }

    public String generateAccessToken(String phoneNumber, String userId, List<String> roles, Map<String, Object> additionalClaims){
            return jwtUtil.generateToken(TokenType.ACCESS,phoneNumber,userId,roles,additionalClaims);
    }
    public String generateRefreshToken(String phoneNumber, String userId,  Map<String, Object> additionalClaims){
        return jwtUtil.generateToken(TokenType.REFRESH,phoneNumber,userId,null,additionalClaims);
    }




    /**
     * Extract phone number (sub)
     */
    public String getPhoneNumber(String token) {
        return jwtUtil.validateAndParse(token).get("phone", String.class);
    }

    /**
     * Extract user id
     */
    public String getUserId(String token) {
        return jwtUtil.validateAndParse(token).getSubject();
    }

    /**
     * Extract scope (CUSTOMER / PROVIDER / ADMIN)
     * CUSTOMER = Patient, other customer
     * PROVIDER = Doctor, Salon
     */
    public List<?> getRoles(String token) {
        return jwtUtil.validateAndParse(token).get("roles", List.class);
    }
    /**
     * Check token expiry
     */
    public boolean isTokenExpired(String token) {
        Date expiration = jwtUtil.validateAndParse(token).getExpiration();
        return expiration.before(new Date());
    }

    public long getAccessTokenExpiry(){
        return 0;
    }
}
