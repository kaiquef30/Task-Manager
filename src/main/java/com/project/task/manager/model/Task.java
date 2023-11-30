package com.project.task.manager.model;


import com.project.task.manager.enums.TaskPriority;
import com.project.task.manager.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "tasks")
public class Task extends RepresentationModel<Task> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Task ID", example = "1")
    private Long id;

    @Column(name = "title")
    @Schema(description = "Title of the task", example = "Complete Project Report")
    private String title;

    @Column(name = "description")
    @Schema(description = "Description of the task", example = "Write a detailed report on project progress.")
    private String description;

    @Column(name = "attachments")
    @ElementCollection
    @Schema(description = "Attachments for the task", example = "attachment1.jpg, attachment2.pdf")
    private List<String> attachments;

    @Column(name = "due_date")
    @Schema(description = "Due Date of the task", example = "2023-11-30")
    private Date dueDate;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "assignee_id")
    @Schema(description = "Assigned User")
    private User assignee;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    @Schema(description = "Priority of the task", example = "HIGH")
    private TaskPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Schema(description = "Status of the task", example = "IN_PROGRESS")
    private TaskStatus status;

}
