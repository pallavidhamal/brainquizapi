package com.brainquizapi.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter 
@Setter
@ToString
public class ResultPdfResponse {
	
	private String categoryName; 
	private String pmapId;
	private String studentid;
	private String emailId; 
	private String categoryId;
	private String score;
	private String studentName;
	private String testName;
	private String testCode;
	private String subDate;
	private String colors;
    private String logoPath;
    private String url;



}
