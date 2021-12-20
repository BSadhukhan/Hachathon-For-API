package com.ninja.lms.service;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ninja.lms.entity.Skill;
import com.ninja.lms.repository.SkillRepository;

@Service
public class SkillService {
	
	@Resource
	SkillRepository skillRepo;
	
	//get all skills
    public List<Skill> getSkills(){
    	return skillRepo.findAll();
    }
	
	public Skill getSkill(int id){
        Optional<Skill> s= skillRepo.findById(id);
        if (s.isEmpty()) {
            throw new RuntimeException();
        }
        return s.get();
    }
	
    public Skill saveSkill(Skill skill ){
        Skill s = skillRepo.save(skill);
        return s;
    }

}
