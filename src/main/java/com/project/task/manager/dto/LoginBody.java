package com.project.task.manager.dto;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Hidden
public class LoginBody {

    @NotBlank
    @Email
    private String email;

    @NotNull
    @NotBlank
    private String password;

}
