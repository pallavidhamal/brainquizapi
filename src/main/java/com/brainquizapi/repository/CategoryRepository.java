package com.brainquizapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.brainquizapi.model.AssessmentEntity;
import com.brainquizapi.model.CategoryEntity;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

	
	 Optional<CategoryEntity> findById(long id);

	// List<CategoryEntity> findByAssessmentId(int assessmentId);
	
	
	 @Query(value =" select count(*) from  category_master cm, question_master qm   where cm.id=qm.category_id and cm.assessment_id = ?1", nativeQuery = true)
	 int getQuestionCntByAssId(String id);
		
	 
  //  List<CategoryEntity> findByAssessmentEntity(AssessmentEntity Ae);

  //  List<CategoryEntity> findByAssessmentId(AssessmentEntity Ae);

	 
}
