package com.projectmicrosoft.microsoft.api.dto;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Hidden
public class UserEditResponse {

    @NotNull
    @NotBlank
    @Size(min=4, max=32)
    private String username;

    @NotBlank
    @Email
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
