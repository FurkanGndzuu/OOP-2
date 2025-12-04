package com.furkangunduz.task_manager.controller;

import com.furkangunduz.task_manager.dto.TaskRequest;
import com.furkangunduz.task_manager.entity.User;
import com.furkangunduz.task_manager.service.TaskService;
import com.furkangunduz.task_manager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public String taskPage(Authentication auth, Model model, @RequestParam(required = false) String date) {
        User user = userService.findByUsername(auth.getName());
        
        if (date != null && !date.isEmpty()) {
            LocalDate selectedDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
            model.addAttribute("tasks", taskService.getUserTasksByDate(user, selectedDate));
            model.addAttribute("selectedDate", date);
        } else {
            model.addAttribute("tasks", taskService.getUserTasks(user));
        }
        
        model.addAttribute("taskRequest", new TaskRequest());
        return "tasks";
    }
    
    @PostMapping
    public String createTask(@Valid @ModelAttribute TaskRequest request, BindingResult result, Authentication auth, Model model) {
        if (result.hasErrors()) {
            User user = userService.findByUsername(auth.getName());
            model.addAttribute("tasks", taskService.getUserTasks(user));
            return "tasks";
        }
        User user = userService.findByUsername(auth.getName());
        taskService.createTask(request, user);
        return "redirect:/tasks";
    }
    
    @PostMapping("/{id}/delete")
    public String deleteTask(@PathVariable Long id, Authentication auth) {
        User user = userService.findByUsername(auth.getName());
        taskService.deleteTask(id, user);
        return "redirect:/tasks";
    }
    
    @PostMapping("/{id}/update")
    public String updateTask(@PathVariable Long id, @Valid @ModelAttribute TaskRequest request, BindingResult result, Model model, Authentication auth) {
        if (result.hasErrors()) {
            User user = userService.findByUsername(auth.getName());
            model.addAttribute("tasks", taskService.getUserTasks(user));
            return "tasks";
        }
        User user = userService.findByUsername(auth.getName());
        taskService.updateTask(id, request, user);
        return "redirect:/tasks";
    }
}