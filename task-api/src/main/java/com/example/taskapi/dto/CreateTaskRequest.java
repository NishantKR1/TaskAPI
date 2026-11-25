package com.example.taskapi.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record CreateTaskRequest(@NotBlank @Size(max = 100) String title,@Size(max = 500) String description, @FutureOrPresent LocalDate dueDate) 
{
        
}