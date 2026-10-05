package com.recruitment.ats.service;

import com.recruitment.ats.model.*;
import com.recruitment.ats.repository.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class AuthService {
 private final UserAccountRepository users; private final CandidateRepository candidates; private final RecruiterRepository recruiters;
 private final BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
 public AuthService(UserAccountRepository u,CandidateRepository c,RecruiterRepository r){users=u;candidates=c;recruiters=r;}
 public Map<String,Object> registerCandidate(Candidate candidate,String password){
   if(users.existsByEmailIgnoreCase(candidate.getEmail())) throw new IllegalArgumentException("Email is already registered");
   Candidate saved=candidates.save(candidate); UserAccount u=new UserAccount();u.setEmail(saved.getEmail());u.setPasswordHash(encoder.encode(password));u.setRole("CANDIDATE");u.setCandidate(saved);users.save(u);
   return session(u);
 }
 public Map<String,Object> login(String email,String password){
   UserAccount u=users.findByEmailIgnoreCase(email).orElseThrow(()->new IllegalArgumentException("Invalid email or password"));
   if(!encoder.matches(password,u.getPasswordHash())) throw new IllegalArgumentException("Invalid email or password");
   return session(u);
 }
 public Map<String,Object> session(UserAccount u){
   Map<String,Object> m=new LinkedHashMap<>();m.put("id",u.getId());m.put("email",u.getEmail());m.put("role",u.getRole());
   if(u.getCandidate()!=null){m.put("candidateId",u.getCandidate().getCandidateId());m.put("name",u.getCandidate().getName());}
   if(u.getRecruiter()!=null){m.put("recruiterId",u.getRecruiter().getRecruiterId());m.put("name",u.getRecruiter().getName());}
   return m;
 }
 public void seedDefaults(){
   if(!users.existsByEmailIgnoreCase("admin@ats.local")){
     Recruiter r=recruiters.findAll().stream().findFirst().orElseGet(()->{Recruiter x=new Recruiter();x.setName("ATS Admin");x.setEmail("admin@ats.local");return recruiters.save(x);});
     UserAccount u=new UserAccount();u.setEmail("admin@ats.local");u.setPasswordHash(encoder.encode("Admin@123"));u.setRole("ADMIN");u.setRecruiter(r);users.save(u);
   }
   if(!users.existsByEmailIgnoreCase("recruiter@ats.local")){
     Recruiter r=recruiters.findAll().stream().findFirst().orElseGet(()->{Recruiter x=new Recruiter();x.setName("ATS Recruiter");x.setEmail("recruiter@ats.local");return recruiters.save(x);});
     UserAccount u=new UserAccount();u.setEmail("recruiter@ats.local");u.setPasswordHash(encoder.encode("Recruiter@123"));u.setRole("RECRUITER");u.setRecruiter(r);users.save(u);
   }
 }
}