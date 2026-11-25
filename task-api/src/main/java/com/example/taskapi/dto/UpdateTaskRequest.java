package com.example.taskapi.dto;

import com.example.taskapi.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/** Full replacement of a task (PUT), so title and status are required. */
public record UpdateTaskRequest(@NotBlank @Size(max = 100) String title, @Size(max = 500) String description, @NotNull TaskStatus status, LocalDate dueDate) 
{
}