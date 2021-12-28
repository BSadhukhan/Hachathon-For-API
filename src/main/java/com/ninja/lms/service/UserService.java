package com.ninja.lms.service;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
	
	public List<UserDto> getAllUsers() {
		String requestType = "GET";
		List<UserDto> userListDTO = new ArrayList<UserDto>();
		List<User> userList = userRepo.findAll();
		if(userList.size()==0) {
    		throw new DataNotFoundException("No User data available !!");
		}				  
		userList.stream().forEach(user -> {
			UserDto usr = mapUserEntityToUserDto(user, requestType);
			userListDTO.add(usr);
		});
		return userListDTO;
	}

	public UserDto getUserWithId(String userId) {
		String requestType = "GET";
		UserDto usrdto = new UserDto();

		Optional<User> user = userRepo.findById(userId);
		if (!user.isPresent()) {
			throw new DataNotFoundException("User(id- " + userId + ") Not Found !!");
		} else {
			usrdto = mapUserEntityToUserDto(user.get(), requestType);
		}
		return usrdto;
	}

	public UserDto insertUser(UserDto userDto) throws Exception {
  /*
		List<User> skillList = userRepo.findAll();
    	if(userList.size() == 0) {
    		throw new DataNotFoundException("No Users data available !!");
    	}
    	String userName =userDto.getUser_name();
    	boolean isUserPresent = checkDuplicateUserName(userList, userName);
    	
    	if(isUserPresent) {
    		throw new FieldValidationException("Failed to create new User details as User already exists !!");
    	}
    	*/
		User user = new User();
		user = mapUserDtoToUserEntity(userDto, "POST");
		
		Date utilDate = new Date();
		user.setCreationTime(new Timestamp(utilDate.getTime()));
		user.setLastModTime(new Timestamp(utilDate.getTime()));

		UserDto newUserDto = mapUserEntityToUserDto(userRepo.save(user), "POST");

		return newUserDto;
	}

	public UserDto updateUser(UserDto userDto, String userId) throws Exception {
		/*
		if(null == userDto.getUser_name() || userDto.getUser_name().equals("")) {
        	throw new FieldValidationException("Failed to update existing User details as User Name is set to blank !!");
		
		  }else if(checkFieldContainsNumberOrSpclChar(userDto.getUser_name())) {
	        	throw new FieldValidationException("Failed to update existing User details as User Name contains numbers / special characters !!");
	        }
	   	
		List<User> userList = userRepo.findAll();
    	if(userList.size() == 0) {
    		throw new DataNotFoundException("No Users data available !!");
    	}
    	boolean isUserIdExists = checkForExistingUserId(userList, userId);
    	if(!isUserIdExists) {
    		throw new DataNotFoundException("User(id- " + userId + ") Not Found !!");
    	}
    	boolean isUserPresent = checkDuplicateUserName(userList, userDto.getUser_name());
    	if(isUserPresent) {
    		throw new FieldValidationException("Failed to update existing User details as User already exists !!");
    	}
    	 */
		User modifiedUser = new User();
		modifiedUser = mapUserDtoToUserEntity(userDto, "PUT");
		Date utilDate = new Date();

		Optional<User> optionalUser = userRepo.findById(userId);
		if (!optionalUser.isPresent()) {
			throw new DataNotFoundException("User(id- " + userId + ") Not Found !!");
		} 
		User existingUser = optionalUser.get();
		modifiedUser.setUserId(existingUser.getUserId());
		modifiedUser.setCreationTime(existingUser.getCreationTime());
		modifiedUser.setLastModTime(new Timestamp(utilDate.getTime()));
		UserDto modifiedUserDto = mapUserEntityToUserDto(userRepo.save(modifiedUser), "PUT");;
		
		
		return modifiedUserDto;

	}

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
		if(null == userDto.getName() || userDto.getName().equals("")) {
			throw new FieldValidationException("Failed to " +  operation + " user, as 'Name' should not be blank / null !! "); 
			//errorMap.put("Name", "Blank / Null");
		}else if(!userDto.getName().contains(",")){
			throw new FieldValidationException("Failed to " +  operation + " user, as 'Name' value should be ',' separated !! "); 
			//errorMap.put("Name", "should be ',' separated");
		}else {
			name = userDto.getName().split(",");
			
			user.setUserFirstName(name[0].trim());
			user.setUserLastName(name[1].trim());
		}
		
		if(userDto.getPhone_number()==0) {
			throw new FieldValidationException("Failed to " +  operation + " user, as 'Phone Number' should not be blank !! "); 
			//errorMap.put("Phone Number", "0");
		}else {
			user.setUserPhoneNumber(userDto.getPhone_number());
		}
		
		if(null == userDto.getLocation() || userDto.getLocation().equals("")) {
			throw new FieldValidationException("Failed to " +  operation + " user, as 'Location' should not be blank !! "); 
			//errorMap.put("Location", "blank");
		}else {
			user.setUserLocation(userDto.getLocation());
			
		}
		
		if(null == userDto.getTime_zone() || userDto.getTime_zone().equals("")) {
			throw new FieldValidationException("Failed to " +  operation + " user, as 'TimeZone' should not be blank !! "); 
			
		}else if (!isTimeZoneValid(userDto.getTime_zone())) {
			throw new FieldValidationException("Failed to " +  operation + " user, as 'TimeZone' is invalid !! "); 
			
		}else {
			user.setUserTimeZone(userDto.getTime_zone());
		}
		
		if( ( null!=userDto.getLinkedin_url() && !userDto.getLinkedin_url().equals("") ) && ( userDto.getLinkedin_url().contains("linkedin") && !isValidURL(userDto.getLinkedin_url()) ) ) {
			throw new FieldValidationException("Failed to " +  operation + " user, as 'LinkedinUrl' is not valid !! "); 
			//errorMap.put("LinkedinUrl", "Invalid");
		}else {
			user.setUserLinkedinUrl(userDto.getLinkedin_url());
		}
		user.setUserEduUg(userDto.getEducation_ug());
		user.setUserEduPg(userDto.getEducation_pg());
		
		if(null == userDto.getVisa_status() || userDto.getVisa_status().equals("")) {
			throw new FieldValidationException("Failed to " +  operation + " user, as 'VisaStatus' should not be blank !! "); 
			
		}else if (!isVisaStatusValid(userDto.getVisa_status())) {
			throw new FieldValidationException("Failed to " +  operation + " user, as 'VisaStatus' is invalid !! "); 
			
		}else {
			user.setUserVisaStatus(userDto.getVisa_status());
		}
		
		user.setUserVisaStatus(userDto.getVisa_status());
		
		user.setUserComments(userDto.getComments());
		/*
		if(!errorMap.isEmpty()) {
			errorMap.put("Message", "Failed to "+  operation +" user !!");
			ObjectMapper objectMapper = new ObjectMapper();
	        String jsonErrorString = objectMapper.writeValueAsString(errorMap);
			throw new FieldValidationException(jsonErrorString);
		}
		*/
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
		
		if( !requestType.equalsIgnoreCase("") && (requestType.equalsIgnoreCase("POST") || requestType.equalsIgnoreCase("PUT")) ){
			String msg = requestType.equalsIgnoreCase("POST") ? SUCCESS_CREATE_MSG : SUCCESS_UPDATE_MSG;
			userDto.setEducation_ug(user.getUserEduUg());
			userDto.setEducation_pg(user.getUserEduPg());
			userDto.setVisa_status(user.getUserVisaStatus());
			userDto.setComments(user.getUserComments());
			userDto.setMessage_response(msg);
		}

		return userDto;
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
