
package com.devjobs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
 
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @NotBlank
    @Column(unique = true)
    private String username;
 
    @NotBlank
    @Email
    @Column(unique = true)
    private String email;
 
    @NotBlank
    private String password;
 
    @Enumerated(EnumType.STRING)
    private Role role;
 
    private String fullName;
    private String bio;
    private String location;
    private String website;
    private String skills;
    private String company;
 
    @Column(updatable = false)
    private LocalDateTime createdAt;
 
    @JsonIgnore
    @OneToMany(mappedBy = "employer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobOffer> jobOffers;
 
    
    @JsonIgnore
    @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications;
 
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
 
    public enum Role {
        TRABAJADOR, EMPRESARIO
    }
 
    // Constructors
    public User() {}
 
    public User(String username, String email, String password, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }
 
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
 
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
 
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
 
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
 
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
 
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
 
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
 
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
 
    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
 
    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }
 
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
 
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
 
    public List<JobOffer> getJobOffers() { return jobOffers; }
    public void setJobOffers(List<JobOffer> jobOffers) { this.jobOffers = jobOffers; }
 
    public List<Application> getApplications() { return applications; }
    public void setApplications(List<Application> applications) { this.applications = applications; }
}
