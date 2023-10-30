package com.projectmicrosoft.microsoft.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Client ID", example = "1")
    private Long id;

    @Column(name = "name_client", nullable = false)
    @Schema(description = "Client Name", example = "John Doe")
    private String name;

    @Column(name = "email_client", nullable = false)
    @Schema(description = "Client Email", example = "johndoe@example.com")
    private String email;

    @Column(name = "phone_client", nullable = false)
    @Schema(description = "Client Phone", example = "+1 (123) 456-7890")
    private String phone;

    @Column(name = "cpf_client", nullable = false, unique = true)
    @Schema(description = "Client CPF", example = "123.456.789-01")
    private String cpf;

    @JsonIgnore
    @OneToMany(mappedBy = "client", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Address> address = new ArrayList<>();

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Address> getAddress() {
        return address;
    }

    public void setAddress(List<Address> address) {
        this.address = address;
    }
}
