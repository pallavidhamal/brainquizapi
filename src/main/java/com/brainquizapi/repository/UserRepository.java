package com.brainquizapi.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.brainquizapi.model.UserMasterEntity;



@Repository
public interface UserRepository extends JpaRepository<UserMasterEntity, Long>{

	  UserMasterEntity findByUsername(String username);

	  boolean existsByUsernameAndIsactive(String username, String isactive);
	
	  
	  UserMasterEntity findByEmailId(String emailid);
	  
	  UserMasterEntity findById(long id);

	  boolean existsByEmailIdAndIsactive(String emailId, String string);
	
	  @Query(value = "SELECT TIMESTAMPDIFF(minute,um.created_date,now() ) as miutes from user_master as um where um.id= ?1", nativeQuery = true)
	  int gethoursToValidateTheLink(Long id);

	  
	  List<UserMasterEntity> findByEmailVerificationStatus(String emailVerificationStatus);

//	  @Query(value = "SELECT * FROM user_master where role_id = 7" , nativeQuery = true)
	  UserMasterEntity findByRoleId(long roleId);

	List<UserMasterEntity> findAllByIsdeleted(String isdeleted);

	UserMasterEntity findByEmailIdAndIsdeleted(String emailId, String isdeleted);

	UserMasterEntity findByUsernameAndIsdeleted(String email, String isdeleted);
	  
	//List<UserMasterEntity> getAll();
	//boolean existsByAndIsdeletedAndIsactive(String username, String IsDeleted);

	}

