/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.devjobs.repository;

import com.devjobs.model.Application;
import com.devjobs.model.JobOffer;
import com.devjobs.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
 
@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByApplicant(User applicant);
    List<Application> findByJobOffer(JobOffer jobOffer);
    Optional<Application> findByApplicantAndJobOffer(User applicant, JobOffer jobOffer);
    boolean existsByApplicantAndJobOffer(User applicant, JobOffer jobOffer);
}
