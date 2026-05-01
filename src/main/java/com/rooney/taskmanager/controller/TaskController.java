package com.rooney.taskmanager.controller;

import com.rooney.taskmanager.dto.TaskDTO;
import com.rooney.taskmanager.model.Task;
import com.rooney.taskmanager.model.Priority;
import com.rooney.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")

public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // Create Task
    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody TaskDTO taskDTO) {

        Task task = Task.builder()
                .title(taskDTO.getTitle())
                .description(taskDTO.getDescription())
                .priority(taskDTO.getPriority())
                .dueDate(taskDTO.getDueDate())
                .completed(taskDTO.isCompleted())
                .build();

        return ResponseEntity.ok(taskService.createTask(task));
    }

    // Get All Tasks (with optional filters)
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) Boolean completed) {

        if (priority != null) {
            return ResponseEntity.ok(taskService.getTasksByPriority(priority));
        }

        if (completed != null) {
            return ResponseEntity.ok(taskService.getTasksByCompleted(completed));
        }

        return ResponseEntity.ok(taskService.getAllTasks());
    }

    // Get Task by ID
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    // Update Task
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id,
                                           @Valid @RequestBody TaskDTO taskDTO) {

        Task task = Task.builder()
                .title(taskDTO.getTitle())
                .description(taskDTO.getDescription())
                .priority(taskDTO.getPriority())
                .dueDate(taskDTO.getDueDate())
                .completed(taskDTO.isCompleted())
                .build();

        return ResponseEntity.ok(taskService.updateTask(id, task));
    }

    // Delete Task
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
