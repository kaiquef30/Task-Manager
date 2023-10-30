package com.projectmicrosoft.microsoft.api.DTO;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
public class LoginResponse {

    private boolean success;
    private String jwt;
    private String failureReason;

    public LoginResponse(boolean success, String jwt) {
        this.success = success;
        this.jwt = jwt;
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
