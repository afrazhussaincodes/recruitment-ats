package com.recruitment.ats.config;
import com.recruitment.ats.model.Job; import com.recruitment.ats.service.AuthService; import com.recruitment.ats.repository.JobRepository;
import org.springframework.boot.CommandLineRunner; import org.springframework.context.annotation.Bean; import org.springframework.context.annotation.Configuration;
@Configuration public class DataInitializer {
 @Bean CommandLineRunner seed(AuthService auth, JobRepository jobs){return args->{auth.seedDefaults(); if(jobs.count()==0){Job j=new Job();j.setTitle("Java Backend Developer");j.setDepartment("Engineering");j.setCompany("Recruitment ATS Demo");j.setLocation("Mumbai");j.setEmploymentType("FULL_TIME");j.setExperience("2-4 years");j.setSalary("₹8-12 LPA");j.setSkills("Java, Spring Boot, MySQL, REST, AWS");j.setDescription("Build scalable backend services for our recruitment platform.");j.setRequirements("Java, Spring Boot, SQL and REST API experience.");jobs.save(j);}};}
}