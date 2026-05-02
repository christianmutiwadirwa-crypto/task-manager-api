package com.rooney.taskmanager.controller;

import com.rooney.taskmanager.dto.TaskDTO;
import com.rooney.taskmanager.dto.PagedResponse;
import com.rooney.taskmanager.model.Task;
import com.rooney.taskmanager.model.Priority;
import com.rooney.taskmanager.model.TaskStatus;
import com.rooney.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

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
    public ResponseEntity<PagedResponse<TaskDTO>> getTasks(
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) Boolean completed,
            @RequestParam(required = false) TaskStatus status,
            @PageableDefault(size = 5) Pageable pageable) {

        // 1. Fetch ALL tasks (no pagination)
        List<Task> tasks = taskService.getAllTasksUnpaged();

        // 2. Convert to DTO (compute status)
        List<TaskDTO> dtoList = tasks.stream()
                .map(taskService::mapToDTO)
                .toList();

        // 3. Apply filters
        if (priority != null) {
            dtoList = dtoList.stream()
                    .filter(t -> t.getPriority() == priority)
                    .toList();
        }

        if (completed != null) {
            dtoList = dtoList.stream()
                    .filter(t -> t.isCompleted() == completed)
                    .toList();
        }

        if (status != null) {
            dtoList = dtoList.stream()
                    .filter(t -> t.getStatus() == status)
                    .toList();
        }

        // 4. Manual pagination
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), dtoList.size());

        List<TaskDTO> pagedList = dtoList.subList(start, end);

        // 5. Build response
        PagedResponse<TaskDTO> response = new PagedResponse<>(
                pagedList,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                dtoList.size(),
                (int) Math.ceil((double) dtoList.size() / pageable.getPageSize())
        );

        return ResponseEntity.ok(response);
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
