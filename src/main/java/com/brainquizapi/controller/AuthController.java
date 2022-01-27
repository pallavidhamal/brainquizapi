
package com.brainquizapi.controller;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.brainquizapi.model.RefreshToken;
import com.brainquizapi.model.UserMasterEntity;
import com.brainquizapi.request.LoginRequest;
import com.brainquizapi.request.TokenRefreshRequest;
import com.brainquizapi.request.UserAddRequest;
import com.brainquizapi.response.BaseResponse;
import com.brainquizapi.response.TokenRefreshResponse;
import com.brainquizapi.security.JwtUtils;
import com.brainquizapi.service.AuthService;
import com.brainquizapi.service.RefreshTokenService;
import com.brainquizapi.util.StringsUtils;

/*import com.scube.edu.model.RefreshToken;
import com.scube.edu.model.UserMasterEntity;
import com.scube.edu.request.LoginRequest;
import com.scube.edu.request.TokenRefreshRequest;
import com.scube.edu.request.UserAddRequest;
import com.scube.edu.response.BaseResponse;
import com.scube.edu.response.TokenRefreshResponse;
import com.scube.edu.security.JwtUtils;
import com.scube.edu.service.AuthService;
import com.scube.edu.service.RefreshTokenService;
import com.scube.edu.util.StringsUtils;*/


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	BaseResponse response = null;
	
	@Autowired
	AuthService	authService;
	  
	@Autowired
	RefreshTokenService	refreshTokenService;
	
	
	@PostMapping("/studentSignUp")
	public ResponseEntity<Object> addUser(@RequestBody UserAddRequest userRequest ,  HttpServletRequest request) {
		
		logger.info("********UsersControllers addUser()********");
		
		
		
		response = new BaseResponse();
		
		try {

			Boolean flag = authService.addUser(userRequest);
			
			response.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
			response.setRespData(flag);
			
			return ResponseEntity.ok(response);
			
		}
		catch (Exception e) {
			logger.error(e.getMessage());
			
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
			//return ResponseEntity.badRequest().body(response);
			return ResponseEntity.ok(response);
		}
		
   }
	
	
	
	@PostMapping("/signin")
	public ResponseEntity<Object> authenticateUser(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {

		logger.info("********************AuthController authenticateUser0******************");
		
		logger.info("********************AuthController authenticateUser1******************");
		logger.error("Message logged at ERROR level");
		
		logger.info("********************AuthController authenticateUser2******************");
		logger.warn("Message logged at WARN level");
		
		logger.info("********************AuthController authenticateUser3******************");
		logger.info("Message logged at INFO level");
		
		logger.info("********************AuthController authenticateUser4******************");
		logger.debug("Message logged at DEBUG level");
		
		logger.error("Message logged at ERROR level");
		logger.warn("Message logged at WARN level");
		logger.info("Message logged at INFO level");
		logger.debug("Message logged at DEBUG level");
		
		response = new BaseResponse();
		
		try {
			response = authService.authenticateUser(loginRequest,request);
			
			
			return ResponseEntity.ok(response);
				
		}catch (Exception e) {
			
			logger.error(e.getMessage()); //BAD creds message comes from here
			
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
			return ResponseEntity.ok(response);
			
			
		}
		
	}

	
	@PostMapping("/resetPassword/{email}")
	public  ResponseEntity<Object> resetPasswordBySendingMail(@PathVariable String email) {
		logger.info("********UsersControllers addUser()********");
		
		response = new BaseResponse();
		
		try {
			response = authService.resetPasswordBySendingEMail(email);
			
			return ResponseEntity.ok(response);
				
		}catch (Exception e) {
			
			
			logger.error(e.getMessage()); //BAD creds message comes from here
			
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
			return ResponseEntity.ok(response);
			
		}
		
   }
	
	
	@GetMapping("/checkFlagOnClickOfLink/{encodeEmail}")
	public  ResponseEntity<Object> checkFlagOnClickOfLink(@PathVariable String encodeEmail) {
		
		response = new BaseResponse();
		
		    try {
					response = authService.checkFlagOnClickOfLink(encodeEmail);
					
					return ResponseEntity.ok(response);
						
				}catch (Exception e) {
					
					logger.error(e.getMessage()); //BAD creds message comes from here
					
					response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
					response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
					response.setRespData(e.getMessage());
					
					return ResponseEntity.badRequest().body(response);
					
				}
			
   }
	
	@PostMapping("/UpdatePassword")
	public  ResponseEntity<Object> UpdatePassword(@RequestBody UserMasterEntity userentity) {
		
		response = new BaseResponse();
		
		try {
			
			response = authService.UpdatePassword(userentity);
			
			return ResponseEntity.ok(response);
				
		}catch (Exception e) {
			
			logger.error(e.getMessage()); //BAD creds message comes from here
			
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
			return ResponseEntity.ok(response);
			
		}
		
   }
	
	
	
	@PostMapping("/employerSignUp")
	public ResponseEntity<Object> addEmployerUser(@RequestBody UserAddRequest userRequest ,  HttpServletRequest request) {
		
		logger.info("********UsersControllers addUser()********");

		response = new BaseResponse();
		
		try {

			Boolean flag = authService.addUser(userRequest);
			
			response.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
			response.setRespData(flag);
			
			return ResponseEntity.ok(response);
			
		}
		catch (Exception e) {
			logger.error(e.getMessage());
			
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
			return ResponseEntity.ok(response);
			
		}
		
   }
	

	@GetMapping("/verifyEmail/{emailId}")
	public  ResponseEntity<Object>   verifyStudentEmail(@PathVariable String emailId) {
		
		response = new BaseResponse();
		boolean flag;
		
		
		
		 try {
			 
			flag = authService.verifyEmail(emailId);

			response.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
			response.setRespData(flag);
			
			return ResponseEntity.ok(response);
			
			
		} catch (Exception e) {
           logger.error(e.getMessage());
			
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
			return ResponseEntity.ok(response);
		}
		
			
		 
   }
	
	@PostMapping("/logout/{userId}/{UserRole}")
	public ResponseEntity<Object> UserlogOut (@PathVariable long userId,@PathVariable String UserRole) {

		logger.info("********************AuthController authenticateUser******************");
		
		response = new BaseResponse();
		
		try {
			String res = authService.logout(userId,UserRole);
			logger.info("********************data******************"+userId+ "" +UserRole);
			response.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
			response.setRespData(res);
			logger.info("********************OK******************");
			return ResponseEntity.ok(response);
				
		}catch (Exception e) {
			
			logger.error(e.getMessage()); //BAD creds message comes from here
			
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
			return ResponseEntity.ok(response);
			
			
		}
		
	}
	
	@Autowired
	JwtUtils jwtUtils;
	
	@PostMapping("/refreshtoken")
	  public ResponseEntity<?> refreshtoken( @RequestBody TokenRefreshRequest request) {
	    String requestRefreshToken = request.getRefreshToken();

	     RefreshToken refreshToken = refreshTokenService.findByToken(requestRefreshToken); 
	    
	     refreshTokenService.verifyExpiration(refreshToken);
	    
	     UserMasterEntity	userMasterEntity = new UserMasterEntity();
	     
	     userMasterEntity.setEmailId(refreshToken.getUser().getEmailId());
	    
	    
	    
	    String token = jwtUtils.generateTokenFromUsername(userMasterEntity.getEmailId());
        return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
	    
	    
		/*
		 * return refreshTokenService.findByToken(requestRefreshToken)
		 * .map(refreshTokenService::verifyExpiration) .map(RefreshToken::getUser)
		 * .map(userMasterEntity -> { String token =
		 * jwtUtils.generateTokenFromUsername(userMasterEntity.getUsername()); return
		 * ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken)); })
		 * .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
		 * "Refresh token is not in database!"));
		 */
	  }

}

