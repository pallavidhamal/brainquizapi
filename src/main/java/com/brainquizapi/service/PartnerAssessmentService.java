package com.brainquizapi.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.configurationprocessor.json.JSONException;

import com.brainquizapi.model.PartnerAssessmentMapEntity;
import com.brainquizapi.response.BaseResponse;
import com.brainquizapi.response.PartnerAssessmentMapResponse;

public interface PartnerAssessmentService {

	public List<PartnerAssessmentMapResponse> getPartnerAssessmentByPartnerId(long id, HttpServletRequest request);

	public List<PartnerAssessmentMapResponse> getPartnerAssessmentMapByPartnerIdAndAssessmentId(long partnerid,
			long assessmentid, HttpServletRequest request);
	
	public PartnerAssessmentMapEntity getDataByPartnerAssessmentId(long partnerAssessmentid, HttpServletRequest request);

}
