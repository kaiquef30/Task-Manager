package com.project.task.manager.dto;

import com.project.task.manager.enums.TaskStatus;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Hidden
public class TaskDTO {

    public TaskDTO() {
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

}
