package com.brainquizapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.brainquizapi.model.EmailQueueStatus;


public interface EmailQueueStatusRepository extends JpaRepository<EmailQueueStatus, Long>{

	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "UPDATE email_queue_status  set status=?1 where id=1", nativeQuery = true)
	Integer updateEmailQueueStatus(String strStatus);
	
	
	
}
