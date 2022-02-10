package com.brainquizapi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(	name = "allresultMaster")
@Getter @Setter
public class AllresultEntity {

	
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column(name = "userId")
	private String userId;
	
	@Column(name = "password")
	private String password; 
	
	@Column(name = "studentName")
	private String studentName; 
	
	@Column(name = "course")
	private String course; 
	
	@Column(name = "testName")
	private String testName; 
	
	@Column(name = "regNo")
	private String regNo; 
	
	@Column(name = "institute")
	private String institute; 
	
	@Column(name = "branch")
	private String branch; 
	
	@Column(name = "batch")
	private String batch;
	
	@Column(name = "subDt")
	private String subDt;
	
	@Column(name = "activeTime")
	private String activeTime; 
	
	@Column(name = "totalQuestions")
	private String totalQuestions; 
	
	@Column(name = "emailId")
	private String emailId; 
	
	@Column(name = "redirectDomain")
	private String redirectDomain; 
	
	@Column(name = "testCode")	
	private String testCode; 
	
	
	@Column(name = "quest1")	
	private String quest1; 
	
	@Column(name = "quest2")	
	private String quest2; 
	
	@Column(name = "quest3")	
	private String quest3; 
	
	@Column(name = "quest4")	
	private String quest4; 
	
	@Column(name = "quest5")	
	private String quest5; 
	
	@Column(name = "quest6")	
	private String quest6; 
	
	@Column(name = "quest7")	
	private String quest7; 
	
	@Column(name = "quest8")	
	private String quest8; 
	
	@Column(name = "quest9")	
	private String quest9; 
	
	@Column(name = "quest10")	
	private String quest10; 
	
	
	
}
