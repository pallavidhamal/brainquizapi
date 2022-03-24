package com.brainquizapi.response;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter 
@Setter
@ToString
public class AllCandidateResultResponse {
	
	private String id;
	private String pmapid;
	private String studentname; 
	private String emailid;
	private String marks;
	private String categoryID; 
	private String category_name;
	private String colors;
}
