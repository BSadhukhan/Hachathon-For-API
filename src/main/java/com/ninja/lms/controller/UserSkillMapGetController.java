package com.ninja.lms.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.ninja.lms.dto.UserWithSkillsDisplayDto;
import com.ninja.lms.exception.FieldValidationException;
import com.ninja.lms.service.UserSkillMapGetService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "User Skill Map Get API", value = "UserSkillMapGetAPI")
public class UserSkillMapGetController {

	@Autowired
	UserSkillMapGetService service;

	@GetMapping("/UserSkillsMap")
	@ApiOperation(value = "List all users with all skill details")
	public ResponseEntity<Map<String, Object>> getAllUsersAndAllSkills() {
		
		Map<String, List<UserWithSkillsDisplayDto>> responseMap = new HashMap<>();

		List<UserWithSkillsDisplayDto> userSkillDtoList = service.fetchAllUserANdSkill();
		responseMap.put("users", userSkillDtoList);
		
		return new ResponseEntity(responseMap, HttpStatus.OK);
	}

	@GetMapping("/UserSkillsMap/{userId}")
	@ApiOperation(value = "List user with the skill details by USER_ID")
	public ResponseEntity<Map<String, Object>> getUserAndSkillByUserId(@PathVariable("userId") String userId) {
		
		Map<String, UserWithSkillsDisplayDto> responseMap = new HashMap<>();
		
		UserWithSkillsDisplayDto displayDto = service.fetchUserAndSkillById(userId);
		
		responseMap.put("users", displayDto);
		
		return new ResponseEntity(responseMap, HttpStatus.OK);
	}

	@GetMapping("/UserSkillBySkill/{skillId}")
	@ApiOperation(value = "List all users details by SKILL_ID")
	public ResponseEntity<Map<String, Object>> getUserBySkillId(@PathVariable("skillId") String paramSkillId) {
		
		Map<String, List<UserWithSkillsDisplayDto>> responseMap = new HashMap<>();
		int skillId = 0;
		
		if(null == paramSkillId || paramSkillId.equals("")) {
			throw new FieldValidationException("Skill ID - " + paramSkillId + " is invalid !!");
		} else {
			try {
				skillId = Integer.parseInt(paramSkillId);
				
			}catch(Exception ex) {
				throw new FieldValidationException("Skill ID - " + paramSkillId + " is invalid !!");
			}
		}

		List<UserWithSkillsDisplayDto> userSkillDtoList = service.fetchUserBySkillId(skillId);
		responseMap.put("users", userSkillDtoList);
				
		return new ResponseEntity(responseMap, HttpStatus.OK);
	}
	
	// endpoint -> /UserSkillsMap?userid= or /UserSkillsMap?skillid=
/*	
	  @GetMapping("/UserSkillsMap")
	  @ApiOperation(value ="List user / users with the skills details by USER_ID / SKILL_ID") 
	  public ResponseEntity<Map<String, UserWithSkillsDisplayDto>> getUserAndSkillById(@RequestParam Map<String,String> paramMap){
		  
		  Map<String, Object> responseMap = new LinkedHashMap<>();
		  
		  if(!paramMap.isEmpty() && paramMap.containsKey("userid")) {
			  
			  UserWithSkillsDisplayDto userSkillMap = service.fetchUserAndSkillById(paramMap.get("userid")); 
			  responseMap.put("users", userSkillMap);
		  } 
		  
		  if(!paramMap.isEmpty() && paramMap.containsKey("skillid")) {
			  int skillId = 0;
			  String paramVal = paramMap.get("skillid");
				if(null == paramVal || paramVal.equals("")) {
					throw new FieldValidationException("Skill ID - " + paramVal + " is invalid !!");
				} else {
					try {
						skillId = Integer.parseInt(paramVal);
						
					}catch(Exception ex) {
						throw new FieldValidationException("Skill ID - " + paramVal + " is invalid !!");
					}
				}
				
				List<UserWithSkillsDisplayDto> userSkillDtoList = service.fetchUserBySkillId(skillId);
				responseMap.put("users", userSkillDtoList);
		  }
		  
		  return new ResponseEntity(responseMap, HttpStatus.OK);	  
	  }
*/	 

}
