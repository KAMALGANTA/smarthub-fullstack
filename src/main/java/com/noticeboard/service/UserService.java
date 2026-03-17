package com.noticeboard.service;

import com.noticeboard.model.User;
import com.noticeboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + usernameOrEmail));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isActive(), true, true, true,
                List.of(new SimpleGrantedAuthority(user.getRole()))
        );
    }

    public User registerUser(String username, String email, String fullName, String password) {
        if (userRepository.existsByUsername(username))
            throw new RuntimeException("Username already exists: " + username);
        if (userRepository.existsByEmail(email))
            throw new RuntimeException("Email already in use: " + email);

        User user = User.builder()
                .username(username)
                .email(email)
                .fullName(fullName)
                .password(passwordEncoder.encode(password))
                .role("ROLE_USER")
                .isActive(true)
                .build();

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public User updateUser(Long id, User updatedUser) {
        User existing = getUserById(id);
        existing.setFullName(updatedUser.getFullName());
        existing.setEmail(updatedUser.getEmail());
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        return userRepository.save(existing);
    }

    public void deleteUser(Long id) {
        User user = getUserById(id);
        user.setActive(false);
        userRepository.save(user);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }
}