package com.example.projet321;

import com.example.projet321.entity.Task;
import com.example.projet321.entity.User;
import com.example.projet321.model.Role;
import com.example.projet321.model.TaskStatus;
import com.example.projet321.repository.TaskRepository;
import com.example.projet321.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;


class Projet321ApplicationTests {
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private User user;
    private Task task;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Créer un utilisateur pour les tests
        user = new User();
        user.setId(1L);
        user.setNom("Test User");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole(Role.USER);

        // Créer une tâche pour les tests
        task = new Task();
        task.setId(1L);
        task.setTitre("Test Task");
        task.setDescription("Test Description");
        task.setStatus(TaskStatus.À_FAIRE);
        task.setAssignedUser(user);

        // Mock du comportement du repository
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void isUserAssignedToTask_shouldReturnTrue_whenUserIsAssigned() {
        // Act
        boolean result = taskService.isUserAssignedToTask(user, task);

        // Assert
        assertTrue(result);
    }

    @Test
    void updateTaskStatus_shouldChangeStatus() {
        // Act
        Task updatedTask = taskService.updateTaskStatus(task, TaskStatus.EN_COURS);

        // Assert
        assertEquals(TaskStatus.EN_COURS, updatedTask.getStatus());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void isUserAssignedToTask_shouldReturnFalse_whenUserIsNotAssigned() {
        // Arrange
        User anotherUser = new User();
        anotherUser.setId(2L);

        // Act
        boolean result = taskService.isUserAssignedToTask(anotherUser, task);

        // Assert
        assertFalse(result);
    }
}