package com.example.projet321.controller;
import com.example.projet321.dto.JwtResponse;
import com.example.projet321.dto.LoginRequest;
import com.example.projet321.dto.MessageResponse;
import com.example.projet321.dto.SignupRequest;
import com.example.projet321.entity.User;
import com.example.projet321.repository.UserRepository;
import com.example.projet321.security.jwt.JwtUtils;
import com.example.projet321.security.services.UserDetailsImpl;
import com.example.projet321.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "API d'authentification des utilisateurs")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    @Operation(summary = "Connexion utilisateur", description = "Permet à un utilisateur de se connecter et obtenir un token JWT")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String role = roles.get(0).replace("ROLE_", "");

        return ResponseEntity.ok(
                new JwtResponse(
                        jwt,
                        userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        role)
        );
    }

    @PostMapping("/signup")
    @Operation(summary = "Inscription utilisateur", description = "Permet à un nouvel utilisateur de s'inscrire avec le rôle USER")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userService.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Erreur: Email déjà utilisé!"));
        }

        // Create new user account with USER role
        User user = userService.createUser(
                signUpRequest.getNom(),
                signUpRequest.getEmail(),
                signUpRequest.getPassword()
        );

        return ResponseEntity.ok(new MessageResponse("Utilisateur enregistré avec succès!"));
    }
}
