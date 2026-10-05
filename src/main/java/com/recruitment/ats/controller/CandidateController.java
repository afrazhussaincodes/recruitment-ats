package com.recruitment.ats.controller;
import com.recruitment.ats.model.Candidate; import com.recruitment.ats.service.CandidateService; import jakarta.validation.Valid; import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import org.springframework.web.multipart.MultipartFile; import java.util.*;
@RestController @RequestMapping("/api/candidates") @CrossOrigin(origins="*")
public class CandidateController {
 private final CandidateService s; public CandidateController(CandidateService s){this.s=s;}
 @GetMapping public List<Candidate> all(){return s.getAllCandidates();}
 @GetMapping("/{id}") public Candidate get(@PathVariable Long id){return s.getCandidateById(id);}
 @PostMapping public ResponseEntity<Candidate> create(@Valid @RequestBody Candidate c){return ResponseEntity.status(201).body(s.createCandidate(c));}
 @PutMapping("/{id}") public Candidate update(@PathVariable Long id,@RequestBody Candidate c){return s.updateCandidate(id,c);}
 @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id){s.deleteCandidate(id);return ResponseEntity.noContent().build();}
 @GetMapping("/search") public List<Candidate> search(@RequestParam String name){return s.searchCandidates(name);}
 @PostMapping("/{id}/resume") public Candidate resume(@PathVariable Long id,@RequestParam MultipartFile file)throws Exception{return s.uploadResume(id,file);}
}