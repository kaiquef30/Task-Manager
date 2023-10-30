package com.projectmicrosoft.microsoft.model;


import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.sql.Timestamp;


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


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Timestamp getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
