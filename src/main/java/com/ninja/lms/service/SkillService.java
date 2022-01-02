package com.ninja.lms.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher; 
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ninja.lms.dto.SkillDto;
import com.ninja.lms.entity.Skill;
import com.ninja.lms.exception.DataNotFoundException;
import com.ninja.lms.exception.FieldValidationException;
import com.ninja.lms.repository.SkillRepository;

@Service
public class SkillService {
	
	@Resource
	SkillRepository skillRepo;
	
	final String SUCCESS_CREATE_MSG = "Successfully Created !!";
	final String SUCCESS_UPDATE_MSG = "Successfully Updated !!";
	
	/** Service method For Fetching all skill details **/
    public List<SkillDto> getSkills() throws Exception{
    	
    	List<Skill> skillList = skillRepo.findAll();
    	if(skillList.size() == 0) {
    		throw new DataNotFoundException("No Skills data available !!");
    	}
    	List<SkillDto> skillDtoList = new ArrayList<SkillDto>();
    	
    	for(Skill skill : skillList) {
    		
    		/** Method calling for transferring skill details from skill entity to skill Dto. SkillDto is used for displaying customized JSON output **/
    		SkillDto skillDto = mapEntitySkillToSkillDto(skill, null);
    		skillDtoList.add(skillDto);
    	}
    	return skillDtoList;
    }
	
    /** Service method For Fetching skill details by skill id **/
	public SkillDto getSkill(int skillId){
        
		Optional<Skill> skill = skillRepo.findById(skillId);
        if (skill.isEmpty()) {
        	throw new DataNotFoundException("Skill(id- " + skillId + ") Not Found !!");
        }
        
        /** Method calling for transferring skill details from skill entity to skill Dto. SkillDto is used for displaying customized JSON output **/
        SkillDto skillDto = mapEntitySkillToSkillDto(skill.get(), null);
        return skillDto;
    }
	
	/** Service method creating new skill details **/
    public SkillDto saveSkill(SkillDto skillDto ){
    	String skillName = skillDto.getSkill_name();
    	
		if(checkFieldContainsNumberOrSpclChar(skillName)) {
        	throw new FieldValidationException("Failed to create new Skill details as Skill Name contains special characters !!");
        }
    	
    	
    	List<Skill> skillList = skillRepo.findAll();
    	
    	/** Checking skill name to prevent duplicate entry **/
    	if(skillList.size() > 0) {
    		boolean isSkillPresent = checkDuplicateSkillName(skillList, skillName);
        	if(isSkillPresent) {
        		throw new FieldValidationException("Failed to create new Skill details as Skill already exists !!");
        	}
    	}
    	
    	Skill skill = new Skill();
    	Date utilDate = new Date();
    	skill.setSkillName(skillName);
        skill.setCreationTime(new Timestamp(utilDate.getTime()));
        skill.setLastModTime(new Timestamp(utilDate.getTime()));
        
        Skill newSkill = skillRepo.save(skill);
        
        /** Method calling for transferring skill details from skill entity to skill Dto. SkillDto is used for displaying customized JSON output **/
        SkillDto newSkillDto = mapEntitySkillToSkillDto(newSkill, SUCCESS_CREATE_MSG);
        
        return newSkillDto;
    }
    
    /** Service method updating existing skill details **/
    public SkillDto updateSkill(SkillDto skillDto, int skillId) {
    	
    	if(checkFieldContainsNumberOrSpclChar(skillDto.getSkill_name())) {
        	throw new FieldValidationException("Failed to update existing Skill details as Skill Name contains special characters !!");
        }
    	
    	List<Skill> skillList = skillRepo.findAll();
    	
    	if(skillList.size() == 0) {
    		throw new DataNotFoundException("No Skill data is available !!");
    	}
    	
    	/** Checking skill id exists or not **/
    	boolean isSkillIdExists = checkForExistingSkillId(skillList, skillId);
    	if(!isSkillIdExists) {
    		throw new DataNotFoundException("Skill(id- " + skillId + ") Not Found !!");
    	}
    	
    	/** Prepare Skill object with existing data by skill id  **/
    	Skill existingSkill = new Skill();
    	for(Skill itr : skillList) {
    		if(itr.getSkillId() == skillId) {
    			existingSkill = itr;
    			break;
    		}
    	}
    	if( !existingSkill.getSkillName().equals("") && !existingSkill.getSkillName().equalsIgnoreCase(skillDto.getSkill_name()) ) {
	    	/** Checking skill name to prevent duplicate entry **/
	    	boolean isSkillPresent = checkDuplicateSkillName(skillList, skillDto.getSkill_name());
	    	if(isSkillPresent) {
	    		throw new FieldValidationException("Failed to update existing Skill details as Skill already exists !!");
	    	}
    	}
    	
    	SkillDto newSkillDto = new SkillDto();
    	Date utilDate = new Date();
    	
		/** Set the existing skill object with new data for PUT operation **/
    	existingSkill.setSkillName(skillDto.getSkill_name());
    	existingSkill.setLastModTime(new Timestamp(utilDate.getTime()));
    	
    	/** Method calling for transferring skill details from skill entity to skil Dto. SkillDto is used for displaying customized JSON output **/
    	newSkillDto = mapEntitySkillToSkillDto(skillRepo.save(existingSkill), SUCCESS_UPDATE_MSG);
    	
    	return newSkillDto;
	}
    
    /** Service method deleting existing skill details by skill id **/
    public void deleteSkill(int skillId) {
    	
    	boolean exists = skillRepo.existsById(skillId);
		if(!exists)
			throw new DataNotFoundException("Skill(id- " + skillId + ") Not Found !!");
		else
			skillRepo.deleteById(skillId);
    }
    
    private SkillDto mapEntitySkillToSkillDto(Skill skill, String msg) {
    	SkillDto skillDto = new SkillDto();
    	
    	skillDto.setSkill_id(skill.getSkillId());
    	skillDto.setSkill_name(skill.getSkillName());
    	skillDto.setMessage_response(msg);
    	
    	return skillDto;
    }
    
    private boolean checkFieldContainsNumberOrSpclChar(String inputStr) {
    	boolean isNotvalid = false;
    	Pattern pattern = Pattern.compile("[^a-zA-Z0-9. \b]");
        Matcher matcher = pattern.matcher(inputStr);
        isNotvalid = matcher.find();
        
    	return isNotvalid;    	
    }
    
    private boolean checkForExistingSkillId(List<Skill> skillList, int skillId) {
    	boolean isSkillIdExists = false;
    	
    	for(Skill skill : skillList) {
    		if(skill.getSkillId() == skillId) {
    			isSkillIdExists = true;
    			break;
    		}
    	}
    	return isSkillIdExists;
    }
    
   private boolean checkDuplicateSkillName(List<Skill> skillList, String skillName) {
	   boolean isPresent = false;
	   
	   for(Skill skill : skillList) {
		   if(skill.getSkillName().equalsIgnoreCase(skillName)) {
			   isPresent = true;
			   break;
		   }
	   }
	   return isPresent;
   }

}
