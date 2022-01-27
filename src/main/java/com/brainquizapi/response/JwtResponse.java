package com.brainquizapi.response;

public class JwtResponse {
	private String token;
	private String type = "Bearer";
	private String refreshToken;
	private Long id;
	private String username;
	
	private String email;
	private Long roles;
	private String firstname;
	private String lastname;
	private String phoneNo;


	public JwtResponse(String accessToken,String refreshToken, Long id, String username, String email, Long roles, String firstname, String lastname, String phoneNo) {
		this.token = accessToken;
		this.refreshToken = refreshToken;
		this.id = id;
		this.username = username;
		this.email = email;
		this.roles = roles;
		this.firstname = firstname;
		this.lastname = lastname;
		this.phoneNo = phoneNo;

	}

	public String getAccessToken() {
		return token;
	}

	public void setAccessToken(String accessToken) {
		this.token = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getTokenType() {
		return type;
	}

	public void setTokenType(String tokenType) {
		this.type = tokenType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	
	

	public Long getRoles() {
		return roles;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	
	
	
}
