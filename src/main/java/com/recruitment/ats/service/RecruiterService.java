package com.recruitment.ats.service;

import com.recruitment.ats.model.Recruiter;
import com.recruitment.ats.repository.RecruiterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecruiterService {

    private final RecruiterRepository recruiterRepository;

    public RecruiterService(RecruiterRepository recruiterRepository) {
        this.recruiterRepository = recruiterRepository;
    }

    public List<Recruiter> getAllRecruiters() {
        return recruiterRepository.findAll();
    }

    public Recruiter getRecruiterById(Long id) {
        return recruiterRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Recruiter not found with ID: " + id));
    }

    public Recruiter createRecruiter(Recruiter recruiter) {
        return recruiterRepository.save(recruiter);
    }

    public Recruiter updateRecruiter(Long id, Recruiter updatedRecruiter) {

        Recruiter existingRecruiter = getRecruiterById(id);

        existingRecruiter.setName(updatedRecruiter.getName());
        existingRecruiter.setEmail(updatedRecruiter.getEmail());

        return recruiterRepository.save(existingRecruiter);
    }

    public void deleteRecruiter(Long id) {

        if (!recruiterRepository.existsById(id)) {
            throw new RuntimeException(
                    "Recruiter not found with ID: " + id);
        }

        recruiterRepository.deleteById(id);
    }
}