package com.projectmicrosoft.microsoft.api.controller.task;


import com.projectmicrosoft.microsoft.api.dto.TaskDTO;
import com.projectmicrosoft.microsoft.api.security.AuthenticatedUser;
import com.projectmicrosoft.microsoft.exception.InvalidAttachmentException;
import com.projectmicrosoft.microsoft.exception.TaskNotFoundException;
import com.projectmicrosoft.microsoft.exception.messages.ClientMessageConfig;
import com.projectmicrosoft.microsoft.exception.messages.TaskMessageConfig;
import com.projectmicrosoft.microsoft.model.Task;
import com.projectmicrosoft.microsoft.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/task")
public class TaskController {

    private final TaskService taskService;

    private final ClientMessageConfig clientMessageConfig;

    private final TaskMessageConfig taskMessageConfig;


    @AuthenticatedUser(requiredRoles = {"ADMIN"})
    @Operation(summary = "Display all tasks")
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> allTasks = taskService.getAllTasks();
        return ResponseEntity.ok(allTasks);
    }

    @AuthenticatedUser(requiredRoles = {"ADMIN"})
    @Operation(summary = "View all tasks for a given user")
    @GetMapping("/user-tasks")
    public ResponseEntity<List<Task>> getAllTasksForCurrentUser(@RequestParam Long userId) {
            List<Task> allTasksForCurrentUser = taskService.getAllTasksForCurrentUser();
            return ResponseEntity.ok(allTasksForCurrentUser);
    }

    @AuthenticatedUser(requiredRoles = {"ADMIN"})
    @Operation(summary = "Create task")
    @ApiResponse(responseCode = "201", description = "Task created successfully.",
            content = {@Content(schema = @Schema(implementation = TaskDTO.class))})
    @ApiResponse(responseCode = "500", description = "Error processing attachments.", content = @Content)
    @ApiResponse(responseCode = "400", description = "Invalid file type or size.", content = @Content)
    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestBody TaskDTO taskDto,
                                        @RequestParam(value = "attachments", required = false)
                                        MultipartFile[] attachments) {
        try {
            Task createdTask = taskService.createTask(taskDto, attachments);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(taskMessageConfig.getErrorProcessingAttachments());
        } catch (InvalidAttachmentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(taskMessageConfig.getInvalidAttachment());
        }
    }

    @AuthenticatedUser(requiredRoles = {"ADMIN"})
    @Operation(summary = "Edit a task")
    @ApiResponse(responseCode = "200", description = "Task edited successfully.",
            content = {@Content(schema = @Schema(implementation = TaskDTO.class))})
    @ApiResponse(responseCode = "500", description = "Error processing attachments.", content = @Content)
    @PutMapping("/update/{taskId}")
    public ResponseEntity<?> updateTask(@PathVariable Long taskId, @RequestBody TaskDTO taskDto,
                                        @RequestParam(value = "attachments", required = false)
                                        MultipartFile[] attachments) {
        try {
            Task updatedTask = taskService.updateTask(taskId, taskDto, attachments);
            return ResponseEntity.status(HttpStatus.OK).body(updatedTask);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(taskMessageConfig.getErrorProcessingAttachments());
        }
    }

    @AuthenticatedUser(requiredRoles = {"USER"})
    @Operation(summary = "Search task by id")
    @ApiResponse(responseCode = "200", description = "Task found successfully.",
            content = @Content)
    @ApiResponse(responseCode = "404", description = "Task not found.", content = @Content)
    @GetMapping("/search/{taskId}")
    public ResponseEntity<?> getTaskById(@PathVariable Long taskId) {
        Task taskFound;
        try {
            taskFound = taskService.getTaskById(taskId);
            return ResponseEntity.status(HttpStatus.OK).body(taskFound);
        } catch (TaskNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(taskMessageConfig.getTaskNotFound());
        }
    }

    @AuthenticatedUser(requiredRoles = {"ADMIN"})
    @Operation(summary = "Delete task")
    @ApiResponse(responseCode = "204", description = "Task deleted successfully.", content = @Content)
    @ApiResponse(responseCode = "404", description = "Task not found.", content = @Content)
    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable Long taskId) {
        try {
            taskService.deleteTask(taskId);
            return ResponseEntity.noContent().build();
        } catch (TaskNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
