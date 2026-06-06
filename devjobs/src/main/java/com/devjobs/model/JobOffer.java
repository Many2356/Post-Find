package com.devjobs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "job_offers")
public class JobOffer {

    /** Identificador único autogenerado. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Título del puesto de trabajo. Obligatorio. */
    @NotBlank
    private String title;

    /** Descripción completa de la oferta (responsabilidades, requisitos, etc.). */
    @Column(columnDefinition = "TEXT")
    private String description;

    /** Nombre de la empresa que publica la oferta. Obligatorio. */
    @NotBlank
    private String company;

    /** Ciudad o zona geográfica donde se desempeña el puesto (opcional). */
    private String location;

    /** Rango salarial o salario ofrecido en texto libre (opcional). */
    private String salary;

    /** Tipo de contrato: Indefinido, Temporal, Prácticas, Freelance, Media jornada (opcional). */
    private String contractType;

    /** Nivel de experiencia requerido: Junior, Mid, Senior, etc. (opcional). */
    private String experienceLevel;

    /** Stack tecnológico requerido, separado por comas (opcional). */
    private String technologies;

    /** Modalidad de trabajo: Presencial, Remoto o Híbrido (opcional). */
    private String remote;

    /**
     * Indica si la oferta está activa y visible para los candidatos.
     * Por defecto es {@code true}.
     */
    private boolean active = true;

    /**
     * Usuario empresario que creó la oferta.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id")
    private User employer;

    /**
     * Lista de solicitudes recibidas para esta oferta.
     * Excluida de la serialización JSON para evitar referencias circulares.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "jobOffer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications;

    /** Fecha y hora de creación. Se establece automáticamente al persistir. */
    @Column(updatable = false)
    private LocalDateTime createdAt;

    /** Fecha y hora de la última actualización. Se actualiza automáticamente. */
    private LocalDateTime updatedAt;

    /**
     * Callback JPA: inicializa {@code createdAt} y {@code updatedAt}
     * cuando el registro se persiste por primera vez.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Callback JPA: actualiza {@code updatedAt} en cada modificación del registro.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    //  Constructor 

    /** Constructor sin argumentos requerido por JPA. */
    public JobOffer() {}

    // Getters y Setters

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