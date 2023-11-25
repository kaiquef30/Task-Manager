package com.projectmicrosoft.microsoft.service;

import com.projectmicrosoft.microsoft.api.dto.TaskDTO;
import com.projectmicrosoft.microsoft.exception.InvalidAttachmentException;
import com.projectmicrosoft.microsoft.exception.TaskNotFoundException;
import com.projectmicrosoft.microsoft.model.Task;
import com.projectmicrosoft.microsoft.model.User;
import com.projectmicrosoft.microsoft.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;


    @Cacheable(value = "taskCache", key = "'allTasks'")
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @CacheEvict(value = "taskCache", key = "'allTasks'", allEntries = true)
    public Task createTask(TaskDTO taskDto, MultipartFile[] attachments) throws IOException, InvalidAttachmentException {
        Task task = modelMapper.map(taskDto, Task.class);

        Long currentUserId = getCurrentUserId();
        User creator = new User();
        creator.setId(currentUserId);
        task.setAssignee(creator);

        if (attachments != null && attachments.length > 0) {
            List<String> attachmentBase64List = new ArrayList<>();

            for (MultipartFile attachment : attachments) {
                try {
                    if (isValidAttachment(attachment)) {
                        byte[] fileBytes = attachment.getBytes();
                        String base64 = Base64.getEncoder().encodeToString(fileBytes);
                        attachmentBase64List.add(base64);
                    } else {
                        throw new InvalidAttachmentException();
                    }
                } catch (IOException e) {
                    throw new IOException();
                }
            }

            task.setAttachments(attachmentBase64List);
        }
        return taskRepository.save(task);
    }

    @CacheEvict(value = "taskCache", key = "'allTasks'", allEntries = true)
    public Task updateTask(Long taskId, TaskDTO taskDto, MultipartFile[] attachments) throws IOException {
        Task updatedTask = modelMapper.map(taskDto, Task.class);
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
        }
        return taskRepository.save(updatedTask);
    }

    @Cacheable(value = "taskCache", key = "#taskId")
    public Task getTaskById(Long taskId) throws TaskNotFoundException {
        return taskRepository.findById(taskId)
                .orElseThrow(TaskNotFoundException::new);
    }

    @CacheEvict(value = "taskCache", key = "#taskId")
    public void deleteTask(Long taskId) throws TaskNotFoundException {
        if (taskRepository.existsById(taskId)) {
            taskRepository.deleteById(taskId);
        } else {
            throw new TaskNotFoundException();
        }
    }

    @Cacheable(value = "taskCache", key = "'tasksForCurrentUser'")
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

    private boolean isValidAttachment(MultipartFile attachment) {
        if (attachment == null) {
            return false;
        }

        long maxSizeBytes = 10 * 1024 * 1024;
        if (attachment.getSize() > maxSizeBytes) {
            return false;
        }

        String contentType = attachment.getContentType();

        if (contentType != null) {
            return contentType.startsWith("image/") || contentType.equals("application/pdf") ||
                    contentType.startsWith("video/");
        }

        return false;
    }
}
