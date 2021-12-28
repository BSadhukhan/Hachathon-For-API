package com.ninja.lms.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ninja.lms.dto.UserSkillMapDto;
import com.ninja.lms.service.UserSkillMapService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "User Skill Map API", value = "UserSkillMapAPI")
public class UserSkillMapController {
	
	@Autowired
	UserSkillMapService service;
	
	@GetMapping("/UserSkills")
	@ApiOperation(value = "List all users-skills")
	public ResponseEntity<List<UserSkillMapDto>> getAllUserSkillMapData(){
		
		List<UserSkillMapDto> userSkillMapList = service.fetchAllUserSkillMapData();
		return new ResponseEntity<>(userSkillMapList, HttpStatus.OK);
	}
	
	@GetMapping("/UserSkills/{id}")
	@ApiOperation(value = "List user-skill details by USER_SKILL_ID")
	public ResponseEntity<UserSkillMapDto> getUserSkillMapDataById(@PathVariable("id") String userSkillId){
		
		UserSkillMapDto userSkill = service.fetchUserSkillMapDataById(userSkillId);
		return new ResponseEntity<>(userSkill, HttpStatus.OK);
	}
	
	@PostMapping("/UserSkills")
	@ApiOperation(value = "Create a new user skill mapping")
	public ResponseEntity<UserSkillMapDto> createUserSkillMap(@Valid @RequestBody UserSkillMapDto userSkillMap) throws URISyntaxException{
		
		UserSkillMapDto mapDto = service.createUserSkillMap(userSkillMap);
		return ResponseEntity.created(new URI("/UserSkills/" + mapDto.getUser_skill_id())).body(mapDto);
	}
	
	@PutMapping("/UserSkills/{id}")
	@ApiOperation(value = "Update an existing user skill mapping")
	public ResponseEntity<UserSkillMapDto> updateUserSkillMap(@RequestBody UserSkillMapDto userSkillMap, @PathVariable("id") String userSkillId){
		
		UserSkillMapDto mapDto = service.updateUserSkillMap(userSkillMap, userSkillId);
		return new ResponseEntity<>(mapDto, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/UserSkills/{id}")
	@ApiOperation(value = "Delete an existing user skill mapping")
	public ResponseEntity<Map<String, String>> deleteUserSkillMap(@PathVariable("id") String userSkillId){
		Map<String, String> responseMap = new TreeMap<String, String>(Collections.reverseOrder());
		
		service.deleteUserSkillMap(userSkillId);
		
		String msg = "The record has been deleted !!";
		responseMap.put("user_skill_id", userSkillId);
		responseMap.put("message_response", msg);
		
		return ResponseEntity.status(HttpStatus.OK).body(responseMap);
	}
	
}
