package com.devjobs.service;

import com.devjobs.dto.DTOs;
import com.devjobs.model.User;
import com.devjobs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public DTOs.UserResponse register(DTOs.RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setCompany(request.getCompany());

        try {
            user.setRole(User.Role.valueOf(request.getRole().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Rol inválido. Use TRABAJADOR o EMPRESARIO");
        }

        User saved = userRepository.save(user);
        return DTOs.UserResponse.fromUser(saved);
    }

    // Admin
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "adminer";

    public DTOs.UserResponse login(DTOs.LoginRequest request) {
        // Acceso admin especial (sin registro en BD)
        if (ADMIN_USERNAME.equals(request.getUsername())
                && ADMIN_PASSWORD.equals(request.getPassword())) {
            DTOs.UserResponse admin = new DTOs.UserResponse();
            admin.setId(-1L);
            admin.setUsername("admin");
            admin.setEmail("admin@devjobs.local");
            admin.setRole("ADMIN");
            admin.setFullName("Administrador");
            return admin;
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return DTOs.UserResponse.fromUser(user);
    }

    public DTOs.UserResponse getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return DTOs.UserResponse.fromUser(user);
    }

    @Transactional
    public DTOs.UserResponse updateProfile(Long id, DTOs.UpdateProfileRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (request.getFullName() != null) user.setFullName(request.getFullName());
        if (request.getBio() != null) user.setBio(request.getBio());
        if (request.getLocation() != null) user.setLocation(request.getLocation());
        if (request.getWebsite() != null) user.setWebsite(request.getWebsite());
        if (request.getSkills() != null) user.setSkills(request.getSkills());
        if (request.getCompany() != null) user.setCompany(request.getCompany());

        // Cambio de rol
        if (request.getRole() != null) {
            try {
                user.setRole(User.Role.valueOf(request.getRole().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Rol inválido. Use TRABAJADOR o EMPRESARIO");
            }
        }

        if (request.getEmail() != null) {
            if (!request.getEmail().equals(user.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("El email ya está en uso");
            }
            user.setEmail(request.getEmail());
        }

        return DTOs.UserResponse.fromUser(userRepository.save(user));
    }

    // Admin: listar todos los usuarios 
    public List<DTOs.UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(DTOs.UserResponse::fromUser)
                .collect(Collectors.toList());
    }

    //  Admin: editar cualquier usuario (incluye contraseña) 
    @Transactional
    public DTOs.UserResponse adminUpdateUser(Long id, DTOs.AdminUpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (request.getUsername() != null && !request.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(request.getUsername()))
                throw new RuntimeException("El nombre de usuario ya está en uso");
            user.setUsername(request.getUsername());
        }
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail()))
                throw new RuntimeException("El email ya está en uso");
            user.setEmail(request.getEmail());
        }
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getRole() != null) {
            try {
                user.setRole(User.Role.valueOf(request.getRole().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Rol inválido. Use TRABAJADOR o EMPRESARIO");
            }
        }
        if (request.getFullName() != null) user.setFullName(request.getFullName());
        if (request.getCompany()  != null) user.setCompany(request.getCompany());

        return DTOs.UserResponse.fromUser(userRepository.save(user));
    }

    //  Admin: crear usuario 
    public DTOs.UserResponse adminCreateUser(DTOs.AdminUpdateUserRequest request) {
        if (request.getUsername() == null || request.getUsername().isBlank())
            throw new RuntimeException("El username es obligatorio");
        if (request.getEmail() == null || request.getEmail().isBlank())
            throw new RuntimeException("El email es obligatorio");
        if (request.getPassword() == null || request.getPassword().isBlank())
            throw new RuntimeException("La contraseña es obligatoria");

        if (userRepository.existsByUsername(request.getUsername()))
            throw new RuntimeException("El nombre de usuario ya está en uso");
        if (userRepository.existsByEmail(request.getEmail()))
            throw new RuntimeException("El email ya está registrado");

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setCompany(request.getCompany());

        String roleStr = request.getRole() != null ? request.getRole() : "TRABAJADOR";
        try {
            user.setRole(User.Role.valueOf(roleStr.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Rol inválido. Use TRABAJADOR o EMPRESARIO");
        }

        return DTOs.UserResponse.fromUser(userRepository.save(user));
    }

    @Transactional
    public void deleteAccount(Long id) {
        userRepository.deleteById(id);
    }
}