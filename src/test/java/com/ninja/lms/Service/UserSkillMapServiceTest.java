package com.ninja.lms.Service;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.doNothing;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import javax.validation.constraints.AssertFalse;

import com.ninja.lms.dto.UserSkillMapDto;
import com.ninja.lms.entity.Skill;
import com.ninja.lms.entity.User;
import com.ninja.lms.entity.UserSkillMap;
import com.ninja.lms.exception.DataNotFoundException;
import com.ninja.lms.repository.SkillRepository;
import com.ninja.lms.repository.UserRepository;
import com.ninja.lms.repository.UserSkillMapRepository;
import com.ninja.lms.service.UserSkillMapService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserSkillMapService.class)
public class UserSkillMapServiceTest {

	@MockBean
	private UserSkillMapRepository userSkillMapRepo;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private SkillRepository skillRepository;

	@Autowired
	private UserSkillMapService usrSkillMapService;

	@Test
	public void fetchAllUserSkillMapDataTest() throws Exception {
		List<UserSkillMap> userSkillMapList = populateUserSkillMapData();
		Mockito.when(userSkillMapRepo.findAll()).thenReturn(userSkillMapList);
		assertEquals(userSkillMapList.size(), this.usrSkillMapService.fetchAllUserSkillMapData().size());
	}

	@Test
	public void fetchUserSkillMapDataByUserIdTest() throws Exception {
		UserSkillMap mockUserSkillMap_1 = populateUserSkillMapData().get(1);
		Mockito.when(userSkillMapRepo.findById(mockUserSkillMap_1.getUserSkillId())).thenReturn(Optional.of(mockUserSkillMap_1));
		assertEquals(mockUserSkillMap_1.getUserSkillId(), this.usrSkillMapService.fetchUserSkillMapDataById(mockUserSkillMap_1.getUserSkillId()).getUser_skill_id());
	}

	@Test
	public void createUserSkillMapTest() throws Exception {
		Date utilDate = new Date();
		User requestMockUser = new User("US03", "Mounika", "Mounika", 5467892340L, "Minneapolis", "EST", "https://www.linkedin.com/in/MounikaMounika", "UG", "PG", 
				"Working in Hachathon TDD", "H4-EAD", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));

		Skill requestMockSkill = new Skill(113, "Selenium", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));

		UserSkillMap requestMockUserSkillMap = new UserSkillMap("US08", requestMockUser, requestMockSkill, 36, new Timestamp(utilDate.getTime()), 
				new Timestamp(utilDate.getTime()));

		Mockito.when(userSkillMapRepo.save(requestMockUserSkillMap)).thenReturn(requestMockUserSkillMap);
		assertEquals(requestMockUserSkillMap.getSkill().getSkillId(), userSkillMapRepo.save(requestMockUserSkillMap).getSkill().getSkillId());
	}
	
	@Test
	void updateUserSkillMapThrowsDataNotFoundExceptionTest() throws Exception {
		String userSkillId="US99";
		
		UserSkillMap requestMockUserSkillMap = populateUserSkillMapData().get(1);
		UserSkillMapDto responseUserSkillMapDto = populateUserSkillMapDto(requestMockUserSkillMap);
		
		Mockito.when(this.userSkillMapRepo.findById(userSkillId)).thenThrow(new DataNotFoundException("User Skill Id- " + userSkillId + " Not Found !!"));

		Assertions.assertThrows(DataNotFoundException.class, () -> this.usrSkillMapService.updateUserSkillMap(responseUserSkillMapDto, userSkillId));

	}
	

	@Test
	void updateUserSkillMapTest() throws Exception {
		Date utilDate = new Date();
		UserSkillMap requestMockUserSkillMap = populateUserSkillMapData().get(0);
		requestMockUserSkillMap.setMonthsOfExp(24);
		requestMockUserSkillMap.setLastModTime(new Timestamp(utilDate.getTime()));

		Mockito.when(userSkillMapRepo.save(requestMockUserSkillMap)).thenReturn(requestMockUserSkillMap);
		assertEquals(requestMockUserSkillMap.getMonthsOfExp(), userSkillMapRepo.save(requestMockUserSkillMap).getMonthsOfExp());

	}
	
	@Test
	void deleteUserSkillThrowsDataNotFoundExceptionTest() {
    	String userSkillId="US99";
		Mockito.when(this.userSkillMapRepo.findById(userSkillId)).thenThrow(new DataNotFoundException("User Skill Id- " + userSkillId + " Not Found !!"));

		Assertions.assertThrows(DataNotFoundException.class, () -> this.usrSkillMapService.deleteUserSkillMap(userSkillId));
	}
	
	@Test
	void deleteUserSkillTest() {
		UserSkillMap requestMockUserSkillMap = populateUserSkillMapData().get(0);
		Optional optional = userSkillMapRepo.findById(requestMockUserSkillMap.getUserSkillId());
		
		doNothing().when(this.userSkillMapRepo).deleteById(requestMockUserSkillMap.getUserSkillId());
		assertEquals(Optional.empty(), optional);
		
	}

	private List<UserSkillMap> populateUserSkillMapData() {
		List<UserSkillMap> mapList = new ArrayList<UserSkillMap>();
		Date utilDate = new Date();

		UserSkillMap userSkill_1, userSkill_2, userSkill_3, userSkill_4, userSkill_5;

		/** User Sample Data **/
		/*
		 * String userId, String userFirstName, String userLastName, long
		 * userPhoneNumber, String userLocation, String userTimeZone, String
		 * userLinkedinUrl, String userEduUg, String userEduPg, String userComments,
		 * String userVisaStatus, Timestamp creationTime, Timestamp lastModTime,
		 */
		User mockUser_1 = new User("U01", "Baisali", "Sadhukhan", 8553334123L, "Pittsburgh", "EST",
				"https://www.linkedin.com/in/BaisaliSadukhan", "UG", "PG", "Working in Hachathon TDD", "H4-EAD",
				new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));

		User mockUser_2 = new User("U02", "Shyla", "Aithala", 8762653456L, "Dallas", "CST",
				"https://www.linkedin.com/in/ShylaAithala", "UG", "PG", "Working in Hachathon TDD", "H4-EAD",
				new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));

		/** Skill Sample Data **/
		// int skillId, String skillName, UserSkillMap userSkill, Timestamp
		// creationTime, Timestamp lastModTime
		Skill mockSkill_1 = new Skill(110, "Java", new Timestamp(utilDate.getTime()),
				new Timestamp(utilDate.getTime()));
		Skill mockSkill_2 = new Skill(111, "SpringBoot", new Timestamp(utilDate.getTime()),
				new Timestamp(utilDate.getTime()));
		Skill mockSkill_3 = new Skill(112, "Jeera", new Timestamp(utilDate.getTime()),
				new Timestamp(utilDate.getTime()));

		/** User_SKILL_MAP Sample Data **/
		// String userSkillId, User user, Skill skill, int monthsOfExp, Timestamp
		// creationTime, Timestamp lastModTime
		userSkill_1 = new UserSkillMap("US01", mockUser_1, mockSkill_1, 36, new Timestamp(utilDate.getTime()),
				new Timestamp(utilDate.getTime()));
		userSkill_2 = new UserSkillMap("US02", mockUser_1, mockSkill_2, 8, new Timestamp(utilDate.getTime()),
				new Timestamp(utilDate.getTime()));

		userSkill_3 = new UserSkillMap("US03", mockUser_1, mockSkill_1, 36, new Timestamp(utilDate.getTime()),
				new Timestamp(utilDate.getTime()));
		userSkill_4 = new UserSkillMap("US04", mockUser_2, mockSkill_1, 24, new Timestamp(utilDate.getTime()),
				new Timestamp(utilDate.getTime()));
		userSkill_5 = new UserSkillMap("US05", mockUser_2, mockSkill_3, 8, new Timestamp(utilDate.getTime()),
				new Timestamp(utilDate.getTime()));

		mapList.add(userSkill_1);
		mapList.add(userSkill_2);
		mapList.add(userSkill_3);
		mapList.add(userSkill_4);
		mapList.add(userSkill_5);

		return mapList;
	}
	
	private UserSkillMapDto populateUserSkillMapDto(UserSkillMap userSkill) {
		UserSkillMapDto mapDto = new UserSkillMapDto();
		
		mapDto.setUser_skill_id(userSkill.getUserSkillId());
		mapDto.setUser_id(userSkill.getUser().getUserId());
		mapDto.setSkill_id(userSkill.getSkill().getSkillId());
		mapDto.setMonths_of_exp(userSkill.getMonthsOfExp());
		
		return mapDto;
	}
}
