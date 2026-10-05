package com.recruitment.ats.service;
import com.recruitment.ats.model.*; import com.recruitment.ats.repository.*; import org.springframework.stereotype.Service; import java.time.LocalDate; import java.util.*;
@Service public class ApplicationService {
 private final ApplicationRepository apps; private final CandidateRepository candidates; private final JobRepository jobs; private final RecruiterRepository recruiters; private final ApplicationStatusHistoryRepository history;
 public ApplicationService(ApplicationRepository a,CandidateRepository c,JobRepository j,RecruiterRepository r,ApplicationStatusHistoryRepository h){apps=a;candidates=c;jobs=j;recruiters=r;history=h;}
 public List<Application> getAllApplications(){return apps.findAll();}
 public Application getApplicationById(Long id){return apps.findById(id).orElseThrow(()->new RuntimeException("Application not found: "+id));}
 public Application createApplication(ApplicationRequest req){
   Candidate c=candidates.findById(req.getCandidateId()).orElseThrow(()->new RuntimeException("Candidate not found"));
   Job j=jobs.findById(req.getJobId()).orElseThrow(()->new RuntimeException("Job not found"));
   if(!"OPEN".equalsIgnoreCase(j.getStatus()))throw new IllegalArgumentException("This job is not open");
   if(apps.existsByCandidate_CandidateIdAndJob_JobId(c.getCandidateId(),j.getJobId()))throw new IllegalArgumentException("You have already applied for this job");
   Recruiter r;
   if(req.getRecruiterId()!=null) r=recruiters.findById(req.getRecruiterId()).orElseThrow(()->new RuntimeException("Recruiter not found"));
   else r=recruiters.findAll().stream().findFirst().orElseThrow(()->new RuntimeException("No recruiter configured"));
   Application a=new Application();a.setCandidate(c);a.setJob(j);a.setRecruiter(r);a.setStatus("APPLIED");a.setAppliedDate(LocalDate.now());a.setCoverLetter(req.getCoverLetter());a.setNotes(req.getNotes());
   Application saved=apps.save(a); ApplicationStatusHistory h=new ApplicationStatusHistory();h.setApplication(saved);h.setNewStatus("APPLIED");history.save(h);return saved;
 }
 public Application createApplication(Long candidateId,Long jobId,Long recruiterId,Application a){ApplicationRequest r=new ApplicationRequest();r.setCandidateId(candidateId);r.setJobId(jobId);r.setRecruiterId(recruiterId);r.setCoverLetter(a.getCoverLetter());r.setNotes(a.getNotes());return createApplication(r);}
 public Application updateStatus(Long id,String status){Set<String> allowed=Set.of("APPLIED","SCREENING","SHORTLISTED","INTERVIEW","SELECTED","REJECTED");String s=status.toUpperCase();if(!allowed.contains(s))throw new IllegalArgumentException("Invalid status");Application a=getApplicationById(id);String old=a.getStatus();if(s.equalsIgnoreCase(old))return a;a.setStatus(s);Application saved=apps.save(a);ApplicationStatusHistory h=new ApplicationStatusHistory();h.setApplication(saved);h.setOldStatus(old);h.setNewStatus(s);history.save(h);return saved;}
 public List<Application> getByStatus(String s){return apps.findByStatusIgnoreCase(s);}
 public List<Application> searchByCandidate(String n){return apps.findByCandidate_NameContainingIgnoreCase(n);}
 public List<Application> getCandidateApplications(Long id){return apps.findByCandidate_CandidateIdOrderByAppliedDateDesc(id);}
 public List<ApplicationStatusHistory> getStatusHistory(Long id){getApplicationById(id);return history.findByApplication_ApplicationIdOrderByChangedAtDesc(id);}
}