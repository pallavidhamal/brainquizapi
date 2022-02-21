package com.brainquizapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brainquizapi.model.AssessmentEntity;
import com.brainquizapi.model.PartnerEntity;




public interface AssessmentRepository extends JpaRepository<AssessmentEntity, Long> {
	
	
	 Optional<AssessmentEntity> findById(long id);
	
	List<AssessmentEntity> findAllByIsdeleted(String isdeleted);

	
}