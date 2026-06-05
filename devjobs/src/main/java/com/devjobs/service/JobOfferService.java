/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.devjobs.service;

import com.devjobs.dto.DTOs;
import com.devjobs.model.Application;
import com.devjobs.model.JobOffer;
import com.devjobs.model.User;
import com.devjobs.repository.ApplicationRepository;
import com.devjobs.repository.JobOfferRepository;
import com.devjobs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
 
import java.util.List;
import java.util.stream.Collectors;
 
@Service
public class JobOfferService {
    @Autowired
    private JobOfferRepository jobOfferRepository;
 
    @Autowired
    private UserRepository userRepository;
 
    @Autowired
    private ApplicationRepository applicationRepository;
 
    @Transactional(readOnly = true)
    public List<DTOs.JobOfferResponse> getAllActive() {
        return jobOfferRepository.findByActiveTrue()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }
 
    @Transactional(readOnly = true)
    public List<DTOs.JobOfferResponse> getAll() {
        return jobOfferRepository.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }
   
    @Transactional(readOnly = true)
    public DTOs.JobOfferResponse getById(Long id) {
        JobOffer offer = jobOfferRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Oferta no encontrada"));
        return toResponse(offer);
    }
 
    public List<DTOs.JobOfferResponse> search(String keyword) {
        return jobOfferRepository
                .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrTechnologiesContainingIgnoreCase(
                        keyword, keyword, keyword)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<DTOs.JobOfferResponse> getByEmployer(Long employerId) {
        User employer = userRepository.findById(employerId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return jobOfferRepository.findByEmployer(employer)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }
 
    @Transactional
    public DTOs.JobOfferResponse create(Long employerId, DTOs.JobOfferRequest request) {
        User employer = userRepository.findById(employerId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
 
        if (employer.getRole() != User.Role.EMPRESARIO) {
            throw new RuntimeException("Solo los empresarios pueden crear ofertas");
        }
 
        JobOffer offer = new JobOffer();
        mapRequestToOffer(request, offer);
        offer.setEmployer(employer);
 
        return toResponse(jobOfferRepository.save(offer));
    }
 
    @Transactional
    public DTOs.JobOfferResponse update(Long offerId, Long employerId, DTOs.JobOfferRequest request) {
        JobOffer offer = jobOfferRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Oferta no encontrada"));
 
        if (!offer.getEmployer().getId().equals(employerId)) {
            throw new RuntimeException("No tienes permiso para editar esta oferta");
        }
 
        mapRequestToOffer(request, offer);
        return toResponse(jobOfferRepository.save(offer));
    }
 
    @Transactional
    public void delete(Long offerId, Long employerId) {
        JobOffer offer = jobOfferRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Oferta no encontrada"));
 
        if (!offer.getEmployer().getId().equals(employerId)) {
            throw new RuntimeException("No tienes permiso para eliminar esta oferta");
        }
 
        jobOfferRepository.delete(offer);
    }
 
    private void mapRequestToOffer(DTOs.JobOfferRequest request, JobOffer offer) {
        offer.setTitle(request.getTitle());
        offer.setDescription(request.getDescription());
        offer.setCompany(request.getCompany());
        offer.setLocation(request.getLocation());
        offer.setSalary(request.getSalary());
        offer.setContractType(request.getContractType());
        offer.setExperienceLevel(request.getExperienceLevel());
        offer.setTechnologies(request.getTechnologies());
        offer.setRemote(request.getRemote());
    }
 
    private DTOs.JobOfferResponse toResponse(JobOffer offer) {
        DTOs.JobOfferResponse res = new DTOs.JobOfferResponse();
        res.setId(offer.getId());
        res.setTitle(offer.getTitle());
        res.setDescription(offer.getDescription());
        res.setCompany(offer.getCompany());
        res.setLocation(offer.getLocation());
        res.setSalary(offer.getSalary());
        res.setContractType(offer.getContractType());
        res.setExperienceLevel(offer.getExperienceLevel());
        res.setTechnologies(offer.getTechnologies());
        res.setRemote(offer.getRemote());
        res.setActive(offer.isActive());
        res.setCreatedAt(offer.getCreatedAt() != null ? offer.getCreatedAt().toString() : null);
        if (offer.getEmployer() != null) {
            res.setEmployerId(offer.getEmployer().getId());
            res.setEmployerUsername(offer.getEmployer().getUsername());
        }
        if (offer.getApplications() != null) {
            res.setApplicantsCount(offer.getApplications().size());
        }
        return res;
    }
}
