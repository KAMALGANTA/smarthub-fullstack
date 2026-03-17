package com.noticeboard.controller;

import com.noticeboard.dto.LoginDTO;
import com.noticeboard.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok(authService.login(loginDTO));
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String email    = request.get("email");
        String fullName = request.get("fullName");
        String password = request.get("password");

        if (username == null || email == null || password == null)
            throw new RuntimeException("username, email, and password are required");

        return ResponseEntity.status(201).body(
                authService.register(username, email, fullName, password));
    }

    @GetMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateToken() {
        return ResponseEntity.ok(Map.of("valid", true, "message", "Token is valid"));
    }
}