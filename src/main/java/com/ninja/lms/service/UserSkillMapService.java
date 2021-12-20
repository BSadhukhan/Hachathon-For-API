package com.ninja.lms.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ninja.lms.dto.UserWithSkillsDisplayDto;
import com.ninja.lms.repository.UserRepository;
import com.ninja.lms.repository.UserSkillMapRepository;
import com.ninja.lms.exception.UserNotFoundException;
import com.ninja.lms.dao.UserSkillMapDAO;
import com.ninja.lms.dto.SkillExpDto;
import com.ninja.lms.dto.UserSkillMapDto;
import com.ninja.lms.entity.UserSkillMap;
import com.ninja.lms.entity.User;

@Service
public class UserSkillMapService {
	
	@Resource
	UserSkillMapRepository userSkillMapRepo;
	
	@Resource
	UserRepository userRepository; 
	
	@Resource
	UserSkillMapDAO mapDao;
	
	public List<UserSkillMapDto> fetchAllUserSkillMapData(){
		
		System.out.println("***************");
		
		List<UserSkillMapDto> returnList = new ArrayList<UserSkillMapDto>();
		List<UserSkillMap> userSkillMapList = userSkillMapRepo.findAll();
		
		System.out.println(userSkillMapList.size());
		
		for(UserSkillMap itr : userSkillMapList) {
			
			UserSkillMapDto mapDto = new UserSkillMapDto();
			System.out.println(itr.getUserSkillId());
			mapDto.setUserSkillId(itr.getUserSkillId());
			mapDto.setUserId(itr.getUser().getUserId());
			mapDto.setSkillId(itr.getSkill().getSkillId());
			mapDto.setMonthsOfExp(itr.getMonthsOfExp());
			
			returnList.add(mapDto);
		}
		
		
		return returnList;
	}
	
	public UserSkillMapDto fetchUserSkillMapDataById(String userSkillId){
		UserSkillMapDto mapDto = new UserSkillMapDto();
		UserSkillMap userSkill = new UserSkillMap();
		Optional<UserSkillMap> optional = userSkillMapRepo.findById(userSkillId);
		
		if(!optional.isPresent()) {
			throw new UserNotFoundException("UserSkillId - "  + userSkillId + " Not Found !!");
		}else {
			userSkill = optional.get();
			mapDto.setUserSkillId(userSkill.getUserSkillId());
			mapDto.setUserId(userSkill.getUser().getUserId());
			mapDto.setSkillId(userSkill.getSkill().getSkillId());
			mapDto.setMonthsOfExp(userSkill.getMonthsOfExp());
		}
		return mapDto;
	}
			
	public UserSkillMapDto createUserSkillMap(UserSkillMapDto userSkillMapDto) {
		System.out.println("***************");
		
		int row = mapDao.insertUserSkillMap(userSkillMapDto);
		
		return userSkillMapDto;
	}
/*	
	public User updateUserSkillMap(UserSkillMapDto userSkillMapDto, String userSkillId) {
		
	}
*/	
	public List<UserWithSkillsDisplayDto> fetchAllUserANdSkill() {
		List<UserWithSkillsDisplayDto> userSkillDtoList  = new ArrayList<UserWithSkillsDisplayDto>();
		List<User> userList = userRepository.findAll();
		
		userList.stream().forEach(user -> {
			UserWithSkillsDisplayDto userSkillDto = mapEntityToDto_UserSkillMap(user);
			userSkillDtoList.add(userSkillDto);
		});
		
		return userSkillDtoList;
	}
	
	public UserWithSkillsDisplayDto fetchUserAndSkillById(String userId) {
		UserWithSkillsDisplayDto userSkillDto = new UserWithSkillsDisplayDto();
		
		Optional<User> user = userRepository.findById(userId);
		if(!user.isPresent()) {
			throw new UserNotFoundException("User(id- "  + userId + ") Not Found !!");
		}else {
			userSkillDto = mapEntityToDto_UserSkillMap(user.get());
		}
		return userSkillDto;
	}
	
	public List<UserWithSkillsDisplayDto> fetchUserBySkillId(int skillId) {
		List<UserWithSkillsDisplayDto> userSkillDtoList  = new ArrayList<UserWithSkillsDisplayDto>();
		List<User> userList = userRepository.findUsersBySkillId(skillId);
		System.out.println("skillId :: "+skillId + " userList.size "+userList.size());
		userList.stream().forEach(user -> {
			UserWithSkillsDisplayDto userSkillDto = mapEntityToDto_UserSkillMap(user);
			userSkillDtoList.add(userSkillDto);
		});
		
		return userSkillDtoList;
	}
	
	private UserWithSkillsDisplayDto mapEntityToDto_UserSkillMap(User user) {
		UserWithSkillsDisplayDto returnDto = new UserWithSkillsDisplayDto();
		Set<SkillExpDto> skillExpDtoList = new HashSet<>();
		
		System.out.println("In mapEntityToDto_User : " + user.getUserId());
		returnDto.setUserId(user.getUserId());
		returnDto.setUserName(user.getUserFirstName() + " " + user.getUserLastName());
		returnDto.setLocation(user.getUserLocation());
		
		Set<UserSkillMap> userSkillMapSet = user.getUserSkillMapSet();
		
		if(userSkillMapSet.size() != 0) {
			//for(UserSkillMap skillMap : userSkills) 
			userSkillMapSet.stream().forEach(skill -> {
				SkillExpDto skillExpDto = populateSkillDto(skill);
				skillExpDtoList.add(skillExpDto);
			});
				
			returnDto.setSkills(skillExpDtoList);
		}
		return returnDto;		
	}
	
	private SkillExpDto populateSkillDto(UserSkillMap userSkillMap) {
		SkillExpDto returnDto = new SkillExpDto();
		
		returnDto.setSkillId(userSkillMap.getSkill().getSkillId());
		
		String skillName = userSkillMap.getSkill().getSkillName();
		returnDto.setSkillName(skillName);
		
		returnDto.setSkillExp(userSkillMap.getMonthsOfExp());
		
		return returnDto;
	}

}
