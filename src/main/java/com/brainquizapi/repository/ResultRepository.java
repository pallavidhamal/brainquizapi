package com.brainquizapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brainquizapi.model.AllresultEntity;


	
	public interface ResultRepository extends JpaRepository<AllresultEntity, Long> {
		


}
