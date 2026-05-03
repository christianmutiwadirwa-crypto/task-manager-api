package com.rooney.taskmanager.dto;

import com.rooney.taskmanager.model.Priority;
import com.rooney.taskmanager.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data

public class TaskDTO {
    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    private Priority priority;

    private LocalDate dueDate;

    private boolean completed;

    private TaskStatus status;

    private Long id;
}
