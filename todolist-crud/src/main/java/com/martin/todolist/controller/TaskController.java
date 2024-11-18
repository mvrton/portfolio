package com.martin.todolist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.martin.todolist.model.Task;
import com.martin.todolist.service.TaskService;

@Controller
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // Vista de la lista de tareas
    @GetMapping("/tasks/view")
    public String getTasks(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Task> taskPage = taskService.getTasksPage(page, 5);
        model.addAttribute("tasks", taskPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", taskPage.getTotalPages());
        return "tasks";  // Nombre del archivo HTML de la vista
    }

    // Agregar una nueva tarea
    @PostMapping("/tasks")
    public String createTask(@RequestParam String name) {
        Task task = new Task();
        task.setName(name);
        task.setCompleted(false); // Las tareas nuevas no están completadas
        taskService.createTask(task);  // Llama al servicio para guardar la tarea

        return "redirect:/tasks/view";  // Redirige a la vista de las tareas
    }

    //Tarea completada
    @GetMapping("/tasks/complete/{id}")
    public String completeTask(@PathVariable Long id) {
        taskService.completeTask(id);  // Marca como completada
        return "redirect:/tasks/view"; // Redirige a la vista principal
    }

    //Tarea descompletada
    @GetMapping("/tasks/uncomplete/{id}")
    public String uncompleteTask(@PathVariable Long id) {
        taskService.uncompleteTask(id);  // Desmarca como completada
        return "redirect:/tasks/view"; // Redirige a la vista principal
    }

    @GetMapping("/tasks/delete/{id}")
    public String deleteTask(@PathVariable Long id, @RequestParam(defaultValue = "0") int page) {
        taskService.deleteTask(id);

        // Verificar si la página actual tiene más tareas
        int remainingTasks = taskService.getTasksPage(page, 5).getNumberOfElements();
        if (remainingTasks == 0 && page > 0) {
            page--; // Redirigir a la página anterior si la actual queda vacía
        }

        return "redirect:/tasks/view?page=" + page;
}

    @GetMapping("/tasks/edit/{id}")
    public String editTaskForm(@PathVariable Long id, @RequestParam(defaultValue = "0") int page, Model model) {
        Task task = taskService.getTaskById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Tarea no encontrada con ID: " + id));
        model.addAttribute("task", task);
        model.addAttribute("currentPage", page); // Pasar la página actual
        return "edit_task";
}

    @PostMapping("/tasks/update")
    public String updateTask(@RequestParam Long id, @RequestParam String name, @RequestParam(defaultValue = "0") int page) {
            taskService.updateTask(id, name);
            return "redirect:/tasks/view?page=" + page; // Redirige a la página de origen
        }

}
