package com.brainquizapi.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.DateUtil;
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
import com.brainquizapi.repository.ResultRepository;
import com.brainquizapi.response.BaseResponse;

@Service
public class ResultServiceImpl implements ResultService{

	
	private static final Logger logger = LoggerFactory.getLogger(PartnerController.class);
	
	private XSSFWorkbook workbook;

	@Autowired
	ResultRepository resultRepository;
	

	BaseResponse response = null;
	
	
	@Override
	public ResponseEntity<BaseResponse> uploadResultFile(MultipartFile resultFile)  throws IOException {
		// TODO Auto-generated method stub
		
		 List<AllresultEntity> allresultEntity = new ArrayList<AllresultEntity>();

		 workbook = new XSSFWorkbook(resultFile.getInputStream());
		 XSSFSheet worksheet = workbook.getSheetAt(0);
		 
		int rowcnt=worksheet.getLastRowNum();
		int cellCnt=worksheet.getRow(0).getLastCellNum() ;
		int noOfColumns = worksheet.getRow(0).getPhysicalNumberOfCells();

		logger.info("Columns="+noOfColumns+"clomncnt="+cellCnt);

		
		for(int i=1;i<rowcnt;i++) {
			
			
			 AllresultEntity oneExcelEntity=new AllresultEntity();
			 XSSFRow row = worksheet.getRow(i);		 
		   
			 if(row!=null) 
		     {
		    	 int rowcell= row.getPhysicalNumberOfCells();
					logger.info("rowcell="+rowcell);
		     
					//if(row.getCell(0)!=null)
					//{

					 
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
				  
				  
				  if (DateUtil.isCellDateFormatted(row.getCell(9))) {
                     // System.out.println("dtt"+row.getCell(9).getDateCellValue());
                      
                      Date date=row.getCell(9).getDateCellValue();
                      oneExcelEntity.setSubDt(date.toString());
                      
                      
                  } else {
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
				  oneExcelEntity.setQuest10(row.getCell(24).getStringCellValue());
				 

					
		    	 
		    	 System.out.println("----------");
		     //}
		
		     }
		     
		     
		     
		     allresultEntity.add(oneExcelEntity);
		     
		     
		     
		     
			}
		
		 resultRepository.saveAll(allresultEntity);
    	 System.out.println("-----saved in db-----");

		
		return null;
		
		
		//return null;
	}
	
	
	

}
