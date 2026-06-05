
package com.devjobs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
 
@Entity
@Table(name = "job_offers")
public class JobOffer {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @NotBlank
    private String title;
 
    @Column(columnDefinition = "TEXT")
    private String description;
 
    @NotBlank
    private String company;
 
    private String location;
    private String salary;
    private String contractType;
    private String experienceLevel;
    private String technologies;
    private String remote;
    private boolean active = true;
 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id")
    private User employer;
 
    @JsonIgnore
    @OneToMany(mappedBy = "jobOffer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications;
 
    @Column(updatable = false)
    private LocalDateTime createdAt;
 
    private LocalDateTime updatedAt;
 
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
 
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
 
    // Constructors
    public JobOffer() {}
 
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
 
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
 
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
 
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
 
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
 
    public String getSalary() { return salary; }
    public void setSalary(String salary) { this.salary = salary; }
 
    public String getContractType() { return contractType; }
    public void setContractType(String contractType) { this.contractType = contractType; }
 
    public String getExperienceLevel() { return experienceLevel; }
    public void setExperienceLevel(String experienceLevel) { this.experienceLevel = experienceLevel; }
 
    public String getTechnologies() { return technologies; }
    public void setTechnologies(String technologies) { this.technologies = technologies; }
 
    public String getRemote() { return remote; }
    public void setRemote(String remote) { this.remote = remote; }
 
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
 
    public User getEmployer() { return employer; }
    public void setEmployer(User employer) { this.employer = employer; }
 
    public List<Application> getApplications() { return applications; }
    public void setApplications(List<Application> applications) { this.applications = applications; }
 
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
 
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
