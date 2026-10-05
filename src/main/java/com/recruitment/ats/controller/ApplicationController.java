package com.recruitment.ats.controller;
import com.recruitment.ats.model.*; import com.recruitment.ats.service.ApplicationService; import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/applications") @CrossOrigin(origins="*")
public class ApplicationController {
 private final ApplicationService s; public ApplicationController(ApplicationService s){this.s=s;}
 @GetMapping public List<Application> all(){return s.getAllApplications();}
 @GetMapping("/{id}") public Application get(@PathVariable Long id){return s.getApplicationById(id);}
 @PostMapping public ResponseEntity<Application> create(@RequestParam Long candidateId,@RequestParam Long jobId,@RequestParam(required=false) Long recruiterId,@RequestBody Application a){return ResponseEntity.status(201).body(s.createApplication(candidateId,jobId,recruiterId,a));}
 @PostMapping("/apply") public ResponseEntity<Application> apply(@RequestBody ApplicationRequest r){return ResponseEntity.status(201).body(s.createApplication(r));}
 @PatchMapping("/{id}/status") public Application status(@PathVariable Long id,@RequestParam String status){return s.updateStatus(id,status);}
 @GetMapping("/status/{status}") public List<Application> byStatus(@PathVariable String status){return s.getByStatus(status);}
 @GetMapping("/search") public List<Application> search(@RequestParam String candidate){return s.searchByCandidate(candidate);}
 @GetMapping("/candidate/{candidateId}") public List<Application> mine(@PathVariable Long candidateId){return s.getCandidateApplications(candidateId);}
 @GetMapping("/{id}/history") public List<ApplicationStatusHistory> history(@PathVariable Long id){return s.getStatusHistory(id);}
}