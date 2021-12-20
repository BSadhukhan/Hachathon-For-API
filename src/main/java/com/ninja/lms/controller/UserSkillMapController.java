package com.ninja.lms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ninja.lms.dto.UserSkillMapDto;
import com.ninja.lms.dto.UserWithSkillsDisplayDto;
import com.ninja.lms.entity.UserSkillMap;
import com.ninja.lms.service.UserSkillMapService;

@RestController
public class UserSkillMapController {
	
	@Autowired
	UserSkillMapService service;
	
	@GetMapping("/UserSkills")
	public ResponseEntity<List<UserSkillMapDto>> getAllUserSkillMapData(){
		
		List<UserSkillMapDto> userSkillMapList = service.fetchAllUserSkillMapData();
		return new ResponseEntity<>(userSkillMapList, HttpStatus.OK);
	}
	
	@GetMapping("UserSkills/{userSkillId}")
	public ResponseEntity<UserSkillMapDto> getUserSkillMapDataById(@PathVariable("userSkillId") String userSkillId){
		
		UserSkillMapDto userSkill = service.fetchUserSkillMapDataById(userSkillId);
		return new ResponseEntity<>(userSkill, HttpStatus.OK);
	}
		
	@PostMapping("/UserSkills")
	public ResponseEntity<UserSkillMapDto> insertUserSkillMap(@RequestBody UserSkillMapDto userSkillMap){
		System.out.println("userSkillMap " + userSkillMap.getUserSkillId());
		UserSkillMapDto mapDto = service.createUserSkillMap(userSkillMap);
		return new ResponseEntity<>(mapDto, HttpStatus.OK);
	}

	@GetMapping("/UserSkillsMap")
	public ResponseEntity<List<UserWithSkillsDisplayDto>> getAllUsersAndAllSkills(){
		
		List<UserWithSkillsDisplayDto> userSkillDtoList = service.fetchAllUserANdSkill();
		return new ResponseEntity<>(userSkillDtoList, HttpStatus.OK);
	}
	
	@GetMapping("/UserSkillsMap/{userId}")
	public ResponseEntity<UserWithSkillsDisplayDto> getUserAndSkillById(@PathVariable("userId") String userId){
		
		UserWithSkillsDisplayDto userSkillMap = service.fetchUserAndSkillById(userId);
		return new ResponseEntity<>(userSkillMap, HttpStatus.OK);
	}
	
	@GetMapping("/UserSkillBySkill/{skillId}")
	public ResponseEntity<List<UserWithSkillsDisplayDto>> getUserBySkillId(@PathVariable("skillId") int skillId){
		
		List<UserWithSkillsDisplayDto> userSkillDtoList = service.fetchUserBySkillId(skillId);
		return new ResponseEntity<>(userSkillDtoList, HttpStatus.OK);
	}
	

}
