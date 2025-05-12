package com.example.projet321.dto;
import com.example.projet321.model.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StatusUpdateRequest {
    @NotNull
    private TaskStatus status;
}
