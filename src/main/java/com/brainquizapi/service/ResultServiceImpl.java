package com.brainquizapi.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.brainquizapi.controller.PartnerController;
import com.brainquizapi.model.AllresultEntity;
import com.brainquizapi.model.AssessmentEntity;
import com.brainquizapi.model.CategoryEntity;
import com.brainquizapi.model.PartnerAssessmentMapEntity;
import com.brainquizapi.model.PartnerEntity;
import com.brainquizapi.repository.CategoryRepository;
import com.brainquizapi.repository.PartnerAssessmentMapRepository;
import com.brainquizapi.repository.ResultRepository;
import com.brainquizapi.request.PartnerAssessmentRequest;
import com.brainquizapi.response.AllCandidateResultResponse;
import com.brainquizapi.response.BaseResponse;
import com.brainquizapi.response.DataTableColumnsResponse;
import com.brainquizapi.response.DataTableDataResponse;
import com.brainquizapi.response.DataTableResponse;
import com.brainquizapi.response.ExcelDataResponse;
import com.brainquizapi.response.PartnerResponse;
import com.brainquizapi.response.ResultPdfResponse;
import com.brainquizapi.response.ResultResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ResultServiceImpl implements ResultService
{
	
	private static final Logger logger = LoggerFactory.getLogger(ResultServiceImpl.class);
	
	private XSSFWorkbook workbook;

	@Autowired
	ResultRepository resultRepository;
	
	@Autowired
	PartnerAssessmentMapRepository pAssessmentRep;
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	PartnerService partnerService;
	
	@Autowired
	PartnerAssessmentService partnerAssessmentService;
	
	@Autowired
	AssessmentService assessmentService;

	BaseResponse response = null;
	
	
	@Override
	public ResponseEntity<BaseResponse> uploadResultFile(MultipartFile resultFile,String pamapId)  throws Exception {
		// TODO Auto-generated method stub
		
		String assId="",ipPartner="",excelPartner="";
		
		List<AllresultEntity> allresultEntity = new ArrayList<AllresultEntity>();

		 workbook = new XSSFWorkbook(resultFile.getInputStream());
		 XSSFSheet worksheet = workbook.getSheetAt(0);
		 
		int rowcnt=worksheet.getLastRowNum();
		int cellCnt=worksheet.getRow(0).getLastCellNum() ;
		int noOfColumns = worksheet.getRow(0).getPhysicalNumberOfCells();

		logger.info("Columns="+noOfColumns+"clomncnt="+cellCnt);

		String dbAssName="",excelAssName="";
		
		Optional<PartnerAssessmentMapEntity> pamapEntity=pAssessmentRep.findById(Long.parseLong(pamapId));
		PartnerAssessmentMapEntity pmEntity = pamapEntity.get();

		String assmId="",partId="";
		
		if(pmEntity!=null) {
		 assmId=pmEntity.getAssessment().getId().toString();
		 partId=pmEntity.getPartner().getId().toString();
		}
		
		
		List<PartnerAssessmentMapEntity> pEntity    = pAssessmentRep.findByPartnerAssId(assmId, partId);
		
		
		
		//pAssessmentRep.findById(id)
		
		
		int noOfQuestionsDb=0;
		noOfQuestionsDb=categoryRepository.getQuestionCntByAssId(assmId);
		
		if(pEntity!=null)
		{
			for(PartnerAssessmentMapEntity entity : pEntity) 
			{
				dbAssName=entity.getPartAssessName();
			}
		}
		else
		{
			 throw new Exception("Mapping for partner and test doesn't exist");
		}	
		
		
		System.out.println("dbAssName="+dbAssName);
		/*
		 * try {
		 */
		
		for(int i=1;i<rowcnt;i++) 
		{
			
			 AllresultEntity oneExcelEntity=new AllresultEntity();
			 XSSFRow row = worksheet.getRow(i);		 
		   
			 if(row!=null) 
		     {
				excelAssName=checkIfCellBlank(row.getCell(4));
				System.out.println("excelAssName="+excelAssName);


				if(!dbAssName.equals(excelAssName))
				{
					 throw new Exception("Excel Test name doesnot match with database test name for row"+ i);
				}
				 
		    	int rowcell= row.getPhysicalNumberOfCells();
				logger.info("rowcell="+rowcell);
		     
				oneExcelEntity.setPartnerId(partId);
				oneExcelEntity.setAssessmentId(assmId); 
				oneExcelEntity.setPmapId(pamapId); 
				oneExcelEntity.setUserId(row.getCell(0).getStringCellValue());
				oneExcelEntity.setPassword(row.getCell(1).getStringCellValue());
				
				Integer stuno=(int) row.getCell(2).getNumericCellValue();
				oneExcelEntity.setStudentName(stuno.toString());
				//oneExcelEntity.setStudent(String(row.getCell(2).getNumericCellValue());
				oneExcelEntity.setCourse(row.getCell(3).getStringCellValue());
				oneExcelEntity.setTestName(row.getCell(4).getStringCellValue());
				oneExcelEntity.setRegNo(row.getCell(5).getStringCellValue());
				oneExcelEntity.setInstitute(row.getCell(6).getStringCellValue());
				//  oneExcelEntity.setBatch(row.getCell(7).getStringCellValue());
				System.out.println(row.getCell(7).getNumericCellValue());
				Integer batch=(int) row.getCell(7).getNumericCellValue();
				oneExcelEntity.setBatch(batch.toString());
				oneExcelEntity.setBranch(row.getCell(8).getStringCellValue());
 			    if (DateUtil.isCellDateFormatted(row.getCell(9))) 
 			    {
 			    //System.out.println("dtt"+row.getCell(9).getDateCellValue());  
 			    Date date=row.getCell(9).getDateCellValue();
 			    oneExcelEntity.setSubDt(date.toString());
                } 
 			    else 
 			    {
                // System.out.println("no"+row.getCell(9).getNumericCellValue());
                Integer subdt=(int) row.getCell(9).getNumericCellValue();
                oneExcelEntity.setSubDt(subdt.toString());
                }
  			    //oneExcelEntity.setSubDt(row.getCell(9).getStringCellValue()); 
				int cellType=row.getCell(10).getCellType();
				// logger.info("active time celltype "+cellType);
    			if(cellType==1)
				{
				oneExcelEntity.setActiveTime(row.getCell(10).getStringCellValue());
				}
				else
				{
				Integer activeTime=(int) row.getCell(10).getNumericCellValue();		        
				oneExcelEntity.setActiveTime(activeTime.toString());			 
				}
				  
				 			
				  
				  Integer noQ=(int) row.getCell(11).getNumericCellValue();
				  
				  if(noOfQuestionsDb!=noQ)
				  {
					  throw new Exception("Excel NoOfQuestions doesnot match with database for row"+i);
				  }
				  
			      oneExcelEntity.setTotalQuestions(noQ.toString());
				  
				  oneExcelEntity.setEmailId(row.getCell(12).getStringCellValue());
				  oneExcelEntity.setRedirectDomain(row.getCell(13).getStringCellValue());
				  oneExcelEntity.setTestCode(row.getCell(14).getStringCellValue());
				  
				  oneExcelEntity.setQuest1(row.getCell(15).getStringCellValue());
				  oneExcelEntity.setQuest2(row.getCell(16).getStringCellValue());
				  oneExcelEntity.setQuest3(row.getCell(17).getStringCellValue());
				  oneExcelEntity.setQuest4(row.getCell(18).getStringCellValue());
				  oneExcelEntity.setQuest5(row.getCell(19).getStringCellValue());
				  oneExcelEntity.setQuest6(row.getCell(20).getStringCellValue());
				  oneExcelEntity.setQuest7(row.getCell(21).getStringCellValue());
				  oneExcelEntity.setQuest8(row.getCell(22).getStringCellValue());
				  oneExcelEntity.setQuest9(row.getCell(23).getStringCellValue());
				
				  oneExcelEntity.setQuest10(checkIfCellBlank(row.getCell(24)));
				  oneExcelEntity.setQuest11(checkIfCellBlank(row.getCell(25)));
				  oneExcelEntity.setQuest12(checkIfCellBlank(row.getCell(26)));
				  oneExcelEntity.setQuest13(checkIfCellBlank(row.getCell(27)));
				  oneExcelEntity.setQuest14(checkIfCellBlank(row.getCell(28)));
				  oneExcelEntity.setQuest15(checkIfCellBlank(row.getCell(29)));
				
				  oneExcelEntity.setQuest16(checkIfCellBlank(row.getCell(30)));
				  oneExcelEntity.setQuest17(checkIfCellBlank(row.getCell(31)));
				  oneExcelEntity.setQuest18(checkIfCellBlank(row.getCell(32)));
				  oneExcelEntity.setQuest19(checkIfCellBlank(row.getCell(33)));
				  oneExcelEntity.setQuest20(checkIfCellBlank(row.getCell(34)));
				  oneExcelEntity.setQuest21(checkIfCellBlank(row.getCell(35)));
				  
				  oneExcelEntity.setQuest22(checkIfCellBlank(row.getCell(36)));
				  oneExcelEntity.setQuest23(checkIfCellBlank(row.getCell(37)));
				  oneExcelEntity.setQuest24(checkIfCellBlank(row.getCell(38)));
				  oneExcelEntity.setQuest25(checkIfCellBlank(row.getCell(39)));
				  oneExcelEntity.setQuest26(checkIfCellBlank(row.getCell(40)));
				  oneExcelEntity.setQuest27(checkIfCellBlank(row.getCell(41)));
				  oneExcelEntity.setQuest28(checkIfCellBlank(row.getCell(42)));
				  oneExcelEntity.setQuest29(checkIfCellBlank(row.getCell(43)));
				  oneExcelEntity.setQuest30(checkIfCellBlank(row.getCell(44)));
				  
				  oneExcelEntity.setQuest31(checkIfCellBlank(row.getCell(45)));
				  oneExcelEntity.setQuest32(checkIfCellBlank(row.getCell(46)));
				  oneExcelEntity.setQuest33(checkIfCellBlank(row.getCell(47)));
				  oneExcelEntity.setQuest34(checkIfCellBlank(row.getCell(48)));
				  oneExcelEntity.setQuest35(checkIfCellBlank(row.getCell(49)));
				  oneExcelEntity.setQuest36(checkIfCellBlank(row.getCell(50)));
				  oneExcelEntity.setQuest37(checkIfCellBlank(row.getCell(51)));
				  oneExcelEntity.setQuest38(checkIfCellBlank(row.getCell(52)));
				  oneExcelEntity.setQuest39(checkIfCellBlank(row.getCell(53)));
				  oneExcelEntity.setQuest40(checkIfCellBlank(row.getCell(54)));
				  
				  
				  oneExcelEntity.setQuest41(checkIfCellBlank(row.getCell(55)));
				  oneExcelEntity.setQuest42(checkIfCellBlank(row.getCell(56)));
				  oneExcelEntity.setQuest43(checkIfCellBlank(row.getCell(57)));
				  oneExcelEntity.setQuest44(checkIfCellBlank(row.getCell(58)));
				  oneExcelEntity.setQuest45(checkIfCellBlank(row.getCell(59)));
				  oneExcelEntity.setQuest46(checkIfCellBlank(row.getCell(60)));
				  oneExcelEntity.setQuest47(checkIfCellBlank(row.getCell(61)));				
				  oneExcelEntity.setQuest48(checkIfCellBlank(row.getCell(62)));
				  oneExcelEntity.setQuest49(checkIfCellBlank(row.getCell(63)));
				  oneExcelEntity.setQuest50(checkIfCellBlank(row.getCell(64)));
				  
				  oneExcelEntity.setQuest51(checkIfCellBlank(row.getCell(65)));
				  oneExcelEntity.setQuest52(checkIfCellBlank(row.getCell(66)));
				  oneExcelEntity.setQuest53(checkIfCellBlank(row.getCell(67)));
				  oneExcelEntity.setQuest54(checkIfCellBlank(row.getCell(68)));
				  oneExcelEntity.setQuest55(checkIfCellBlank(row.getCell(69)));
				  oneExcelEntity.setQuest56(checkIfCellBlank(row.getCell(70)));
				  oneExcelEntity.setQuest57(checkIfCellBlank(row.getCell(71)));
				  oneExcelEntity.setQuest58(checkIfCellBlank(row.getCell(72)));
				  oneExcelEntity.setQuest59(checkIfCellBlank(row.getCell(73)));
				  oneExcelEntity.setQuest60(checkIfCellBlank(row.getCell(74)));
				 
				  oneExcelEntity.setQuest61(checkIfCellBlank(row.getCell(75)));
				  oneExcelEntity.setQuest62(checkIfCellBlank(row.getCell(76)));
				  oneExcelEntity.setQuest63(checkIfCellBlank(row.getCell(77)));
				  oneExcelEntity.setQuest64(checkIfCellBlank(row.getCell(78)));
				  oneExcelEntity.setQuest65(checkIfCellBlank(row.getCell(79)));
				  oneExcelEntity.setQuest66(checkIfCellBlank(row.getCell(80)));
				  oneExcelEntity.setQuest67(checkIfCellBlank(row.getCell(81)));
				  oneExcelEntity.setQuest68(checkIfCellBlank(row.getCell(82)));
				  oneExcelEntity.setQuest69(checkIfCellBlank(row.getCell(83)));
				  oneExcelEntity.setQuest70(checkIfCellBlank(row.getCell(84)));
				  
				  oneExcelEntity.setQuest71(checkIfCellBlank(row.getCell(85)));
				  oneExcelEntity.setQuest72(checkIfCellBlank(row.getCell(86)));
				  oneExcelEntity.setQuest73(checkIfCellBlank(row.getCell(87)));
				  oneExcelEntity.setQuest74(checkIfCellBlank(row.getCell(88)));
				  oneExcelEntity.setQuest75(checkIfCellBlank(row.getCell(89)));
				  oneExcelEntity.setQuest76(checkIfCellBlank(row.getCell(90)));
				  oneExcelEntity.setQuest77(checkIfCellBlank(row.getCell(91)));
				  oneExcelEntity.setQuest78(checkIfCellBlank(row.getCell(92)));
				  oneExcelEntity.setQuest79(checkIfCellBlank(row.getCell(93)));
				  oneExcelEntity.setQuest70(checkIfCellBlank(row.getCell(94)));
				  
				  oneExcelEntity.setQuest81(checkIfCellBlank(row.getCell(95)));
				  oneExcelEntity.setQuest82(checkIfCellBlank(row.getCell(96)));
				  oneExcelEntity.setQuest83(checkIfCellBlank(row.getCell(97)));
				  oneExcelEntity.setQuest84(checkIfCellBlank(row.getCell(98)));
				  oneExcelEntity.setQuest85(checkIfCellBlank(row.getCell(99)));
				  oneExcelEntity.setQuest86(checkIfCellBlank(row.getCell(100)));
				  oneExcelEntity.setQuest87(checkIfCellBlank(row.getCell(111)));
				  oneExcelEntity.setQuest88(checkIfCellBlank(row.getCell(112)));
				  oneExcelEntity.setQuest89(checkIfCellBlank(row.getCell(113)));
				  oneExcelEntity.setQuest90(checkIfCellBlank(row.getCell(114)));
				  
				  
				  oneExcelEntity.setQuest91(checkIfCellBlank(row.getCell(115)));
				  oneExcelEntity.setQuest92(checkIfCellBlank(row.getCell(116)));
				  oneExcelEntity.setQuest93(checkIfCellBlank(row.getCell(117)));
				  oneExcelEntity.setQuest94(checkIfCellBlank(row.getCell(118)));
				  oneExcelEntity.setQuest95(checkIfCellBlank(row.getCell(119)));
				  oneExcelEntity.setQuest96(checkIfCellBlank(row.getCell(120)));
				  oneExcelEntity.setQuest97(checkIfCellBlank(row.getCell(121)));
				  oneExcelEntity.setQuest98(checkIfCellBlank(row.getCell(122)));
				  oneExcelEntity.setQuest99(checkIfCellBlank(row.getCell(123)));
				  oneExcelEntity.setQuest100(checkIfCellBlank(row.getCell(124)));

					
		    	 
		    	 System.out.println("----------");
		     //}
		
		     }
		     
		     
		     
		     allresultEntity.add(oneExcelEntity);
		     
			}//excel row for loop
		
		  
		
		 resultRepository.saveAll(allresultEntity);
    	 System.out.println("-----saved in db-----");
    	 /*	}
	
	 * catch(Exception e) { throw new
	 * Exception("Invalid Excel- Cell format needs to be checked"); }
	 */
		
		return null;
		
		
		//return null;
	}
	
	
	public String checkIfCellBlank(XSSFCell cell)
	{
		
		if(cell!=null && cell.toString()!="")
			return cell.getStringCellValue();
		else
			return "";
	}


	@Override
	public List<ResultPdfResponse> getResultParams() {
		
		logger.info("*****ResultServiceImpl getResultParams*****");
		
		List<Map<String,String>> list = resultRepository.getResultParams();
		final ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
		
		List<ResultPdfResponse> resp = new ArrayList<>();
		
		for(int i = 0 ; i < list.size() ; i++) {
			final ResultPdfResponse pojo = mapper.convertValue(list.get(i), ResultPdfResponse.class);
			resp.add(pojo);
		}
		
		return resp;
	}
	
	
	@Override
	public JSONObject getCandidateResultParams(long partnerid , long assessmentid, long partnerAssessmentid , HttpServletRequest request) throws JSONException {
		
		JSONObject map = new JSONObject();
		
		logger.info("*****ResultServiceImpl getCandidateResultParams*****");
		
		PartnerEntity partnerEntity = partnerService.getPartnerById(partnerid);
		
		AssessmentEntity assessmentEntity = assessmentService.getAssessmentEntityById(assessmentid);
		
		PartnerAssessmentMapEntity partnerAssessmentMapEntity =  partnerAssessmentService.getDataByPartnerAssessmentId(partnerAssessmentid,request);
		
		List<CategoryEntity> categoryEntities = categoryRepository.findByAssessmentEntity(assessmentEntity);

		JSONArray jsonArrayColumns = new JSONArray();
		
		JSONObject	jsonObjectColumnsEmailid	= new JSONObject();
		
		jsonObjectColumnsEmailid.put("title","emailid".toUpperCase());
		jsonObjectColumnsEmailid.put("data","emailid");
		
		jsonArrayColumns.put(jsonObjectColumnsEmailid);
		
		JSONObject	jsonObjectColumnsStudentname	= new JSONObject();
		
		jsonObjectColumnsStudentname.put("title","studentname".toUpperCase());
		jsonObjectColumnsStudentname.put("data","studentname");
		
		jsonArrayColumns.put(jsonObjectColumnsStudentname);
		
		for(CategoryEntity CategoryEntity : categoryEntities) {
			
			JSONObject	jsonObjectColumns	= new JSONObject();
			
			jsonObjectColumns.put("title",CategoryEntity.getCategoryName().toUpperCase());
			jsonObjectColumns.put("data",CategoryEntity.getCategoryName());
			
			jsonArrayColumns.put(jsonObjectColumns);
		}
		
		JSONObject	jsonObjectColumnsActions	= new JSONObject();
		
		jsonObjectColumnsActions.put("title","actions".toUpperCase());
		jsonObjectColumnsActions.put("data","actions");
		
		jsonArrayColumns.put(jsonObjectColumnsActions);
		
		List<Map<String,String>> list = resultRepository.getTableResultParams( partnerEntity, assessmentEntity,partnerAssessmentMapEntity );
		final ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
		
		JSONArray jsonArrayData = new JSONArray();
		
		List<AllCandidateResultResponse> resp = new ArrayList<>();
		
		String studentname = "";
		
		JSONObject	jsonObjectData = null;
		
		int j = 0;
		
		for(int i = 0 ; i < list.size() ; i++) {
			final AllCandidateResultResponse pojo = mapper.convertValue(list.get(i), AllCandidateResultResponse.class);
			resp.add(pojo);
			j++;
			
			String marks = "";
			
			if(pojo.getColors().equalsIgnoreCase("red")) {
				marks = "<span class='badge badge-pill badge-danger font-weight-bold'>"+pojo.getMarks()+"</span>" ; 
			}
			if(pojo.getColors().equalsIgnoreCase("green")) {
				marks ="<span class='badge badge-pill badge-success'>"+pojo.getMarks()+"</span>"; 
			}
			if(pojo.getColors().equalsIgnoreCase("amber")) {
				marks ="<span class='badge badge-pill badge-warning'>"+pojo.getMarks()+"</span>"; 
			}
			
			if(!pojo.getStudentname().equalsIgnoreCase(studentname)) {
				
				jsonObjectData	= new JSONObject();
				
				jsonObjectData.put("studentname", pojo.getStudentname());
				jsonObjectData.put("emailid", pojo.getEmailid());
				jsonObjectData.put("actions", "<a href=''type='button'  data-toggle='modal' data-target='#mailModal' class='btn btn-primary btn-sm'  title='Resend Result'>Resend Result</a>");
				jsonObjectData.put(pojo.getCategory_name(), marks);
				
			}else {
				
				jsonObjectData.put(pojo.getCategory_name(), marks);
			}
			
			if(categoryEntities.size() == j) {
				j=0;
				jsonArrayData.put(jsonObjectData);
			}
			
			studentname = pojo.getStudentname();
		}
		
		map.put("columns", jsonArrayColumns);
		map.put("data", jsonArrayData);
		
		return map;
	}
	
	


	@Override
	public List<ExcelDataResponse> validateExcel(String pmapId) {
		// TODO Auto-generated method stub
		
		final ObjectMapper mapper = new ObjectMapper();
		
		List<ExcelDataResponse> respList = new ArrayList<>();

		List<Map<String ,Object>> excelCompareList = resultRepository.validateExcel(pmapId);
		
		System.out.println(excelCompareList.size());
		for(int i=0; i<excelCompareList.size(); i++) {
			final ExcelDataResponse excelDataResponse = mapper.convertValue(excelCompareList.get(i), ExcelDataResponse.class);
	//		logger.info(String.valueOf(i) + "-->"+exce
			ExcelDataResponse oneResp=new ExcelDataResponse();
			
			oneResp.setEmail_id(excelDataResponse.getEmail_id());
			oneResp.setStudent_name(excelDataResponse.getStudent_name());
			oneResp.setTest_name(excelDataResponse.getTest_name());
			oneResp.setTotal_questions(excelDataResponse.getTotal_questions());
			respList.add(oneResp);
			System.out.println(excelDataResponse.getEmail_id());
		}
		return respList;
	}

}
