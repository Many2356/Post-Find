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

    /** Identificador único autogenerado. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    /** Nombre de usuario único. No puede estar en blanco. */
    @NotBlank
    @Column(unique = true)
    private String username;
 
    /** Dirección de correo electrónico única y validada. No puede estar en blanco. */
    @NotBlank
    @Email
    @Column(unique = true)
    private String email;
 
    /** Contraseña almacenada como hash BCrypt. No puede estar en blanco. */
    @NotBlank
    private String password;
 
    /** Rol del usuario dentro de la plataforma. */
    @Enumerated(EnumType.STRING)
    private Role role;
 
    /** Nombre completo visible en el perfil (opcional). */
    private String fullName;

    /** Biografía o descripción personal (opcional). */
    private String bio;

    /** Localización geográfica del usuario (opcional). */
    private String location;

    /** URL del sitio web o portfolio personal (opcional). */
    private String website;

    /** Lista de tecnologías / habilidades separadas por comas (opcional). */
    private String skills;

    /** Nombre de la empresa asociada al usuario (relevante para EMPRESARIO). */
    private String company;
 
    /** Fecha y hora de creación del registro. Se establece automáticamente al persistir. */
    @Column(updatable = false)
    private LocalDateTime createdAt;
 
    /**
     * Ofertas de empleo publicadas por este usuario (solo EMPRESARIO).
     * Excluido de la serialización JSON para evitar referencias circulares.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "employer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobOffer> jobOffers;
 
    /**
     * Solicitudes enviadas por este usuario (solo TRABAJADOR).
     * Excluido de la serialización JSON para evitar referencias circulares.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications;
 
    /**
     * Callback JPA: establece {@code createdAt} en el momento de persistir
     * el registro por primera vez.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
 
    /**
     * Roles disponibles en la plataforma.
     */
    public enum Role {
        TRABAJADOR, EMPRESARIO, ADMIN
    }
 
    // Constructors
    public User() {}
 
    /**
     * Constructor de conveniencia con los campos mínimos obligatorios.
     *
     * @param username nombre de usuario único
     * @param email    dirección de correo electrónico
     * @param password contraseña hasheada con BCrypt
     * @param role     rol del usuario en la plataforma
     */
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