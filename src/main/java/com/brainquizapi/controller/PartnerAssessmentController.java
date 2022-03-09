package com.brainquizapi.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brainquizapi.model.PartnerAssessmentMapEntity;
import com.brainquizapi.response.BaseResponse;
import com.brainquizapi.response.PartnerAssessmentMapResponse;
import com.brainquizapi.service.AssessmentService;
import com.brainquizapi.service.PartnerAssessmentService;
import com.brainquizapi.util.StringsUtils;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/partnerassessment")
public class PartnerAssessmentController {

	
	private static final Logger logger = LoggerFactory.getLogger(PartnerAssessmentController.class);

	BaseResponse response = null;
	

	@Autowired 
	PartnerAssessmentService partnerAssessmentService;
	
	
	@GetMapping("/selectPartnerAssessmentByPartnerId/{id}")
	public ResponseEntity<Object> selectPartnerAssessmentByPartnerId(@PathVariable long id,  HttpServletRequest request) {
		
		response = new BaseResponse();
		
		try {

			
			List<PartnerAssessmentMapResponse> responseData = partnerAssessmentService.getPartnerAssessmentByPartnerId(id, request); 
			
			
			response.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
			response.setRespData(responseData);
			
			return ResponseEntity.ok(response);
			
		}catch (Exception e) {
			
			e.printStackTrace();
			
			logger.error(e.getMessage());
			
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
			return ResponseEntity.badRequest().body(response);
			
		}
	}
		
	@GetMapping("/getPartnerAssessmentMapByPartnerIdAndAssessmentId/{partnerid}/{assessmentid}")
	public ResponseEntity<Object> getPartnerAssessmentMapByPartnerIdAndAssessmentId(@PathVariable long partnerid,@PathVariable long assessmentid,  HttpServletRequest request) {
		
		response = new BaseResponse();
		
		try {

			
			List<PartnerAssessmentMapResponse> responseData = partnerAssessmentService.getPartnerAssessmentMapByPartnerIdAndAssessmentId(partnerid,assessmentid, request); 
			
			
			response.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
			response.setRespData(responseData);
			
			return ResponseEntity.ok(response);
			
		}catch (Exception e) {
			
			e.printStackTrace();
			
			logger.error(e.getMessage());
			
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
			return ResponseEntity.badRequest().body(response);
			
		}
	}	
	
}