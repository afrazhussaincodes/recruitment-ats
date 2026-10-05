package com.recruitment.ats.controller;

import com.recruitment.ats.model.Recruiter;
import com.recruitment.ats.service.RecruiterService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recruiters")
public class RecruiterController {

    private final RecruiterService recruiterService;

    public RecruiterController(RecruiterService recruiterService) {
        this.recruiterService = recruiterService;
    }

    @GetMapping
    public ResponseEntity<List<Recruiter>> getAllRecruiters() {
        return ResponseEntity.ok(recruiterService.getAllRecruiters());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recruiter> getRecruiter(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                recruiterService.getRecruiterById(id));
    }

    @PostMapping
    public ResponseEntity<Recruiter> createRecruiter(
            @Valid @RequestBody Recruiter recruiter) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(recruiterService.createRecruiter(recruiter));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Recruiter> updateRecruiter(
            @PathVariable Long id,
            @Valid @RequestBody Recruiter recruiter) {

        return ResponseEntity.ok(
                recruiterService.updateRecruiter(id, recruiter));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecruiter(
            @PathVariable Long id) {

        recruiterService.deleteRecruiter(id);

        return ResponseEntity.noContent().build();
    }
}