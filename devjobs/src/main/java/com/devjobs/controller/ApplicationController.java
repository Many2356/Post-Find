/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.devjobs.controller;

import com.devjobs.dto.DTOs;
import com.devjobs.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 
import java.util.Map;
 
@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "http://localhost:4200")
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
}
