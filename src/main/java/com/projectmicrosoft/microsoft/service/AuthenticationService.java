package com.projectmicrosoft.microsoft.service;


import com.projectmicrosoft.microsoft.api.dto.LoginBody;
import com.projectmicrosoft.microsoft.api.dto.PasswordResetBody;
import com.projectmicrosoft.microsoft.api.dto.RegistrationBody;
import com.projectmicrosoft.microsoft.enums.UserRoles;
import com.projectmicrosoft.microsoft.exception.EmailFailureException;
import com.projectmicrosoft.microsoft.exception.EmailNotFoundException;
import com.projectmicrosoft.microsoft.exception.InvalidCredentialsException;
import com.projectmicrosoft.microsoft.model.User;
import com.projectmicrosoft.microsoft.model.VerificationToken;
import com.projectmicrosoft.microsoft.repository.TokenRepository;
import com.projectmicrosoft.microsoft.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import com.projectmicrosoft.microsoft.api.DTO.LoginResponse;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final UserRepository userRepository;

    private final EncryptionService encryptionService;

    private final EmailService emailService;

    private final TokenRepository tokenRepository;

    private final JWTService jwtService;

    private final ModelMapper modelMapper;


    @Transactional
    public User registerUser(RegistrationBody registrationBody) throws EmailFailureException {
        User user = modelMapper.map(registrationBody, User.class);
        user.setRoles(List.of(UserRoles.USER));
        user.setPassword(encryptionService.encryptPassword(registrationBody.getPassword()));
        VerificationToken verificationToken = createVerificationToken(user);
        emailService.sendVerificationEmail(verificationToken);
        return userRepository.save(user);
    }

    public LoginResponse loginUser(LoginBody loginBody) throws EmailFailureException {
        User user = findUserByEmail(loginBody.getEmail());
        validatePassword(loginBody.getPassword(), user);

        String jwt = jwtService.generateJWT(user);
        return new LoginResponse(true, jwt);
    }


    public boolean verifyUser(String token) {
        Optional<VerificationToken> opToken = tokenRepository.findByToken(token);
        if (opToken.isPresent()) {
            VerificationToken verificationToken = opToken.get();
            User user = verificationToken.getUser();
            if (!user.isEmailVerified()) {
                user.setEmailVerified(true);
                userRepository.save(user);
                tokenRepository.deleteByUser(user);
                return true;
            }
        }
        return false;
    }

    public void forgotPassword(String email) throws EmailFailureException, EmailNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String token = jwtService.generatePasswordResetJWT(user);
            emailService.sendResetPasswordEmail(user, token);
        } else {
            throw new EmailNotFoundException();
        }
    }

    @Transactional
    public void resetPassword(PasswordResetBody body) {
        String email = jwtService.getResetPasswordEmail(body.getToken());
        Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setPassword(encryptionService.encryptPassword(body.getPassword()));
            userRepository.save(user);
        }
    }

    public LoginResponse handleUnverifiedUser(User user) throws EmailFailureException {
        List<VerificationToken> verificationTokens = user.getVerificationTokens();
        boolean resend = verificationTokens.isEmpty() ||
                verificationTokens.get(0).getCreatedTimestamp().before(new Timestamp(System.currentTimeMillis() - (60 * 60 * 1000)));
        if (resend) {
            VerificationToken verificationToken = createVerificationToken(user);
            tokenRepository.save(verificationToken);
            emailService.sendVerificationEmail(verificationToken);
        }
        String reason = resend ? "USER_NOT_VERIFIED_EMAIL_RESENT" : "USER_NOT_VERIFIED";
        return new LoginResponse(false, reason);
    }

    @Transactional
    public VerificationToken createVerificationToken(User user) {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(jwtService.generateVerificationJWT(user));
        verificationToken.setCreatedTimestamp(new Timestamp(System.currentTimeMillis()));
        verificationToken.setUser(user);
        user.getVerificationTokens().add(verificationToken);
        return verificationToken;
    }

    public User findUserByEmail(String email) {
        Optional<User> opUser = userRepository.findByEmailIgnoreCase(email);
        if (opUser.isPresent()) {
            return opUser.get();
        } else {
            throw new InvalidCredentialsException();
        }
    }

    public void validatePassword(String inputPassword, User user) {
        if (!encryptionService.verifyPassword(inputPassword, user.getPassword())) {
            throw new InvalidCredentialsException();
        }
    }

}
