package com.brainquizapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.brainquizapi.model.AssessmentEntity;
import com.brainquizapi.model.PartnerEntity;




public interface AssessmentRepository extends JpaRepository<AssessmentEntity, Long> {
	
	
	 Optional<AssessmentEntity> findById(long id);
	
	List<AssessmentEntity> findAllByIsdeleted(String isdeleted);

	
	
	@Query(value = "SELECT asMst.* FROM partner_assessment_map as pmap left join "
			+ " assessment_master asMst on pmap.fk_assessment = asMst.id "
			+ " where pmap.fk_partner = ?1 group by pmap.fk_assessment", nativeQuery = true)
	
	List<AssessmentEntity> findAssessmentByPartnerAssessmentGroubAndIsdeleted(PartnerEntity partnerEntity);

	
}