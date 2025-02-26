package com.hafsa.ecommerce.controller;

import com.hafsa.ecommerce.model.User;
import com.hafsa.ecommerce.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
        User user = authService.registerUser(
                signupRequest.getEmail(),
                signupRequest.getPassword(),
                User.Role.CUSTOMER
        );

        return ResponseEntity.ok("Utilisateur enregistré avec succès!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authService.authenticateUser(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );

        // Vous pourriez ajouter un JWT token ici pour une authentification sans état
        Map<String, Object> response = new HashMap<>();
        response.put("email", loginRequest.getEmail());
        response.put("role", authentication.getAuthorities());

        return ResponseEntity.ok(response);
    }
}

class SignupRequest {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}

class LoginRequest {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}