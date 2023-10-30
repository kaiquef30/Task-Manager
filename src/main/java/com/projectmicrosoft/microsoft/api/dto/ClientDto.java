package com.projectmicrosoft.microsoft.api.DTO;

import com.projectmicrosoft.microsoft.model.Address;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

}
