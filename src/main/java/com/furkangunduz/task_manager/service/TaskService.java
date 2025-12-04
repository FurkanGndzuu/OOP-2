package com.furkangunduz.task_manager.service;

import com.furkangunduz.task_manager.dto.TaskRequest;
import com.furkangunduz.task_manager.entity.Task;
import com.furkangunduz.task_manager.entity.User;
import com.furkangunduz.task_manager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {
    
    @Autowired
    private TaskRepository taskRepository;
    
    public List<Task> getUserTasks(User user) {
        return taskRepository.findByUserOrderByCreatedAtDesc(user);
    }
    
    public List<Task> getUserTasksByDate(User user, LocalDate date) {
        return taskRepository.findByUserAndDate(user, date);
    }
    
    public Task createTask(TaskRequest request, User user) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus() != null ? request.getStatus() : "TODO");
        task.setUser(user);
        return taskRepository.save(task);
    }
    
    public Task updateTask(Long id, TaskRequest request, User user) {
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Task not found"));
        
        if (!task.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        return taskRepository.save(task);
    }
    
    public void deleteTask(Long id, User user) {
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Task not found"));
        
        if (!task.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        
        taskRepository.deleteById(id);
    }
}