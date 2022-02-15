package com.brainquizapi.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.brainquizapi.model.PartnerEntity;
import com.brainquizapi.request.PartnerRequest;
import com.brainquizapi.response.BaseResponse;
import com.brainquizapi.response.PartnerResponse;


public interface PartnerService {
	
	//public List<PartnerResponse> getPartnerList(Long id,HttpServletRequest request);

	public List<PartnerResponse> getPartnerList(HttpServletRequest request);
	public String saveDocument(MultipartFile file);
	public String addPartner(PartnerRequest partnerRequest) throws Exception;
	public List<PartnerResponse> getPartner(Long id,HttpServletRequest request);
	public String updatePartner(PartnerRequest partnerRequest) throws Exception;

	public BaseResponse deletePartner(Long id,HttpServletRequest request) throws Exception;
	PartnerEntity getPartnerById(Long id);

}
