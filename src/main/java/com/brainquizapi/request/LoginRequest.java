package com.brainquizapi.request;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class LoginRequest {
	
	//@NotBlank(message = "Username cannot be empty.")
	private String username;

	//@NotBlank(message = "Password Cannot be empty.")
	private String password;

}
