package com.brainquizapi.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(	name = "questionMaster")
//@Getter @Setter
public class QuestionEntity extends CreateUpdate{

	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name = "questionName")
	private String questionName;
	
	@ManyToOne
    @JoinColumn(name="category_id", nullable=false)   
	private CategoryEntity categoryEntity;

	
	@OneToMany(mappedBy = "questionEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AnswersEntity> answers ;


	
	
	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getQuestionName() {
		return questionName;
	}


	public void setQuestionName(String questionName) {
		this.questionName = questionName;
	}


	public CategoryEntity getCategoryEntity() {
		return categoryEntity;
	}


	public void setCategoryEntity(CategoryEntity categoryEntity) {
		this.categoryEntity = categoryEntity;
	}


	public Set<AnswersEntity> getAnswers() {
		return answers;
	}


	public void setAnswers(Set<AnswersEntity> answers) {
		this.answers = answers;
		
		for(AnswersEntity b : answers) {
	        b.setQuestionEntity(this);
	    }
		
	}
	
	
	
	///
	
	
	
	
	
}
