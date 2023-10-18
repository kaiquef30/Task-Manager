package com.projectmicrosoft.microsoft.api.controller.task;


import com.projectmicrosoft.microsoft.api.dto.TaskDto;
import com.projectmicrosoft.microsoft.api.security.AuthenticatedUser;
import com.projectmicrosoft.microsoft.exception.TaskNotFoundException;
import com.projectmicrosoft.microsoft.exception.messages.ClientMessageConfig;
import com.projectmicrosoft.microsoft.exception.messages.TaskMessageConfig;
import com.projectmicrosoft.microsoft.model.Task;
import com.projectmicrosoft.microsoft.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final ClientMessageConfig clientMessageConfig;
    private final TaskMessageConfig taskMessageConfig;

    public TaskController(TaskService taskService, ClientMessageConfig clientMessageConfig,
                          TaskMessageConfig taskMessageConfig) {
        this.taskService = taskService;
        this.clientMessageConfig = clientMessageConfig;
        this.taskMessageConfig = taskMessageConfig;
    }

    @AuthenticatedUser(requiredRole = "ADMIN")
    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestBody TaskDto taskDto,
                                        @RequestParam(value = "attachments", required = false)
                                        MultipartFile[] attachments) {
        try {
            taskService.createTask(taskDto, attachments);
            return ResponseEntity.status(HttpStatus.CREATED).body(taskMessageConfig.getTaskCreatedSuccessfully());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(taskMessageConfig.getErrorProcessingAttachments());
        }
    }

    @AuthenticatedUser(requiredRole = "ADMIN")
    @PutMapping("/update/{taskId}")
    public ResponseEntity<?> updateTask(@PathVariable Long taskId, @RequestBody TaskDto taskDto,
                                        @RequestParam(value = "attachments", required = false)
                                        MultipartFile[] attachments) {
        try {
            taskService.updateTask(taskId, taskDto, attachments);
            return ResponseEntity.status(HttpStatus.OK).body(taskMessageConfig.getTaskUpdatedSuccessfully());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(taskMessageConfig.getErrorProcessingAttachments());
        } catch (TaskNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(taskMessageConfig.getTaskNotFound());
        }
    }

    @AuthenticatedUser(requiredRole = "USER")
    @GetMapping("/search/{taskId}")
    public ResponseEntity<?> getTaskById(@PathVariable Long taskId) {
        Task taskFound;
        try {
            taskFound = taskService.getTaskById(taskId);
            return ResponseEntity.status(HttpStatus.FOUND).body(taskFound);
        } catch (TaskNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(taskMessageConfig.getTaskNotFound());
        }
    }


}
