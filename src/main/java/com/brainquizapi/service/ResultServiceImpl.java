package com.brainquizapi.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
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
import com.brainquizapi.request.PartnerRequest;
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
public class ResultServiceImpl implements ResultService {

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
	public ResponseEntity<BaseResponse> uploadResultFile(MultipartFile resultFile, String pamapId) throws Exception {
		// TODO Auto-generated method stub

		int delRep = resultRepository.DeleteRecordsByPaMapID(pamapId);
		int rejRep = resultRepository.DeleteRejectionRecordsByPaMapID(pamapId);

		logger.info("delRep=" + delRep + "rejRep=" + rejRep);

		String assId = "", ipPartner = "", excelPartner = "";

		List<AllresultEntity> allresultEntity = new ArrayList<AllresultEntity>();

		workbook = new XSSFWorkbook(resultFile.getInputStream());
		XSSFSheet worksheet = workbook.getSheetAt(0);

		int rowcnt = worksheet.getLastRowNum();
		int cellCnt = worksheet.getRow(0).getLastCellNum();
		int noOfColumns = worksheet.getRow(0).getPhysicalNumberOfCells();

		logger.info("Columns=" + noOfColumns + "clomncnt=" + cellCnt + "rowcnt=" + rowcnt);

		String dbAssName = "", excelAssName = "";

		Optional<PartnerAssessmentMapEntity> pamapEntity = pAssessmentRep.findById(Long.parseLong(pamapId));
		PartnerAssessmentMapEntity pmEntity = pamapEntity.get();

		String assmId = "", partId = "";

		if (pmEntity != null) {
			assmId = pmEntity.getAssessment().getId().toString();
			partId = pmEntity.getPartner().getId().toString();
		}

		List<PartnerAssessmentMapEntity> pEntity = pAssessmentRep.findByPartnerAssId(assmId, partId);

		// pAssessmentRep.findById(id)

		int noOfQuestionsDb = 0;
		noOfQuestionsDb = categoryRepository.getQuestionCntByAssId(assmId);

		if (pEntity != null) {
			for (PartnerAssessmentMapEntity entity : pEntity) {
				dbAssName = entity.getPartAssessName();
			}
		} else {
			throw new Exception("Mapping for partner and test doesn't exist");
		}

		System.out.println("dbAssName=" + dbAssName);
		/*
		 * try {
		 */

		for (int i = 1; i <= rowcnt; i++) {

			AllresultEntity oneExcelEntity = new AllresultEntity();
			XSSFRow row = worksheet.getRow(i);

			if (row != null) {
				excelAssName = checkIfCellBlank(row.getCell(4));
				System.out.println(i + "excelAssName=" + excelAssName + "--" + row.getCell(0));

				if (!dbAssName.equals(excelAssName)) {
					throw new Exception("Excel Test name doesnot match with database test name for row" + i);
				}

				int rowcell = row.getPhysicalNumberOfCells();
				logger.info("rowcell=" + rowcell);

				oneExcelEntity.setPartnerId(partId);
				oneExcelEntity.setAssessmentId(assmId);
				oneExcelEntity.setPmapId(pamapId);
				
				oneExcelEntity.setUserId(getFormattedCellValue(row.getCell(0)));
				oneExcelEntity.setPassword(getFormattedCellValue(row.getCell(1)));
				oneExcelEntity.setStudentName(getFormattedCellValue(row.getCell(2)));
				
				// oneExcelEntity.setStudent(String(getFormattedCellValue(row.getCell(2).getNumericCellValue());
				oneExcelEntity.setCourse(getFormattedCellValue(row.getCell(3)));
				oneExcelEntity.setTestName(getFormattedCellValue(row.getCell(4)));
				oneExcelEntity.setRegNo(getFormattedCellValue(row.getCell(5)));
				oneExcelEntity.setInstitute(getFormattedCellValue(row.getCell(6)));
				
				oneExcelEntity.setBatch(getFormattedCellValue(row.getCell(7)));
				oneExcelEntity.setBranch(getFormattedCellValue(row.getCell(8)));
				
				
				if (DateUtil.isCellDateFormatted(row.getCell(9))) {
					
					System.out.println("excel"+row.getCell(9).getDateCellValue());
					
					Date date = row.getCell(9).getDateCellValue();
					
					System.out.println(new SimpleDateFormat("MM-dd-yyyy").format(date));
					System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(date));

					
					System.out.println("tostring"+date.toString());
					
					oneExcelEntity.setSubDt(new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(date));
				} else {
					
					System.out.println("excel else"+row.getCell(9).getDateCellValue());
					oneExcelEntity.setSubDt(getFormattedCellValue(row.getCell(9)));
				}
			
				oneExcelEntity.setActiveTime(getFormattedCellValue(row.getCell(10)));

				String noQuestion=getFormattedCellValue(row.getCell(11));
				
				/*
				 * if(noQuestion=="") { throw new
				 * Exception("NoOfQuestions can not be empty for row" + i); }
				 */
				
				Integer noQ;
				try {
				 noQ =Integer.parseInt(noQuestion);
				}
				catch (NumberFormatException nfe) {
					throw new Exception("NoOfQuestions should be number for row " + i);
			    }

				if (noOfQuestionsDb != noQ) {
					throw new Exception("Excel NoOfQuestions doesnot match with database for row " + i);
				}

				oneExcelEntity.setTotalQuestions(noQuestion);

				oneExcelEntity.setEmailId(getFormattedCellValue(row.getCell(12)));
				oneExcelEntity.setRedirectDomain(getFormattedCellValue(row.getCell(13)));
				oneExcelEntity.setTestCode(getFormattedCellValue(row.getCell(14)));

				oneExcelEntity.setQuest1(getFormattedCellValue(row.getCell(15)));
				oneExcelEntity.setQuest2(getFormattedCellValue(row.getCell(16)));
				oneExcelEntity.setQuest3(getFormattedCellValue(row.getCell(17)));
				oneExcelEntity.setQuest4(getFormattedCellValue(row.getCell(18)));
				oneExcelEntity.setQuest5(getFormattedCellValue(row.getCell(19)));
				oneExcelEntity.setQuest6(getFormattedCellValue(row.getCell(20)));
				oneExcelEntity.setQuest7(getFormattedCellValue(row.getCell(21)));
				oneExcelEntity.setQuest8(getFormattedCellValue(row.getCell(22)));
				oneExcelEntity.setQuest9(getFormattedCellValue(row.getCell(23)));

				oneExcelEntity.setQuest10(getFormattedCellValue(row.getCell(24)));
				oneExcelEntity.setQuest11(getFormattedCellValue(row.getCell(25)));
				oneExcelEntity.setQuest12(getFormattedCellValue(row.getCell(26)));
				oneExcelEntity.setQuest13(getFormattedCellValue(row.getCell(27)));
				oneExcelEntity.setQuest14(getFormattedCellValue(row.getCell(28)));
				oneExcelEntity.setQuest15(getFormattedCellValue(row.getCell(29)));

				oneExcelEntity.setQuest16(getFormattedCellValue(row.getCell(30)));
				oneExcelEntity.setQuest17(getFormattedCellValue(row.getCell(31)));
				oneExcelEntity.setQuest18(getFormattedCellValue(row.getCell(32)));
				oneExcelEntity.setQuest19(getFormattedCellValue(row.getCell(33)));
				oneExcelEntity.setQuest20(getFormattedCellValue(row.getCell(34)));
				oneExcelEntity.setQuest21(getFormattedCellValue(row.getCell(35)));

				oneExcelEntity.setQuest22(getFormattedCellValue(row.getCell(36)));
				oneExcelEntity.setQuest23(getFormattedCellValue(row.getCell(37)));
				oneExcelEntity.setQuest24(getFormattedCellValue(row.getCell(38)));
				oneExcelEntity.setQuest25(getFormattedCellValue(row.getCell(39)));
				oneExcelEntity.setQuest26(getFormattedCellValue(row.getCell(40)));
				oneExcelEntity.setQuest27(getFormattedCellValue(row.getCell(41)));
				oneExcelEntity.setQuest28(getFormattedCellValue(row.getCell(42)));
				oneExcelEntity.setQuest29(getFormattedCellValue(row.getCell(43)));
				oneExcelEntity.setQuest30(getFormattedCellValue(row.getCell(44)));

				oneExcelEntity.setQuest31(getFormattedCellValue(row.getCell(45)));
				oneExcelEntity.setQuest32(getFormattedCellValue(row.getCell(46)));
				oneExcelEntity.setQuest33(getFormattedCellValue(row.getCell(47)));
				oneExcelEntity.setQuest34(getFormattedCellValue(row.getCell(48)));
				oneExcelEntity.setQuest35(getFormattedCellValue(row.getCell(49)));
				oneExcelEntity.setQuest36(getFormattedCellValue(row.getCell(50)));
				oneExcelEntity.setQuest37(getFormattedCellValue(row.getCell(51)));
				oneExcelEntity.setQuest38(getFormattedCellValue(row.getCell(52)));
				oneExcelEntity.setQuest39(getFormattedCellValue(row.getCell(53)));
				oneExcelEntity.setQuest40(getFormattedCellValue(row.getCell(54)));

				oneExcelEntity.setQuest41(getFormattedCellValue(row.getCell(55)));
				oneExcelEntity.setQuest42(getFormattedCellValue(row.getCell(56)));
				oneExcelEntity.setQuest43(getFormattedCellValue(row.getCell(57)));
				oneExcelEntity.setQuest44(getFormattedCellValue(row.getCell(58)));
				oneExcelEntity.setQuest45(getFormattedCellValue(row.getCell(59)));
				oneExcelEntity.setQuest46(getFormattedCellValue(row.getCell(60)));
				oneExcelEntity.setQuest47(getFormattedCellValue(row.getCell(61)));
				oneExcelEntity.setQuest48(getFormattedCellValue(row.getCell(62)));
				oneExcelEntity.setQuest49(getFormattedCellValue(row.getCell(63)));
				oneExcelEntity.setQuest50(getFormattedCellValue(row.getCell(64)));

				oneExcelEntity.setQuest51(getFormattedCellValue(row.getCell(65)));
				oneExcelEntity.setQuest52(getFormattedCellValue(row.getCell(66)));
				oneExcelEntity.setQuest53(getFormattedCellValue(row.getCell(67)));
				oneExcelEntity.setQuest54(getFormattedCellValue(row.getCell(68)));
				oneExcelEntity.setQuest55(getFormattedCellValue(row.getCell(69)));
				oneExcelEntity.setQuest56(getFormattedCellValue(row.getCell(70)));
				oneExcelEntity.setQuest57(getFormattedCellValue(row.getCell(71)));
				oneExcelEntity.setQuest58(getFormattedCellValue(row.getCell(72)));
				oneExcelEntity.setQuest59(getFormattedCellValue(row.getCell(73)));
				oneExcelEntity.setQuest60(getFormattedCellValue(row.getCell(74)));

				oneExcelEntity.setQuest61(getFormattedCellValue(row.getCell(75)));
				oneExcelEntity.setQuest62(getFormattedCellValue(row.getCell(76)));
				oneExcelEntity.setQuest63(getFormattedCellValue(row.getCell(77)));
				oneExcelEntity.setQuest64(getFormattedCellValue(row.getCell(78)));
				oneExcelEntity.setQuest65(getFormattedCellValue(row.getCell(79)));
				oneExcelEntity.setQuest66(getFormattedCellValue(row.getCell(80)));
				oneExcelEntity.setQuest67(getFormattedCellValue(row.getCell(81)));
				oneExcelEntity.setQuest68(getFormattedCellValue(row.getCell(82)));
				oneExcelEntity.setQuest69(getFormattedCellValue(row.getCell(83)));
				oneExcelEntity.setQuest70(getFormattedCellValue(row.getCell(84)));

				oneExcelEntity.setQuest71(getFormattedCellValue(row.getCell(85)));
				oneExcelEntity.setQuest72(getFormattedCellValue(row.getCell(86)));
				oneExcelEntity.setQuest73(getFormattedCellValue(row.getCell(87)));
				oneExcelEntity.setQuest74(getFormattedCellValue(row.getCell(88)));
				oneExcelEntity.setQuest75(getFormattedCellValue(row.getCell(89)));
				oneExcelEntity.setQuest76(getFormattedCellValue(row.getCell(90)));
				oneExcelEntity.setQuest77(getFormattedCellValue(row.getCell(91)));
				oneExcelEntity.setQuest78(getFormattedCellValue(row.getCell(92)));
				oneExcelEntity.setQuest79(getFormattedCellValue(row.getCell(93)));
				oneExcelEntity.setQuest70(getFormattedCellValue(row.getCell(94)));

				oneExcelEntity.setQuest81(getFormattedCellValue(row.getCell(95)));
				oneExcelEntity.setQuest82(getFormattedCellValue(row.getCell(96)));
				oneExcelEntity.setQuest83(getFormattedCellValue(row.getCell(97)));
				oneExcelEntity.setQuest84(getFormattedCellValue(row.getCell(98)));
				oneExcelEntity.setQuest85(getFormattedCellValue(row.getCell(99)));
				oneExcelEntity.setQuest86(getFormattedCellValue(row.getCell(100)));
				oneExcelEntity.setQuest87(getFormattedCellValue(row.getCell(111)));
				oneExcelEntity.setQuest88(getFormattedCellValue(row.getCell(112)));
				oneExcelEntity.setQuest89(getFormattedCellValue(row.getCell(113)));
				oneExcelEntity.setQuest90(getFormattedCellValue(row.getCell(114)));

				oneExcelEntity.setQuest91(getFormattedCellValue(row.getCell(115)));
				oneExcelEntity.setQuest92(getFormattedCellValue(row.getCell(116)));
				oneExcelEntity.setQuest93(getFormattedCellValue(row.getCell(117)));
				oneExcelEntity.setQuest94(getFormattedCellValue(row.getCell(118)));
				oneExcelEntity.setQuest95(getFormattedCellValue(row.getCell(119)));
				oneExcelEntity.setQuest96(getFormattedCellValue(row.getCell(120)));
				oneExcelEntity.setQuest97(getFormattedCellValue(row.getCell(121)));
				oneExcelEntity.setQuest98(getFormattedCellValue(row.getCell(122)));
				oneExcelEntity.setQuest99(getFormattedCellValue(row.getCell(123)));
				oneExcelEntity.setQuest100(getFormattedCellValue(row.getCell(124)));

				System.out.println("----------");
				// }

			}

			allresultEntity.add(oneExcelEntity);

		} // excel row for loop

		resultRepository.saveAll(allresultEntity);
		System.out.println("-----saved in db-----");
		/*
		 * }
		 * 
		 * catch(Exception e) { throw new
		 * Exception("Invalid Excel- Cell format needs to be checked"); }
		 */

		return null;

		// return null;
	}

	public String checkIfCellBlank(XSSFCell cell) {

		if (cell != null && cell.toString() != "")
			return cell.getStringCellValue();
		else
			return "";
	}
	
	
	public String getFormattedCellValue(XSSFCell cell) {

		String cellValue="";
		if (cell != null && cell.toString() != "")
		{
			int cellType = cell.getCellType();
			if (cellType == 1) 
			{
				cellValue=cell.getStringCellValue();
			} 
			else
			{
				Integer intVal = (int) cell.getNumericCellValue();
				cellValue=intVal.toString();
			}
		}
		else
			cellValue="";  //for blank/null cells
		
		
		
		
		return cellValue;
	}
	

	@Override
	public List<ResultPdfResponse> getResultParams(int pmapId, int studentid) {

		logger.info("*****ResultServiceImpl getResultParams*****");

		List<Map<String, String>> list = resultRepository.getResultParams(pmapId, studentid);
		final ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper

		List<ResultPdfResponse> resp = new ArrayList<>();

		for (int i = 0; i < list.size(); i++) {
			final ResultPdfResponse pojo = mapper.convertValue(list.get(i), ResultPdfResponse.class);
			resp.add(pojo);
		}

		return resp;
	}

	@Override
	public Map<String, Object> getCandidateResultParams(long partnerid, long assessmentid, long partnerAssessmentid,
			HttpServletRequest request) throws Exception {

// 		JSONObject map = new JSONObject();
		Map<String, Object> map = new HashMap<String, Object>();

		logger.info("*****ResultServiceImpl getCandidateResultParams*****");

		PartnerEntity partnerEntity = partnerService.getPartnerById(partnerid);

		AssessmentEntity assessmentEntity = assessmentService.getAssessmentEntityById(assessmentid);

		PartnerAssessmentMapEntity partnerAssessmentMapEntity = partnerAssessmentService
				.getDataByPartnerAssessmentId(partnerAssessmentid, request);

		List<CategoryEntity> categoryEntities = categoryRepository.findByAssessmentEntity(assessmentEntity);

//			JSONArray jsonArrayColumns = new JSONArray();
		List<Map<String, String>> jsonArrayColumns = new ArrayList<Map<String, String>>();

//			JSONObject	jsonObjectColumnsEmailid	= new JSONObject();
		Map<String, String> jsonObjectColumnsEmailid = new HashMap<String, String>();

		jsonObjectColumnsEmailid.put("title", "emailid".toUpperCase());
		jsonObjectColumnsEmailid.put("data", "emailid");

		jsonArrayColumns.add(jsonObjectColumnsEmailid);

//			JSONObject	jsonObjectColumnsStudentname	= new JSONObject();

		Map<String, String> jsonObjectColumnsStudentname = new HashMap<String, String>();

		jsonObjectColumnsStudentname.put("title", "studentname".toUpperCase());
		jsonObjectColumnsStudentname.put("data", "studentname");

		jsonArrayColumns.add(jsonObjectColumnsStudentname);

		for (CategoryEntity CategoryEntity : categoryEntities) {

//				JSONObject	jsonObjectColumns	= new JSONObject();

			Map<String, String> jsonObjectColumns = new HashMap<String, String>();

			jsonObjectColumns.put("title", CategoryEntity.getCategoryName().toUpperCase());
			jsonObjectColumns.put("data", CategoryEntity.getCategoryName());

			jsonArrayColumns.add(jsonObjectColumns);
		}

//			JSONObject	jsonObjectColumnsActions	= new JSONObject();

		Map<String, String> jsonObjectColumnsActions = new HashMap<String, String>();

		jsonObjectColumnsActions.put("title", "actions".toUpperCase());
		jsonObjectColumnsActions.put("data", "actions");

		jsonArrayColumns.add(jsonObjectColumnsActions);

		List<Map<String, String>> list = resultRepository.getTableResultParams(partnerEntity, assessmentEntity,
				partnerAssessmentMapEntity);
		final ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper

//		JSONArray jsonArrayData = new JSONArray();
		
		List<Map<String, String>> jsonArrayData = new ArrayList<Map<String, String>>();

		List<AllCandidateResultResponse> resp = new ArrayList<>();

		String studentname = "";

//		JSONObject jsonObjectData = null;

		Map<String, String> jsonObjectData = null; 
		
		int j = 0;

		for (int i = 0; i < list.size(); i++) {
			final AllCandidateResultResponse pojo = mapper.convertValue(list.get(i), AllCandidateResultResponse.class);
			resp.add(pojo);
			j++;

			String marks = "";

			if (pojo.getColors().equalsIgnoreCase("red")) {
				marks = "<span class='badge badge-pill badge-danger font-weight-bold'>" + pojo.getMarks() + "</span>";
			}
			if (pojo.getColors().equalsIgnoreCase("green")) {
				marks = "<span class='badge badge-pill badge-success'>" + pojo.getMarks() + "</span>";
			}
			if (pojo.getColors().equalsIgnoreCase("amber")) {
				marks = "<span class='badge badge-pill badge-warning'>" + pojo.getMarks() + "</span>";
			}

			if (!pojo.getStudentname().equalsIgnoreCase(studentname)) {

				jsonObjectData = new HashMap<String, String>();

				jsonObjectData.put("studentname", pojo.getStudentname());
				jsonObjectData.put("emailid", pojo.getEmailid());
				jsonObjectData.put("actions",
						"<a href=''type='button'  data-toggle='modal' data-target='#mailModal' class='btn btn-primary btn-sm resendResult'  title='Resend Result' id="+pojo.getId()+"  pmapid="+pojo.getPmapid()+" >Resend Result</a>");
				jsonObjectData.put(pojo.getCategory_name(), marks);

			} else {

				jsonObjectData.put(pojo.getCategory_name(), marks);
			}

			if (categoryEntities.size() == j) {
				j = 0;
				jsonArrayData.add(jsonObjectData);
			}

			studentname = pojo.getStudentname();
		}
		
		String jsonColumns = new ObjectMapper().writeValueAsString(jsonArrayColumns);
		String jsonData = new ObjectMapper().writeValueAsString(jsonArrayData);
		
		System.out.println("================jsonColumns=================="+jsonColumns);
		
		map.put("columns",jsonArrayColumns);
		map.put("data", jsonArrayData);

		return map;
	}

	@Override
	public List<ExcelDataResponse> validateExcel(String pmapId) {
		// TODO Auto-generated method stub

		final ObjectMapper mapper = new ObjectMapper();

		List<ExcelDataResponse> respList = new ArrayList<>();

		List<Map<String, Object>> excelCompareList = resultRepository.validateExcel(pmapId);

		System.out.println(excelCompareList.size());
		for (int i = 0; i < excelCompareList.size(); i++) {
			final ExcelDataResponse excelDataResponse = mapper.convertValue(excelCompareList.get(i),
					ExcelDataResponse.class);
			// logger.info(String.valueOf(i) + "-->"+exce
			ExcelDataResponse oneResp = new ExcelDataResponse();

			oneResp.setEmail_id(excelDataResponse.getEmail_id());
			oneResp.setStudent_name(excelDataResponse.getStudent_name());
			oneResp.setTest_name(excelDataResponse.getTest_name());
			oneResp.setTotal_questions(excelDataResponse.getTotal_questions());
			respList.add(oneResp);
			System.out.println(excelDataResponse.getEmail_id());
		}
		return respList;
	}

	@Override
	public boolean ResultCalculation(String pmapId) {
		// TODO Auto-generated method stub
		boolean flag;

		flag = resultRepository.AllResultColToRow(pmapId);

		return flag;
	}

	@Override
	public boolean resendAllToEmailQueue(String pmapId,String assId,String partId) {
		// TODO Auto-generated method stub
		boolean flag;
		
		flag=resultRepository.insertInToEmailQueue(pmapId,assId,partId);
		return flag;
	}

}
