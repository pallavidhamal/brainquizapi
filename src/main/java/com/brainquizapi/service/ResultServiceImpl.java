package com.brainquizapi.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.brainquizapi.controller.PartnerController;
import com.brainquizapi.model.AllresultEntity;
import com.brainquizapi.model.PartnerAssessmentMapEntity;
import com.brainquizapi.model.PartnerEntity;
import com.brainquizapi.repository.CategoryRepository;
import com.brainquizapi.repository.PartnerAssessmentMapRepository;
import com.brainquizapi.repository.ResultRepository;
import com.brainquizapi.request.PartnerAssessmentRequest;
import com.brainquizapi.response.BaseResponse;
import com.brainquizapi.response.PartnerResponse;

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

	BaseResponse response = null;
	
	
	@Override
	public ResponseEntity<BaseResponse> uploadResultFile(MultipartFile resultFile,String partnerId ,String assessmentId,String pamapId)  throws Exception {
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
		List<PartnerAssessmentMapEntity> pEntity    = pAssessmentRep.findByPartnerAssId(assessmentId, partnerId);
		int noOfQuestionsDb=0;
		noOfQuestionsDb=categoryRepository.getQuestionCntByAssId(assessmentId);
		
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
		     
				oneExcelEntity.setPartnerId(partnerId);
				oneExcelEntity.setAssessmentId(assessmentId); 
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

}
