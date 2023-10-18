package com.projectmicrosoft.microsoft.service;

import com.projectmicrosoft.microsoft.exception.TaskNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.modelmapper.ModelMapper;

import com.projectmicrosoft.microsoft.repository.TaskRepository;
import com.projectmicrosoft.microsoft.model.Task;
import com.projectmicrosoft.microsoft.api.dto.TaskDto;
import com.projectmicrosoft.microsoft.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;

    public TaskService(TaskRepository taskRepository, ModelMapper modelMapper) {
        this.taskRepository = taskRepository;
        this.modelMapper = modelMapper;
    }


    public Task createTask(TaskDto taskDto, MultipartFile[] attachments) throws IOException {
        Task task = modelMapper.map(taskDto, Task.class);

        Long currentUserId = getCurrentUserId();
        User creator = new User();
        creator.setId(currentUserId);
        task.setAssignee(creator);

        if (attachments != null && attachments.length > 0) {
            List<String> attachmentBase64List = new ArrayList<>();

            for (MultipartFile attachment : attachments) {
                try {
                    byte[] fileBytes = attachment.getBytes();
                    String base64 = Base64.getEncoder().encodeToString(fileBytes);
                    attachmentBase64List.add(base64);
                } catch (IOException e) {
                    throw new IOException();
                }
            }

            task.setAttachments(attachmentBase64List);
        }

        return taskRepository.save(task);
    }



    public Task updateTask(Long taskId, TaskDto taskDto, MultipartFile[] attachments) throws IOException, TaskNotFoundException {
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(TaskNotFoundException::new);

        Task updatedTask = modelMapper.map(taskDto, Task.class);
        updatedTask.setId(existingTask.getId());
        if (attachments != null && attachments.length > 0) {
            List<String> attachmentBase64List = new ArrayList<>();

            for (MultipartFile attachment : attachments) {
                try {
                    byte[] fileBytes = attachment.getBytes();
                    String base64 = Base64.getEncoder().encodeToString(fileBytes);
                    attachmentBase64List.add(base64);
                } catch (IOException e) {
                    throw new IOException();
                }
            }
            updatedTask.setAttachments(attachmentBase64List);
        } else {
            updatedTask.setAttachments(existingTask.getAttachments());
        }
        return taskRepository.save(updatedTask);
    }


    public Task getTaskById(Long taskId) throws TaskNotFoundException {
        return taskRepository.findById(taskId)
                .orElseThrow(TaskNotFoundException::new);
    }


    public void deleteTask(Long taskId) throws TaskNotFoundException {
        if (taskRepository.existsById(taskId)) {
            taskRepository.deleteById(taskId);
        } else {
            throw new TaskNotFoundException();
        }

    }


    public List<Task> getAllTasksForCurrentUser() {
        Long currentUserId = getCurrentUserId();
        return taskRepository.findAllByAssigneeId(currentUserId);
    }



    public Long getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((User) principal).getId();
        } else {
            throw new IllegalStateException();
        }
    }

    private Task getTaskOrThrow(Long taskId) throws TaskNotFoundException {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isPresent()) {
            return taskOptional.get();
        } else {
            throw new TaskNotFoundException();
        }
    }
}
