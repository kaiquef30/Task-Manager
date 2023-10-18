package com.projectmicrosoft.microsoft.api.dto;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.constraints.*;

@Hidden
public class RegistrationBody {

    @NotNull
    @NotBlank
    @Size(min=4, max=32)
    private String firstName;
    @NotNull
    @NotBlank
    @Size(min=4, max=32)
    private String lastName;
    @NotNull
    @NotBlank
    @Size(min=4, max=32)
    private String username;
    @NotBlank
    @Email
    private String email;
    @NotNull
    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$")
    @Size(min=6, max=32)
    private String password;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
