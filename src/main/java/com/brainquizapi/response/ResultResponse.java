package com.brainquizapi.response;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class ResultResponse {
	
	public int respCode;
	private String respMessage;
	private Object respData;
	
}