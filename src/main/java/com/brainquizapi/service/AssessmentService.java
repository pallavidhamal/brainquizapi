package com.brainquizapi.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import com.brainquizapi.request.AssessmentRequest;
import com.brainquizapi.request.PartnerAssessmentRequest;
import com.brainquizapi.request.PartnerRequest;
import com.brainquizapi.response.AssessmentResponse;
import com.brainquizapi.response.PartnerAssessmentResponse;

	
	public interface AssessmentService {
		
		public String addAssessment(AssessmentRequest assessmentRequest) throws Exception;	
		public AssessmentResponse getAssessment(@RequestBody AssessmentRequest assessmentRequest ) ;

		public List<AssessmentResponse> getAllAssessments(@RequestBody AssessmentRequest assessmentRequest ) ;
		public String updateAssessmnent(@RequestBody AssessmentRequest assessmentRequest) ;
		public String addPartnerAssessment(PartnerAssessmentRequest partAssessRequest);
		public List<PartnerAssessmentResponse> getAllPartnerAssessmentMaps(HttpServletRequest request);
		public List<AssessmentResponse> getAllAssessments(HttpServletRequest request);

}
