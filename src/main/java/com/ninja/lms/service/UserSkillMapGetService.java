package com.ninja.lms.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ninja.lms.dto.SkillExpDto;
import com.ninja.lms.dto.UserWithSkillsDisplayDto;
import com.ninja.lms.entity.Skill;
import com.ninja.lms.entity.User;
import com.ninja.lms.entity.UserSkillMap;
import com.ninja.lms.exception.DataNotFoundException;
import com.ninja.lms.repository.SkillRepository;
import com.ninja.lms.repository.UserRepository;

@Service
public class UserSkillMapGetService {
	
	@Resource
	private UserRepository userRepository;
	
	@Resource
	private SkillRepository skillRepository;
	
	public List<UserWithSkillsDisplayDto> fetchAllUserANdSkill() {
		List<UserWithSkillsDisplayDto> userSkillDtoList = new ArrayList<UserWithSkillsDisplayDto>();
		List<User> userList = userRepository.findAll();
		
		if(userList.size() == 0) {
			throw new DataNotFoundException("No User Skill map data available !!");
		}

		userList.stream().forEach(user -> {
			UserWithSkillsDisplayDto userSkillDto = mapEntityToDto(user);
			userSkillDtoList.add(userSkillDto);
		});

		return userSkillDtoList;
	}

	public UserWithSkillsDisplayDto fetchUserAndSkillById(String userId) {
		UserWithSkillsDisplayDto userSkillDto = new UserWithSkillsDisplayDto();

		Optional<User> user = userRepository.findById(userId);
		if (!user.isPresent()) {
			throw new DataNotFoundException("User(id- " + userId + ") Not Found !!");
		} 
		userSkillDto = mapEntityToDto(user.get());
		
		return userSkillDto;
	}

	public List<UserWithSkillsDisplayDto> fetchUserBySkillId(int skillId) {
		List<UserWithSkillsDisplayDto> userSkillDtoList = new ArrayList<UserWithSkillsDisplayDto>();
		
		Optional<Skill> skill = skillRepository.findById(skillId);
		if(!skill.isPresent()) {
			throw new DataNotFoundException("Skill(id- " + skillId + ") Not Found !!");
		}
		
		List<User> userList = userRepository.findUsersBySkillId(skillId);
		if(userList.size() == 0) {
			throw new DataNotFoundException("No User is assigned to this Skill ID-> "+ skillId +" !!");
		}
		
		userList.stream().forEach(user -> {
			UserWithSkillsDisplayDto userSkillDto = mapEntityToDto(user);
			userSkillDtoList.add(userSkillDto);
		});

		return userSkillDtoList;
	}
	
	private UserWithSkillsDisplayDto mapEntityToDto(User user) {
		UserWithSkillsDisplayDto returnDto = new UserWithSkillsDisplayDto();
		Set<SkillExpDto> skillExpDtoList = new HashSet<>();

		returnDto.setId(user.getUserId());
		returnDto.setFirstName(user.getUserFirstName());
		returnDto.setLastName(user.getUserLastName());

		Set<UserSkillMap> userSkillMapSet = user.getUserSkillMapSet();

		if (userSkillMapSet.size() > 0) {
			
			userSkillMapSet.stream().forEach(skill -> {
				SkillExpDto skillExpDto = populateSkillDto(skill);
				skillExpDtoList.add(skillExpDto);
			});

			returnDto.setSkillmap(skillExpDtoList);
		}
		return returnDto;
	}
	
	private SkillExpDto populateSkillDto(UserSkillMap userSkillMap) {
		SkillExpDto returnDto = new SkillExpDto();

		returnDto.setId(userSkillMap.getSkill().getSkillId());

		String skillName = userSkillMap.getSkill().getSkillName();
		returnDto.setSkill(skillName);

		returnDto.setExp(userSkillMap.getMonthsOfExp());

		return returnDto;
	}

}
