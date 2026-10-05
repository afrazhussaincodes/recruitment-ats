package com.recruitment.ats.service;
import com.recruitment.ats.model.Job; import com.recruitment.ats.repository.JobRepository; import org.springframework.stereotype.Service; import java.util.*;
@Service public class JobService {
 private final JobRepository repo; public JobService(JobRepository r){repo=r;}
 public List<Job> getAllJobs(){return repo.findAll();} public List<Job> openJobs(){return repo.findByStatusIgnoreCase("OPEN");}
 public List<Job> search(String q){return repo.findByTitleContainingIgnoreCaseOrLocationContainingIgnoreCaseOrSkillsContainingIgnoreCase(q,q,q);}
 public Job getJobById(Long id){return repo.findById(id).orElseThrow(()->new RuntimeException("Job not found: "+id));}
 public Job createJob(Job j){return repo.save(j);}
 public Job updateJob(Long id,Job u){Job e=getJobById(id);e.setTitle(u.getTitle());e.setDepartment(u.getDepartment());e.setCompany(u.getCompany());e.setLocation(u.getLocation());e.setEmploymentType(u.getEmploymentType());e.setExperience(u.getExperience());e.setSalary(u.getSalary());e.setDescription(u.getDescription());e.setRequirements(u.getRequirements());e.setSkills(u.getSkills());e.setStatus(u.getStatus());e.setClosingDate(u.getClosingDate());return repo.save(e);}
 public void deleteJob(Long id){if(!repo.existsById(id))throw new RuntimeException("Job not found");repo.deleteById(id);}
}