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
	
	
	@Column(name = "partnerId")	
	private String partnerId; 
	
	@Column(name = "assessmentId")	
	private String assessmentId; 
	
	@Column(name = "pmapId")	
	private String pmapId; 
	
	
	@Column(name = "quest1",length = 2)	
	private String quest1; 
	
	@Column(name = "quest2" ,length = 2)	
	private String quest2; 
	
	@Column(name = "quest3",length = 2)	
	private String quest3; 
	
	@Column(name = "quest4",length = 2)	
	private String quest4; 
	
	@Column(name = "quest5",length = 2)	
	private String quest5; 
	
	@Column(name = "quest6",length = 2)	
	private String quest6; 
	
	@Column(name = "quest7",length = 2)		
	private String quest7; 
	
	@Column(name = "quest8",length = 2)		
	private String quest8; 
	
	@Column(name = "quest9",length = 2)		
	private String quest9; 
	
	@Column(name = "quest10",length = 2)		
	private String quest10; 
	
	@Column(name = "quest11",length = 2)		
	private String quest11; 
	
	@Column(name = "quest12",length = 2)		
	private String quest12; 
	
	@Column(name = "quest13",length = 2)		
	private String quest13; 
	
	@Column(name = "quest14",length = 2)		
	private String quest14; 
	
	@Column(name = "quest15",length = 2)		
	private String quest15; 
	
	@Column(name = "quest16",length = 2)		
	private String quest16; 
	
	@Column(name = "quest17",length = 2)		
	private String quest17; 
	
	@Column(name = "quest18",length = 2)		
	private String quest18; 
	
	@Column(name = "quest19",length = 2)		
	private String quest19; 
	
	@Column(name = "quest20",length = 2)		
	private String quest20;
	
	
	@Column(name = "quest21",length = 2)		
	private String quest21; 
	
	@Column(name = "quest22",length = 2)		
	private String quest22; 
	
	@Column(name = "quest23",length = 2)		
	private String quest23; 
	
	@Column(name = "quest24",length = 2)		
	private String quest24; 
	
	@Column(name = "quest25",length = 2)		
	private String quest25; 
	
	@Column(name = "quest26",length = 2)		
	private String quest26; 
	
	@Column(name = "quest27",length = 2)		
	private String quest27; 
	
	@Column(name = "quest28",length = 2)		
	private String quest28; 
	
	@Column(name = "quest29",length = 2)		
	private String quest29; 
	
	@Column(name = "quest30",length = 2)		
	private String quest30; 
	
	@Column(name = "quest31",length = 2)		
	private String quest31; 
	
	@Column(name = "quest32",length = 2)		
	private String quest32; 
	
	@Column(name = "quest33",length = 2)		
	private String quest33; 
	
	@Column(name = "quest34",length = 2)		
	private String quest34; 
	
	@Column(name = "quest35",length = 2)		
	private String quest35; 
	
	@Column(name = "quest36",length = 2)		
	private String quest36; 
	
	@Column(name = "quest37",length = 2)		
	private String quest37; 
	
	@Column(name = "quest38",length = 2)		
	private String quest38; 
	
	@Column(name = "quest39",length = 2)		
	private String quest39; 
	
	@Column(name = "quest40",length = 2)		
	private String quest40; 
	
	@Column(name = "quest41",length = 2)		
	private String quest41; 
	
	@Column(name = "quest42",length = 2)		
	private String quest42; 
	
	@Column(name = "quest43",length = 2)		
	private String quest43; 
	
	@Column(name = "quest44",length = 2)		
	private String quest44; 
	
	@Column(name = "quest45",length = 2)		
	private String quest45; 
	
	@Column(name = "quest46",length = 2)		
	private String quest46; 
	
	@Column(name = "quest47",length = 2)		
	private String quest47; 
	
	@Column(name = "quest48",length = 2)		
	private String quest48; 
	
	@Column(name = "quest49",length = 2)		
	private String quest49; 
	
	@Column(name = "quest50",length = 2)		
	private String quest50; 
	
	
	  @Column(name = "quest51",length = 2) private String quest51;
	  
	  @Column(name = "quest52",length = 2) private String quest52;
	  
	  @Column(name = "quest53",length = 2) private String quest53;
	  
	  @Column(name = "quest54",length = 2) private String quest54;
	  
	  @Column(name = "quest55",length = 2) private String quest55;
	  
	  @Column(name = "quest56",length = 2) private String quest56;
	  
	  @Column(name = "quest57",length = 2) private String quest57;
	  
	  @Column(name = "quest58",length = 2) private String quest58;
	  
	  @Column(name = "quest59",length = 2) private String quest59;
	  
	  @Column(name = "quest60",length = 2) private String quest60;
	  
	  @Column(name = "quest61",length = 2) private String quest61;
	  
	  @Column(name = "quest62",length = 2) private String quest62;
	  
	  @Column(name = "quest63",length = 2) private String quest63;
	  
	  @Column(name = "quest64",length = 2) private String quest64;
	  
	  @Column(name = "quest65",length = 2) private String quest65;
	  
	  @Column(name = "quest66",length = 2) private String quest66;
	  
	  @Column(name = "quest67",length = 2) private String quest67;
	  
	  @Column(name = "quest68",length = 2) private String quest68;
	  
	  @Column(name = "quest69",length = 2) private String quest69;
	  
	  @Column(name = "quest70",length = 2) private String quest70;
	  
	  @Column(name = "quest71",length = 2) private String quest71;
	  
	  @Column(name = "quest72",length = 2) private String quest72;
	  
	  @Column(name = "quest73",length = 2) private String quest73;
	  
	  @Column(name = "quest74",length = 2) private String quest74;
	  
	  @Column(name = "quest75",length = 2) private String quest75;
	  
	  @Column(name = "quest76",length = 2) private String quest76;
	  
	  @Column(name = "quest77",length = 2) private String quest77;
	  
	  @Column(name = "quest78",length = 2) private String quest78;
	  
	  @Column(name = "quest79",length = 2) private String quest79;
	  
	  @Column(name = "quest80",length = 2) private String quest80;
	  
	  @Column(name = "quest81",length = 2) private String quest81;
	  
	
	  @Column(name = "quest82",length = 2) private String quest82;
	  
	  @Column(name = "quest83",length = 2) private String quest83;
	  
	  @Column(name = "quest84",length = 2) private String quest84;
	  
	  @Column(name = "quest85",length = 2) private String quest85;
	  
	  @Column(name = "quest86",length = 2) private String quest86;
	  
	  @Column(name = "quest87",length = 2) private String quest87;
	  
	  @Column(name = "quest88",length = 2) private String quest88;
	  
	  @Column(name = "quest89",length = 2) private String quest89;
	  
	  @Column(name = "quest90",length = 2) private String quest90;
	  
	  
	  @Column(name = "quest91",length = 2) private String quest91;
	  
	  @Column(name = "quest92",length = 2) private String quest92;
	  
	  @Column(name = "quest93",length = 2) private String quest93;
	  
	  @Column(name = "quest94",length = 2) private String quest94;
	  
	  @Column(name = "quest95",length = 2) private String quest95;
	  
	  @Column(name = "quest96",length = 2) private String quest96;
	  
	  @Column(name = "quest97",length = 2) private String quest97;
	  
	  @Column(name = "quest98",length = 2) private String quest98;
	  
	  @Column(name = "quest99",length = 2) private String quest99;
	  
	  @Column(name = "quest100" ,length = 2) private String quest100;
	 
	  
	  
	 }
