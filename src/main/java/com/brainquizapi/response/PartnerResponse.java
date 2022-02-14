package com.brainquizapi.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter 
@Setter
@ToString
public class PartnerResponse {
	
	private Long id;

	private String partnername; 
	private String website;
	private String url;
	private String companydetails; 
	private String emailid;
	private String ccemailid;
	private String mobileno;
	private String logopath;
}
