
package com.brainquizapi.service;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Date;
import java.util.HashMap;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Service;

import com.brainquizapi.model.RefreshToken;
import com.brainquizapi.model.UserMasterEntity;
import com.brainquizapi.repository.UserRepository;
import com.brainquizapi.request.LoginRequest;
import com.brainquizapi.request.UserAddRequest;
import com.brainquizapi.response.BaseResponse;
import com.brainquizapi.response.JwtResponse;
import com.brainquizapi.security.JwtUtils;
import com.brainquizapi.util.StringsUtils;

 
@Service
public class AuthServiceImpl implements AuthService{
	
	private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
	
	Base64.Encoder baseEncoder = Base64.getEncoder();
      
      
	 BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	 
	 @Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	JwtUtils jwtUtils;
	
//	@Autowired
	//VerifierService verifierService;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	RefreshTokenService refreshTokenService;
	
	BaseResponse  baseResponse = null;
    Base64.Decoder decoder = Base64.getDecoder();  
    
	 @Value("${file.url-dir}")
     private String url;
	 

	@Override
	public BaseResponse authenticateUser(LoginRequest loginRequest, HttpServletRequest request) throws Exception {
	  	
        baseResponse	= new BaseResponse();
		
		logger.info("loginRequest.email "+ loginRequest.getUsername());

		
		  if(loginRequest.getUsername().equalsIgnoreCase("")) { 
			  throw new Exception("Error: Login Id cannot be empty!"); 
		  }
		  if(loginRequest.getPassword().equalsIgnoreCase("")) { 
			  throw new Exception("Error: Password cannot be empty!"); 
		  }
		  
		 
		// logger.info("---------checking values-----------  "+ userRepository.existsByUsernameAndIsactive(loginRequest.getUsername() ,"Y"));

		
		// if(!userRepository.existsByUsernameAndIsactive(loginRequest.getUsername(),"Y"))
	    if(!userRepository.existsByEmailIdAndIsactive(loginRequest.getUsername(),"Y"))
		 {
			 
			 //  msg = "User is not active";
			 throw new Exception("User unauthorized!");
		}
	    
		// UserMasterEntity masterEntity =  userRepository.findByUsername(loginRequest.getUsername());

		UserMasterEntity masterEntity =  userRepository.findByEmailIdAndIsdeleted(loginRequest.getUsername(),"N");
		
	//	System.out.println("===masterEntity======="+masterEntity.getEmailVerificationStatus());
		 
		 if(!masterEntity.getEmailVerificationStatus().equalsIgnoreCase("Verified"))
		 {
			 
			//  msg = "Email is nor verified";
			 throw new Exception("Email is not Verified!");
		}
		 
	
  
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
	
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();	
	
		RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
		
	
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(new JwtResponse(jwt,refreshToken.getToken(), 
				 userDetails.getId(), 
				 userDetails.getUsername(), 
				 userDetails.getEmail(),
				 userDetails.getRole(), 
				 userDetails.getFirstname(),
				 userDetails.getLastname(),
				 userDetails.getPhoneNo()
				 ));
		logger.info("USER INFo"+baseResponse);
		return baseResponse;
       
	
	}

	
	@Override
	public Boolean addUser(UserAddRequest userAddRequest) throws Exception {
		
		
		 UserMasterEntity userMasterEntity  =new  UserMasterEntity();
		 
		 System.out.println("------------"+userMasterEntity.getEmailId());
		 
		  UserMasterEntity userEntities  = userRepository.findByEmailIdAndIsdeleted(userAddRequest.getEmailId(),"N");
		 
		
			if(userEntities != null) {
				
				throw new Exception("This email id already exists");
			}
		
			userMasterEntity.setPanNumber(userAddRequest.getPanNumber());
			userMasterEntity.setCompanyName(userAddRequest.getCompanyName());
//			userMasterEntity.setCompanyEmailId(userAddRequest.getEmailId());
			userMasterEntity.setGSTNo(userAddRequest.getGstNo());
//			userMasterEntity.setContactPersonName(userAddRequest.getContactPersonName());
//			userMasterEntity.setContactPersonPhone(userAddRequest.getContactPersonPhoneNo());
			userMasterEntity.setFirstName(userAddRequest.getFirstName());
			userMasterEntity.setLastName(userAddRequest.getLastName());
			userMasterEntity.setEmailId(userAddRequest.getEmailId());
			userMasterEntity.setPhoneNo(userAddRequest.getPhoneNumber());
			userMasterEntity.setPassword(encoder.encode(userAddRequest.getPassword()));
			userMasterEntity.setEmailVerificationStatus("N");
			userMasterEntity.setUniversityId(userAddRequest.getUniversityId());
			userMasterEntity.setUsername(userAddRequest.getEmailId());
			userMasterEntity.setRoleId(userAddRequest.getRoleId());
			userMasterEntity.setIsactive("Y");
			userMasterEntity.setIsdeleted("N");
		 
	
	     userRepository.save(userMasterEntity);
	
	//	 emailService.sendVerificationEmail(userAddRequest.getEmailId(), url);
			
		return true;
		
	}
	



	@Override
	public BaseResponse resetPasswordBySendingEMail(String email) throws Exception {
		 
		baseResponse	= new BaseResponse();
			
		logger.info("----------resetPasswordBySendingEMail---------");
		
		 UserMasterEntity userEntities  = userRepository.findByUsernameAndIsdeleted(email, "N");
		 
			if(userEntities == null) {
				throw new Exception("User Unauthorised");
			}
		
			 UserMasterEntity entities =  userRepository.getOne(userEntities.getId());
			 entities.setForgotPasswordFlag("Y");
			
			 
			 userRepository.save(entities);
			 
			 String encodeEmail = baseEncoder.encodeToString(email.getBytes(StandardCharsets.UTF_8)) ;
			 
			 logger.info("---------encodeEmail-------"+encodeEmail);
			
			//Send Email 
		//	emailService.sendEmail(email,encodeEmail, url);
			
			baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
			baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
			baseResponse.setRespData("success");
			 
			return baseResponse;
	}


	@Override
	public BaseResponse UpdatePassword(UserMasterEntity reqUserentity) throws Exception {
		
		baseResponse	= new BaseResponse();
		
		 logger.info("---------Email-------"+reqUserentity.getUsername());   
		 logger.info("---------password-------"+reqUserentity.getPassword());
		 
		 String encodeEmailId = reqUserentity.getUsername();
		 
		  String dStr = new String(decoder.decode(encodeEmailId));  


		 UserMasterEntity userEntities  = userRepository.findByUsername(dStr);
		 
		   if(userEntities == null || encodeEmailId== null) {
			   
				throw new Exception(" Invalid Email");
			}
		
		   
		   //Here checking password flag
		
		   if(userEntities.getForgotPasswordFlag().equalsIgnoreCase("Y")) {
	        
			     UserMasterEntity entities =  userRepository.getOne(userEntities.getId());
				 entities.setPassword(encoder.encode(reqUserentity.getPassword()));
				 entities.setForgotPasswordFlag("N");
				 
				 userRepository.save(entities);
		   }else {
			   
			    throw new Exception("Not a valid User");
		   }
		 
		 
		
		
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData("success");
		 
		return baseResponse;
	}


	@Override
	public BaseResponse checkFlagOnClickOfLink(String encodeEmail) throws Exception {
		
        baseResponse	= new BaseResponse();
        String flag = "";
		
       // Base64.Decoder decoder = Base64.getDecoder();  
        // Decoding string  
        String dStr = new String(decoder.decode(encodeEmail));  
       
        UserMasterEntity userEntities  = userRepository.findByUsername(dStr);
        
        if(userEntities == null) {
        	
			throw new Exception("Error: User Not Found");
		}
        System.out.println("-----------------ForgotPasswordFlag-------------------"+userEntities.getForgotPasswordFlag());
        
        if(userEntities.getForgotPasswordFlag().equalsIgnoreCase("Y")) {
        	
        	flag ="success";
        	
        }else {
        	
        	throw new Exception("Forgot password Flag is not checked");
        	//flag = "failure";
        }
        
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(flag);
		 
		return baseResponse;
	}
	
	
	 public static String encode(String input) {

	      return Base64.getEncoder().encodeToString(input.getBytes());

	  }

	  //Decode:
	  public static String decode(String input) {

	      byte[] decodedBytes = Base64.getDecoder().decode(input);
	      return new String(decodedBytes);

	  }


	@Override
	public boolean verifyEmail(String emailId) throws Exception {
		
	 Base64.Decoder decoder = Base64.getDecoder();  
	        // Decoding string  
	  String decodedEmail = new String(decoder.decode(emailId)); 
	        
	       // logger.info("---------decodedEmail-------"+decodedEmail);
	        
	       UserMasterEntity userEntities  = userRepository.findByUsernameAndIsdeleted(decodedEmail,"N");
	       
	       
		   if(userEntities == null) {
				throw new Exception("Invalid Email");
			}
		   
		   
		 int hours = userRepository.gethoursToValidateTheLink(userEntities.getId());
		  logger.info("---------hours-------"+hours);
		  
		  if(hours < 1440 ) {
			   if(userEntities.getEmailVerificationStatus().equalsIgnoreCase("N"))
			   {
			  
			     UserMasterEntity entities =  userRepository.getOne(userEntities.getId());
				 entities.setEmailVerificationStatus("Verified");
				 userRepository.save(entities);
			   }
			   else {
				   throw new Exception("Email is verified already.");
			   }
			  
		  }else {
			  
			  throw new Exception("Error: Link is Expired!!!");
			  
		  }

		
		return true;
	}


	@Override
	public String logout(long userid,String UserRole) throws Exception {
		
		logger.info("----------authController logout---------");
		
		int rowcnt=0;
		String resp = null;
		UserMasterEntity entities=userRepository.findById(userid);
		if(entities!=null)
		{
		
		if(entities.getRoleId()==3)
		{
			//rowcnt=verifierService.updateListonLogout(userid);
			resp="Succesfully updated " +rowcnt+ " records";
			
		}
		
		}
		return resp;

	
	
}
}
