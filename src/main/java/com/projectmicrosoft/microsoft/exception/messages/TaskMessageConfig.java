package com.projectmicrosoft.microsoft.exception.messages;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "task.message")
public class TaskMessageConfig {

    private String taskCreatedSuccessfully;

    private String taskDeletedSuccessfully;

    private String taskUpdatedSuccessfully;

    private String taskNotFound;

    private String errorProcessingAttachments;

    private String invalidAttachment;

    public String getInvalidAttachment() {
        return invalidAttachment;
    }

    public void setInvalidAttachment(String invalidAttachment) {
        this.invalidAttachment = invalidAttachment;
    }

    public String getErrorProcessingAttachments() {
        return errorProcessingAttachments;
    }

    public void setErrorProcessingAttachments(String errorProcessingAttachments) {
        this.errorProcessingAttachments = errorProcessingAttachments;
    }

    public String getTaskNotFound() {
        return taskNotFound;
    }

    public void setTaskNotFound(String taskNotFound) {
        this.taskNotFound = taskNotFound;
    }

    public String getTaskCreatedSuccessfully() {
        return taskCreatedSuccessfully;
    }

    public void setTaskCreatedSuccessfully(String taskCreatedSuccessfully) {
        this.taskCreatedSuccessfully = taskCreatedSuccessfully;
    }


    public String getTaskDeletedSuccessfully() {
        return taskDeletedSuccessfully;
    }

    public void setTaskDeletedSuccessfully(String taskDeletedSuccessfully) {
        this.taskDeletedSuccessfully = taskDeletedSuccessfully;
    }

    public String getTaskUpdatedSuccessfully() {
        return taskUpdatedSuccessfully;
    }

    public void setTaskUpdatedSuccessfully(String taskUpdatedSuccessfully) {
        this.taskUpdatedSuccessfully = taskUpdatedSuccessfully;
    }
}
