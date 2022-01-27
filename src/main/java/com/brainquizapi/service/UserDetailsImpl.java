package com.brainquizapi.service;

import java.util.Collection;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.brainquizapi.model.UserMasterEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDetailsImpl implements UserDetails {
	private static final long serialVersionUID = 1L;

	private Long id;

	private String username;
	
	private String userid;

	private String email;
	
	private String firstname;
	
	private String lastname;
	
	private String phoneNo;


	@JsonIgnore
	private String password;

	private Long authorities;

	public UserDetailsImpl(Long id, String username, String email, String password,	Long roleid, String firstname, String lastname,String phoneNo) {
		this.id = id;
		
		this.username = username;
		this.email = email;
		this.password = password;
		this.authorities = roleid;
		this.firstname = firstname;
		this.lastname = lastname;
		this.phoneNo = phoneNo;

	}

	public static UserDetailsImpl build(UserMasterEntity user) {
		/*
		 * List<GrantedAuthority> authorities = user.getRoles().stream().map(roles ->
		 * new SimpleGrantedAuthority(roles.getName())).collect(Collectors.toList());
		 * 
		 * return new UserDetailsImpl( user.getId(), user.getUsername(),
		 * user.getEmail(), user.getPassword(), authorities);
		 */
	
		return new UserDetailsImpl(
				user.getId(), 
				user.getUsername(), 
				user.getEmailId(),
				user.getPassword(), 
				user.getRoleId(),
				user.getFirstName(),
				user.getLastName(),user.getPhoneNo());

		
	}

	public Long getRole() {
		return authorities;
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}
	
	public String getUserid() {
		return userid;
	}
	
	public String getFirstname() {
		return firstname;
	}
	
	public String getLastname() {
		return lastname;
	}
	

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserDetailsImpl user = (UserDetailsImpl) o;
		return Objects.equals(id, user.id);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	
}
