package com.brainquizapi.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.brainquizapi.response.BaseResponse;
import com.brainquizapi.response.PartnerResponse;
import com.brainquizapi.service.PartnerService;
import com.brainquizapi.util.StringsUtils;
import com.brainquizapi.request.PartnerRequest;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/partner")
public class PartnerController {

	private static final Logger logger = LoggerFactory.getLogger(PartnerController.class);

	BaseResponse response = null;

	@Autowired 
	PartnerService partnerService;
	
	@GetMapping("/getAllPartners") 
	public ResponseEntity<Object> getAllPartners(HttpServletRequest request) {
		
		response = new BaseResponse();
		
		try {
			
			
            List<PartnerResponse> responseData =partnerService.getPartnerList(request);
			
			response.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
			response.setRespData(responseData);
			
			return ResponseEntity.ok(response);
			
			
				  
		}catch (Exception e) {
			
			logger.error(e.getMessage()); 
			
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
			return ResponseEntity.ok(response);
			
		}
		
	}
	
	
	
	@PostMapping(value = "/uploadPartnerLogo",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)  // , 
	public ResponseEntity<BaseResponse> uploadPartnerLogo (@RequestParam("file") MultipartFile file) {
		
		System.out.println("*******uploadPartnerLogo********"+ file);
		
		response = new BaseResponse();
		
	    try {
	    	String path = partnerService.saveDocument(file);
				
				response.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
				response.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
				response.setRespData(path);
				
				return ResponseEntity.ok(response);
					
			}catch (Exception e) {
				
				logger.error(e.getMessage()); //BAD creds message comes from here
				
				response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
				response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
				response.setRespData(e.getMessage());
				
				return ResponseEntity.badRequest().body(response);
				
			}
	}
	
	@PostMapping("/addPartner")
	public ResponseEntity<Object> addpartner(@RequestBody PartnerRequest partnerRequest ,  HttpServletRequest request) {
		
		logger.info("********partnerController addpartner()********");
		response = new BaseResponse();
		
		try {

			String flag = partnerService.addPartner(partnerRequest);
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
	
	
	
	@GetMapping("/getPartner/{id}")
	public ResponseEntity<Object> getPartner(@PathVariable Long id,HttpServletRequest request) {
		
		response = new BaseResponse();
		
		try {
			
			
            List<PartnerResponse> responseData = partnerService.getPartner(id,request);
			
			response.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
			response.setRespData(responseData);
			
			return ResponseEntity.ok(response);
			
			
				  
		}catch (Exception e) {
			
			logger.error(e.getMessage()); //BAD creds message comes from here
			
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
//			return ResponseEntity.badRequest().body(response);
			return ResponseEntity.ok(response);
			
		}
		
	}
	
	
	@PostMapping("/updatePartner")
	public ResponseEntity<Object> updatePartner(@RequestBody PartnerRequest partnerRequest ,  HttpServletRequest request) {
		
		logger.info("********partnerRequest() updatePartner********");
		response = new BaseResponse();
				
		
		
		try {
			
			String resp = partnerService.updatePartner(partnerRequest);
		
			if(!resp.equals("Success"))
			{
				response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);

				response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);

			}
			else
			{
				response.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);

				response.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);

			}
			response.setRespData(resp);
			
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
	
	
	@DeleteMapping("/deletePartner/{id}")
	public ResponseEntity<Object> deletePartner(@PathVariable long id,  HttpServletRequest request) {
		
		response = new BaseResponse();
		
		try {

			
			response = partnerService.deletePartner(id, request); 
			
			
			return ResponseEntity.ok(response);
			
		}catch (Exception e) {
			
			logger.error(e.getMessage());
			
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
			return ResponseEntity.badRequest().body(response);
			
		}
		
		
	}
	
	
}
