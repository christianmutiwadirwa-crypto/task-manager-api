package com.rooney.taskmanager.repository;

import com.rooney.taskmanager.model.Task;
import com.rooney.taskmanager.model.Priority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long>{
    List<Task> findByPriority(Priority priority);

    List<Task> findByCompleted(boolean completed);
}
