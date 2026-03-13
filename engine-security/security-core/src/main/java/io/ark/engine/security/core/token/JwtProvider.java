package io.ark.engine.security.core.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @Description: JWT 的生成和解析，Token生成/解析/验签,
 * @Author: Noah Zhou
 */
@RequiredArgsConstructor
public class JwtProvider {
    private final JwtProperties jwtProperties;

    public TokenClaims parseToken(String token) {
        Claims claims = Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload();
        return new TokenClaims(
                claims.getSubject(),
                claims.get(TokenClaims.USERNAME, String.class),
                new HashSet<>(claims.get(TokenClaims.PERMISSIONS, Set.class)),
                claims.get(TokenClaims.CLIENT_TYPE, String.class),
                claims.get(TokenClaims.TOKEN_TYPE, String.class)
        );
    }

    public String generateToken(TokenClaims tokenClaims, Duration expires) {
        return Jwts.builder()
                .subject(tokenClaims.userId())
                .claim(TokenClaims.USERNAME, tokenClaims.username())
                .claim(TokenClaims.PERMISSIONS, tokenClaims.permissions())
                .claim(TokenClaims.CLIENT_TYPE, tokenClaims.clientType())
                .claim(TokenClaims.TOKEN_TYPE, tokenClaims.tokenType())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expires.toMillis()))
                .signWith(getSecretKey())
                .compact();
    }

    public Duration getRemaining(String token){
        Date expiration = Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload().getExpiration();
        long remaining = expiration.getTime() - System.currentTimeMillis();
        return Duration.ofMillis(Math.max(0,remaining));
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }
}
