package com.furkangunduz.task_manager.repository;

import com.furkangunduz.task_manager.entity.Task;
import com.furkangunduz.task_manager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUser(User user);
    List<Task> findByUserOrderByCreatedAtDesc(User user);
}
