package com.brainquizapi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(	name = "answersMaster")
@Getter @Setter
public class AnswersEntity extends CreateUpdate{

	
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name = "answerName")
	private String answerName;
	
	
	@Column(name = "answerScore")
	private String answerScore;
	
	@Column(name = "answerOption")
	private String answerOption;
	
	@ManyToOne
    @JoinColumn(name="question_id", nullable=false)   
	private QuestionEntity questionEntity;
	
}
