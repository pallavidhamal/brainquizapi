package com.brainquizapi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(	name = "partner_assessment_map")
@Getter @Setter
public class PartnerAssessmentMapEntity extends CreateUpdate{
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@OneToOne
	@JoinColumn(name = "fk_partner")
	private PartnerEntity partner; 
	
	@OneToOne
	@JoinColumn(name = "fk_assessment")
	private AssessmentEntity assessment; 
	
	@Column(name = "from_date")
	private String fromDate;
	
	@Column(name = "to_date")
	private String toDate;
	
	@Column(name = "alt_email")
	private String altEmailId;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "partner_assessment_name")
	private String partAssessName;

}
