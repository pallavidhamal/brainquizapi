package com.brainquizapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brainquizapi.model.PartnerAssessmentMapEntity;

public interface PartnerAssessmentMapRepository extends JpaRepository<PartnerAssessmentMapEntity, Long>  {

	List<PartnerAssessmentMapEntity> findAllByIsdeleted(String isdeleted);

}
