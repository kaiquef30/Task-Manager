package com.projectmicrosoft.microsoft.exception.messages;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "client.message")
public class ClientMessageConfig {

    private String clientSuccessfullyRegistered;

    private String theClientAlreadyExists;

    private String clientNotFound;

    private String clientSuccessfullyDeleted;

    private String clientUpdatedSuccessfully;

}
