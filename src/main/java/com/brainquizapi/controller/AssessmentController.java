package com.brainquizapi.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brainquizapi.request.AssessmentRequest;
import com.brainquizapi.request.PartnerRequest;
import com.brainquizapi.response.AssessmentResponse;
import com.brainquizapi.response.BaseResponse;
import com.brainquizapi.response.PartnerResponse;
import com.brainquizapi.service.AssessmentService;
import com.brainquizapi.service.PartnerService;
import com.brainquizapi.util.StringsUtils;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/assessment")
public class AssessmentController {

	
	private static final Logger logger = LoggerFactory.getLogger(PartnerController.class);

	BaseResponse response = null;

	@Autowired 
	AssessmentService assessmentService;
	
	@PostMapping("/addAssessment")
	public ResponseEntity<Object> addAssessment(@RequestBody AssessmentRequest assessmentRequest ,  HttpServletRequest request) {
		
		response = new BaseResponse();

		try {

			String flag = assessmentService.addAssessment(assessmentRequest);
			if(!flag.equals("success"))
			{
				response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);

				response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);

			}
			else
			{
				response.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
				response.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);


			}
			response.setRespData(flag);
			
			return ResponseEntity.ok(response);
			
		}
		catch (Exception e) {
			logger.error(e.getMessage());
			
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
			//return ResponseEntity.badRequest().body(response);
			return ResponseEntity.ok(response);
		}
		
   }
	
	
	@PostMapping("/getAssessment")
	public AssessmentResponse getAssessment(@RequestBody AssessmentRequest assessmentRequest ,  HttpServletRequest request) {

	
		AssessmentResponse AR=assessmentService.getAssessment(assessmentRequest);
		
		return AR;
	}
	
	@PostMapping("/getAllAssessments")
	public List<AssessmentResponse> getAllAssessments(AssessmentRequest assessmentRequest) {
		
		return null;
	}
	
	@PostMapping("/updateAssessmnent")
	public String updateAssessmnent(@RequestBody AssessmentRequest assessmentRequest) {
		
		
		String response=assessmentService.updateAssessmnent(assessmentRequest);
		
		return response;
	
	}
	
	
	
	
}
