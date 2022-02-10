package com.brainquizapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.brainquizapi.model.AssessmentEntity;
import com.brainquizapi.model.CategoryEntity;

public interface CategoryRepository extends CrudRepository<CategoryEntity, Long> {

	
	 Optional<CategoryEntity> findById(long id);

	//List<CategoryEntity> findByAssessmentId(int assessmentId);
	
	
  //  List<CategoryEntity> findByAssessmentEntity(AssessmentEntity Ae);

  //  List<CategoryEntity> findByAssessmentId(AssessmentEntity Ae);

}
