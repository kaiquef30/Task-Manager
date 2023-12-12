package com.project.task.manager.api.resources.task;


import com.project.task.manager.dto.TaskDTO;
import com.project.task.manager.security.AuthenticatedUser;
import com.project.task.manager.exception.InvalidAttachmentException;
import com.project.task.manager.exception.TaskNotFoundException;
import com.project.task.manager.exception.messages.ClientMessageConfig;
import com.project.task.manager.exception.messages.TaskMessageConfig;
import com.project.task.manager.model.Task;
import com.project.task.manager.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/task")
public class TaskResource {

    private final TaskService taskService;

    private final ClientMessageConfig clientMessageConfig;

    private final TaskMessageConfig taskMessageConfig;


    @AuthenticatedUser(requiredRoles = {"ADMIN"})
    @Operation(summary = "Display all tasks")
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(
            @PageableDefault(
                    sort = "id",
                    direction = Sort.Direction.ASC,
                    size = 3
            )Pageable page) {
        List<Task> allTasks = taskService.getAllTasks(page);
        return ResponseEntity.ok(allTasks.stream().map(task ->
                task.add(linkTo(methodOn(TaskResource.class).getTaskById(task.getId())).withRel("go to task")))
                .toList());
    }

    @AuthenticatedUser(requiredRoles = {"ADMIN"})
    @Operation(summary = "View all tasks for a given user")
    @GetMapping("/{userId}")
    public ResponseEntity<List<Task>> getAllTasksForCurrentUser(@PathVariable Long userId) {
            List<Task> allTasksForCurrentUser = taskService.getAllTasksForCurrentUser();
            return ResponseEntity.ok(allTasksForCurrentUser.stream().map(task ->
                    task.add(linkTo(methodOn(TaskResource.class).getAllTasks(Pageable.unpaged()))
                            .withRel("Return to all tasks")))
                    .toList());
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

    @AuthenticatedUser(requiredRoles = {"ADMIN"})
    @Operation(summary = "Search task by id")
    @ApiResponse(responseCode = "200", description = "Task found successfully.",
            content = @Content)
    @ApiResponse(responseCode = "404", description = "Task not found.", content = @Content)
    @GetMapping("/search/{taskId}")
    public ResponseEntity<?> getTaskById(@PathVariable Long taskId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(taskService.getTaskById(taskId)
                    .add(linkTo(methodOn(TaskResource.class).getAllTasks(Pageable.unpaged()))
                            .withRel("Return to all tasks")));
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
