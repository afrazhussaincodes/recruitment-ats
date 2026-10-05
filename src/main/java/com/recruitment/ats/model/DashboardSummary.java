package com.recruitment.ats.model;
public class DashboardSummary {
 public long totalApplications,applied,screening,shortlisted,interview,selected,rejected,totalCandidates,openJobs;
 public DashboardSummary(long t,long a,long s,long sh,long i,long se,long r,long c,long j){totalApplications=t;applied=a;screening=s;shortlisted=sh;interview=i;selected=se;rejected=r;totalCandidates=c;openJobs=j;}
 public long getTotalApplications(){return totalApplications;} public long getApplied(){return applied;} public long getScreening(){return screening;} public long getShortlisted(){return shortlisted;} public long getInterview(){return interview;} public long getSelected(){return selected;} public long getRejected(){return rejected;} public long getTotalCandidates(){return totalCandidates;} public long getOpenJobs(){return openJobs;}
}