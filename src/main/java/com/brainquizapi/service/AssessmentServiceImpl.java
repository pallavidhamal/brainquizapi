package com.brainquizapi.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.brainquizapi.model.AnswersEntity;
import com.brainquizapi.model.AssessmentEntity;
import com.brainquizapi.model.CategoryEntity;
import com.brainquizapi.model.CreateUpdate;
import com.brainquizapi.model.PartnerAssessmentMapEntity;
import com.brainquizapi.model.PartnerEntity;
import com.brainquizapi.model.QuestionEntity;
import com.brainquizapi.repository.AssessmentRepository;
import com.brainquizapi.repository.CategoryRepository;
import com.brainquizapi.repository.PartnerAssessmentMapRepository;
import com.brainquizapi.repository.PartnerRepository;
import com.brainquizapi.request.AnswersVo;
import com.brainquizapi.request.AssessmentRequest;
import com.brainquizapi.request.CategoryVo;
import com.brainquizapi.request.PartnerAssessmentRequest;
import com.brainquizapi.request.PartnerRequest;
import com.brainquizapi.request.QuestionsVo;
import com.brainquizapi.response.AssessmentResponse;

@Service
public class AssessmentServiceImpl implements AssessmentService {

	
	private static final Logger logger = LoggerFactory.getLogger(PartnerServiceImpl.class);
	
	@Autowired
	PartnerService partnerService;

	 @Autowired
	 AssessmentRepository assessmentRepository;
	 
	 @Autowired
	 CategoryRepository categoryRepository;
	 
	 @Autowired
	 PartnerAssessmentMapRepository partnerAssessmentMapRepository;
	
	@Override
	public String addAssessment(AssessmentRequest assessmentRequest) throws Exception {
		
		
		String resp = null;
		
		AssessmentEntity assessmentEntity=new AssessmentEntity();
		
		assessmentEntity.setTestName(assessmentRequest.getTestName());
		assessmentEntity.setCreateby(assessmentRequest.getCreateBy());
		assessmentEntity.setIsdeleted("N");
		
		assessmentRequest.toString();
		
		System.out.println("assessment request input");		
		System.out.println("name"+assessmentRequest.getTestName());
		System.out.println(assessmentRequest.getCatogories().size());
		
		Set<CategoryVo> CatSet = new HashSet<>();
		CatSet=assessmentRequest.getCatogories();		 
	    Set<CategoryEntity> catogories= new HashSet<>(); //to set for save
	    
	    
	    
	    
		for (CategoryVo CatVo : CatSet)
		{
			 System.out.println(" cat name" + CatVo.getCategoryName());
			 
			 CategoryEntity Ce=new CategoryEntity();
			 Ce.setCategoryName(CatVo.getCategoryName());
			 Ce.setCategorySeq(CatVo.getCategorySeq());
			 Ce.setNoOfQuestions(CatVo.getNoOfQuestions());
			 Ce.setRedFrom(CatVo.getRedFrom());
			 Ce.setRedTo(CatVo.getRedTo());
			 Ce.setGreenFrom(CatVo.getGreenFrom());
			 Ce.setGreenTo(CatVo.getGreenTo());
			 Ce.setAmberFrom(CatVo.getAmberFrom());
			 Ce.setAmberTo(CatVo.getAmberTo());
			 
			 Ce.setCreateby(assessmentRequest.getCreateBy());
			 Ce.setIsdeleted("N");

			 
			 
			 Set<QuestionsVo> QuestionSet = new HashSet<>();
			 QuestionSet=CatVo.getQuestions();
			 Set<QuestionEntity> questions= new HashSet<>(); //to set for save
			 			 
			 
			 for (QuestionsVo QuestVo : QuestionSet)
			 {
				 QuestionEntity Qe=new QuestionEntity();
				 Qe.setQuestionNo(QuestVo.getQuestionNo());
				 Qe.setQuestionName(QuestVo.getQuestionName());
				 Qe.setCreateby(assessmentRequest.getCreateBy());
				 Qe.setIsdeleted("N");
				 
				 
				 Set<AnswersVo> answerSet = new HashSet<>();
				 answerSet=QuestVo.getAnswers();
				 Set<AnswersEntity> answers= new HashSet<>(); //to set for save
				 
				 for (AnswersVo ansVo : answerSet)
				 {
					 AnswersEntity Ae=new AnswersEntity();
					 Ae.setAnswerOption(ansVo.getOptionno());
					 Ae.setAnswerName(ansVo.getAnswername());
					 Ae.setAnswerScore(ansVo.getScore());
					 
					 Ae.setCreateby(assessmentRequest.getCreateBy());
					 Ae.setIsdeleted("N");		
					 
					 
					 answers.add(Ae);
				 }
				 
				 
				 
				 Qe.setAnswers(answers);
				 
				 
				 
				 questions.add(Qe);
				 
				 
			
			 }
			 Ce.setQuestions(questions);
			 
			 
			 catogories.add(Ce);
			 
		}		 
		
		
		assessmentEntity.setCatogories(catogories);
		
		
		
		assessmentRepository.save(assessmentEntity);
		 
		resp="success";
		 
		return resp;
		 
		
		
	}

	@Override
	public AssessmentResponse getAssessment(AssessmentRequest assessmentRequest) {
		
		
		String assId=assessmentRequest.getId();
		AssessmentResponse Ar=new AssessmentResponse();
		
		if(assId!=null)
		{
			 Optional<AssessmentEntity> assesEntity=assessmentRepository.findById(Long.parseLong(assId));
			 AssessmentEntity assEntity = assesEntity.get();

			
			Ar.setTestName(assEntity.getTestName());
			
		}
		else
		{
			//record does not exist
		}
		
		
		// TODO Auto-generated method stub
		return Ar; 
	}

	@Override
	public List<AssessmentResponse> getAllAssessments(AssessmentRequest assessmentRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String updateAssessmnent(@RequestBody AssessmentRequest assessmentRequest) {
		// TODO Auto-generated method stub
		
		String resp = null;	
		boolean flg;
		
		Optional<AssessmentEntity> assessmentEntity = assessmentRepository.findById(Long.parseLong(assessmentRequest.getId()));
		     
		AssessmentEntity editEntity = assessmentEntity.get();
		
		editEntity.setTestName(assessmentRequest.getTestName());	   
		
		Set<CategoryVo> CatSet = new HashSet<>();
		CatSet=assessmentRequest.getCatogories();		 
	  
		Set<CategoryEntity> catogories= editEntity.getCatogories();
	    
		
	    
	    
		for (CategoryVo CatVo : CatSet)
		{
			// CategoryEntity Ce=new CategoryEntity();
		     Optional<CategoryEntity> Centity = categoryRepository.findById(Long.parseLong(CatVo.getId()));
		     CategoryEntity Ce = Centity.get();

			 Ce.setCategoryName(CatVo.getCategoryName());
			 Ce.setCategorySeq(CatVo.getCategorySeq());
			 catogories.add(Ce);
		}
		
		
		
		editEntity.setCatogories(catogories);
			   
		assessmentRepository.save(editEntity);
	    resp = "Success";
	   
		
		
		
		return resp;
	}

	@Override
	public String addPartnerAssessment(PartnerAssessmentRequest partAssessRequest) {
		
		logger.info("*****AssessmentServiceImpl addPartnerAssessment*****");
		
		PartnerAssessmentMapEntity ent = new PartnerAssessmentMapEntity();
		
		PartnerEntity partEnt = partnerService.getPartnerById(Long.valueOf(partAssessRequest.getPartnerid()));
		Optional<AssessmentEntity> assEntt = assessmentRepository.findById(Long.parseLong(partAssessRequest.getAssessmentid()));
		AssessmentEntity assEnt = assEntt.get();
		
		ent.setAssessment(assEnt);
		ent.setPartner(partEnt);
		ent.setAltEmailId(partAssessRequest.getAltemailid());
		ent.setFromDate(partAssessRequest.getFromdate());
		ent.setToDate(partAssessRequest.getTodate());
		ent.setPartAssessName(partAssessRequest.getPartassessmentname());
		
		partnerAssessmentMapRepository.save(ent);
		
		return "success";
	}
	
}
