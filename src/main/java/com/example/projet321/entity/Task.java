package com.example.projet321.entity;
import com.example.projet321.model.TaskStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titre;
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.À_FAIRE;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User assignedUser;

    public Task(String titre, String description, TaskStatus status, User assignedUser) {
        this.titre = titre;
        this.description = description;
        this.status = status;
        this.assignedUser = assignedUser;
    }

}
