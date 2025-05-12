package com.example.projet321.dto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class TaskRequest {

    private String titre;


    private String description;

    @NotNull
    private Long assignedUserId;
}