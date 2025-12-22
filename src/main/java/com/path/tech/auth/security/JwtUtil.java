package com.path.tech.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class JwtUtil {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    private final String issuer;
    private final long accessTokenValiditySec;
    private final long refreshTokenValiditySec;
    public JwtUtil(PrivateKey privateKey, PublicKey publicKey, String issuer, long accessTokenExpiry, long refreshTokenExpiry) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.issuer = issuer;
        this.accessTokenValiditySec = accessTokenExpiry;
        this.refreshTokenValiditySec = refreshTokenExpiry;
    }

    public String generateToken(TokenType type, String phoneNumber, String userId, List<String> roles, Map<String, Object> additionalClaims) {
        Instant now = Instant.now();
        JwtBuilder jwtBuilder = Jwts.builder()
                .subject(userId) // stable identifier
                .claim("phone",phoneNumber) // phone cannot be subject, since it can change.
                .claim("uid", userId)
                .issuer(issuer)
                .issuedAt(Date.from(now));
        if(type==TokenType.ACCESS) {
            jwtBuilder.claim("roles", roles)
                    .audience().add("api").and()
                    .expiration(Date.from(now.plusSeconds(accessTokenValiditySec)));
        }else{
            jwtBuilder.claim("type","refresh")
                    .audience().add("auth-service").and()
                    .expiration(Date.from(now.plusSeconds(refreshTokenValiditySec)));
        }
        if(additionalClaims!=null){
            additionalClaims.forEach((k,v)->{
                // hardening, additional claims should not overwrite
                // additional claims can be tenantId, providerId, region
                if(!Set.of("sub","uid","roles","type","exp","aud").contains(k)){
                    jwtBuilder.claim(k,v);
                }
            });
        }
        return jwtBuilder.signWith(privateKey, Jwts.SIG.RS256).compact();
    }

    /**
     * Validate token and parse Claims
     * @param token - jwt token
     * @return claims
     */
    public Claims validateAndParse(String token) {
        return Jwts.parser()
                .verifyWith(publicKey)
                .requireIssuer(issuer)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
