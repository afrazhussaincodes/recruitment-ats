package com.recruitment.ats.repository;
import com.recruitment.ats.model.Interview; import org.springframework.data.jpa.repository.JpaRepository; import java.util.Optional; import java.util.List;
public interface InterviewRepository extends JpaRepository<Interview,Long>{Optional<Interview> findByApplication_ApplicationId(Long id); List<Interview> findByApplication_Candidate_CandidateId(Long id);}