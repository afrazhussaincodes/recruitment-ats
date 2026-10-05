package com.recruitment.ats.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="applications", uniqueConstraints=@UniqueConstraint(name="uk_candidate_job", columnNames={"candidate_id","job_id"}))
public class Application {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long applicationId;
    @ManyToOne @JoinColumn(name="candidate_id",nullable=false) private Candidate candidate;
    @ManyToOne @JoinColumn(name="job_id",nullable=false) private Job job;
    @ManyToOne @JoinColumn(name="recruiter_id",nullable=false) private Recruiter recruiter;
    @NotNull @Column(nullable=false,length=30) private String status="APPLIED";
    @NotNull @Column(nullable=false) private LocalDate appliedDate;
    @Column(columnDefinition="TEXT") private String notes;
    @Column(columnDefinition="TEXT") private String coverLetter;
    private LocalDateTime updatedAt;
    @PrePersist protected void onCreate(){updatedAt=LocalDateTime.now();if(appliedDate==null)appliedDate=LocalDate.now();}
    @PreUpdate protected void onUpdate(){updatedAt=LocalDateTime.now();}
    public Application(){}
    public Long getApplicationId(){return applicationId;} public void setApplicationId(Long v){applicationId=v;}
    public Candidate getCandidate(){return candidate;} public void setCandidate(Candidate v){candidate=v;}
    public Job getJob(){return job;} public void setJob(Job v){job=v;}
    public Recruiter getRecruiter(){return recruiter;} public void setRecruiter(Recruiter v){recruiter=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;}
    public LocalDate getAppliedDate(){return appliedDate;} public void setAppliedDate(LocalDate v){appliedDate=v;}
    public String getNotes(){return notes;} public void setNotes(String v){notes=v;}
    public String getCoverLetter(){return coverLetter;} public void setCoverLetter(String v){coverLetter=v;}
    public LocalDateTime getUpdatedAt(){return updatedAt;}
}