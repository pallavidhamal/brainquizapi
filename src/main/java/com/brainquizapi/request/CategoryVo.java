package com.brainquizapi.request;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter 
@Setter
@ToString
public class CategoryVo {

	private String id;
	private String categoryName;
	private int categorySeq;	
	private int noOfQuestions;	
	private int redFrom;	
	private int redTo;	
	private int greenFrom;	
	private int greenTo;	
	private int amberFrom;	
	private int amberTo;
    private Set<QuestionsVo> questions = new HashSet<>();

	
}
