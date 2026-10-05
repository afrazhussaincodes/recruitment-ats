package com.recruitment.ats.service;
import com.recruitment.ats.model.*; import com.recruitment.ats.repository.ApplicationRepository; import org.springframework.stereotype.Service; import java.time.*; import java.util.*;
@Service public class ExceptionService {
 private final ApplicationRepository apps; public ExceptionService(ApplicationRepository a){apps=a;}
 public List<Application> getExceptionApplications(){LocalDate cutoff=LocalDate.now().minusDays(7);return apps.findAll().stream().filter(a->a.getAppliedDate()!=null&&a.getAppliedDate().isBefore(cutoff)&&Set.of("APPLIED","SCREENING","SHORTLISTED").contains(a.getStatus())).toList();}
}