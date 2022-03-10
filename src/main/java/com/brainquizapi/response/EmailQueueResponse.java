package com.brainquizapi.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;


@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailQueueResponse {

	public String id;
	public int studentid;
	public String email_id;
	public int partnerId;
	public int assessmentId;
	public int pmapId;
	public String emailstatus;
	public int emailcount;

}



	
	
	

