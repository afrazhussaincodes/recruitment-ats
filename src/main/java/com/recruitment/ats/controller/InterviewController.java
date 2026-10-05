package com.recruitment.ats.controller;
import com.recruitment.ats.model.*; import com.recruitment.ats.repository.*; import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/interviews") @CrossOrigin(origins="*")
public class InterviewController {
 private final InterviewRepository repo; private final ApplicationRepository apps;
 public InterviewController(InterviewRepository r,ApplicationRepository a){repo=r;apps=a;}
 @GetMapping public List<Interview> all(){return repo.findAll();}
 @GetMapping("/candidate/{candidateId}") public List<Interview> candidate(@PathVariable Long candidateId){return repo.findByApplication_Candidate_CandidateId(candidateId);}
 @PostMapping public ResponseEntity<Interview> create(@RequestBody Map<String,Object> b){
   Long appId=Long.valueOf(b.get("applicationId").toString()); Application a=apps.findById(appId).orElseThrow(()->new RuntimeException("Application not found"));
   Interview i=repo.findByApplication_ApplicationId(appId).orElse(new Interview());i.setApplication(a);i.setScheduledAt(java.time.LocalDateTime.parse(b.get("scheduledAt").toString()));i.setType((String)b.getOrDefault("type","ONLINE"));i.setInterviewer((String)b.getOrDefault("interviewer","Recruiter"));i.setMeetingLink((String)b.getOrDefault("meetingLink",""));i.setNotes((String)b.getOrDefault("notes",""));i.setStatus("SCHEDULED");return ResponseEntity.status(201).body(repo.save(i));
 }
 @PatchMapping("/{id}/status") public Interview status(@PathVariable Long id,@RequestParam String status){Interview i=repo.findById(id).orElseThrow();i.setStatus(status.toUpperCase());return repo.save(i);}
}