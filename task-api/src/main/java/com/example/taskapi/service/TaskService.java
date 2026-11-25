package com.example.taskapi.service;

import com.example.taskapi.dto.CreateTaskRequest;
import com.example.taskapi.dto.TaskResponse;
import com.example.taskapi.dto.UpdateTaskRequest;
import com.example.taskapi.exception.TaskNotFoundException;
import com.example.taskapi.model.Task;
import com.example.taskapi.model.TaskStatus;
import com.example.taskapi.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Business logic lives here. Each public method is one transaction. */
@Service
@Transactional(readOnly = true)
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional
    public TaskResponse create(CreateTaskRequest request) {
        Task task = new Task(request.title(), request.description(), request.dueDate());
        return TaskResponse.from(taskRepository.save(task));
    }

    public Page<TaskResponse> list(TaskStatus status, Pageable pageable) {
        Page<Task> tasks = (status == null)
                ? taskRepository.findAll(pageable)
                : taskRepository.findByStatus(status, pageable);
        return tasks.map(TaskResponse::from);
    }

    public TaskResponse get(Long id) {
        return TaskResponse.from(findOrThrow(id));
    }

    @Transactional
    public TaskResponse update(Long id, UpdateTaskRequest request) {
        Task task = findOrThrow(id);
        task.update(request.title(), request.description(), request.status(), request.dueDate());
        return TaskResponse.from(task); // changes are flushed automatically at commit
    }

    @Transactional
    public void delete(Long id) {
        taskRepository.delete(findOrThrow(id));
    }

    private Task findOrThrow(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }
}