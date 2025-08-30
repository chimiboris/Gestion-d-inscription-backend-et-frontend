package com.inscription.app.security.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.*;
import java.util.stream.Collectors;
import java.util.Base64;
import java.util.Date;


@Component
public class TokenProvider {
    private final Logger log = LoggerFactory.getLogger(TokenProvider.class);

    @Value("${jhipster.security.authentication.jwt.base64-secret}")
    private String base64Secret;

    @Value("${jhipster.security.authentication.jwt.token-validity-in-seconds}")
    private long tokenValidityInSeconds;

    private SecretKey key;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Base64.getDecoder().decode(base64Secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(Authentication authentication, boolean rememberMe) {
        String authorities = authentication.getAuthorities().stream()
            .map(grantedAuthority -> grantedAuthority.getAuthority())
            .collect(Collectors.joining(" "));

        long now = (new Date()).getTime();
        long validity = rememberMe ? tokenValidityInSeconds * 2000 : tokenValidityInSeconds * 1000;

        return Jwts.builder()
            .setSubject(authentication.getName())
            .claim("auth", authorities)
            .signWith(SignatureAlgorithm.HS512, key)
            .setIssuedAt(new Date(now))
            .setExpiration(new Date(now + validity))
            .compact();
    }
}
