package com.brainquizapi.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(	name = "assessmentMaster")

public class AssessmentEntity extends CreateUpdate{


	

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name = "testName")
	private String testName;
	
	
	@OneToMany(mappedBy = "assessmentEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CategoryEntity> catogories ;
	
	
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
 
	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public Set<CategoryEntity> getCatogories() {
		return catogories;
	}

	public void setCatogories(Set<CategoryEntity> catogories) {
		this.catogories = catogories;
		
		for(CategoryEntity b : catogories) {
	        b.setAssessmentEntity(this);
	    }
		
	}
	
	
	
		
	
}
