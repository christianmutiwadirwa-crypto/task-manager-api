package com.rooney.taskmanager.repository;

import com.rooney.taskmanager.model.Task;
import com.rooney.taskmanager.model.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByPriority(Priority priority, Pageable pageable);

    Page<Task> findByCompleted(Boolean completed, Pageable pageable);
}