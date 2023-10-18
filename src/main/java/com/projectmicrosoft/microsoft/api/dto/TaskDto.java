package com.projectmicrosoft.microsoft.api.dto;

import com.projectmicrosoft.microsoft.enums.TaskStatus;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

@Hidden
public class TaskDto {

    public TaskDto() {
        this.status = TaskStatus.PENDING;
    }

    @NotNull
    @NotBlank
    private String title;
    @NotNull
    @NotBlank
    private String description;
    @NotNull
    private Date dueDate;

    private List<String> attachments;

    @NotNull
    private TaskStatus status;

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public List<String> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<String> attachments) {
        this.attachments = attachments;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}
