package com.project.task.manager.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.project.task.manager.model.User;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
@CacheConfig(cacheNames = "jwtCache")
public class JWTService {

    @Value("${jwt.algorithm.key}")
    private String algorithmKey;

    @Value("${jwt.issuer}")
    private String issuer;

    private Algorithm algorithm;

    public static final String EMAIL_KEY = "EMAIL";

    private static final String VERIFICATION_EMAIL_KEY = "VERIFICATION_EMAIL";

    public static final String RESET_PASSWORD_EMAIL_KEY = "RESET_PASSWORD_EMAIL";

    private static final long EXPIRATION_DURATION_IN_SECONDS = 1800;

    private static final long VERIFICATION_EXPIRATION_DURATION_IN_SECONDS = 86400;

    @PostConstruct
    public void postConstruct() {
        algorithm = Algorithm.HMAC256(algorithmKey);
    }

    public Date calculateExpirationTime(long expirationDurationInSeconds) {
        return Date.from(Instant.now().plusSeconds(expirationDurationInSeconds));
    }

    @Cacheable(value = "jwtCache", key = "#user.email", unless = "#result == null")
    public String generateJWT(User user) {
        return JWT.create()
                .withClaim(EMAIL_KEY, user.getEmail())
                .withExpiresAt(calculateExpirationTime(EXPIRATION_DURATION_IN_SECONDS))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    public String generateVerificationJWT(User user) {
        return JWT.create()
                .withClaim(VERIFICATION_EMAIL_KEY, user.getEmail())
                .withExpiresAt(calculateExpirationTime(VERIFICATION_EXPIRATION_DURATION_IN_SECONDS))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    public String generatePasswordResetJWT(User user) {
        return JWT.create()
                .withClaim(RESET_PASSWORD_EMAIL_KEY, user.getEmail())
                .withExpiresAt(calculateExpirationTime(EXPIRATION_DURATION_IN_SECONDS))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    public String getResetPasswordEmail(String token) {
        DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
        return jwt.getClaim(RESET_PASSWORD_EMAIL_KEY).asString();
    }

    public String getEmail(String token) {
        DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
        return jwt.getClaim(EMAIL_KEY).asString();
    }
}
