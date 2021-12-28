package com.ninja.lms.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ninja.lms.repository.SkillRepository;
import com.ninja.lms.repository.UserRepository;
import com.ninja.lms.repository.UserSkillMapRepository;
import com.ninja.lms.exception.DataNotFoundException;
import com.ninja.lms.dto.UserSkillMapDto;
import com.ninja.lms.entity.UserSkillMap;
import com.ninja.lms.entity.Skill;
import com.ninja.lms.entity.User;

@Service
public class UserSkillMapService {

	@Resource
	private UserSkillMapRepository userSkillMapRepo;

	@Resource
	private UserRepository userRepository;

	@Resource
	private SkillRepository skillRepository;

	private final String SUCCESS_CREATE_MSG = "Successfully Created !!";
	private final String SUCCESS_UPDATE_MSG = "Successfully Updated !!";

	public List<UserSkillMapDto> fetchAllUserSkillMapData() {

		List<UserSkillMapDto> returnList = new ArrayList<UserSkillMapDto>();
		List<UserSkillMap> userSkillMapList = userSkillMapRepo.findAll();

		for (UserSkillMap itr : userSkillMapList) {

			UserSkillMapDto mapDto = populateUserSkillMapDto(itr, null);

			returnList.add(mapDto);
		}

		return returnList;
	}

	public UserSkillMapDto fetchUserSkillMapDataById(String userSkillId) {
		UserSkillMapDto mapDto = new UserSkillMapDto();
		UserSkillMap userSkill = new UserSkillMap();
		Optional<UserSkillMap> optional = userSkillMapRepo.findById(userSkillId);

		if (!optional.isPresent()) {
			throw new DataNotFoundException("UserSkillId - " + userSkillId + " Not Found !!");
		} else {
			userSkill = optional.get();
			mapDto = populateUserSkillMapDto(userSkill, null);
		}
		return mapDto;
	}

	public UserSkillMapDto createUserSkillMap(UserSkillMapDto userSkillMapDto) {
		Date utilDate = new Date();
		UserSkillMap userSkillEntity = new UserSkillMap();

		User user = null;
		String userId = userSkillMapDto.getUser_id();
		Optional<User> optionalUser = userRepository.findById(userId);

		Skill skill = null;
		int skillId = userSkillMapDto.getSkill_id();
		Optional<Skill> optionalSkill = skillRepository.findById(skillId);

		if (!optionalUser.isPresent()) {
			throw new DataNotFoundException("UserId - " + userId + " Not Found !!");
		} else if (!optionalSkill.isPresent()) {
			throw new DataNotFoundException("SkillId - " + skillId + " Not Found !!");
		} else {
			user = optionalUser.get();
			skill = optionalSkill.get();
		}
		
		userSkillEntity.setUser(user);
		userSkillEntity.setSkill(skill);
		userSkillEntity.setMonthsOfExp(userSkillMapDto.getMonths_of_exp());
		userSkillEntity.setCreationTime(new Timestamp(utilDate.getTime()));
		userSkillEntity.setLastModTime(new Timestamp(utilDate.getTime()));

		UserSkillMapDto newUserSkillDto = populateUserSkillMapDto(userSkillMapRepo.save(userSkillEntity), SUCCESS_CREATE_MSG);
		return newUserSkillDto;
	}
	
	public UserSkillMapDto updateUserSkillMap(UserSkillMapDto userSkillMapDto, String userSkillId) {
		Date utilDate = new Date();
		
		Optional<UserSkillMap> optionalMap = userSkillMapRepo.findById(userSkillId);
		if (!optionalMap.isPresent()) {
			throw new DataNotFoundException("UserSkillId - " + userSkillId + " Not Found !!");
		}
		UserSkillMap existingMapEntity = optionalMap.get();
		
		String existingUserId = existingMapEntity.getUser().getUserId();
		String modifiedUserId = userSkillMapDto.getUser_id();
		if ((modifiedUserId != null || modifiedUserId != "") && !existingUserId.equalsIgnoreCase(modifiedUserId)) {
			throw new DataNotFoundException("User_Id-> " + modifiedUserId + " is not mapped with User_Skill_Id-> " + userSkillId + " !!");
		}
		
		int existingSkillId = existingMapEntity.getSkill().getSkillId();
		int modifiedSkillId = userSkillMapDto.getSkill_id();
		if (modifiedSkillId != 0 && existingSkillId != modifiedSkillId) {
			throw new DataNotFoundException("Skill_Id-> " + modifiedSkillId + " is not mapped with User_Skill_Id-> " + userSkillId + " !!");
		}

		existingMapEntity.setMonthsOfExp(userSkillMapDto.getMonths_of_exp());
		existingMapEntity.setLastModTime(new Timestamp(utilDate.getTime()));

		UserSkillMapDto newUserSkillDto = populateUserSkillMapDto(userSkillMapRepo.save(existingMapEntity), SUCCESS_UPDATE_MSG);
		return newUserSkillDto;
	}

	public void deleteUserSkillMap(String userSkillId) {

		boolean exists = userSkillMapRepo.existsById(userSkillId);
		if (!exists) {
			throw new DataNotFoundException("User Skill Id- " + userSkillId + " Not Found !!");
		}
		userSkillMapRepo.deleteById(userSkillId);
	}

	private UserSkillMapDto populateUserSkillMapDto(UserSkillMap newMapEntity, String msg) {
		UserSkillMapDto mapDto = new UserSkillMapDto();

		mapDto.setUser_skill_id(newMapEntity.getUserSkillId());
		mapDto.setUser_id(newMapEntity.getUser().getUserId());
		mapDto.setSkill_id(newMapEntity.getSkill().getSkillId());
		mapDto.setMonths_of_exp(newMapEntity.getMonthsOfExp());
		mapDto.setMessage_response(msg);

		return mapDto;
	}

}
