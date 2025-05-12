package com.example.projet321.controller;
import com.example.projet321.dto.MessageResponse;
import com.example.projet321.dto.StatusUpdateRequest;
import com.example.projet321.dto.TaskRequest;
import com.example.projet321.entity.Task;
import com.example.projet321.entity.User;
import com.example.projet321.model.Role;
import com.example.projet321.security.services.UserDetailsImpl;
import com.example.projet321.service.TaskService;
import com.example.projet321.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tasks")
@SecurityRequirement(name = "bearer-jwt")
@Tag(name = "Tasks", description = "API de gestion des tâches")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @PostMapping
    @Operation(summary = "Créer une tâche", description = "Permet à un utilisateur authentifié de créer une nouvelle tâche")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> createTask(@Valid @RequestBody TaskRequest taskRequest) {
        Optional<User> assignedUserOpt = userService.findById(taskRequest.getAssignedUserId());
        if (assignedUserOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Erreur: Utilisateur assigné non trouvé"));
        }

        Task task = taskService.createTask(
                taskRequest.getTitre(),
                taskRequest.getDescription(),
                assignedUserOpt.get()
        );

        return ResponseEntity.ok(task);
    }

    @GetMapping("/me")
    @Operation(summary = "Mes tâches", description = "Récupère les tâches de l'utilisateur connecté")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Task>> getMyTasks() {
        UserDetailsImpl userDetails = getCurrentUser();
        Optional<User> userOpt = userService.findById(userDetails.getId());

        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Task> tasks = taskService.findTasksByUser(userOpt.get());
        return ResponseEntity.ok(tasks);
    }

    @GetMapping
    @Operation(summary = "Toutes les tâches", description = "Récupère toutes les tâches (Admin uniquement)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.findAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Mettre à jour le statut", description = "Met à jour le statut d'une tâche (uniquement par l'utilisateur assigné)")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateTaskStatus(@PathVariable Long id, @Valid @RequestBody StatusUpdateRequest statusRequest) {
        Optional<Task> taskOpt = taskService.findById(id);
        if (taskOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Task task = taskOpt.get();
        UserDetailsImpl userDetails = getCurrentUser();
        Optional<User> userOpt = userService.findById(userDetails.getId());

        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Erreur: Utilisateur non trouvé"));
        }

        User user = userOpt.get();

        // Vérifier si l'utilisateur est assigné à cette tâche ou est admin
        if (!taskService.isUserAssignedToTask(user, task) && user.getRole() != Role.ADMIN) {
            return ResponseEntity.status(403).body(
                    new MessageResponse("Erreur: Vous n'êtes pas autorisé à modifier cette tâche")
            );
        }

        task = taskService.updateTaskStatus(task, statusRequest.getStatus());
        return ResponseEntity.ok(task);
    }

    private UserDetailsImpl getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetailsImpl) authentication.getPrincipal();
    }
}
