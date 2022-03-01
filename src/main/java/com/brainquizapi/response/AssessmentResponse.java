package com.brainquizapi.response;

import java.util.HashSet;
import java.util.Set;

import com.brainquizapi.request.CategoryVo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter 
@Setter
@ToString
public class AssessmentResponse {

		private String  id;
		private String testName;
		private String createBy;
		private String createDate;
	    private Set<CategoryVo> catogories = new HashSet<>();

}
