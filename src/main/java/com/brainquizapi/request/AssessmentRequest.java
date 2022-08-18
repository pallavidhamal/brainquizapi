package com.brainquizapi.request;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.brainquizapi.model.CategoryEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter 
@Setter
@ToString
public class AssessmentRequest {
	
	
	private String  id;
	private String testName;
	private String createBy;
    private Set<CategoryVo> catogories = new HashSet<>();
  //  private List<CategoryVo> catogories = new ArrayList<CategoryVo>;
    
    

}
