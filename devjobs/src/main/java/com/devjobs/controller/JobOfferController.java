/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.devjobs.controller;

import com.devjobs.dto.DTOs;
import com.devjobs.service.JobOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 
import java.util.Map;
 
@RestController
@RequestMapping("/api/offers")
@CrossOrigin(origins = "http://localhost:4200")
public class JobOfferController {
    @Autowired
    private JobOfferService jobOfferService;
 
    @GetMapping
    public ResponseEntity<?> getAllOffers(@RequestParam(required = false) String search) {
        if (search != null && !search.trim().isEmpty()) {
            return ResponseEntity.ok(jobOfferService.search(search));
        }
        return ResponseEntity.ok(jobOfferService.getAllActive());
    }
 
    @GetMapping("/{id}")
    public ResponseEntity<?> getOffer(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(jobOfferService.getById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
 
    @GetMapping("/employer/{employerId}")
    public ResponseEntity<?> getByEmployer(@PathVariable Long employerId) {
        return ResponseEntity.ok(jobOfferService.getByEmployer(employerId));
    }
 
    @PostMapping
    public ResponseEntity<?> createOffer(@RequestParam Long employerId,
                                          @RequestBody DTOs.JobOfferRequest request) {
        try {
            return ResponseEntity.ok(jobOfferService.create(employerId, request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
 
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOffer(@PathVariable Long id,
                                          @RequestParam Long employerId,
                                          @RequestBody DTOs.JobOfferRequest request) {
        try {
            return ResponseEntity.ok(jobOfferService.update(id, employerId, request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
 
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOffer(@PathVariable Long id,
                                          @RequestParam Long employerId) {
        try {
            jobOfferService.delete(id, employerId);
            return ResponseEntity.ok(Map.of("message", "Oferta eliminada correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
