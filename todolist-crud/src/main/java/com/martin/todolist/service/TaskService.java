package com.martin.todolist.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.martin.todolist.model.Task;
import com.martin.todolist.repository.TaskRepository;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // Crear una nueva tarea
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    // Eliminar tarea por ID
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    // Marcar una tarea como completada
    public void completeTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tarea no encontrada"));
        task.setCompleted(true);
        taskRepository.save(task);
    }

    // Desmarcar una tarea como completada
    public void uncompleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tarea no encontrada"));
        task.setCompleted(false);
        taskRepository.save(task);
    }

    // Obtener todas las tareas en formato paginado
    public Page<Task> getTasksPage(int page, int size) {
        return taskRepository.findAll(PageRequest.of(page, size));
    }

    // Obtener una tarea por su ID
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    // Actualizar el nombre de una tarea (edición)
    public void updateTask(Long id, String newName) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tarea no encontrada"));
        task.setName(newName);
        taskRepository.save(task);
    }
}
