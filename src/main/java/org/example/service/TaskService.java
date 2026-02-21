package org.example.service;

import org.example.dto.Task;
import org.example.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public Task createTask(Task task) {
        task.setId(UUID.randomUUID().toString());
        task.setCreatedAt(System.currentTimeMillis());
        task.setUpdatedAt(System.currentTimeMillis());
        return taskRepository.save(task);
    }

    public List<Task> getTasksByUserId(String userId) {
        return taskRepository.findByUserId(userId);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(String id, String userId) {
        return taskRepository.findById(userId, id);
    }

    public Task updateTask(Task task) {
        Task existingTask = taskRepository.findById(task.getUserId(), task.getId());
        if (existingTask != null) {
            task.setCreatedAt(existingTask.getCreatedAt());
            task.setUpdatedAt(System.currentTimeMillis());
            return taskRepository.save(task);
        }
        return null;
    }

    public boolean deleteTask(String id, String userId) {
        Task task = taskRepository.findById(userId, id);
        if (task != null) {
            taskRepository.delete(userId, id);
            return true;
        }
        return false;
    }
}
