package com.brainquizapi.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter 
@Setter
@ToString

public class PartnerAssessmentMapResponse {
	
    private Long id;
	private String partnername; 
	private String assesmentname; 
	private String partassesmentname;
	private String fromDate;
	private String toDate;
	private String altEmailId;
	private String status;
	

}