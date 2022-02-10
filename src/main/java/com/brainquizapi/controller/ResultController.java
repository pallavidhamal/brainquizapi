package com.brainquizapi.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.brainquizapi.response.BaseResponse;
import com.brainquizapi.service.AssessmentService;
import com.brainquizapi.service.ResultService;
import com.brainquizapi.util.StringsUtils;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/result")
public class ResultController {
	
	
	private static final Logger logger = LoggerFactory.getLogger(PartnerController.class);

	BaseResponse response = null;
	
	@Autowired 
	ResultService resultService;
	
	@PostMapping(value = "/uploadResultFile" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<BaseResponse> uploadResultFile (@RequestParam MultipartFile resultFile) {
		
		
		response = new BaseResponse();
		
	    try {
	    	
	    	//List<UniversityStudDocResponse> List = resultService.uploadResultFile(resultFile);
	    	resultService.uploadResultFile(resultFile);
				response.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
				response.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
				response.setRespData(null);
				
				return ResponseEntity.ok(response);
					
			}catch (Exception e) {
				
				logger.error(e.getMessage()); //BAD creds message comes from here
				
				response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
				response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
				response.setRespData(e.getMessage());
				
				return ResponseEntity.badRequest().body(response);
				
			}
	}
	
	
	
	
	

}
