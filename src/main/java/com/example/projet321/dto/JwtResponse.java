package com.example.projet321.dto;
import lombok.Data;
@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String nom;
    private String email;
    private String role;

    public JwtResponse(String accessToken, Long id, String nom, String email, String role) {
        this.token = accessToken;
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.role = role;
    }
}