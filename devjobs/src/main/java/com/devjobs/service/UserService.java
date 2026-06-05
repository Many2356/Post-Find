/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.devjobs.service;

import com.devjobs.dto.DTOs;
import com.devjobs.model.User;
import com.devjobs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
 
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
 
    public DTOs.UserResponse login(DTOs.LoginRequest request) {
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
        if (request.getEmail() != null) {
            if (!request.getEmail().equals(user.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("El email ya está en uso");
            }
            user.setEmail(request.getEmail());
        }
 
        return DTOs.UserResponse.fromUser(userRepository.save(user));
    }
 
    @Transactional
    public void deleteAccount(Long id) {
        userRepository.deleteById(id);
    }
}
