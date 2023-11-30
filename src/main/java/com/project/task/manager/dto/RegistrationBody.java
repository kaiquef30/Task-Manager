package com.project.task.manager.dto;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
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

}
