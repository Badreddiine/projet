package com.example.projet321.service;
import com.example.projet321.entity.User;
import com.example.projet321.entity.Task;
import com.example.projet321.model.TaskStatus;
import com.example.projet321.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public Task createTask(String titre, String description, User assignedUser) {
        Task task = new Task();
        task.setTitre(titre);
        task.setDescription(description);
        task.setStatus(TaskStatus.À_FAIRE);
        task.setAssignedUser(assignedUser);
        return taskRepository.save(task);
    }

    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> findTasksByUser(User user) {
        return taskRepository.findByAssignedUser(user);
    }

    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    public Task updateTaskStatus(Task task, TaskStatus newStatus) {
        task.setStatus(newStatus);
        return taskRepository.save(task);
    }

    public boolean isUserAssignedToTask(User user, Task task) {
        return task.getAssignedUser() != null && task.getAssignedUser().getId().equals(user.getId());
    }
}
