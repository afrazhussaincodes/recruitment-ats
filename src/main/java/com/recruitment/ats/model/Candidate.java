package com.recruitment.ats.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "candidates")
public class Candidate {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long candidateId;

    @NotBlank @Column(nullable = false, length = 100)
    private String name;
    @Email @NotBlank @Column(nullable = false, unique = true, length = 150)
    private String email;
    @Column(length = 20) private String phone;
    @Column(length = 100) private String location;
    @Column(length = 1000) private String profileSummary;
    @Column(length = 1000) private String skills;
    @Column(length = 1000) private String education;
    @Column(length = 1000) private String experience;
    @Column(length = 255) private String resumeFileName;
    @Column(length = 500) private String resumePath;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist protected void onCreate() { createdAt=LocalDateTime.now(); updatedAt=LocalDateTime.now(); }
    @PreUpdate protected void onUpdate() { updatedAt=LocalDateTime.now(); }

    public Candidate() {}
    public Long getCandidateId(){return candidateId;} public void setCandidateId(Long v){candidateId=v;}
    public String getName(){return name;} public void setName(String v){name=v;}
    public String getEmail(){return email;} public void setEmail(String v){email=v;}
    public String getPhone(){return phone;} public void setPhone(String v){phone=v;}
    public String getLocation(){return location;} public void setLocation(String v){location=v;}
    public String getProfileSummary(){return profileSummary;} public void setProfileSummary(String v){profileSummary=v;}
    public String getSkills(){return skills;} public void setSkills(String v){skills=v;}
    public String getEducation(){return education;} public void setEducation(String v){education=v;}
    public String getExperience(){return experience;} public void setExperience(String v){experience=v;}
    public String getResumeFileName(){return resumeFileName;} public void setResumeFileName(String v){resumeFileName=v;}
    public String getResumePath(){return resumePath;} public void setResumePath(String v){resumePath=v;}
    public LocalDateTime getCreatedAt(){return createdAt;} public LocalDateTime getUpdatedAt(){return updatedAt;}
}