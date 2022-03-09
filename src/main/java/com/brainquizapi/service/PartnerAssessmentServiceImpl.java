package com.brainquizapi.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brainquizapi.model.AssessmentEntity;
import com.brainquizapi.model.PartnerAssessmentMapEntity;
import com.brainquizapi.model.PartnerEntity;
import com.brainquizapi.repository.PartnerAssessmentMapRepository;
import com.brainquizapi.repository.PartnerRepository;
import com.brainquizapi.response.BaseResponse;
import com.brainquizapi.response.PartnerAssessmentMapResponse;

@Service
public class PartnerAssessmentServiceImpl implements PartnerAssessmentService {

	@Autowired
	PartnerAssessmentMapRepository assessmentMapRepository; 
	
	@Autowired
	PartnerService partnerService;
	
	@Autowired
	AssessmentService assessmentService;
	
	@Override
	public List<PartnerAssessmentMapResponse> getPartnerAssessmentByPartnerId(long id, HttpServletRequest request) {
		// TODO Auto-generated method stub
		
			
		PartnerEntity partnerEntity = partnerService.getPartnerById(id);
		
		
		List<PartnerAssessmentMapResponse> assessmentMapResponses = new ArrayList<PartnerAssessmentMapResponse>();
		
		
		List<PartnerAssessmentMapEntity> partnerAssessmentMapEntities =  assessmentMapRepository.findByPartnerAndIsdeleted(partnerEntity,"N");
		
		
		for(PartnerAssessmentMapEntity assessmentMapEntity : partnerAssessmentMapEntities ) {
			
			PartnerAssessmentMapResponse assessmentMapResponse = new PartnerAssessmentMapResponse();
			
			if(assessmentMapEntity.getStatus() == null) {
			
				System.out.println("==============status========null=========");
				
			}
			
			
			assessmentMapResponse.setId(assessmentMapEntity.getId());
			assessmentMapResponse.setPartnername(partnerService.getPartnerById(assessmentMapEntity.getPartner().getId()).getPartnerName());
			assessmentMapResponse.setAssesmentname(assessmentService.getAssessmentById(assessmentMapEntity.getAssessment().getId()).getTestName());
			assessmentMapResponse.setPartassesmentname(assessmentMapEntity.getPartAssessName());
			assessmentMapResponse.setFromDate(assessmentMapEntity.getFromDate());
			assessmentMapResponse.setToDate(assessmentMapEntity.getToDate());
			assessmentMapResponse.setStatus(assessmentMapEntity.getStatus());
			
			
			assessmentMapResponses.add(assessmentMapResponse);
		}
		
		
		return assessmentMapResponses;
	}

	@Override
	public List<PartnerAssessmentMapResponse> getPartnerAssessmentMapByPartnerIdAndAssessmentId(long partnerid,
			long assessmentid, HttpServletRequest request) {
		// TODO Auto-generated method stub
		
			
		PartnerEntity partnerEntity = partnerService.getPartnerById(partnerid);
		
		AssessmentEntity assessmentEntity = assessmentService.getAssessmentEntityById(assessmentid);
		
		
		List<PartnerAssessmentMapResponse> assessmentMapResponses = new ArrayList<PartnerAssessmentMapResponse>();
		
		
		List<PartnerAssessmentMapEntity> partnerAssessmentMapEntities =  assessmentMapRepository.findByPartnerAndAssessmentAndIsdeleted(partnerEntity,assessmentEntity,"N");
		
		
		for(PartnerAssessmentMapEntity assessmentMapEntity : partnerAssessmentMapEntities ) {
			
			PartnerAssessmentMapResponse assessmentMapResponse = new PartnerAssessmentMapResponse();
			
			
			assessmentMapResponse.setId(assessmentMapEntity.getId());
			assessmentMapResponse.setPartnername(partnerService.getPartnerById(assessmentMapEntity.getPartner().getId()).getPartnerName());
			assessmentMapResponse.setAssesmentname(assessmentService.getAssessmentById(assessmentMapEntity.getAssessment().getId()).getTestName());
			assessmentMapResponse.setPartassesmentname(assessmentMapEntity.getPartAssessName());
			assessmentMapResponse.setFromDate(assessmentMapEntity.getFromDate());
			assessmentMapResponse.setToDate(assessmentMapEntity.getToDate());
			assessmentMapResponse.setStatus(assessmentMapEntity.getStatus());
			
			
			assessmentMapResponses.add(assessmentMapResponse);
		}
		
		return assessmentMapResponses;
	}

	@Override
	public PartnerAssessmentMapEntity getDataByPartnerAssessmentId(long partnerAssessmentid,
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		
		PartnerAssessmentMapEntity partnerAssessmentMapEntities =  assessmentMapRepository.findById(partnerAssessmentid).get();
		
		return partnerAssessmentMapEntities;
	}



}
