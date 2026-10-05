package com.recruitment.ats.model;
import jakarta.persistence.*; import java.time.LocalDateTime;
@Entity @Table(name="interviews")
public class Interview {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long interviewId;
 @OneToOne @JoinColumn(name="application_id",nullable=false,unique=true) private Application application;
 private LocalDateTime scheduledAt;
 @Column(length=30) private String type;
 @Column(length=150) private String interviewer;
 @Column(length=500) private String meetingLink;
 @Column(length=30) private String status="SCHEDULED";
 @Column(columnDefinition="TEXT") private String notes;
 public Interview(){}
 public Long getInterviewId(){return interviewId;} public Application getApplication(){return application;} public void setApplication(Application v){application=v;}
 public LocalDateTime getScheduledAt(){return scheduledAt;} public void setScheduledAt(LocalDateTime v){scheduledAt=v;}
 public String getType(){return type;} public void setType(String v){type=v;} public String getInterviewer(){return interviewer;} public void setInterviewer(String v){interviewer=v;}
 public String getMeetingLink(){return meetingLink;} public void setMeetingLink(String v){meetingLink=v;} public String getStatus(){return status;} public void setStatus(String v){status=v;}
 public String getNotes(){return notes;} public void setNotes(String v){notes=v;}
}