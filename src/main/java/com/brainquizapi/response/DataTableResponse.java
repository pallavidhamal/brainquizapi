package com.brainquizapi.response;
import java.util.List;

import org.springframework.boot.configurationprocessor.json.JSONObject;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class DataTableResponse {
	
	private List<DataTableColumnsResponse> columns;
	private List<DataTableDataResponse> data;
	
}