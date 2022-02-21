package com.brainquizapi.service;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.brainquizapi.request.PartnerAssessmentRequest;
import com.brainquizapi.response.BaseResponse;

public interface ResultService {

	 ResponseEntity<BaseResponse> uploadResultFile (@RequestParam MultipartFile resultFile,String partnerId ,String assessmentId,String pamapId)  throws Exception ;

	
}
