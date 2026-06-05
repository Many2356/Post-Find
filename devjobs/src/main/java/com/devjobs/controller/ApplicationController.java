package com.devjobs.controller;

import com.devjobs.dto.DTOs;
import com.devjobs.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "https://jobportal-frontend-u7wp.onrender.com")
public class ApplicationController {
    @Autowired
    private ApplicationService applicationService;

    @PostMapping("/apply/{jobOfferId}")
    public ResponseEntity<?> apply(@PathVariable Long jobOfferId,
                                   @RequestParam Long applicantId,
                                   @RequestBody(required = false) DTOs.ApplicationRequest request) {
        try {
            return ResponseEntity.ok(applicationService.apply(applicantId, jobOfferId, request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(applicationService.getByApplicant(userId));
    }

    @GetMapping("/offer/{jobOfferId}")
    public ResponseEntity<?> getByOffer(@PathVariable Long jobOfferId) {
        return ResponseEntity.ok(applicationService.getByJobOffer(jobOfferId));
    }

    @GetMapping("/check")
    public ResponseEntity<?> hasApplied(@RequestParam Long applicantId,
                                         @RequestParam Long jobOfferId) {
        boolean applied = applicationService.hasApplied(applicantId, jobOfferId);
        return ResponseEntity.ok(Map.of("applied", applied));
    }

    @DeleteMapping("/{applicationId}")
    public ResponseEntity<?> withdraw(@PathVariable Long applicationId,
                                       @RequestParam Long applicantId) {
        try {
            applicationService.withdraw(applicantId, applicationId);
            return ResponseEntity.ok(Map.of("message", "Solicitud retirada correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // El empresario cambia el estado de una solicitud
    @PatchMapping("/{applicationId}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long applicationId,
                                           @RequestParam Long employerId,
                                           @RequestBody Map<String, String> body) {
        try {
            String newStatus = body.get("status");
            if (newStatus == null || newStatus.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("error", "El campo 'status' es obligatorio"));
            }
            return ResponseEntity.ok(applicationService.updateStatus(applicationId, employerId, newStatus));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}