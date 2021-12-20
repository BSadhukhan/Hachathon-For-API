package com.ninja.lms.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ninja.lms.entity.Skill;
import com.ninja.lms.exception.UserNotFoundException;
import com.ninja.lms.repository.SkillRepository;

@Service
public class SkillService {
	
	@Resource
	SkillRepository skillRepo;
	
	//get all skills
    public List<Skill> getSkills(){
    	return skillRepo.findAll();
    }
	
	public Skill getSkill(int skillId){
        Optional<Skill> skill = skillRepo.findById(skillId);
        if (skill.isEmpty()) {
        	throw new UserNotFoundException("Skill(id- " + skillId + ") Not Found !!");
        }
        return skill.get();
    }
	
    public Skill saveSkill(Skill skill ){
        Date utilDate = new Date();
        skill.setCreationTime(new Timestamp(utilDate.getTime()));
        skill.setLastModTime(new Timestamp(utilDate.getTime()));
        
        Skill newSkill = skillRepo.save(skill);
        
        return newSkill;
    }
    
    public Skill updateSkill(Skill skill, int skillId) {
    	Date utilDate = new Date();
		
    	Skill existingSkill = skillRepo.findById(skillId).orElse(null);
    	if(null == existingSkill) {
    		throw new UserNotFoundException("Skill(id- " + skillId + ") Not Found !!");
    	}else {
    		existingSkill.setSkillName(skill.getSkillName());
    		existingSkill.setLastModTime(new Timestamp(utilDate.getTime()));
    	}
		
		return skillRepo.save(existingSkill);
	}
    
    public void deleteSkill(int skillId) {
    	
    	boolean exists = skillRepo.existsById(skillId);
		if(!exists)
			throw new UserNotFoundException("Skill(id- " + skillId + ") Not Found !!");
		else
			skillRepo.deleteById(skillId);
    }

}
