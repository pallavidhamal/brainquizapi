package com.brainquizapi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(	name = "userMaster")
@Getter @Setter
public class UserMasterEntity  extends CreateUpdate{
	
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	
	@Column(name = "roleId")
	private Long roleId;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "universityId")
	private Long universityId;
	
	@Size(max = 100)
	@Column(name = "firstName")
	private String firstName;
	
	@Size(max = 100)
	@Column(name = "lastName")
	private String lastName;
	
	
	
	@Size(max = 100)
	@Column(name = "companyName")
	private String companyName;
	
	@Size(max = 100)
	@Column(name = "companyEmailId")
	private String companyEmailId;

	@Size(max = 100)
	@Column(name = "emailId")
	private String emailId;
	
	@Size(max = 100)
	@Column(name = "phoneNo")
	private String phoneNo;
	
	
	@Size(max = 100)
	@Column(name = "contactPersonName")
	private String contactPersonName;
	
	
	@Size(max = 100)
	@Column(name = "contactPersonPhone")
	private String contactPersonPhone;
	

	@Size(max = 100)
	@Column(name = "GSTNo")
	private String GSTNo;
	

	@Size(max = 100)
	@Column(name = "password")
	private String password;
	
	
	@Size(max = 100)
	@Column(name = "emailVerificationStatus")
	private String emailVerificationStatus;
	
	@Size(max = 100)
	@Column(name = "is_active")
	private String isactive;
	
	

	@Size(max = 100)
	@Column(name = "forgotPasswordFlag")
	private String forgotPasswordFlag;
	
	@Column(name = "PAN_number")
	private String panNumber;

	
	
	public UserMasterEntity() {
		
	}
	
	
    public UserMasterEntity(String emailId, String GSTNo,String companyname) {
		
		super();
		this.emailId = emailId;
		this.GSTNo = GSTNo;
	}
	


}
