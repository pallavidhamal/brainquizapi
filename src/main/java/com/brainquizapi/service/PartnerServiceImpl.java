package com.brainquizapi.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.brainquizapi.controller.PartnerController;
import com.brainquizapi.model.PartnerEntity;
import com.brainquizapi.repository.PartnerRepository;
import com.brainquizapi.request.PartnerRequest;
import com.brainquizapi.response.BaseResponse;
import com.brainquizapi.response.PartnerResponse;
import com.brainquizapi.util.FileStorageService;
import com.brainquizapi.util.StringsUtils;

@Service
public class PartnerServiceImpl implements PartnerService {
	
	private static final Logger logger = LoggerFactory.getLogger(PartnerServiceImpl.class);

	 @Autowired
	 PartnerRepository partnerRepository;
	 
	 @Autowired
	 private FileStorageService fileStorageService;

	@Override
	public List<PartnerResponse> getPartnerList(HttpServletRequest request) {
		// TODO Auto-generated method stub
		logger.info("*******get all partners*******");
		
		List<PartnerEntity> list = partnerRepository.findAllByIsdeleted("N");
		
		List<PartnerResponse> respList = new ArrayList<>();
		
		for(PartnerEntity partnerEnt: list) {
			
			PartnerResponse partner = new PartnerResponse();
			
			
			partner.setId(partnerEnt.getId());
			partner.setPartnername(partnerEnt.getPartnerName());
			partner.setWebsite(partnerEnt.getWebsite());
			partner.setCompanydetails(partnerEnt.getCompanyDetails());
			partner.setEmailid(partnerEnt.getEmailId());
			partner.setMobileno(partnerEnt.getMobileNo());
			
			
			respList.add(partner);
			
		}
		
		return respList;		}
	
	
	
	public String saveDocument (MultipartFile file) {
		String fileSubPath = "file/";
		String flag = "1";
		String filePath;
		filePath = fileStorageService.storeFile(file , fileSubPath);
		logger.info("---------PartnerServiceImpl saveDocument----------------");
		
		
		return filePath;
		
	}

	
	@Override
	public String addPartner(PartnerRequest partnerRequest) throws Exception {
			
			PartnerEntity partnerMasterEntity  = new  PartnerEntity();
			PartnerEntity Response =partnerRepository.findByPartnerNameAndIsdeleted(partnerRequest.getPartnername(),"N");
			String resp = null;
			
			if(Response!=null)
			{
				resp="Partner already exist!";
			}
			else
			{
				PartnerEntity partnerEntity  = new  PartnerEntity();
				PartnerEntity partnerMaster=partnerRepository.findByPartnerName(partnerRequest.getPartnername());
				if(partnerMaster!=null) {
					partnerEntity.setIsdeleted("N");
					partnerEntity.setUpdatedate(new Date());
					partnerRepository.save(partnerEntity);

				}
				else
				{
					partnerMasterEntity.setPartnerName(partnerRequest.getPartnername());
					partnerMasterEntity.setEmailId(partnerRequest.getEmailid());
					partnerMasterEntity.setCompanyDetails(partnerRequest.getCompanydetails());					
					partnerMasterEntity.setLogoPath(partnerRequest.getLogopath());
					partnerMasterEntity.setMobileNo(partnerRequest.getMobileno());
					partnerMasterEntity.setWebsite(partnerRequest.getWebsite());	
					partnerMasterEntity.setUrl(partnerRequest.getUrl());
					partnerMasterEntity.setCcEmailId(partnerRequest.getCcemailid());
					partnerMasterEntity.setCreateby(partnerRequest.getCreated_by()); // Logged User Id 
					partnerMasterEntity.setIsdeleted("N"); // By Default N	
		
					partnerRepository.save(partnerMasterEntity);
				}
			resp="success";
			
			}
			return resp;
			
		}
	
	
	@Override
	public List<PartnerResponse> getPartner(Long id, HttpServletRequest request) {
		logger.info("ID"+id);
		 List<PartnerResponse> partList = new ArrayList<>();
			
			List<PartnerEntity> partnerEntity    = partnerRepository.findByIdAndIsdeleted(id,"N");
			for(PartnerEntity entity : partnerEntity) {
				
				PartnerResponse response = new PartnerResponse();

				response.setId(entity.getId());
				
				
				response.setPartnername(entity.getPartnerName());
				response.setEmailid(entity.getEmailId());
				response.setCompanydetails(entity.getCompanyDetails());					
				response.setLogopath(entity.getLogoPath());
				response.setMobileno(entity.getMobileNo());
				response.setWebsite(entity.getWebsite());	
				response.setCcemailid(entity.getCcEmailId());
				response.setUrl(entity.getUrl());
				
				
				
				partList.add(response);
			}
			return partList;
	}
	
	
	@Override
	public String updatePartner(PartnerRequest partnerEntity) throws Exception {
		
		String resp = null;	
		boolean flg;
		
		
		
			   Optional<PartnerEntity> partnerEntit = partnerRepository.findById(partnerEntity.getId());
		     
			   PartnerEntity partnerMasterEntity = partnerEntit.get();

			   
			  // if(partnerEntit!=null) {
			  // PartnerEntity partnerMasterEntity=new PartnerEntity(); 
			  
			    partnerMasterEntity.setPartnerName(partnerEntity.getPartnername());
				partnerMasterEntity.setEmailId(partnerEntity.getEmailid());
				partnerMasterEntity.setCompanyDetails(partnerEntity.getCompanydetails());				
				partnerMasterEntity.setLogoPath(partnerEntity.getLogopath());
				partnerMasterEntity.setMobileNo(partnerEntity.getMobileno());
				partnerMasterEntity.setWebsite(partnerEntity.getWebsite());										
				partnerMasterEntity.setCreateby(partnerEntity.getCreated_by());
				partnerMasterEntity.setUrl(partnerEntity.getUrl());
				partnerMasterEntity.setCcEmailId(partnerEntity.getCcemailid());
				partnerMasterEntity.setUpdatedate(new Date());
			   
			   partnerRepository.save(partnerMasterEntity);
		       resp = "Success";
	
		 
		return resp;
	}



	@Override
	public BaseResponse deletePartner(Long id, HttpServletRequest request) throws Exception{
		
		BaseResponse baseResponse	= new BaseResponse();	
		
		
		 Optional<PartnerEntity> partnerEntit  = partnerRepository.findById(id);
		   PartnerEntity partnerMasterEntity = partnerEntit.get();

		   if(partnerMasterEntity == null) {
			   
				throw new Exception(" Invalid ID");
				
			}else {
							

//				streamEntities  = streamRespository.deleteById(id);
				partnerMasterEntity.setIsdeleted("Y");
				partnerRepository.save(partnerMasterEntity);
		
		 
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData("success");
			
			
	}
		 
		return baseResponse;
	}
	
	@Override
	public PartnerEntity getPartnerById(Long id) {
		
		logger.info("*****PartnerServiceImpl getPartnerById*****"+id);
		
		Optional<PartnerEntity> partnerEntit  = partnerRepository.findById(id);
		PartnerEntity partnerMasterEntity = partnerEntit.get();
		
		return partnerMasterEntity;
	}
	
	
}

