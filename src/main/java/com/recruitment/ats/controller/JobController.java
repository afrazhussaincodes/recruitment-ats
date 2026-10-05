package com.recruitment.ats.controller;
import com.recruitment.ats.model.Job; import com.recruitment.ats.service.JobService; import jakarta.validation.Valid; import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/jobs") @CrossOrigin(origins="*")
public class JobController {
 private final JobService s; public JobController(JobService s){this.s=s;}
 @GetMapping public List<Job> all(@RequestParam(required=false) String q){return q==null||q.isBlank()?s.getAllJobs():s.search(q);}
 @GetMapping("/open") public List<Job> open(){return s.openJobs();}
 @GetMapping("/{id}") public Job get(@PathVariable Long id){return s.getJobById(id);}
 @PostMapping public ResponseEntity<Job> create(@Valid @RequestBody Job j){return ResponseEntity.status(201).body(s.createJob(j));}
 @PutMapping("/{id}") public Job update(@PathVariable Long id,@RequestBody Job j){return s.updateJob(id,j);}
 @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id){s.deleteJob(id);return ResponseEntity.noContent().build();}
}