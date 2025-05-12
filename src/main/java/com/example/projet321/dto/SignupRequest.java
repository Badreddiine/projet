package com.example.projet321.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequest {

    private String nom;


    @Email
    private String email;


    @Size(min = 6, max = 40)
    private String password;
}

