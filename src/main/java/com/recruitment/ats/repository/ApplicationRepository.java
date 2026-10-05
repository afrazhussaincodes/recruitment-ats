package com.recruitment.ats.repository;
import com.recruitment.ats.model.Application; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface ApplicationRepository extends JpaRepository<Application,Long>{
 List<Application> findByStatusIgnoreCase(String status);
 List<Application> findByCandidate_NameContainingIgnoreCase(String name);
 List<Application> findByCandidate_CandidateIdOrderByAppliedDateDesc(Long id);
 boolean existsByCandidate_CandidateIdAndJob_JobId(Long candidateId, Long jobId);
 long countByCandidate_CandidateId(Long candidateId);
}