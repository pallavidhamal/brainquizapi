package com.brainquizapi.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.brainquizapi.request.PartnerAssessmentRequest;
import com.brainquizapi.response.AllCandidateResultResponse;
import com.brainquizapi.response.BaseResponse;
import com.brainquizapi.response.ExcelDataResponse;
import com.brainquizapi.response.PartnerAssessmentMapResponse;
import com.brainquizapi.response.ResultPdfResponse;
import com.brainquizapi.response.ResultResponse;

public interface ResultService {

	 ResponseEntity<BaseResponse> uploadResultFile (@RequestParam MultipartFile resultFile,String pamapId)  throws Exception ;

	List<ResultPdfResponse> getResultParams();

	List<ExcelDataResponse> validateExcel(String pmapId);

	JSONObject getCandidateResultParams(long partnerid,
			long assessmentid, long partnerAssessmentid, HttpServletRequest request) throws JSONException;
	
	boolean ResultCalculation(String pmapId);

}
