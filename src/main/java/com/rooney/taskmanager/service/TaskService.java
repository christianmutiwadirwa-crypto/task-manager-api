package com.rooney.taskmanager.service;


import com.rooney.taskmanager.exception.ResourceNotFoundException;
import com.rooney.taskmanager.model.Task;
import com.rooney.taskmanager.model.Priority;
import com.rooney.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service

public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // Create Task
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    // Get All Tasks
    public List<Task> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        markOverdueTasks(tasks);
        return tasks;
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
    public List<Task> getTasksByPriority(Priority priority) {
        return taskRepository.findByPriority(priority);
    }

    // Filter by Completion
    public List<Task> getTasksByCompleted(boolean completed) {
        return taskRepository.findByCompleted(completed);
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
