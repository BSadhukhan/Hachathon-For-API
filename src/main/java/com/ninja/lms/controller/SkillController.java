package com.ninja.lms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

	@GetMapping("/skills")
	public List<Skill> getAllSkills() {
		return skillService.getSkills();
	}

	@GetMapping("/skills/{id}")
	public Skill getSkill(@PathVariable int id) {
		Skill s = skillService.getSkill(id);
		return s;
	}
	
	@PostMapping("/skill")
    public Skill addSkill(@RequestBody Skill skill) {
    	return skillService.saveSkill(skill);
    }

	@PutMapping("/skills/{id}")
	public Skill putSkill(@RequestBody Skill skill) {
		Skill sk = skillService.saveSkill(skill);
		return sk;
	}
	/*
	@DeleteMapping("/skills/{id}")
    public String deleteSkill(@PathVariable int id) {
    	return skillService.deleteSkill(id);
    }
    */
}
