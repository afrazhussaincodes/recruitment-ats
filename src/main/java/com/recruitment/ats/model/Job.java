package com.recruitment.ats.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name="jobs")
public class Job {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long jobId;
    @NotBlank @Column(nullable=false,length=150) private String title;
    @Column(length=100) private String department;
    @Column(length=100) private String company = "Recruitment ATS";
    @Column(length=100) private String location;
    @Column(length=50) private String employmentType = "FULL_TIME";
    @Column(length=50) private String experience;
    @Column(length=50) private String salary;
    @Column(columnDefinition="TEXT") private String description;
    @Column(columnDefinition="TEXT") private String requirements;
    @Column(length=1000) private String skills;
    @Column(nullable=false,length=30) private String status="OPEN";
    private LocalDateTime postedAt;
    private LocalDateTime closingDate;
    @PrePersist protected void onCreate(){if(postedAt==null)postedAt=LocalDateTime.now();}
    public Job(){}
    public Long getJobId(){return jobId;} public void setJobId(Long v){jobId=v;}
    public String getTitle(){return title;} public void setTitle(String v){title=v;}
    public String getDepartment(){return department;} public void setDepartment(String v){department=v;}
    public String getCompany(){return company;} public void setCompany(String v){company=v;}
    public String getLocation(){return location;} public void setLocation(String v){location=v;}
    public String getEmploymentType(){return employmentType;} public void setEmploymentType(String v){employmentType=v;}
    public String getExperience(){return experience;} public void setExperience(String v){experience=v;}
    public String getSalary(){return salary;} public void setSalary(String v){salary=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public String getRequirements(){return requirements;} public void setRequirements(String v){requirements=v;}
    public String getSkills(){return skills;} public void setSkills(String v){skills=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;}
    public LocalDateTime getPostedAt(){return postedAt;} public void setPostedAt(LocalDateTime v){postedAt=v;}
    public LocalDateTime getClosingDate(){return closingDate;} public void setClosingDate(LocalDateTime v){closingDate=v;}
}