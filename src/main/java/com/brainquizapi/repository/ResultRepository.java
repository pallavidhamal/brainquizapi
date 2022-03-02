package com.brainquizapi.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.brainquizapi.model.AllresultEntity;
import com.brainquizapi.response.ResultPdfResponse;


	
	public interface ResultRepository extends JpaRepository<AllresultEntity, Long> {

		@Query(value="SELECT cm.category_name as categoryName,pmapId,studentid,email_id as emailId,categoryId,sum(score) as score,student_name as studentName "
				+ ",test_name as testName, test_code as testCode, sub_dt as subDate "
				+ "FROM resultcatscore rs, category_master cm where rs.categoryID=cm.id and studentid=27 group by categoryID",nativeQuery = true)
		List<Map<String, String>> getResultParams();
		

		@Query(value="Call validateExcel(?)", nativeQuery = true)
		List<Map<String, Object>> validateExcel(String pmapId);
}
