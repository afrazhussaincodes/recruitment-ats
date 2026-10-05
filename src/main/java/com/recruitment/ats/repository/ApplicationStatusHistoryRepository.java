package com.recruitment.ats.repository;

import com.recruitment.ats.model.ApplicationStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationStatusHistoryRepository
        extends JpaRepository<ApplicationStatusHistory, Long> {

    List<ApplicationStatusHistory>
    findByApplication_ApplicationIdOrderByChangedAtDesc(
            Long applicationId);
}