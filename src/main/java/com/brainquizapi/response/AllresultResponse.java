package com.brainquizapi.response;

import java.util.Set;

import javax.persistence.Column;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter 
@Setter
@ToString
public class AllresultResponse {

	private Long id;

	private String userId;
	private String password; 
	private String studentName; 
	private String course; 
	private String testName; 
	private String regNo; 
	private String institute; 
	private String branch; 
	private String batch;
	private String subDt;
	private String activeTime; 
	private String totalQuestions; 
	private String emailId; 
	private String redirectDomain; 
	private String testCode; 
	private String quest1; 
	private String quest2; 
	private String quest3; 
	private String quest4; 
	private String quest5; 
	private String quest6; 
	private String quest7; 
	private String quest8; 
	private String quest9; 
	private String quest10; 
	
	
}
