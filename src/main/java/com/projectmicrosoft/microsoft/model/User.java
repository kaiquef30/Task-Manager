package com.projectmicrosoft.microsoft.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.projectmicrosoft.microsoft.enums.UserRoles;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "local_user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Schema(description = "User identifier", example = "12")
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    @Schema(description = "User's username", example = "john_doe")
    private String username;

    @JsonIgnore
    @Column(name = "password", nullable = false, length = 1000)
    @Schema(description = "User's password", example = "password123")
    private String password;

    @Column(name = "email", nullable = false, unique = true, length = 320)
    @Schema(description = "User's email address", example = "user@example.com")
    private String email;

    @Column(name = "first_name", nullable = false)
    @Schema(description = "User's first name", example = "John")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @Schema(description = "User's last name", example = "Doe")
    private String lastName;

    @Column(name = "office")
    @ElementCollection(targetClass = UserRoles.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Schema(description = "List of user roles", example = "[ROLE_USER, ROLE_ADMIN]")
    private List<UserRoles> roles;


    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id desc")
    private List<VerificationToken> verificationTokens = new ArrayList<>();

    @Column(name = "email_verified", nullable = false)
    @Schema(description = "Flag indicating whether the user's email is verified", example = "false")
    private Boolean emailVerified = false;

    @Column(name = "registration_date")
    @Schema(description = "Date of user registration", example = "2023-10-19")
    private Date registrationDate;

    public Boolean isEmailVerified() {
        return emailVerified;
    }

    @PrePersist
    protected void onCreate() {
        registrationDate = new Date();
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (UserRoles role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toString()));
        }
        return authorities;
    }

}
