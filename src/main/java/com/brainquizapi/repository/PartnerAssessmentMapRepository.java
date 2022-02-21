package com.brainquizapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.brainquizapi.model.PartnerAssessmentMapEntity;

public interface PartnerAssessmentMapRepository extends JpaRepository<PartnerAssessmentMapEntity, Long>  {

	List<PartnerAssessmentMapEntity> findAllByIsdeleted(String isdeleted);
	
	@Query(value = "SELECT * FROM brainquiz.partner_assessment_map where fk_assessment=?1 and fk_partner=?2", nativeQuery = true)
	List<PartnerAssessmentMapEntity> findByPartnerAssId(String assId,String partnerId);

}
