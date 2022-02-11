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
@Table(	name = "partnerMaster")
@Getter @Setter
public class PartnerEntity  extends CreateUpdate{
	
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column(name = "partnerName")
	private String partnerName; 
	
	@Column(name = "website")
	private String website;

	@Column(name = "mobileNo")
	private String mobileNo;
	
	@Column(name = "companyDetails")
	private String companyDetails;
	
	@Column(name = "emailId")
	private String emailId;
	
	@Column(name = "logoPath")
	private String logoPath;
	
	@Column(name = "ccEmailId")
	private String ccEmailId;
	
	@Column(name = "url")
	private String url;
	
	
}