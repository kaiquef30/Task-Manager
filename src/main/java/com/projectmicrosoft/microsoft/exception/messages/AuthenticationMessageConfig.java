package com.projectmicrosoft.microsoft.exception.messages;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "authentication.message")
public class AuthenticationMessageConfig {

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

    public String getInvalidCredentials() {
        return invalidCredentials;
    }

    public void setInvalidCredentials(String invalidCredentials) {
        this.invalidCredentials = invalidCredentials;
    }

    public String getPasswordResetSuccessfully() {
        return passwordResetSuccessfully;
    }
    public void setPasswordResetSuccessfully(String passwordResetSuccessfully) {
        this.passwordResetSuccessfully = passwordResetSuccessfully;
    }
    public String getErrorSendingVerificationEmail() {
        return errorSendingVerificationEmail;
    }
    public void setErrorSendingVerificationEmail(String errorSendingVerificationEmail) {
        this.errorSendingVerificationEmail = errorSendingVerificationEmail;
    }

    public String getEmailAlreadyVerified() {
        return emailAlreadyVerified;
    }
    public void setEmailAlreadyVerified(String emailAlreadyVerified) {
        this.emailAlreadyVerified = emailAlreadyVerified;
    }

    public String getEmailNotVerified() {
        return emailNotVerified;
    }

    public void setEmailNotVerified(String emailNotVerified) {
        this.emailNotVerified = emailNotVerified;
    }

    public String getUserSuccessfullyRegistered() {
        return userSuccessfullyRegistered;
    }
    public void setUserSuccessfullyRegistered(String userSuccessfullyRegistred) {
        this.userSuccessfullyRegistered = userSuccessfullyRegistred;
    }
    public String getSuccessfullyVerified() {
        return successfullyVerified;
    }

    public void setSuccessfullyVerified(String successfullyVerified) {
        this.successfullyVerified = successfullyVerified;
    }

    public String getInternalServerError() {
        return internalServerError;
    }

    public void setInternalServerError(String internalServerError) {
        this.internalServerError = internalServerError;
    }

    public String getEmailAlreadyRegistered() {
        return emailAlreadyRegistered;
    }

    public void setEmailAlreadyRegistered(String emailAlreadyRegistered) {
        this.emailAlreadyRegistered = emailAlreadyRegistered;
    }

    public String getForgotPasswordOk() {
        return forgotPasswordOk;
    }

    public void setForgotPasswordOk(String forgotPasswordOk) {
        this.forgotPasswordOk = forgotPasswordOk;
    }

}
