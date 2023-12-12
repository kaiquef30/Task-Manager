package com.project.task.manager.service;


import com.project.task.manager.dto.LoginBody;
import com.project.task.manager.dto.LoginResponse;
import com.project.task.manager.dto.PasswordResetBody;
import com.project.task.manager.dto.RegistrationBody;
import com.project.task.manager.exception.EmailNotFoundException;
import com.project.task.manager.exception.InvalidCredentialsException;
import com.project.task.manager.repository.TokenRepository;
import com.project.task.manager.repository.UserRepository;
import com.project.task.manager.enums.UserRoles;
import com.project.task.manager.model.User;
import com.project.task.manager.model.VerificationToken;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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

    private static final String USER_NOT_VERIFIED_EMAIL_RESENT = "User not verified. Verification email resent.";

    private static final String USER_NOT_VERIFIED = "User not verified. Please check your email inbox.";



    @Transactional
    public User registerUser(RegistrationBody registrationBody) {
        userAlreadyExists(registrationBody.getEmail());
        User user = modelMapper.map(registrationBody, User.class);
        user.setRoles(List.of(UserRoles.ADMIN));
        user.setPassword(encryptionService.encryptPassword(registrationBody.getPassword()));
        VerificationToken verificationToken = createVerificationToken(user);
        emailService.sendVerificationEmail(verificationToken);
        return userRepository.save(user);
    }

    public LoginResponse loginUser(LoginBody loginBody) {
        User user = findUserByEmail(loginBody.getEmail());
        validatePassword(loginBody.getPassword(), user);

        LoginResponse unverifiedUserResponse = handleUnverifiedUser(user);

        if (!unverifiedUserResponse.isSuccess()) {
            return unverifiedUserResponse;
        }

        String jwt = jwtService.generateJWT(user);
        return new LoginResponse(true, jwt);
    }



    @Transactional
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

    public void forgotPassword(String email) {
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

    public LoginResponse handleUnverifiedUser(User user) {
        List<VerificationToken> verificationTokens = user.getVerificationTokens();
        boolean resend = verificationTokens.isEmpty() ||
                verificationTokens.get(0).getCreatedTimestamp()
                        .before(new Timestamp(System.currentTimeMillis() - (60 * 1000)));
        if (resend) {
            VerificationToken verificationToken = createVerificationToken(user);
            tokenRepository.save(verificationToken);
            emailService.sendVerificationEmail(verificationToken);
        }
        String reason = resend ? USER_NOT_VERIFIED_EMAIL_RESENT : USER_NOT_VERIFIED;
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

    public void userAlreadyExists(String email) {
        Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(email);
        if (optionalUser.isPresent()) {
            throw new InvalidCredentialsException();
        }
    }

}