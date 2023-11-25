package com.projectmicrosoft.microsoft.api.dto;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Hidden
public class TeamDTO {
    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String description;

}
