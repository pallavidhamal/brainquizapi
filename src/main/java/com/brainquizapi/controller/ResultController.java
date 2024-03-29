package com.brainquizapi.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.brainquizapi.request.PartnerRequest;
import com.brainquizapi.response.BaseResponse;
import com.brainquizapi.response.ExcelDataResponse;
import com.brainquizapi.response.ResultPdfResponse;
import com.brainquizapi.service.EmailService;
import com.brainquizapi.service.ResultService;
import com.brainquizapi.util.StringsUtils;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/result")
public class ResultController {
	
	
	private static final Logger logger = LoggerFactory.getLogger(PartnerController.class);

	BaseResponse response = null;
	
	@Autowired 
	ResultService resultService;
	
	@Autowired
	EmailService emailService;
	
	/*
	 * @Autowired ResultRepository resultRepo;
	 */
	
	/*
	 * @PostMapping( path = "/uploadFileTodo", consumes =
	 * MediaType.MULTIPART_FORM_DATA_VALUE, produces =
	 * MediaType.APPLICATION_JSON_VALUE ) public ResponseEntity<Todo>
	 * saveTodo(@RequestParam("title") String title,
	 * 
	 * @RequestParam("description") String description,
	 * 
	 * @RequestParam("file") MultipartFile file) {
	 */
	
	
	@PostMapping(value = "/uploadResultFile" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<BaseResponse> uploadResultFile (@RequestParam MultipartFile resultFile,@RequestParam("pamapId") String pamapId) {
		
		
		response = new BaseResponse();
		
	    try {
	    	
	    	//List<UniversityStudDocResponse> List = resultService.uploadResultFile(resultFile);
	    	System.out.println("pamapId"+pamapId);
	    	resultService.uploadResultFile(resultFile,pamapId);
			response.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
			response.setRespData(null);
				
				return ResponseEntity.ok(response);
					
			}catch (Exception e) {
				
				logger.error(e.getMessage()); //BAD creds message comes from here
				
				response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
				response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
				response.setRespData(e.getMessage());
				
				return ResponseEntity.badRequest().body(response);
				
			}
	}
	
	@PostMapping(value = "/resendAllToEmailQueue" )
	public ResponseEntity<BaseResponse> resendAllToEmailQueue (@RequestBody PartnerRequest partnerRequest,HttpServletRequest request) {
		
		
		response = new BaseResponse();
		
	    try {
	    	
	    	String pamapId=partnerRequest.getPamapId();	    	
	    	String assId=partnerRequest.getAssId();
	    	String partId=partnerRequest.getPartId();

	    	
	    	System.out.println("resendAllToEmailQueue  pamapId"+pamapId+"--assId--"+assId+"-partId-"+partId);

	    	//List<UniversityStudDocResponse> List = resultService.uploadResultFile(resultFile);
	    	resultService.resendAllToEmailQueue(pamapId,assId,partId);
			response.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
			response.setRespData(null);
				
			return ResponseEntity.ok(response);
					
			}catch (Exception e) {
				
				logger.error(e.getMessage()); //BAD creds message comes from here
				
				response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
				response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
				response.setRespData(e.getMessage());
				
				return ResponseEntity.badRequest().body(response);
				
			}
	}
	
	
	
	
	
	@PostMapping("/exportresultpdf")
    public ResponseEntity<BaseResponse> exportToPDF(@RequestBody PartnerRequest partnerRequest,HttpServletRequest request) throws Exception {
		
		logger.info("*****ResultController exportResultPdf*****");
		response = new BaseResponse();
		try {
			
			Long id=partnerRequest.getId();
			String pamapId=partnerRequest.getPamapId();
			int pmapId=Integer.parseInt(pamapId);
			String emailIds=partnerRequest.getEmailid();
			
			List<ResultPdfResponse> result = resultService.getResultParams(pmapId,id.intValue());
			emailService.exportResult(result,emailIds);
			
			response.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
			response.setRespData(null);
			
			return ResponseEntity.ok(response);
			
		}catch(Exception e) {
        	
			logger.error(e.getMessage()); //BAD creds message comes from here
			
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
			return ResponseEntity.badRequest().body(response);        }
	}
	
	/*
	 * @PostMapping("/exportAllresultpdf") public void
	 * exportAllresultpdf(HttpServletResponse response ,HttpServletRequest request)
	 * throws Exception {
	 * 
	 * logger.info("*****ResultController exportResultPdf*****");
	 * 
	 * try {
	 * 
	 * 
	 * 
	 * // List<ResultPdfResponse> result = resultService.getResultParams(); //
	 * emailService.exportResult( result);
	 * 
	 * 
	 * 
	 * 
	 * }catch(Exception e) {
	 * 
	 * logger.info("-----imageLocation---------------"+e.getMessage()); } }
	 */
	
	
	@PostMapping("/validateExcel")
	public ResponseEntity<Object> validateExcel(@RequestBody PartnerRequest partnerRequest ,  HttpServletRequest request) {
		
		logger.info("********partnerController addpartner()********");
		response = new BaseResponse();
		
		try {
			String pamapId=partnerRequest.getPamapId();
			List<ExcelDataResponse> respList= resultService.validateExcel(pamapId);
			
				response.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
				response.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);


			response.setRespData(respList);
			
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
	
	@PostMapping("/resultCalculation")
	public ResponseEntity<Object> ResultCalculation(@RequestBody PartnerRequest partnerRequest ,  HttpServletRequest request) {
		
		logger.info("********ResultCalculation********");
		response = new BaseResponse();
		boolean flag;
		
		try {
			String pamapId=partnerRequest.getPamapId();
			flag= resultService.ResultCalculation(pamapId);
			
			response.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
			response.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
			response.setRespData(flag);
			
			return ResponseEntity.ok(response);
			
		}
		catch (Exception e) {
			
			e.printStackTrace();
			logger.error(e.getMessage());
			
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
			//return ResponseEntity.badRequest().body(response);
			return ResponseEntity.ok(response);
		}
		
   }
	
	
	  @GetMapping(
	  "/getPartnerAssessmentMapByPartnerIdAndAssessmentIdAndPartnerAssessmentid/{partnerid}/{assessmentid}/{partnerAssessmentid}")
	  public ResponseEntity<BaseResponse> getPartnerAssessmentMapByPartnerIdAndAssessmentId(@PathVariable long
	  partnerid,@PathVariable long assessmentid, @PathVariable long
	  partnerAssessmentid , HttpServletRequest request) {
	  
	  response = new BaseResponse();
	  
	  try {
	  
		  Map<String, Object> responseData = resultService.getCandidateResultParams(partnerid,assessmentid,partnerAssessmentid, request);
	  
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
	  
	  return ResponseEntity.badRequest().body(response); } }
	 
}
