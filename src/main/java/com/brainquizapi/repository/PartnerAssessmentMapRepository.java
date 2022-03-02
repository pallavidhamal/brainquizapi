package com.brainquizapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.brainquizapi.model.PartnerAssessmentMapEntity;
import com.brainquizapi.model.PartnerEntity;

public interface PartnerAssessmentMapRepository extends JpaRepository<PartnerAssessmentMapEntity, Long>  {

	List<PartnerAssessmentMapEntity> findAllByIsdeleted(String isdeleted);
	
	@Query(value = "SELECT * FROM brainquiz.partner_assessment_map where fk_assessment=?1 and fk_partner=?2", nativeQuery = true)
	List<PartnerAssessmentMapEntity> findByPartnerAssId(String assId,String partnerId);

	
	@Query(value = "SELECT id , alt_email , from_date ,partner_assessment_name, to_date , fk_assessment , fk_partner ,created_by , created_date ,is_deleted , updated_by , updated_date "
			+ " , case when tb.status is null  then case when DATE_FORMAT(NOW(), '%Y-%m-%d') BETWEEN tb.from_date AND tb.to_date then  'In Progress' "
			+ "  when DATE_FORMAT(NOW(), '%Y-%m-%d') < tb.from_date then  'Scheduled' when DATE_FORMAT(NOW(), '%Y-%m-%d') > tb.to_date then  'Completed' end else tb.status end as status "
			+ " FROM brainquiz.partner_assessment_map as tb where tb.is_deleted = ?2 and tb.fk_partner = ?1", nativeQuery = true)
	
	List<PartnerAssessmentMapEntity> findByPartnerAndIsdeleted(PartnerEntity partnerEntity , String Isdeleted);
	
}
