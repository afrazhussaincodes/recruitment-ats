package com.recruitment.ats.model;
public class ApplicationRequest { private Long candidateId; private Long jobId; private Long recruiterId; private String coverLetter; private String notes;
 public Long getCandidateId(){return candidateId;} public void setCandidateId(Long v){candidateId=v;} public Long getJobId(){return jobId;} public void setJobId(Long v){jobId=v;}
 public Long getRecruiterId(){return recruiterId;} public void setRecruiterId(Long v){recruiterId=v;} public String getCoverLetter(){return coverLetter;} public void setCoverLetter(String v){coverLetter=v;}
 public String getNotes(){return notes;} public void setNotes(String v){notes=v;}
}