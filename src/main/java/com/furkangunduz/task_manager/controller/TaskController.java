package com.furkangunduz.task_manager.controller;

import com.furkangunduz.task_manager.dto.TaskRequest;
import com.furkangunduz.task_manager.entity.User;
import com.furkangunduz.task_manager.service.TaskService;
import com.furkangunduz.task_manager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public String taskPage(Authentication auth, Model model) {
        User user = userService.findByUsername(auth.getName());
        model.addAttribute("tasks", taskService.getUserTasks(user));
        model.addAttribute("taskRequest", new TaskRequest());
        return "tasks";
    }
    
    @PostMapping
    public String createTask(@ModelAttribute TaskRequest request, Authentication auth) {
        User user = userService.findByUsername(auth.getName());
        taskService.createTask(request, user);
        return "redirect:/tasks";
    }
    
    @PostMapping("/{id}/delete")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/tasks";
    }
    
    @PostMapping("/{id}/update")
    public String updateTask(@PathVariable Long id, @ModelAttribute TaskRequest request) {
        taskService.updateTask(id, request);
        return "redirect:/tasks";
    }
}