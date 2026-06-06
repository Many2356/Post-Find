package com.devjobs.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "applications", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"applicant_id", "job_offer_id"})
})
public class Application {

    /** Identificador único autogenerado. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Usuario candidato que envió la solicitud.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_id")
    private User applicant;

    /**
     * Oferta de empleo a la que se ha aplicado.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_offer_id")
    private JobOffer jobOffer;

    /**
     * Estado actual de la solicitud.
     * Valor por defecto: PENDIENTE.
     */
    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDIENTE;

    /** Carta de presentación escrita por el candidato (opcional). */
    private String coverLetter;

    /** Fecha y hora en que se envió la solicitud. Se establece automáticamente. */
    @Column(updatable = false)
    private LocalDateTime appliedAt;

    /**
     * Callback JPA: establece {@code appliedAt} al persistir la solicitud.
     */
    @PrePersist
    protected void onCreate() {
        this.appliedAt = LocalDateTime.now();
    }

    /**
     * Posibles estados de una solicitud a lo largo de su ciclo de vida.
     */
    public enum Status {
        PENDIENTE,
        REVISADO,
        ACEPTADO,
        RECHAZADO
    }

    // Constructor

    public Application() {}

    // Getters y Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getApplicant() { return applicant; }
    public void setApplicant(User applicant) { this.applicant = applicant; }

    public JobOffer getJobOffer() { return jobOffer; }
    public void setJobOffer(JobOffer jobOffer) { this.jobOffer = jobOffer; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public String getCoverLetter() { return coverLetter; }
    public void setCoverLetter(String coverLetter) { this.coverLetter = coverLetter; }

    public LocalDateTime getAppliedAt() { return appliedAt; }
    public void setAppliedAt(LocalDateTime appliedAt) { this.appliedAt = appliedAt; }
}