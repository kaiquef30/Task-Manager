package com.microsoft.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.projectmicrosoft.microsoft.model.User;
import com.projectmicrosoft.microsoft.service.JWTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JWTServiceTest {

    @Mock
    private JWTService jwtService;

    @Mock
    private DecodedJWT decodedJWT;

    @BeforeEach
     void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtService = new JWTService();
        ReflectionTestUtils.setField(jwtService, "algorithmKey", "yourAlgorithmKey");
        ReflectionTestUtils.setField(jwtService, "issuer", "yourIssuer");
        jwtService.postConstruct();
    }

    @Test
     void testGenerateJWT() {
        var user = new User();
        user.setEmail("test@example.com");
        String jwt = jwtService.generateJWT(user);
        assertNotNull(jwt);
    }

    @Test
     void testGenerateVerificationJWT() {
        User user = new User();
        user.setEmail("test@example.com");
        String jwt = jwtService.generateVerificationJWT(user);
        assertNotNull(jwt);
    }

    @Test
     void testGeneratePasswordResetJWT() {
        User user = new User();
        user.setEmail("test@example.com");
        String jwt = jwtService.generatePasswordResetJWT(user);
        assertNotNull(jwt);
    }

    @Test
     void testCalculateExpirationTime() {
        long expirationDurationInSeconds = 3600;
        Date expirationTime = jwtService.calculateExpirationTime(expirationDurationInSeconds);
        assertNotNull(expirationTime);

        Instant currentInstant = Instant.now();
        Instant expectedInstant = currentInstant.plusSeconds(expirationDurationInSeconds);

        assertTrue(expirationTime.toInstant().isAfter(currentInstant));
        assertTrue(expirationTime.toInstant().isBefore(expectedInstant));
    }

}
