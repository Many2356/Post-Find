
package com.devjobs.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
 
@Entity
@Table(name = "applications", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"applicant_id", "job_offer_id"})
})
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_id")
    private User applicant;
 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_offer_id")
    private JobOffer jobOffer;
 
    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDIENTE;
 
    private String coverLetter;
 
    @Column(updatable = false)
    private LocalDateTime appliedAt;
 
    @PrePersist
    protected void onCreate() {
        this.appliedAt = LocalDateTime.now();
    }
 
    public enum Status {
        PENDIENTE, REVISADO, ACEPTADO, RECHAZADO
    }
 
    // Constructors
    public Application() {}
 
    // Getters and Setters
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
