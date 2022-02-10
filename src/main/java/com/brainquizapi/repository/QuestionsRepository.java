package com.brainquizapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brainquizapi.model.AssessmentEntity;
import com.brainquizapi.model.CategoryEntity;
import com.brainquizapi.model.QuestionEntity;

public interface QuestionsRepository extends JpaRepository<QuestionEntity, Long> {
	
	
	//List<Answers> findByQuestionId(int categoryId);


}
