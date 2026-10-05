package com.recruitment.ats.model;
import jakarta.persistence.*; import jakarta.validation.constraints.Email; import jakarta.validation.constraints.NotBlank;
@Entity @Table(name="recruiters")
public class Recruiter {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long recruiterId;
 @NotBlank @Column(nullable=false,length=100) private String name;
 @Email @NotBlank @Column(nullable=false,unique=true,length=150) private String email;
 public Recruiter(){} public Long getRecruiterId(){return recruiterId;} public void setRecruiterId(Long v){recruiterId=v;}
 public String getName(){return name;} public void setName(String v){name=v;} public String getEmail(){return email;} public void setEmail(String v){email=v;}
}