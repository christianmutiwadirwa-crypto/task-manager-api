package com.rooney.taskmanager.service;


import com.rooney.taskmanager.dto.TaskDTO;
import com.rooney.taskmanager.exception.ResourceNotFoundException;
import com.rooney.taskmanager.model.Task;
import com.rooney.taskmanager.model.Priority;
import com.rooney.taskmanager.model.TaskStatus;
import com.rooney.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

@Service

public class TaskService {
    private final TaskRepository taskRepository;

    public List<Task> getAllTasksUnpaged() {
        return taskRepository.findAll();
    }

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // Create Task
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    // Get All Tasks
    public Page<Task> getAllTasks(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    // Get Task by ID
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
    }

    // Update Task
    public Task updateTask(Long id, Task updatedTask) {
        Task existingTask = getTaskById(id);

        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setPriority(updatedTask.getPriority());
        existingTask.setDueDate(updatedTask.getDueDate());
        existingTask.setCompleted(updatedTask.isCompleted());

        return taskRepository.save(existingTask);
    }

    // Delete Task
    public void deleteTask(Long id) {
        Task task = getTaskById(id);
        taskRepository.delete(task);
    }

    // Filter by Priority
    public Page<Task> getTasksByPriority(Priority priority, Pageable pageable) {
        return taskRepository.findByPriority(priority, pageable);
    }

    public Page<Task> getTasksByCompleted(Boolean completed, Pageable pageable) {
        return taskRepository.findByCompleted(completed, pageable);
    }
    private TaskStatus determineStatus(Task task) {

        if (task.isCompleted()) {
            return TaskStatus.COMPLETED;
        }

        if (task.getDueDate() != null &&
                task.getDueDate().isBefore(LocalDate.now())) {
            return TaskStatus.OVERDUE;
        }

        return TaskStatus.ACTIVE;
    }

    public TaskDTO mapToDTO(Task task) {
        TaskDTO dto = new TaskDTO();

        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setPriority(task.getPriority());
        dto.setDueDate(task.getDueDate());
        dto.setCompleted(task.isCompleted());

        dto.setStatus(determineStatus(task));

        return dto;
    }


    // 🔥 Business Logic: Mark Overdue Tasks
    private void markOverdueTasks(List<Task> tasks) {
        LocalDate today = LocalDate.now();

        for (Task task : tasks) {
            if (!task.isCompleted() &&
                    task.getDueDate() != null &&
                    task.getDueDate().isBefore(today)) {

                System.out.println("Task overdue: " + task.getTitle());
            }
        }
    }
}
