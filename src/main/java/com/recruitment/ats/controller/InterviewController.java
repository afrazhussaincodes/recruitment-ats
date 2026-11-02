package com.recruitment.ats.controller;

import com.recruitment.ats.model.Application;
import com.recruitment.ats.model.Interview;
import com.recruitment.ats.repository.ApplicationRepository;
import com.recruitment.ats.repository.InterviewRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/interviews")
@CrossOrigin(origins = "*")
public class InterviewController {

    private static final ZoneId APP_TIME_ZONE = ZoneId.of("Asia/Kolkata");

    private final InterviewRepository repo;
    private final ApplicationRepository apps;

    public InterviewController(InterviewRepository r, ApplicationRepository a) {
        repo = r;
        apps = a;
    }

    @GetMapping
    public List<Interview> all() {
        return repo.findAll();
    }

    @GetMapping("/candidate/{candidateId}")
    public List<Interview> candidate(@PathVariable Long candidateId) {
        return repo.findByApplication_Candidate_CandidateId(candidateId);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String, Object> b) {
        try {
            Object scheduledAtValue = b.get("scheduledAt");
            if (scheduledAtValue == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Interview date and time are required."));
            }

            LocalDateTime scheduledAt = LocalDateTime.parse(scheduledAtValue.toString());
            LocalDateTime now = LocalDateTime.now(APP_TIME_ZONE);

            // Reject both past times and the current minute/time.
            if (!scheduledAt.isAfter(now)) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Interview cannot be scheduled in the past. Please choose a future date and time."));
            }

            Object applicationIdValue = b.get("applicationId");
            if (applicationIdValue == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Application ID is required."));
            }

            Long appId = Long.valueOf(applicationIdValue.toString());
            Application a = apps.findById(appId)
                    .orElseThrow(() -> new IllegalArgumentException("Application not found"));

            Interview i = repo.findByApplication_ApplicationId(appId).orElse(new Interview());
            i.setApplication(a);
            i.setScheduledAt(scheduledAt);
            i.setType((String) b.getOrDefault("type", "ONLINE"));
            i.setInterviewer((String) b.getOrDefault("interviewer", "Recruiter"));
            i.setMeetingLink((String) b.getOrDefault("meetingLink", ""));
            i.setNotes((String) b.getOrDefault("notes", ""));
            i.setStatus("SCHEDULED");

            return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(i));
        } catch (DateTimeParseException | NumberFormatException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid interview date/time or application ID."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/status")
    public Interview status(@PathVariable Long id, @RequestParam String status) {
        Interview i = repo.findById(id).orElseThrow();
        i.setStatus(status.toUpperCase());
        return repo.save(i);
    }
}
