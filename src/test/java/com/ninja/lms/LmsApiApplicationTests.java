package com.ninja.lms;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.ninja.lms.entity.Skill;
import com.ninja.lms.entity.User;
import com.ninja.lms.entity.UserSkillMap;
import com.ninja.lms.repository.UserRepository;
import com.ninja.lms.service.UserService;

@SpringBootTest
class LmsApiApplicationTests {

	@Autowired
	UserService userService;
	
	@MockBean
	UserRepository userRepo;
	
	@Test
	public void getAllUserTest() {
		
		Date utilDate = new Date();
		
		UserSkillMap userSkill_1 = null;
		
		//String userId, String userFirstName, String userLastName, long userPhoneNumber, String userLocation, String userTimeZone, 
		//String userLinkedinUrl, String userEduUg, String userEduPg, String userComments, String userVisaStatus, Timestamp creationTime, 
		//Timestamp lastModTime, Set<UserSkillMap> userSkillMapSet 
		
		User user = new User("U12", "Baisali", "Sadhukhan", 1111111111, "Pittsburgh", "EST", "jhsgf", "B.Tech", "PG", "nbdjhgd" , "H4", 
				new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()), new HashSet<UserSkillMap>());
				
		//int skillId, String skillName, UserSkillMap userSkill, Timestamp creationTime, Timestamp lastModTime		
		Skill skill = new Skill(110, "JavaSpring", userSkill_1, new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		
		//String userSkillId, User user, Skill skill, int monthsOfExp, Timestamp creationTime, Timestamp lastModTime
		userSkill_1 = new UserSkillMap("US11", user, skill, 12, new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		
		when(userRepo.findAll()).thenReturn(Stream.of(user).collect(Collectors.toList()));
		assertEquals(1, userService.getAllUsers().size());
	}

}
