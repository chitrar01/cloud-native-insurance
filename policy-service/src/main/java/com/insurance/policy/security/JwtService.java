package com.insurance.policy.security;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Jwts;

@Service
public class JwtService {
    private final SecretKey secretKey;
    private final String issuer;
    private final long ttlMinutes;

    

    public JwtService(
        @Value("${app.jwt.secret}") String secret, 
        @Value("${app.jwt.issuer}") String issuer, 
        @Value("${app.jwt.ttl-minutes}") long ttlMinutes) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.issuer = issuer;
        this.ttlMinutes = ttlMinutes;
    }

    public String generate(String subject, List<String> roles) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(ttlMinutes * 60);
        return Jwts.builder()
            .issuer(issuer)
            .subject(subject)
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiry))
            .claim("roles", roles)
            .signWith(secretKey)
            .compact();
    }

}
