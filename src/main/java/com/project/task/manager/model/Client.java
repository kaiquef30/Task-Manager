package com.project.task.manager.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "client")
public class Client extends RepresentationModel<Client> {

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

}
