package com.brainquizapi.util;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.brainquizapi.model.EmailQueueStatus;
import com.brainquizapi.repository.EmailQueueStatusRepository;
import com.brainquizapi.repository.ResultRepository;
import com.brainquizapi.response.EmailQueueResponse;
import com.brainquizapi.response.ResultPdfResponse;
import com.brainquizapi.service.EmailService;
import com.brainquizapi.service.ResultService;
import com.fasterxml.jackson.databind.ObjectMapper;
//import time;
@Service
public class ResultEmailScheduler {
	
	@Autowired
	ResultRepository resultRepository;
	
	@Autowired 
	ResultService resultService;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	EmailQueueStatusRepository emailQueueStatusRepository;
	
	@Scheduled(cron = "${ResultEmailScheduler.cronTime}")
	public int sendMailFromQueue() throws Exception {
		
		final ObjectMapper mapper = new ObjectMapper();

		String currentStats="listening";
		Long id=(long) 1;
		Optional<EmailQueueStatus> emailQueueStatus=emailQueueStatusRepository.findById(id);
		//System.out.println("here111");

		EmailQueueStatus eStatus=emailQueueStatus.get();
		
		  if(emailQueueStatus!=null) { 
			 currentStats=eStatus.getStatus();
		  //System.out.println("here"+currentStats);
		  }
		 
		
		if(currentStats.equals("listening"))
		{
			int iststus=emailQueueStatusRepository.updateEmailQueueStatus("running");
			//System.out.println("updated");
			
			List<Map<String ,Object>> emailqueueList=resultRepository.getEmailQueue();
			
			for(int i=0; i<emailqueueList.size(); i++) {
				
				final EmailQueueResponse emailQueueResponse = mapper.convertValue(emailqueueList.get(i), EmailQueueResponse.class);
				
				int pmapId=emailQueueResponse.getPmapId();
				int studentid=emailQueueResponse.getStudentid();
				//EmailQueueResponse oneResp=new EmailQueueResponse();
				//emailQueueResponse.get
				List<ResultPdfResponse> result = resultService.getResultParams(pmapId,studentid);
				emailService.exportResult(result,"");
				//time.sleep(1);
				
				
				System.out.println("pmapId="+pmapId+"studentid"+studentid);
				resultRepository.updateEmailQueueCount(pmapId, studentid);
			}
			
			 iststus=emailQueueStatusRepository.updateEmailQueueStatus("listening");

			
		}
		else
		{
			//System.out.println("not updated");

		}
		
		
		
		
		
		return 0;
		
	}
	
	
	@PostConstruct
    public void init() {
        //LOG.info(Arrays.asList(environment.getDefaultProfiles()));
		System.out.println("calling on startup");
		emailQueueStatusRepository.updateEmailQueueStatus("listening");
    }
}
