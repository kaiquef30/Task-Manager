package com.projectmicrosoft.microsoft.api.dto;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Hidden
public class TeamsDto {
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @NotBlank
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
