package com.brainquizapi.response;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class DataTableColumnsResponse {
	
	private String title;
	private String data;
	
}