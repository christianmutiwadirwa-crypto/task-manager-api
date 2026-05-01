package com.rooney.taskmanager.dto;

import com.rooney.taskmanager.model.Priority;
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
}
