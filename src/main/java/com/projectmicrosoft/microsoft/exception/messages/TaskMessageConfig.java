package com.projectmicrosoft.microsoft.exception.messages;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "task.message")
public class TaskMessageConfig {

    private String taskCreatedSuccessfully;

    private String taskDeletedSuccessfully;

    private String taskUpdatedSuccessfully;

    private String taskNotFound;

    private String errorProcessingAttachments;

    private String invalidAttachment;

}
