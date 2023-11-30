package com.microsoft.service;

import com.project.task.manager.dto.LoginBody;
import com.project.task.manager.dto.LoginResponse;
import com.project.task.manager.dto.PasswordResetBody;
import com.project.task.manager.dto.RegistrationBody;
import com.project.task.manager.exception.EmailFailureException;
import com.project.task.manager.exception.EmailNotFoundException;
import com.project.task.manager.exception.InvalidCredentialsException;
import com.project.task.manager.model.User;
import com.project.task.manager.model.VerificationToken;
import com.project.task.manager.repository.TokenRepository;
import com.project.task.manager.repository.UserRepository;
import com.project.task.manager.service.AuthenticationService;
import com.project.task.manager.service.EmailService;
import com.project.task.manager.service.EncryptionService;
import com.project.task.manager.service.JWTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EncryptionService encryptionService;
    @Mock
    private VerificationToken verificationToken;

    @Mock
    private EmailService emailService;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private JWTService jwtService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private AuthenticationService authenticationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationService = new AuthenticationService(userRepository, encryptionService, emailService,
                tokenRepository, jwtService, modelMapper);
    }

    @Test
    public void testRegisterUser() throws EmailFailureException {
        var registrationBody = new RegistrationBody();
        var user = new User();

        when(modelMapper.map(eq(registrationBody), eq(User.class))).thenReturn(user);
        when(encryptionService.encryptPassword(eq(registrationBody.getPassword()))).thenReturn("encryptedPassword");
        when(userRepository.save(eq(user))).thenReturn(user);

        User registeredUser = authenticationService.registerUser(registrationBody);

        verify(userRepository, times(1)).save(user);
        verify(emailService, times(1)).sendVerificationEmail(any(VerificationToken.class));

        assertNotNull(registeredUser);
    }


    @Test
    public void testSuccessfulLogin() throws EmailFailureException {

        var loginBody = new LoginBody();
        User user = new User();

        loginBody.setEmail("example@email.com");
        loginBody.setPassword("password");

        user.setEmail("example@email.com");
        user.setPassword("hashedPassword");

        String mockJWT = "mockedJWT";

        Mockito.when(userRepository.findByEmailIgnoreCase(loginBody.getEmail())).thenReturn(Optional.of(user));
        Mockito.when(jwtService.generateJWT(user)).thenReturn(mockJWT);
        Mockito.when(encryptionService.verifyPassword(loginBody.getPassword(), user.getPassword())).thenReturn(true);

        LoginResponse response = authenticationService.loginUser(loginBody);

        assertTrue(response.isSuccess());
        assertEquals(mockJWT, response.getJwt());
    }

    @Test
    public void testVerifyUser() {
        String token = "fakeToken";
        VerificationToken verificationToken = mock(VerificationToken.class);
        User user = mock(User.class);
        when(user.getEmailVerified()).thenReturn(false);

        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(verificationToken));
        when(verificationToken.getUser()).thenReturn(user);

        boolean result = authenticationService.verifyUser(token);

        assertTrue(result);
        assertFalse(user.isEmailVerified());
        verify(userRepository).save(user);
        verify(tokenRepository).deleteByUser(user);
    }

    @Test
    public void testVerifyUser_UserAlreadyVerified() {
        String token = "fakeToken";

        VerificationToken verificationToken = mock(VerificationToken.class);
        User user = mock(User.class);

        when(verificationToken.getUser()).thenReturn(user);
        when(user.isEmailVerified()).thenReturn(true);

        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(verificationToken));

        boolean result = authenticationService.verifyUser(token);

        assertFalse(result);

        verify(userRepository, never()).save(user);
        verify(tokenRepository, never()).deleteByUser(user);
    }


    @Test
    public void testVerifyUser_TokenNotFound() {
        String token = "fakeToken";

        when(tokenRepository.findByToken(token)).thenReturn(Optional.empty());

        boolean result = authenticationService.verifyUser(token);

        assertFalse(result);
        verify(userRepository, never()).save(any());
        verify(tokenRepository, never()).deleteByUser(any());
    }

    @Test
    public void testForgotPassword() throws EmailFailureException {
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(user));
        when(jwtService.generatePasswordResetJWT(user)).thenReturn("fakeResetToken");
        doNothing().when(emailService).sendResetPasswordEmail(user, "fakeResetToken");

        assertDoesNotThrow(() -> authenticationService.forgotPassword(email));

        verify(userRepository).findByEmailIgnoreCase(email);
        verify(jwtService).generatePasswordResetJWT(user);
        verify(emailService).sendResetPasswordEmail(user, "fakeResetToken");
    }

    @Test
    public void testForgotPassword_EmailNotFound() throws EmailFailureException {
        String email = "test@example.com";

        when(userRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.empty());

        assertThrows(EmailNotFoundException.class, () -> authenticationService.forgotPassword(email));

        verify(jwtService, never()).generatePasswordResetJWT(any());
        verify(emailService, never()).sendResetPasswordEmail(any(), any());
    }


    @Test
    public void testResetPassword() {
        String token = "your_test_token";
        String email = "test@example.com";
        String newPassword = "newPassword123";

        var resetBody = new PasswordResetBody();
        resetBody.setToken(token);
        resetBody.setPassword(newPassword);

        User user = new User();
        user.setEmail(email);

        when(jwtService.getResetPasswordEmail(token)).thenReturn(email);
        when(userRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(user));

        when(encryptionService.encryptPassword(newPassword)).thenReturn("encryptedPassword123");

        authenticationService.resetPassword(resetBody);

        verify(userRepository).save(user);

        assertEquals("encryptedPassword123", user.getPassword());
    }


    @Test
    public void testCreateVerificationToken() {
        User user = new User();
        VerificationToken verificationToken = new VerificationToken();

        when(jwtService.generateVerificationJWT(user)).thenReturn("fakeVerificationToken");

        VerificationToken result = authenticationService.createVerificationToken(user);

        assertNotNull(result);
        assertEquals("fakeVerificationToken", result.getToken());
        assertNotNull(result.getCreatedTimestamp());
        assertEquals(user, result.getUser());

        verify(jwtService).generateVerificationJWT(user);
    }

    @Test
    public void testFindUserByEmail() throws InvalidCredentialsException {
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(user));

        User result = authenticationService.findUserByEmail(email);

        assertNotNull(result);
        assertEquals(user, result);

        verify(userRepository).findByEmailIgnoreCase(email);
    }

    @Test
    public void testFindUserByEmail_UserNotFound() {
        String email = "test@example.com";

        when(userRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> authenticationService.findUserByEmail(email));

        verify(userRepository).findByEmailIgnoreCase(email);
    }

    @Test
    public void testValidatePassword() {
        User user = new User();
        user.setPassword("hashedPassword");

        when(encryptionService.verifyPassword("correctPassword", user.getPassword())).thenReturn(true);

        assertDoesNotThrow(() -> authenticationService.validatePassword("correctPassword", user));

        when(encryptionService.verifyPassword("incorrectPassword", user.getPassword())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> authenticationService.validatePassword("incorrectPassword", user));
    }
}

