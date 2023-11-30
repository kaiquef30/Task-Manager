package com.project.task.manager.exception.messages;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "team.message")
public class TeamMessagesConfig {

    private String userSuccessfullyAssociateWithTeam;

    private String userNotFound;

    private String teamNotFound;

    private String teamAlreadyExists;

    private String teamDeletedSucessfully;

    private String userIsAlreadyOnTheTeam;

    private String userAddedSuccessfully;

    private String userRemovedSuccessfully;

}
