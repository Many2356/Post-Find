/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.devjobs.dto;

import com.devjobs.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class DTOs {
    // Auth DTOs
    public static class RegisterRequest {
        @NotBlank private String username;
        @NotBlank @Email private String email;
        @NotBlank private String password;
        @NotBlank private String role;
        private String fullName;
        private String company;
 
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public String getCompany() { return company; }
        public void setCompany(String company) { this.company = company; }
    }
 
    public static class LoginRequest {
        @NotBlank private String username;
        @NotBlank private String password;
 
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
 
    public static class UserResponse {
        private Long id;
        private String username;
        private String email;
        private String role;
        private String fullName;
        private String bio;
        private String location;
        private String website;
        private String skills;
        private String company;
 
        public static UserResponse fromUser(User user) {
            UserResponse res = new UserResponse();
            res.id = user.getId();
            res.username = user.getUsername();
            res.email = user.getEmail();
            res.role = user.getRole().name();
            res.fullName = user.getFullName();
            res.bio = user.getBio();
            res.location = user.getLocation();
            res.website = user.getWebsite();
            res.skills = user.getSkills();
            res.company = user.getCompany();
            return res;
        }
 
        public Long getId() { return id; }
        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public String getRole() { return role; }
        public String getFullName() { return fullName; }
        public String getBio() { return bio; }
        public String getLocation() { return location; }
        public String getWebsite() { return website; }
        public String getSkills() { return skills; }
        public String getCompany() { return company; }
    }
 
    public static class UpdateProfileRequest {
        private String fullName;
        private String bio;
        private String location;
        private String website;
        private String skills;
        private String company;
        private String email;
 
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
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }
 
    // JobOffer DTOs
    public static class JobOfferRequest {
        @NotBlank private String title;
        private String description;
        @NotBlank private String company;
        private String location;
        private String salary;
        private String contractType;
        private String experienceLevel;
        private String technologies;
        private String remote;
 
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
    }
 
    public static class JobOfferResponse {
        private Long id;
        private String title;
        private String description;
        private String company;
        private String location;
        private String salary;
        private String contractType;
        private String experienceLevel;
        private String technologies;
        private String remote;
        private boolean active;
        private Long employerId;
        private String employerUsername;
        private String createdAt;
        private int applicantsCount;
 
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
        public Long getEmployerId() { return employerId; }
        public void setEmployerId(Long employerId) { this.employerId = employerId; }
        public String getEmployerUsername() { return employerUsername; }
        public void setEmployerUsername(String employerUsername) { this.employerUsername = employerUsername; }
        public String getCreatedAt() { return createdAt; }
        public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
        public int getApplicantsCount() { return applicantsCount; }
        public void setApplicantsCount(int applicantsCount) { this.applicantsCount = applicantsCount; }
    }
 
    // Application DTOs
    public static class ApplicationRequest {
        private String coverLetter;
        public String getCoverLetter() { return coverLetter; }
        public void setCoverLetter(String coverLetter) { this.coverLetter = coverLetter; }
    }
 
    public static class ApplicationResponse {
        private Long id;
        private Long jobOfferId;
        private String jobOfferTitle;
        private String jobOfferCompany;
        private Long applicantId;
        private String applicantUsername;
        private String status;
        private String coverLetter;
        private String appliedAt;
 
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getJobOfferId() { return jobOfferId; }
        public void setJobOfferId(Long jobOfferId) { this.jobOfferId = jobOfferId; }
        public String getJobOfferTitle() { return jobOfferTitle; }
        public void setJobOfferTitle(String jobOfferTitle) { this.jobOfferTitle = jobOfferTitle; }
        public String getJobOfferCompany() { return jobOfferCompany; }
        public void setJobOfferCompany(String jobOfferCompany) { this.jobOfferCompany = jobOfferCompany; }
        public Long getApplicantId() { return applicantId; }
        public void setApplicantId(Long applicantId) { this.applicantId = applicantId; }
        public String getApplicantUsername() { return applicantUsername; }
        public void setApplicantUsername(String applicantUsername) { this.applicantUsername = applicantUsername; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getCoverLetter() { return coverLetter; }
        public void setCoverLetter(String coverLetter) { this.coverLetter = coverLetter; }
        public String getAppliedAt() { return appliedAt; }
        public void setAppliedAt(String appliedAt) { this.appliedAt = appliedAt; }
    }
}
