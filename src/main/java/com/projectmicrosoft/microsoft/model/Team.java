package com.projectmicrosoft.microsoft.model;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "teams")
public class Team extends RepresentationModel<Team> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID", example = "1")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    @Schema(description = "Name", example = "Team A")
    private String name;

    @Column(name = "numbers_of_users")
    @Schema(description = "Number of Users", example = "5")
    private int numbersOfUsers;

    @Column(name = "creator_id")
    @Schema(description = "Creator ID", example = "1")
    private Long creator;

    @ManyToMany
    @JoinTable(name = "team_user",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @Schema(description = "Users")
    private List<User> users;

}
