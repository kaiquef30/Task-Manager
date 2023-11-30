package com.project.task.manager.exception.messages;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "authentication.message")
public class AuthenticationMessageConfig {

    private String userAlreadyExists;

    private String forgotPasswordOk;

    private String userSuccessfullyRegistered;

    private String emailAlreadyRegistered;

    private String internalServerError;

    private String successfullyVerified;

    private String emailNotVerified;

    private String emailAlreadyVerified;

    private String invalidCredentials;

    private String errorSendingVerificationEmail;

    private String passwordResetSuccessfully;

}
