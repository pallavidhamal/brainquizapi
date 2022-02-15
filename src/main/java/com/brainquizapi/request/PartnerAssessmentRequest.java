package com.brainquizapi.request;

import java.util.HashSet;
import java.util.Set;

import com.brainquizapi.model.CategoryEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter 
@Setter
@ToString
public class PartnerAssessmentRequest {
	
	
	private String  assessmentid;
	private String partnerid;
	private String fromdate;
	private String todate;
	private String altemailid;
	private String partassessmentname;
    

}
