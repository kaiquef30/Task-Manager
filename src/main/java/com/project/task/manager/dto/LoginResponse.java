package com.project.task.manager.dto;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Hidden
public class LoginResponse {

    private boolean success;
    private String jwt;
    private String failureReason;

    public LoginResponse(boolean success, String jwt) {
        this.success = success;
        this.jwt = jwt;
    }

}
