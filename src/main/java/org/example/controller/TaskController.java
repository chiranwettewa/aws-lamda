package org.example.controller;

import org.example.dto.Task;
import org.example.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"https://podiweda.com", "https://www.podiweda.com"}, allowCredentials = "true")
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private static final Logger log = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task, Authentication authentication) {
        log.info("Creating task for user.");
        String userId = getUserIdFromAuth(authentication);
        task.setUserId(userId);
        Task createdTask = taskService.createTask(task);
        log.info("Task created: {}", createdTask.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(Authentication authentication) {
        log.info("Fetching all tasks");
        String userId = getUserIdFromAuth(authentication);
        log.info("User ID: {}", userId);
        List<Task> tasks = taskService.getTasksByUserId(userId);
        log.info("Found {} tasks", tasks.size());
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Task>> getAllPublicTasks() {
        log.info("Fetching all public tasks");
        List<Task> tasks = taskService.getAllTasks();
        log.info("Found {} public tasks", tasks.size());
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable String id, Authentication authentication) {
        log.info("Fetching task: {}", id);
        String userId = getUserIdFromAuth(authentication);
        Task task = taskService.getTaskById(id, userId);
        if (task == null) {
            log.warn("Task not found: {}", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable String id, @RequestBody Task task, Authentication authentication) {
        log.info("Updating task: {}", id);
        String userId = getUserIdFromAuth(authentication);
        task.setId(id);
        task.setUserId(userId);
        Task updatedTask = taskService.updateTask(task);
        if (updatedTask == null) {
            log.warn("Task not found for update: {}", id);
            return ResponseEntity.notFound().build();
        }
        log.info("Task updated: {}", id);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable String id, Authentication authentication) {
        log.info("Deleting task: {}", id);
        String userId = getUserIdFromAuth(authentication);
        boolean deleted = taskService.deleteTask(id, userId);
        if (!deleted) {
            log.warn("Task not found for deletion: {}", id);
            return ResponseEntity.notFound().build();
        }
        log.info("Task deleted: {}", id);
        return ResponseEntity.noContent().build();
    }

    private String getUserIdFromAuth(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        return jwt.getSubject();
    }
}
