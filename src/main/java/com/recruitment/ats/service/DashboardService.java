package com.recruitment.ats.service;
import com.recruitment.ats.model.DashboardSummary; import com.recruitment.ats.repository.*; import org.springframework.stereotype.Service;
@Service public class DashboardService {
 private final DashboardRepository d; private final ApplicationRepository a; private final CandidateRepository c; private final JobRepository j;
 public DashboardService(DashboardRepository d,ApplicationRepository a,CandidateRepository c,JobRepository j){this.d=d;this.a=a;this.c=c;this.j=j;}
 public DashboardSummary getSummary(){return new DashboardSummary(a.count(),d.countApplied(),d.countScreening(),d.countShortlisted(),d.countInterview(),d.countSelected(),d.countRejected(),c.count(),j.findByStatusIgnoreCase("OPEN").size());}
}