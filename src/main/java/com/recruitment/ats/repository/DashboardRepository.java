package com.recruitment.ats.repository;

import com.recruitment.ats.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DashboardRepository extends JpaRepository<Application, Long> {

    @Query("SELECT COUNT(a) FROM Application a WHERE UPPER(a.status) = 'APPLIED'")
    long countApplied();

    @Query("SELECT COUNT(a) FROM Application a WHERE UPPER(a.status) = 'SCREENING'")
    long countScreening();

    @Query("SELECT COUNT(a) FROM Application a WHERE UPPER(a.status) = 'SHORTLISTED'")
    long countShortlisted();

    @Query("SELECT COUNT(a) FROM Application a WHERE UPPER(a.status) = 'INTERVIEW'")
    long countInterview();

    @Query("SELECT COUNT(a) FROM Application a WHERE UPPER(a.status) = 'SELECTED'")
    long countSelected();

    @Query("SELECT COUNT(a) FROM Application a WHERE UPPER(a.status) = 'REJECTED'")
    long countRejected();
}