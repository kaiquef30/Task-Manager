package com.projectmicrosoft.microsoft.model;


import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;


@Getter
@Setter
@Hidden
@Entity
@Table(name = "verification_token")
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Schema(description = "ID", example = "1")
    private Long id;

    @Column(name = "token", nullable = false, unique = true)
    @Schema(description = "Token", example = "abcdef12345")
    private String token;

    @Column(name = "created_timestamp", nullable = false)
    @Schema(description = "Created Timestamp", example = "2023-10-28T10:15:30Z")
    private Timestamp createdTimestamp;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "User")
    private User user;

}
