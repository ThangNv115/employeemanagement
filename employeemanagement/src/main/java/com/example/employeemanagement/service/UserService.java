package com.example.employeemanagement.service;

import com.example.employeemanagement.entity.Role;
import com.example.employeemanagement.entity.User;
import com.example.employeemanagement.repository.RoleRepository;
import com.example.employeemanagement.repository.UserRepository;
import com.example.employeemanagement.ultil.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private WebSocketService webSocketService;

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
    }

    @Transactional
    public User updateUser(Long id, User updatedUser, String roleName) {
        User existingUser = findById(id);

        // Update email if changed
        if (!existingUser.getEmail().equals(updatedUser.getEmail())) {
            if (userRepository.existsByEmail(updatedUser.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }
            existingUser.setEmail(updatedUser.getEmail());
        }

        // Update password if provided
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            if (!updatedUser.getPassword().matches(Constants.PASSWORD_PATTERN)) {
                throw new IllegalArgumentException("Password does not meet requirements");
            }
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        // Update role
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        existingUser.getRoles().clear();
        existingUser.getRoles().add(role);

        return userRepository.save(existingUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = findById(id);
        userRepository.delete(user);
    }

    @Transactional
    public User createUser(User user, String roleName) {
        // Validate password
        if (!user.getPassword().matches(Constants.PASSWORD_PATTERN)) {
            throw new IllegalArgumentException("Password does not meet requirements");
        }

        // Check if username or email already exists
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Add role
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        user.getRoles().add(role);

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void update_is_online(Long userId, Integer status) {
        userRepository.updateUserOnlineStatus(userId, status);
        webSocketService.notifyStatusChange(userId, status);
    }

}
