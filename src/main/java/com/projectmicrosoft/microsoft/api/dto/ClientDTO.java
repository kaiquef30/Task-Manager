package com.projectmicrosoft.microsoft.api.dto;

import com.projectmicrosoft.microsoft.model.Address;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

@Getter
@Setter
@Hidden
public class ClientDTO {

    @Email
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    private String name;

    @NotBlank
    @NotNull
    @CPF
    private String cpf;

    @NotBlank
    @NotNull
    private String phone;

    private Address address;

}
