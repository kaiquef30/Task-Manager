package com.projectmicrosoft.microsoft.model;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "teams")
public class Team {

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

    public int getNumbersOfUsers() {
        return numbersOfUsers;
    }

    public void setNumbersOfUsers(int numbersOfUsers) {
        this.numbersOfUsers = numbersOfUsers;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
