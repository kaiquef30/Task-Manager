package com.projectmicrosoft.microsoft.model;


import com.projectmicrosoft.microsoft.enums.TaskPriority;
import com.projectmicrosoft.microsoft.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tasks")
public class Task {

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



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<String> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<String> attachments) {
        this.attachments = attachments;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}
