package com.projectmicrosoft.microsoft.exception.messages;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "team.message")
public class TeamMessagesConfig {

    private String userSuccessfullyAssociateWithTeam;

    private String userNotFound;

    private String teamNotFound;

    private String teamCreatedSuccessfully;

    private String teamAlreadyExists;

    private String teamEditedSucessfully;

    private String teamDeletedSucessfully;

    private String userIsAlreadyOnTheTeam;

    private String userAddedSuccessfully;

    private String userRemovedSuccessfully;

    public String getUserRemovedSuccessfully() {
        return userRemovedSuccessfully;
    }

    public void setuserRemovedSuccessfully(String userRemovedSuccessfully) {
        this.userRemovedSuccessfully = userRemovedSuccessfully;
    }

    public String getUserAddedSuccessfully() {
        return userAddedSuccessfully;
    }

    public void setUserAddedSuccessfully(String userAddedSuccessfully) {
        this.userAddedSuccessfully = userAddedSuccessfully;
    }

    public String getUserIsAlreadyOnTheTeam() {
        return userIsAlreadyOnTheTeam;
    }

    public void setUserIsAlreadyOnTheTeam(String userIsAlreadyOnTheTeam) {
        this.userIsAlreadyOnTheTeam = userIsAlreadyOnTheTeam;
    }

    public String getTeamDeletedSucessfully() {
        return teamDeletedSucessfully;
    }

    public void setTeamDeletedSucessfully(String teamDeletedSucessfully) {
        this.teamDeletedSucessfully = teamDeletedSucessfully;
    }

    public String getTeamEditedSucessfully() {
        return teamEditedSucessfully;
    }

    public void setTeamEditedSucessfully(String teamEditedSucessfully) {
        this.teamEditedSucessfully = teamEditedSucessfully;
    }

    public String getUserSuccessfullyAssociateWithTeam() {
        return userSuccessfullyAssociateWithTeam;
    }


    public String getUserNotFound() {
        return userNotFound;
    }


    public String getTeamNotFound() {
        return teamNotFound;
    }


    public String getTeamCreatedSuccessfully() {
        return teamCreatedSuccessfully;
    }

    public String getTeamAlreadyExists() {
        return teamAlreadyExists;
    }

    public void setUserSuccessfullyAssociateWithTeam(String userSuccessfullyAssociateWithTeam) {
        this.userSuccessfullyAssociateWithTeam = userSuccessfullyAssociateWithTeam;
    }

    public void setUserNotFound(String userNotFound) {
        this.userNotFound = userNotFound;
    }

    public void setTeamNotFound(String teamNotFound) {
        this.teamNotFound = teamNotFound;
    }

    public void setTeamCreatedSuccessfully(String teamCreatedSuccessfully) {
        this.teamCreatedSuccessfully = teamCreatedSuccessfully;
    }

    public void setTeamAlreadyExists(String teamAlreadyExists) {
        this.teamAlreadyExists = teamAlreadyExists;
    }
}
