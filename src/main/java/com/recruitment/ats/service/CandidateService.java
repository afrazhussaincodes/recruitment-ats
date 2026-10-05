package com.recruitment.ats.service;
import com.recruitment.ats.model.Candidate; import com.recruitment.ats.repository.CandidateRepository; import org.springframework.stereotype.Service; import org.springframework.web.multipart.MultipartFile;
import java.io.*; import java.nio.file.*; import java.util.*;
@Service public class CandidateService {
 private final CandidateRepository repo; public CandidateService(CandidateRepository r){repo=r;}
 public List<Candidate> getAllCandidates(){return repo.findAll();} public Candidate getCandidateById(Long id){return repo.findById(id).orElseThrow(()->new RuntimeException("Candidate not found: "+id));}
 public Candidate createCandidate(Candidate c){return repo.save(c);}
 public Candidate updateCandidate(Long id,Candidate u){Candidate e=getCandidateById(id);e.setName(u.getName());e.setPhone(u.getPhone());e.setLocation(u.getLocation());e.setProfileSummary(u.getProfileSummary());e.setSkills(u.getSkills());e.setEducation(u.getEducation());e.setExperience(u.getExperience());return repo.save(e);}
 public void deleteCandidate(Long id){if(!repo.existsById(id))throw new RuntimeException("Candidate not found");repo.deleteById(id);}
 public List<Candidate> searchCandidates(String name){return repo.findByNameContainingIgnoreCase(name);}
 public Candidate uploadResume(Long id,MultipartFile file) throws IOException{
   if(file.isEmpty())throw new IllegalArgumentException("Resume file is empty");
   String safe=UUID.randomUUID()+"_"+Path.of(file.getOriginalFilename()).getFileName(); Path dir=Paths.get("uploads/resumes");Files.createDirectories(dir);Path target=dir.resolve(safe);Files.copy(file.getInputStream(),target,StandardCopyOption.REPLACE_EXISTING);
   Candidate c=getCandidateById(id);c.setResumeFileName(file.getOriginalFilename());c.setResumePath(target.toString());return repo.save(c);
 }
}