package com.brainquizapi.service;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.brainquizapi.request.PartnerAssessmentRequest;
import com.brainquizapi.response.BaseResponse;
import com.brainquizapi.response.ExcelDataResponse;
import com.brainquizapi.response.ResultPdfResponse;

public interface ResultService {

	 ResponseEntity<BaseResponse> uploadResultFile (@RequestParam MultipartFile resultFile,String pamapId)  throws Exception ;

	List<ResultPdfResponse> getResultParams();

	List<ExcelDataResponse> validateExcel(String pmapId);
}
