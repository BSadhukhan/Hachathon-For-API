package com.ninja.lms.service;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ninja.lms.dto.UserDto;
import com.ninja.lms.entity.User;
import com.ninja.lms.exception.DataNotFoundException;
import com.ninja.lms.exception.FieldValidationException;
import com.ninja.lms.repository.UserRepository;

@Service
public class UserService {

	@Resource
	UserRepository userRepo;
	
	final private String SUCCESS_CREATE_MSG = "Successfully Created !!";
	final private String SUCCESS_UPDATE_MSG = "Successfully Updated !!";
	
	/** Service For Fetching all user details **/
	public List<UserDto> getAllUsers() {
		String requestType = "GET";
		List<UserDto> userListDTO = new ArrayList<UserDto>();
		List<User> userList = userRepo.findAll();
		userList.stream().forEach(user -> {
			/** Method calling for transferring user details from User entity to User Dto. UserDto is used for displaying customized JSON output **/
			UserDto usr = mapUserEntityToUserDto(user, requestType);
			userListDTO.add(usr);
		});
		return userListDTO;
	}

	/** Service For Fetching user details by user id **/
	public UserDto getUserWithId(String userId) {
		String requestType = "GET";
		UserDto usrdto = new UserDto();

		Optional<User> user = userRepo.findById(userId);
		if (!user.isPresent()) {
			throw new DataNotFoundException("User(id- " + userId + ") Not Found !!"); 
		} else {
			/** Method calling for transferring user details from User entity to User Dto. UserDto is used for displaying customized JSON output **/
			usrdto = mapUserEntityToUserDto(user.get(), requestType);
		}
		return usrdto;
	}
	
	/** Service method For inserting user details **/
	public UserDto insertUser(UserDto userDto) throws Exception {
		
		/** Checking phone number to prevent duplicate entry **/
		List<User> userList = userRepo.findAll();
		if(userList.size() > 0) {
	    	boolean isPhoneNumberExists = checkDuplicatePhoneNumber(userList, userDto.getPhone_number());
			if(isPhoneNumberExists) {
				throw new FieldValidationException("Failed to create new User details as phone number already exists !!");
			}
		}
		
		User user = new User();
		
		/** Method calling for transferring user details from UserDto to User entity. UserDto is used for getting request payload from JSON input in POST request **/
		user = mapUserDtoToUserEntity(userDto, "POST");
		
		Date utilDate = new Date();
		user.setCreationTime(new Timestamp(utilDate.getTime()));
		user.setLastModTime(new Timestamp(utilDate.getTime()));
		
		/** Method calling for transferring user details from User entity to User Dto. UserDto is used for displaying customized JSON output **/
		UserDto newUserDto = mapUserEntityToUserDto(userRepo.save(user), "POST");

		return newUserDto;
	}

	/** Service method For updating existing user details **/
	public UserDto updateUser(UserDto userDto, String userId) throws Exception {
		
		List<User> userList = userRepo.findAll();
		if(userList.size() == 0) {
			throw new DataNotFoundException("No User data is available !!");
    	}
		
		/** Checking for existing User ID **/
		boolean isUserIdExists = checkForExistingUserId(userList, userId);
		if(!isUserIdExists) {
			throw new DataNotFoundException("User ID-> " + userId + " Not Found !!");
		}
		
		/** Method calling for transferring user details from UserDto to User entity. UserDto is used for getting request payload from JSON input in PUT request **/
		User modifiedUser = mapUserDtoToUserEntity(userDto, "PUT");
		Date utilDate = new Date();
		
		/** Prepare User object with existing data by user id  **/
		User existingUser = new User();
		for(User itr : userList) {
			if(itr.getUserId().equalsIgnoreCase(userId)) {
				existingUser = itr;
			}
		}
		
		/** Checking phone number to prevent duplicate entry **/
		if(existingUser.getUserPhoneNumber() != 0 && (existingUser.getUserPhoneNumber() != userDto.getPhone_number()) ) {
			boolean isPhoneNumberExists = checkDuplicatePhoneNumber(userList, userDto.getPhone_number());
			if(isPhoneNumberExists) {
				throw new FieldValidationException("Failed to update existing User details as phone number already exists !!");
			}
		}
		
		modifiedUser.setUserId(existingUser.getUserId());
		modifiedUser.setCreationTime(existingUser.getCreationTime());
		modifiedUser.setLastModTime(new Timestamp(utilDate.getTime()));
		
		/** Method calling for transferring user details from User entity to User Dto. UserDto is used for displaying customized JSON output **/
		UserDto modifiedUserDto = mapUserEntityToUserDto(userRepo.save(modifiedUser), "PUT");;
		
		return modifiedUserDto;
	}

	/** Service method For deleting existing user details **/
	public void deleteUserById(String userId) {
		
		boolean exists = userRepo.existsById(userId);
		if(!exists)
			throw new DataNotFoundException("User(id- " + userId + ") Not Found !!");
		else
			userRepo.deleteById(userId);

	}
	
	private User mapUserDtoToUserEntity(UserDto userDto, String requestType) throws JsonProcessingException {

		String operation = requestType.equalsIgnoreCase("POST") ? "create new" : "update existing";
		
		User user = new User();
		String[] name; 

		if(!userDto.getName().contains(",")){
			throw new FieldValidationException("Failed to " +  operation + " user, as 'Name' value should be ',' separated !! "); 
			
		}else {
			name = userDto.getName().split(",");
			if (name.length == 2) {
				
				String firstName = name[0];
				String lastName = name[1];
				if (checkFieldContainsNumberOrSpclChar(firstName)) {
					throw new FieldValidationException("Failed to " + operation + " user, as 'Name' contains numbers / special characters !! ");
				} else {
					user.setUserFirstName(firstName.trim());
				}
				if (checkFieldContainsNumberOrSpclChar(lastName)) {
					throw new FieldValidationException("Failed to " + operation + " user, as 'Name' contains numbers / special characters !! ");
				} else {
					user.setUserLastName(lastName.trim());
				}
			}else {
				throw new FieldValidationException("Failed to " + operation + " user, as 'Name' is invalid !! ");
			}

		}
		
		user.setUserPhoneNumber(userDto.getPhone_number());
		
		user.setUserLocation(userDto.getLocation());
		
		if (!isTimeZoneValid(userDto.getTime_zone())) {
			throw new FieldValidationException("Failed to " +  operation + " user, as 'TimeZone' is invalid !! "); 
			
		}else {
			user.setUserTimeZone(userDto.getTime_zone());
		}
		
		if( ( null!=userDto.getLinkedin_url() && !userDto.getLinkedin_url().equals("") ) && ( !userDto.getLinkedin_url().contains("linkedin") && !isValidURL(userDto.getLinkedin_url()) ) ) {
			throw new FieldValidationException("Failed to " +  operation + " user, as 'LinkedinUrl' is not valid !! "); 

		}else {
			user.setUserLinkedinUrl(userDto.getLinkedin_url());
		}
		
		user.setUserEduUg(userDto.getEducation_ug());
		
		user.setUserEduPg(userDto.getEducation_pg());
		
		if (!isVisaStatusValid(userDto.getVisa_status())) {
			throw new FieldValidationException("Failed to " +  operation + " user, as 'VisaStatus' is invalid !! "); 
			
		}else {
			user.setUserVisaStatus(userDto.getVisa_status());
		}
		
		user.setUserComments(userDto.getComments());
		
		return user;
	}

	private UserDto mapUserEntityToUserDto(User user, String requestType) {
		UserDto userDto = new UserDto();

		userDto.setUser_id(user.getUserId());
		userDto.setName(user.getUserFirstName() + "," + user.getUserLastName());
		userDto.setPhone_number(user.getUserPhoneNumber());
		userDto.setLocation(user.getUserLocation());
		userDto.setTime_zone(user.getUserTimeZone());
		userDto.setLinkedin_url(user.getUserLinkedinUrl());
		
		if( !requestType.equals("") && (requestType.equalsIgnoreCase("POST") || requestType.equalsIgnoreCase("PUT")) ){
			String msg = requestType.equalsIgnoreCase("POST") ? SUCCESS_CREATE_MSG : SUCCESS_UPDATE_MSG;
			userDto.setEducation_ug(user.getUserEduUg());
			userDto.setEducation_pg(user.getUserEduPg());
			userDto.setVisa_status(user.getUserVisaStatus());
			userDto.setComments(user.getUserComments());
			userDto.setMessage_response(msg);
		}

		return userDto;
	}
	
	private boolean checkDuplicatePhoneNumber(List<User> userList, long phoneNumber) {
		   boolean isPresent = false;
		   
		   for(User user : userList) {
			   if(user.getUserPhoneNumber() == phoneNumber) {
				   isPresent = true;
				   break;
			   }
		   }
		   return isPresent;
	}
	
	private boolean checkForExistingUserId(List<User> userList, String userId) {
    	boolean isExists = false;
    	
    	for(User user : userList) {
    		if(user.getUserId().equalsIgnoreCase(userId)) {
    			isExists = true;
    			break;
    		}
    	}
    	return isExists;
    }
	
	private boolean checkFieldContainsNumberOrSpclChar(String inputStr) {
    	boolean isNotvalid = false;
    	Pattern pattern = Pattern.compile("[^a-zA-Z]");
        Matcher matcher = pattern.matcher(inputStr);
        isNotvalid = matcher.find();
        
    	return isNotvalid;    	
    }
	
	private boolean isValidURL(String url) {
	    try {
	        new URL(url).toURI();
	    } catch (MalformedURLException | URISyntaxException e) {
	        return false;
	    }

	    return true;
	}
	private boolean isTimeZoneValid(String timeZone) {
		boolean isValid = false;
		List<String> timeZoneList = new ArrayList<String>(List.of("PST", "MST", "CST", "EST", "IST"));
		
		for(String itr : timeZoneList) {
			if(itr.equalsIgnoreCase(timeZone)) {
				isValid = true;
				break;
			}
		}
		return isValid;
	}
	private boolean isVisaStatusValid(String visaStatus) {
		boolean isValid = false;
		List<String> visaStatusList = new ArrayList<String>(List.of("Not-Specified", "NA", "GC-EAD", "H4-EAD", "H4", "H1B", 
				"Canada-EAD", "Indian-Citizen", "US-Citizen", "Canada-Citizen"));
		for(String itr : visaStatusList) {
			if(itr.equalsIgnoreCase(visaStatus)) {
				isValid = true;
				break;
			}
		}
		return isValid;
	}

}
