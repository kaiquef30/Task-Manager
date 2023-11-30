package com.project.task.manager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Hidden
@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JsonIgnore
    private Long id;
    @Column(name = "address_line_1", nullable = false, length = 512)
    @Schema(description = "Address Line 1", example = "Alytus")
    private String addressLine1;

    @Column(name = "address_line_2", length = 512)
    @Schema(description = "Address Line 2", example = "Apartment 123")
    private String addressLine2;

    @Column(name = "city", nullable = false)
    @Schema(description = "City", example = "New York")
    private String city;

    @Column(name = "country", nullable = false, length = 75)
    @Schema(description = "Country", example = "United States")
    private String country;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    @Schema(description = "Client")
    private Client client;

}
