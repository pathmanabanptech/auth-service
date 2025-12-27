package com.path.tech.auth.service;

import com.path.tech.auth.dto.*;
import com.path.tech.auth.exception.AuthErrorCode;
import com.path.tech.auth.exception.AuthException;
import com.path.tech.auth.model.RefreshTokenEntity;
import com.path.tech.auth.repository.TokenRepository;
import com.path.tech.auth.security.JwtUtil;
import com.path.tech.auth.security.TokenType;
import com.path.tech.auth.security.UserRole;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class TokenService {

  private JwtUtil jwtUtil;

  private TokenRepository tokenRepository;

  public TokenService(JwtUtil jwtUtil,TokenRepository tokenRepository){
      this.jwtUtil = jwtUtil;
       this.tokenRepository = tokenRepository;
  }

    public String generateAccessToken(String phoneNumber, String userId, List<String> roles, Map<String, Object> additionalClaims){
            return jwtUtil.generateToken(TokenType.ACCESS,phoneNumber,userId,roles,additionalClaims);
    }
    public String generateRefreshToken(String phoneNumber, String userId,  Map<String, Object> additionalClaims){
        return jwtUtil.generateToken(TokenType.REFRESH,phoneNumber,userId,null,additionalClaims);
    }


    public AuthTokenResponse issueTokens(OtpRequest otpRequest, UUID uid, String otpExpirySec) {
        UUID jti = UUID.randomUUID();
        //Access Token
        String token = generateAccessToken(otpRequest.getPhoneNumber(), uid.toString(), List.of(UserRole.CUSTOMER.name()),null);
        //Refresh Token
        String refreshToken = generateRefreshToken(otpRequest.getPhoneNumber(), uid.toString(),
                Map.of("jti",jti.toString()));
        RefreshTokenEntity entity = new RefreshTokenEntity();
        entity.setJti(jti);
        entity.setUserId(uid);
        entity.setRevoked(false);
        entity.setCreatedAt(Instant.now());
        entity.setExpiresAt(Instant.now().plusSeconds(jwtUtil.getRefreshTokenValiditySec()));
        //JTI
        tokenRepository.save(entity);

        AuthTokenResponse response = new AuthTokenResponse();
        response.setStatus(AuthStatus.OTP_VERIFIED);
        response.setMessage("OTP Verified Successfully!");
        response.setAccessToken(token);
        response.setRefreshToken(refreshToken);
        response.setExpiresIn(Long.parseLong(otpExpirySec));
        response.setTokenType("Bearer");
        response.setUserId(uid);
        response.setCreatedDateTime(LocalDateTime.now());
        response.setUpdatedDateTime(LocalDateTime.now());
        return response;
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
    public List<String> getRoles(String token) {

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
        return jwtUtil.getAccessTokenValiditySec();
    }

    public RefreshTokenResponse validateAndGenerateRefreshToken(String refreshToken) {
        Claims claims = jwtUtil.validateAndParse(refreshToken);

        // 1 Ensure this is a refresh token
        if (!"refresh".equals(claims.get("type"))) {
            throw new AuthException(HttpStatus.UNAUTHORIZED, AuthErrorCode.REFRESH_TOKEN_INVALID,"Invalid token type");
        }

        // 2 Audience check
        if (!claims.getAudience().contains("auth-service")) {
            throw new AuthException(HttpStatus.UNAUTHORIZED,AuthErrorCode.REFRESH_TOKEN_INVALID,"Invalid audience");
        }

        // 3  Check revocation / reuse
        if (isTokenRevoked(claims)) {
            throw new AuthException(HttpStatus.UNAUTHORIZED,AuthErrorCode.REFRESH_TOKEN_REVOKED,"Refresh token revoked");
        }
        String userId = claims.getSubject();
        String phone = claims.get("phone", String.class);
        List<String> roles = getRoles(refreshToken);
        String newAccessToken = generateAccessToken(
                phone,
                userId,
                roles, null
        );

        return RefreshTokenResponse.builder()
                .accessToken(newAccessToken)
                .expiresIn(getAccessTokenExpiry())
                .tokenType("Bearer")
                .build();
    }

    private boolean isTokenRevoked(Claims claims) {
        UUID jti = UUID.fromString(claims.getId());
        RefreshTokenEntity token =tokenRepository.findByJtiAndRevokedFalse(jti);
        return token == null;
    }

    public void logout(RefreshTokenRequest request) {
        Claims claims = jwtUtil.validateAndParse(request.getRefreshToken());
        UUID jti = UUID.fromString(claims.getId());
        tokenRepository.revoke(jti);
    }
}
