/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.devjobs.repository;

import com.devjobs.model.JobOffer;
import com.devjobs.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
 
@Repository
public interface JobOfferRepository extends JpaRepository<JobOffer, Long> {
    List<JobOffer> findByActiveTrue();
    List<JobOffer> findByEmployer(User employer);
    List<JobOffer> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrTechnologiesContainingIgnoreCase(
        String title, String description, String technologies);
}
