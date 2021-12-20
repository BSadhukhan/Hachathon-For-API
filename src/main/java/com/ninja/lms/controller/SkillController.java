package com.ninja.lms.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

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

import com.ninja.lms.entity.Skill;
import com.ninja.lms.service.SkillService;

@RestController
public class SkillController {

	@Autowired
	SkillService skillService;

	@GetMapping("/Skills")
	public ResponseEntity<List<Skill>> getAllSkills() {
		return new ResponseEntity<>(skillService.getSkills(),HttpStatus.OK);
	}

	@GetMapping("/Skills/{id}")
	public ResponseEntity<Skill> getSkill(@PathVariable int id) {
		Skill skill = skillService.getSkill(id);
		return new ResponseEntity<>(skill, HttpStatus.OK);
	}
	
	@PostMapping("/Skills")
    public ResponseEntity<Skill> CreateSkill(@RequestBody Skill skill) throws URISyntaxException{
    	Skill newSkill = skillService.saveSkill(skill);
    	return ResponseEntity.created(new URI("/Skills/" + newSkill.getSkillId())).body(newSkill);
    }

	@PutMapping("/Skills/{id}")
	public ResponseEntity<Skill> updateSkill(@RequestBody Skill skill, @PathVariable("id") int skillId) {
		Skill newSkill = skillService.updateSkill(skill, skillId);
		return new ResponseEntity<>(newSkill, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/Skills/{id}")
    public ResponseEntity<String> deleteSkill(@PathVariable int id) {
    	skillService.deleteSkill(id);
    	String msg = "Skill - " + id + " has been deleted !!";
		return ResponseEntity.status(HttpStatus.OK).body(msg);
    }
   
}
