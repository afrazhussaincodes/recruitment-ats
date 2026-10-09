package com.recruitment.ats;

import com.recruitment.ats.model.Application;
import com.recruitment.ats.repository.ApplicationRepository;
import com.recruitment.ats.repository.CandidateRepository;
import com.recruitment.ats.repository.JobRepository;
import com.recruitment.ats.repository.RecruiterRepository;
import com.recruitment.ats.repository.ApplicationStatusHistoryRepository;
import com.recruitment.ats.service.ApplicationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecruitmentAtsApplicationTests {

    private ApplicationRepository apps;
    private CandidateRepository candidates;
    private JobRepository jobs;
    private RecruiterRepository recruiters;
    private ApplicationStatusHistoryRepository history;
    private ApplicationService service;

    @BeforeEach
    void setUp() {
        apps = mock(ApplicationRepository.class);
        candidates = mock(CandidateRepository.class);
        jobs = mock(JobRepository.class);
        recruiters = mock(RecruiterRepository.class);
        history = mock(ApplicationStatusHistoryRepository.class);

        service = new ApplicationService(
                apps, candidates, jobs, recruiters, history
        );
    }

    @Test
    void invalidStatusIsRejected() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.updateStatus(1L, "INVALID_STATUS")
        );

        verifyNoInteractions(apps);
    }

    @Test
    void missingApplicationIsRejectedForValidStatus() {
        when(apps.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> service.updateStatus(1L, "SCREENING")
        );
    }

    @Test
    void validStatusCanBeUpdated() {
        Application application = new Application();
        application.setStatus("APPLIED");

        when(apps.findById(1L)).thenReturn(Optional.of(application));
        when(apps.save(application)).thenReturn(application);

        Application result = service.updateStatus(1L, "SCREENING");

        assertEquals("SCREENING", result.getStatus());
        verify(apps).save(application);
        verify(history).save(any());
    }
}