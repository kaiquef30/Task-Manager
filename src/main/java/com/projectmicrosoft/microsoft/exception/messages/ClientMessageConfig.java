package com.projectmicrosoft.microsoft.exception.messages;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "client.message")
public class ClientMessageConfig {

    private String clientSuccessfullyRegistered;
    private String theClientAlreadyExists;
    private String needToBeLoggedInToCreateAClient;
    private String needToBeLoggedInToDeleteAClient;
    private String youDoNotHavePermissionToEditAClient;
    private String youDoNotHavePermissionToCreateAClient;
    private String clientNotFound;
    private String clientSuccessfullyDeleted;
    private String clientUpdatedSuccessfully;
    private String youDoNotHavePermissionToDeleteAClient;

    public String getYouDoNotHavePermissionToDeleteAClient() {
        return youDoNotHavePermissionToDeleteAClient;
    }

    public void setYouDoNotHavePermissionToDeleteAClient(String youDoNotHavePermissionToDeleteAClient) {
        this.youDoNotHavePermissionToDeleteAClient = youDoNotHavePermissionToDeleteAClient;
    }

    public String getYouDoNotHavePermissionToCreateAClient() {
        return youDoNotHavePermissionToCreateAClient;
    }

    public void setYouDoNotHavePermissionToCreateAClient(String youDoNotHavePermissionToCreateAClient) {
        this.youDoNotHavePermissionToCreateAClient = youDoNotHavePermissionToCreateAClient;
    }

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

    public String getNeedToBeLoggedInToCreateAClient() {
        return needToBeLoggedInToCreateAClient;
    }

    public void setNeedToBeLoggedInToCreateAClient(String needToBeLoggedInToCreateAClient) {
        this.needToBeLoggedInToCreateAClient = needToBeLoggedInToCreateAClient;
    }

    public String getNeedToBeLoggedInToDeleteAClient() {
        return needToBeLoggedInToDeleteAClient;
    }

    public void setNeedToBeLoggedInToDeleteAClient(String needToBeLoggedInToDeleteAClient) {
        this.needToBeLoggedInToDeleteAClient = needToBeLoggedInToDeleteAClient;
    }

    public String getYouDoNotHavePermissionToEditAClient() {
        return youDoNotHavePermissionToEditAClient;
    }

    public void setYouDoNotHavePermissionToEditAClient(String youDoNotHavePermissionToEditAClient) {
        this.youDoNotHavePermissionToEditAClient = youDoNotHavePermissionToEditAClient;
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
