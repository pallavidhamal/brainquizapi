package com.brainquizapi.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(	name = "categoryMaster")
//@Getter @Setter
public class CategoryEntity extends CreateUpdate{

	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name = "categoryName")
	private String categoryName;
	
	
	@Column(name = "categorySeq")
	private int categorySeq;
	
	@Column(name = "noOfQuestions")
	private int noOfQuestions;
	
	
	@Column(name = "redFrom")
	private int redFrom;
	
	@Column(name = "redTo")
	private int redTo;
	
	@Column(name = "greenFrom")
	private int greenFrom;
	
	@Column(name = "greenTo")
	private int greenTo;
	
	@Column(name = "amberFrom")
	private int amberFrom;
	
	@Column(name = "amberTo")
	private int amberTo;
	
	
	@ManyToOne
    @JoinColumn(name="assessment_id", nullable=false)   
	private AssessmentEntity assessmentEntity;
	
	
	@OneToMany(mappedBy = "categoryEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<QuestionEntity> questions ;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getCategoryName() {
		return categoryName;
	}


	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}


	public int getCategorySeq() {
		return categorySeq;
	}


	public void setCategorySeq(int categorySeq) {
		this.categorySeq = categorySeq;
	}


	public int getNoOfQuestions() {
		return noOfQuestions;
	}


	public void setNoOfQuestions(int noOfQuestions) {
		this.noOfQuestions = noOfQuestions;
	}


	public int getRedFrom() {
		return redFrom;
	}


	public void setRedFrom(int redFrom) {
		this.redFrom = redFrom;
	}


	public int getRedTo() {
		return redTo;
	}


	public void setRedTo(int redTo) {
		this.redTo = redTo;
	}


	public int getGreenFrom() {
		return greenFrom;
	}


	public void setGreenFrom(int greenFrom) {
		this.greenFrom = greenFrom;
	}


	public int getGreenTo() {
		return greenTo;
	}


	public void setGreenTo(int greenTo) {
		this.greenTo = greenTo;
	}


	public int getAmberFrom() {
		return amberFrom;
	}


	public void setAmberFrom(int amberFrom) {
		this.amberFrom = amberFrom;
	}


	public int getAmberTo() {
		return amberTo;
	}


	public void setAmberTo(int amberTo) {
		this.amberTo = amberTo;
	}


	public AssessmentEntity getAssessmentEntity() {
		return assessmentEntity;
	}


	public void setAssessmentEntity(AssessmentEntity assessmentEntity) {
		this.assessmentEntity = assessmentEntity;
	}


	public Set<QuestionEntity> getQuestions() {
		return questions;
	}


	public void setQuestions(Set<QuestionEntity> questions) {
		this.questions = questions;
		
		for(QuestionEntity b : questions) {
	        b.setCategoryEntity(this);
	    }
		
	}
	
	
	
	
	//////////
	
	
	
	
	
}
