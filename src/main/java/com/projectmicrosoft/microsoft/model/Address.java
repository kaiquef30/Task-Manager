package com.projectmicrosoft.microsoft.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "address_line_1", nullable = false, length = 512)
    private String addressLine1;
    @Column(name = "address_line_2", length = 512)
    private String addressLine2;
    @Column(name = "city", nullable = false)
    private String city;
    @Column(name = "country", nullable = false, length = 75)
    private String country;
    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

}
