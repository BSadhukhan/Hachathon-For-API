package com.ninja.lms.Service;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import com.ninja.lms.dto.SkillExpDto;
import com.ninja.lms.dto.UserWithSkillsDisplayDto;
import com.ninja.lms.entity.Skill;
import com.ninja.lms.entity.User;
import com.ninja.lms.entity.UserSkillMap;
import com.ninja.lms.exception.DataNotFoundException;
import com.ninja.lms.repository.SkillRepository;
import com.ninja.lms.repository.UserRepository;
import com.ninja.lms.repository.UserSkillMapRepository;
import com.ninja.lms.service.UserSkillMapGetService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserSkillMapGetServiceTest.class)
public class UserSkillMapGetServiceTest {
	
	@MockBean
	private UserSkillMapGetService mockService;
	
	@MockBean
	private SkillRepository mockSkillRepo;
	
	@MockBean
	private UserRepository mockUserRepo;
	
	@MockBean
	private UserSkillMapRepository userSkillRepository;
	
	@Test
	public void getAllUsersAndSkillsTest() throws Exception {

		List<User> userList = populateUserWithSkillData();
		
		Mockito.when(mockUserRepo.findAll()).thenReturn(userList);
		assertEquals(userList.size(),mockUserRepo.findAll().size());
		assertEquals(userList.get(1).getUserFirstName(),mockUserRepo.findAll().get(1).getUserFirstName());
	}
	
	@Test
	@WithMockUser(username="APIPROCESSING", password="2xx@Success")
	public void getUserAndSkillByIdTest() throws Exception {
		
		User requestMockUser = populateUserWithSkillData().get(0);
		
		Mockito.when(mockUserRepo.findById(requestMockUser.getUserId())).thenReturn(Optional.of(requestMockUser));
		assertEquals(requestMockUser.getUserId(),mockUserRepo.findById(requestMockUser.getUserId()).get().getUserId());
	}
	
	@Test
	public void getUserBySkillIdTest() throws Exception {
		int skillId = 114;
		
		List<User> userList = new ArrayList<User>();
		userList.add(populateUserWithSkillData().get(0));
		userList.add(populateUserWithSkillData().get(2));
		userList.add(populateUserWithSkillData().get(4));
		
		List<UserWithSkillsDisplayDto> responseDto = populateUserWithSkillsDisplayDto(userList);
		
		Mockito.when(mockUserRepo.findUsersBySkillId(skillId)).thenReturn(userList);
		Mockito.when(mockService.fetchUserBySkillId(skillId)).thenReturn(responseDto);
		assertEquals(userList.size(), mockService.fetchUserBySkillId(skillId).size());
	}
	
	@Test
	public void getUserBySkillIdThrowsExceptionWhenSkillIDNotFoundTest() throws Exception {
		int skillId = 99;
		Mockito.when(this.mockUserRepo.findUsersBySkillId(skillId)).thenThrow(new DataNotFoundException("Skill(id- " + skillId + ") Not Found !!"));
    	
        Assertions.assertThrows(DataNotFoundException.class, () -> this.mockUserRepo.findUsersBySkillId(skillId).size());
    }

	
	private List<User> populateUserWithSkillData() {
		Date utilDate = new Date();
		
		List<User> userList = new ArrayList<User>();
		
		/** Declare User Skill Map object **/
		UserSkillMap userSkill_1, userSkill_2, userSkill_3, userSkill_4, userSkill_5, userSkill_6, userSkill_7, userSkill_8, userSkill_9, userSkill_10;
		
		/** Declare HashSet Of User Skill Map **/
		HashSet<UserSkillMap> mapList_1 = new HashSet<UserSkillMap>();
		HashSet<UserSkillMap> mapList_2 = new HashSet<UserSkillMap>();
		HashSet<UserSkillMap> mapList_3 = new HashSet<UserSkillMap>();
		HashSet<UserSkillMap> mapList_4 = new HashSet<UserSkillMap>();
		HashSet<UserSkillMap> mapList_5 = new HashSet<UserSkillMap>();
		
		/** Skill Sample Data **/
		//int skillId, String skillName, UserSkillMap userSkill, Timestamp creationTime, Timestamp lastModTime		
		Skill mockSkill_1 = new Skill(110, "Java", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		Skill mockSkill_2 = new Skill(111, "SpringBoot", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		Skill mockSkill_3 = new Skill(112, "Jeera", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		Skill mockSkill_4 = new Skill(113, "Selenium", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		Skill mockSkill_5 = new Skill(114, "Postman", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		
		
		/** User Sample Data **/
		/*
		 * String userId, String userFirstName, String userLastName, long userPhoneNumber, String userLocation, String userTimeZone, String userLinkedinUrl, 
		 * String userEduUg, String userEduPg, String userComments, String userVisaStatus, Timestamp creationTime, Timestamp lastModTime, Set<UserSkillMap> userSkillMapSet
		 */ 
		User mockUser_1 = new User("US01", "Baisali", "Sadhukhan", 8553334123L, "Pittsburgh", "EST", "https://www.linkedin.com/in/BaisaliSadukhan", 
				"UG", "PG", "Working in Hachathon TDD", "H4-EAD", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()), mapList_1);
		
		User mockUser_2 = new User("US02", "Shyla", "Aithala", 8762653456L, "Dallas", "CST", "https://www.linkedin.com/in/ShylaAithala", 
				"UG", "PG", "Working in Hachathon TDD", "H4-EAD", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()), mapList_2);
		
		User mockUser_3 = new User("US03", "Mounika", "Mounika", 5467892340L, "Minneapolis", "EST", "https://www.linkedin.com/in/MounikaMounika", 
				"UG", "PG", "Working in Hachathon TDD", "H4-EAD", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()), mapList_3);
		
		User mockUser_4 = new User("US04", "Megha", "Patel", 6754562345L, "Richmond", "EST", "https://www.linkedin.com/in/MeghaPatel", 
				"UG", "PG", "Working in Hachathon TDD-BDD", "H4-EAD", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()), mapList_4);
		
		User mockUser_5 = new User("US05", "Priyanka", "Tripathy", 8761113456L, "New Jersey", "EST", "https://www.linkedin.com/in/PriyankaTripathy", 
				"UG", "PG", "Working in Hachathon TDD-BDD", "H4-EAD", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()), mapList_5);
		
		/** User_SKILL_MAP Sample Data **/
		//String userSkillId, User user, Skill skill, int monthsOfExp, Timestamp creationTime, Timestamp lastModTime
		userSkill_1 = new UserSkillMap("US01", mockUser_1, mockSkill_1, 36, new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		userSkill_2 = new UserSkillMap("US02", mockUser_1, mockSkill_2, 8, new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		userSkill_3 = new UserSkillMap("US03", mockUser_1, mockSkill_5, 8, new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		
		userSkill_4 = new UserSkillMap("US04", mockUser_2, mockSkill_1, 24, new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		userSkill_5 = new UserSkillMap("US05", mockUser_2, mockSkill_2, 8, new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		
		userSkill_6 = new UserSkillMap("US06", mockUser_3, mockSkill_1, 12, new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		userSkill_7 = new UserSkillMap("US07", mockUser_3, mockSkill_5, 8, new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		
		userSkill_8 = new UserSkillMap("US08", mockUser_4, mockSkill_4, 36, new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		
		userSkill_9 = new UserSkillMap("US09", mockUser_5, mockSkill_3, 8, new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		userSkill_10 = new UserSkillMap("US10", mockUser_5, mockSkill_5, 12, new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		
		mapList_1.add(userSkill_1);
		mapList_1.add(userSkill_2);
		mapList_1.add(userSkill_3);
		
		mapList_2.add(userSkill_4);
		mapList_2.add(userSkill_5);
		
		mapList_3.add(userSkill_6);
		mapList_3.add(userSkill_7);
		
		mapList_4.add(userSkill_8);
		
		mapList_5.add(userSkill_9);
		mapList_5.add(userSkill_10);
		
		userList.add(mockUser_1);
		userList.add(mockUser_2);
		userList.add(mockUser_3);
		userList.add(mockUser_4);
		userList.add(mockUser_5);
		
		return userList;
	}
	
	private List<UserWithSkillsDisplayDto> populateUserWithSkillsDisplayDto(List<User> paramUserList){
		List<UserWithSkillsDisplayDto> returnDtoList = new ArrayList<UserWithSkillsDisplayDto>();
		List<User> userList = paramUserList;
		
		userList.stream().forEach(user -> {
			UserWithSkillsDisplayDto userSkillDto = mapEntityToDto(user);
			returnDtoList.add(userSkillDto);
		});
		
		
		return returnDtoList;
	}
	
	private UserWithSkillsDisplayDto mapEntityToDto(User user) {
		UserWithSkillsDisplayDto returnDto = new UserWithSkillsDisplayDto();
		Set<SkillExpDto> skillExpDtoList = new HashSet<>();

		returnDto.setId(user.getUserId());
		returnDto.setFirstName(user.getUserFirstName());
		returnDto.setLastName(user.getUserLastName());

		Set<UserSkillMap> userSkillMapSet = user.getUserSkillMapSet();

		if (userSkillMapSet.size() != 0) {
			// for(UserSkillMap skillMap : userSkills)
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
