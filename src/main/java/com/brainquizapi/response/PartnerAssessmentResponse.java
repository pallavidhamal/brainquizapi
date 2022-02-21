package com.brainquizapi.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter 
@Setter
@ToString
public class PartnerAssessmentResponse {
	
	private Long id;

	private String altemail; 
	private String fromdate;
	private String todate;
	private String partnerassessmentname; 
	private String partnername;
	private String partnerid;
	private String assessmentname;
	private String assessmentid;
	private String status;
}
