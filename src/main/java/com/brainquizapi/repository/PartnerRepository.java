package com.brainquizapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brainquizapi.model.PartnerEntity;

public interface PartnerRepository extends JpaRepository<PartnerEntity, Long> {
	
	List<PartnerEntity> findAllByIsdeleted(String isdeleted);
	PartnerEntity findByPartnerNameAndIsdeleted( String Partnername,String isdeleted);
	PartnerEntity findByPartnerName(String partnerName);
	List<PartnerEntity> findByIdAndIsdeleted(Long id,String isdeleted);

}
 

