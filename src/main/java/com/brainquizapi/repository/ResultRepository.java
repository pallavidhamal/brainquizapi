package com.brainquizapi.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.brainquizapi.model.AllresultEntity;
import com.brainquizapi.model.AssessmentEntity;
import com.brainquizapi.model.PartnerAssessmentMapEntity;
import com.brainquizapi.model.PartnerEntity;
import com.brainquizapi.response.ResultPdfResponse;
import org.springframework.stereotype.Repository;


	@Repository
	public interface ResultRepository extends JpaRepository<AllresultEntity, Long> {

	
	/*
	 * @Query(
	 * value="SELECT cm.category_name as categoryName,pmapId,studentid,email_id as emailId,categoryId,sum(score) as score,student_name as studentName "
	 * + ",test_name as testName, test_code as testCode, sub_dt as subDate " +
	 * "FROM resultcatscore rs, category_master cm where rs.categoryID=cm.id and pmapId=? and studentid=? group by categoryID"
	 * ,nativeQuery = true) List<Map<String, String>> getResultParams(int pmapId,int
	 * studentid);
	 */
	 
		
		
	
	  @Query(
	  value="SELECT cm.category_name as categoryName,pmapId,studentid,rs.email_id as emailId,categoryId,sum(score) as score,student_name as studentName "
	  + ",test_name as testName, test_code as testCode, sub_dt as subDate ,pm.logo_path as logoPath,pm.url as url "
	  +", case when sum(Ifnull(score,0)) BETWEEN cm.red_from AND cm.red_to then 'red'  "
	  +" when sum(Ifnull(score,0)) BETWEEN cm.green_from AND cm.green_to then 'green' "
	  +"  when sum(Ifnull(score,0)) BETWEEN cm.amber_from AND cm.amber_to then 'amber' end as colors "
	  +" FROM resultcatscore rs, category_master cm , partner_master pm where rs.categoryID=cm.id and rs.partnerId=pm.id and pmapId=? and studentid=? group by categoryID"
	  ,nativeQuery = true) List<Map<String, String>> getResultParams(int pmapId,int  studentid);
	 
		

		@Query(value="Call validateExcel(?)", nativeQuery = true)
		List<Map<String, Object>> validateExcel(String pmapId);
		
		
		@Query(value=" select finVal.studentid as id,finVal.pmapid,Ifnull ( finVal.student_name ,'') as studentname ,Ifnull ( finVal.email_id ,'') as emailid ,Ifnull ( finVal.marks ,'') as marks, "
				+ " Ifnull ( finVal.categoryID ,'') as categoryID,Ifnull ( finVal.category_name ,'') as  category_name ,Ifnull ( finVal.colors ,'') as colors "
				+ " from ( SELECT  aa.studentid as studentid,aa.pmapid,aa.student_name , aa.email_id , sum(Ifnull(score,0)) as marks , aa.categoryID  , cmst.id ,cmst.category_name , "
				+ " case when sum(Ifnull(score,0)) BETWEEN cmst.red_from AND cmst.red_to then 'red'  "
				+ "		 when sum(Ifnull(score,0)) BETWEEN cmst.green_from AND cmst.green_to then 'green' "
				+ "      when sum(Ifnull(score,0)) BETWEEN cmst.amber_from AND cmst.amber_to then 'amber' end as colors FROM resultcatscore as aa  "
				+ " left join category_master as cmst on aa.categoryID = cmst.id and cmst.assessment_id = aa.assessmentId "
				+ " where  partnerId = ?1 and assessmentId  = ?2 and pmapId  = ?3 group by studentid , categoryID ) as finVal  order by student_name , id",nativeQuery = true)
		List<Map<String, String>> getTableResultParams(PartnerEntity partnerEntity,AssessmentEntity assessmentEntity,PartnerAssessmentMapEntity partnerAssessmentMapEntity );
		
		
		
		
		
		@Query(value="Call AllResultColToRow(?)", nativeQuery = true)
		boolean AllResultColToRow(String pmapId);
		
		@Modifying(clearAutomatically = true)
		@Transactional
		@Query(value="delete FROM allresult_master where pmap_id=? ", nativeQuery = true)
		int DeleteRecordsByPaMapID(String pmapId);
		
		@Modifying(clearAutomatically = true)
		@Transactional
		@Query(value="delete FROM allresult_master_rejections where pmap_id=? ", nativeQuery = true)
		int DeleteRejectionRecordsByPaMapID(String pmapId);
		
		@Query(value="SELECT id,studentid, email_id, partnerId,  assessmentId, pmapId,emailstatus,emailcount FROM emailqueue where emailcount is null" ,nativeQuery = true)
		List<Map<String, Object>> getEmailQueue();
		
		@Modifying(clearAutomatically = true)
		@Transactional
		@Query(value = "UPDATE emailqueue set emailcount=IFNULL(emailcount, 0) + 1 where pmapId=? and studentid=? ", nativeQuery = true)
		Integer updateEmailQueueCount(int pmapId,int studentid);
		
		
	/*
	 * @Transactional
	 * 
	 * @Query(value =
	 * " insert into  EmailQueue(studentid,email_id,partnerId,assessmentId,pmapId)  SELECT studentid,email_id,partnerId,assessmentId,pmapid FROM brainquiz.resultcatscore where pmapid=? group by studentid "
	 * , nativeQuery = true) Integer insertInToEmailQueue(String pmapId);
	 */
		
		
		
		@Query(value="Call InsertResndEmailQueue(?,?,?)", nativeQuery = true)
		boolean insertInToEmailQueue(String pmapId,String assId,String partId);
		
		@Query(value="Call UpdateMailSentMappings()", nativeQuery = true)
		boolean UpdateMailSentMappings();
		
		
}
