package com.projectmicrosoft.microsoft.exception.messages;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "client.message")
public class ClientMessageConfig {

    private String clientSuccessfullyRegistered;
    private String theClientAlreadyExists;
    private String clientNotFound;
    private String clientSuccessfullyDeleted;
    private String clientUpdatedSuccessfully;


    public String getClientSuccessfullyRegistered() {
        return clientSuccessfullyRegistered;
    }

    public void setClientSuccessfullyRegistered(String clientSuccessfullyRegistered) {
        this.clientSuccessfullyRegistered = clientSuccessfullyRegistered;
    }

    public String getTheClientAlreadyExists() {
        return theClientAlreadyExists;
    }

    public void setTheClientAlreadyExists(String theClientAlreadyExists) {
        this.theClientAlreadyExists = theClientAlreadyExists;
    }


    public String getClientNotFound() {
        return clientNotFound;
    }

    public void setClientNotFound(String clientNotFound) {
        this.clientNotFound = clientNotFound;
    }

    public String getClientSuccessfullyDeleted() {
        return clientSuccessfullyDeleted;
    }

    public void setClientSuccessfullyDeleted(String clientSuccessfullyDeleted) {
        this.clientSuccessfullyDeleted = clientSuccessfullyDeleted;
    }

    public String getClientUpdatedSuccessfully() {
        return clientUpdatedSuccessfully;
    }

    public void setClientUpdatedSuccessfully(String clientUpdatedSuccessfully) {
        this.clientUpdatedSuccessfully = clientUpdatedSuccessfully;
    }
}
