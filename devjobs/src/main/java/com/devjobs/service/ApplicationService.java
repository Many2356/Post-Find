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
public class ApplicationService {
    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobOfferRepository jobOfferRepository;

    @Transactional
    public DTOs.ApplicationResponse apply(Long applicantId, Long jobOfferId, DTOs.ApplicationRequest request) {
        User applicant = userRepository.findById(applicantId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (applicant.getRole() != User.Role.TRABAJADOR) {
            throw new RuntimeException("Solo los trabajadores pueden solicitar ofertas");
        }

        JobOffer jobOffer = jobOfferRepository.findById(jobOfferId)
                .orElseThrow(() -> new RuntimeException("Oferta no encontrada"));

        if (applicationRepository.existsByApplicantAndJobOffer(applicant, jobOffer)) {
            throw new RuntimeException("Ya has solicitado esta oferta");
        }

        Application application = new Application();
        application.setApplicant(applicant);
        application.setJobOffer(jobOffer);
        application.setCoverLetter(request != null ? request.getCoverLetter() : null);

        return toResponse(applicationRepository.save(application));
    }

    public List<DTOs.ApplicationResponse> getByApplicant(Long applicantId) {
        User applicant = userRepository.findById(applicantId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return applicationRepository.findByApplicant(applicant)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<DTOs.ApplicationResponse> getByJobOffer(Long jobOfferId) {
        JobOffer jobOffer = jobOfferRepository.findById(jobOfferId)
                .orElseThrow(() -> new RuntimeException("Oferta no encontrada"));
        return applicationRepository.findByJobOffer(jobOffer)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public boolean hasApplied(Long applicantId, Long jobOfferId) {
        User applicant = userRepository.findById(applicantId).orElse(null);
        JobOffer jobOffer = jobOfferRepository.findById(jobOfferId).orElse(null);
        if (applicant == null || jobOffer == null) return false;
        return applicationRepository.existsByApplicantAndJobOffer(applicant, jobOffer);
    }

    @Transactional
    public void withdraw(Long applicantId, Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        if (!application.getApplicant().getId().equals(applicantId)) {
            throw new RuntimeException("No tienes permiso para retirar esta solicitud");
        }

        applicationRepository.delete(application);
    }

    // El empresario cambia el estado de una solicitud
    @Transactional
    public DTOs.ApplicationResponse updateStatus(Long applicationId, Long employerId, String newStatus) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        // Verificar que la oferta pertenece a este empresario
        if (!application.getJobOffer().getEmployer().getId().equals(employerId)) {
            throw new RuntimeException("No tienes permiso para modificar esta solicitud");
        }

        Application.Status status;
        try {
            status = Application.Status.valueOf(newStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Estado no válido. Usa: PENDIENTE, REVISADO, ACEPTADO, RECHAZADO");
        }

        application.setStatus(status);
        return toResponse(applicationRepository.save(application));
    }

    private DTOs.ApplicationResponse toResponse(Application app) {
        DTOs.ApplicationResponse res = new DTOs.ApplicationResponse();
        res.setId(app.getId());
        res.setStatus(app.getStatus().name());
        res.setCoverLetter(app.getCoverLetter());
        res.setAppliedAt(app.getAppliedAt() != null ? app.getAppliedAt().toString() : null);

        if (app.getApplicant() != null) {
            res.setApplicantId(app.getApplicant().getId());
            res.setApplicantUsername(app.getApplicant().getUsername());
        }
        if (app.getJobOffer() != null) {
            res.setJobOfferId(app.getJobOffer().getId());
            res.setJobOfferTitle(app.getJobOffer().getTitle());
            res.setJobOfferCompany(app.getJobOffer().getCompany());
        }
        return res;
    }
}