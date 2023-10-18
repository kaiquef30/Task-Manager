package com.projectmicrosoft.microsoft.api.dto;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;

@Hidden
public class LoginResponse {

    private boolean success;
    private String jwt;
    private String failureReason;

    public LoginResponse(boolean success, String jwt) {
        this.success = success;
        this.jwt = jwt;
    }

    public LoginResponse(boolean success, HttpStatus httpStatus, String failureReason) {
        this.success = success;
        this.failureReason = failureReason;
    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

}
