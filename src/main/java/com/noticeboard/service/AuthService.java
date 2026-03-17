package com.noticeboard.service;

import com.noticeboard.dto.LoginDTO;
import com.noticeboard.model.User;
import com.noticeboard.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public Map<String, Object> login(LoginDTO loginDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getUsernameOrEmail(), loginDTO.getPassword()));

        UserDetails userDetails = userService.loadUserByUsername(loginDTO.getUsernameOrEmail());
        String token = jwtUtil.generateToken(userDetails);
        User user = userService.getUserByUsername(userDetails.getUsername());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("tokenType", "Bearer");
        response.put("expiresIn", jwtUtil.getExpirationTime());
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("role", user.getRole());
        response.put("fullName", user.getFullName());
        return response;
    }

    public Map<String, Object> register(String username, String email,
                                         String fullName, String password) {
        User user = userService.registerUser(username, email, fullName, password);
        UserDetails userDetails = userService.loadUserByUsername(username);
        String token = jwtUtil.generateToken(userDetails);

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("tokenType", "Bearer");
        response.put("expiresIn", jwtUtil.getExpirationTime());
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("role", user.getRole());
        response.put("fullName", user.getFullName());
        response.put("message", "Registration successful");
        return response;
    }
}