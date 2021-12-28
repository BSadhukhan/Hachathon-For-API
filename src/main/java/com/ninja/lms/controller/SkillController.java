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

import com.ninja.lms.dto.SkillDto;
import com.ninja.lms.exception.FieldValidationException;
import com.ninja.lms.service.SkillService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "Skills API", value = "SkillsAPI")
public class SkillController {

	@Autowired
	SkillService skillService;

	@GetMapping("/Skills")
	@ApiOperation(value = "List all skills")
	public ResponseEntity<List<SkillDto>> getAllSkills() throws Exception {
		return new ResponseEntity<>(skillService.getSkills(),HttpStatus.OK);
	}

	@GetMapping("/Skills/{id}")
	@ApiOperation(value = "List skill by SKILL_ID")
	public ResponseEntity<SkillDto> getSkillById(@PathVariable("id") String paramSkillId) {
		
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
		SkillDto skillDto = skillService.getSkill(skillId);
		return new ResponseEntity<>(skillDto, HttpStatus.OK);
	}
	
	@PostMapping("/Skills")
	@ApiOperation(value = "Create a new skill")
    public ResponseEntity<SkillDto> createSkill(@Valid @RequestBody SkillDto skilldto) throws URISyntaxException{
		
		SkillDto newSkillDto = skillService.saveSkill(skilldto);
    	return ResponseEntity.created(new URI("/Skills/" + newSkillDto.getSkill_id())).body(newSkillDto);
    }

	@PutMapping("/Skills/{id}")
	@ApiOperation(value = "Update an existing skill")
	public ResponseEntity<SkillDto> updateSkill(@RequestBody SkillDto skilldto, @PathVariable("id") String paramSkillId) {
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
		SkillDto newSkillDto = skillService.updateSkill(skilldto, skillId);
		return new ResponseEntity<>(newSkillDto, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/Skills/{id}")
	@ApiOperation(value = "Delete an existing skill")
    public ResponseEntity<Map<String, String>> deleteSkill(@PathVariable("id") String paramSkillId) {
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
		
		Map<String, String> responseMap = new TreeMap<String, String>(Collections.reverseOrder());
		
    	skillService.deleteSkill(skillId);
    	
    	String msg = "The record has been deleted !!";
		responseMap.put("Skill_Id", paramSkillId);
		responseMap.put("message_response", msg);
		
    	return ResponseEntity.status(HttpStatus.OK).body(responseMap);
    }
   
}
