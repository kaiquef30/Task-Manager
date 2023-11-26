package com.projectmicrosoft.microsoft.api.dto;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Hidden
public class LoginResponse {

    @Getter
    private boolean success;
    private String jwt;
    private String failureReason;

    public LoginResponse(boolean success, String jwt) {
        this.success = success;
        this.jwt = jwt;
    }

}
