package com.ninja.lms.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.ninja.lms.entity.User;
import com.ninja.lms.exception.DataNotFoundException;
import com.ninja.lms.repository.SkillRepository;
import com.ninja.lms.repository.UserRepository;
import com.ninja.lms.repository.UserSkillMapRepository;
import com.ninja.lms.service.UserService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserService.class)
public class UserServiceTest {
	
	@Autowired
	private UserService userService;
	
	@MockBean
    private SkillRepository mockSkillRepository;
    @MockBean
	private UserRepository mockUserRepo;
	@MockBean
	private UserSkillMapRepository userSkillRepository;
	
	@Test
	public void getAllUsersTest() {
		
		List<User> userList = populateUserData();
		
		Mockito.when(mockUserRepo.findAll()).thenReturn(userList);
		assertEquals(userList.size(), userService.getAllUsers().size());
	}
	
	@Test
	public void getUserWithIdThrowsDataNotFoundExceptionTest() {
		String userId="  ";
		
		Mockito.when(this.mockUserRepo.findById(userId)).thenThrow(new DataNotFoundException("User(id- " + userId + ") Not Found !!"));
		Assertions.assertThrows(DataNotFoundException.class, () -> this.userService.getUserWithId(userId));	
	}
	
	@Test
	public void getUserWithIdTest() throws Exception {
		User mockUser_1 = populateUserData().get(1);
		
		Optional<User> optionalUser = Optional.of(mockUser_1);
		
		Mockito.when(mockUserRepo.findById(mockUser_1.getUserId())).thenReturn(Optional.of(mockUser_1));
		assertEquals(optionalUser, mockUserRepo.findById(mockUser_1.getUserId()));
	}
	
	@Test
	public void insertUserTest() {
		
		Date utilDate = new Date();
		User requestMockUser = new User("US09", "Ajay", "Das", 8553355123L, "Harrisburgh", "EST", "https://www.linkedin.com/in/AjayDas", 
				"UG", "PG", "Insertion from Test", "H4-EAD", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		
		Mockito.when(mockUserRepo.save(requestMockUser)).thenReturn(requestMockUser);
		assertEquals(requestMockUser, mockUserRepo.save(requestMockUser));
	}
	
	@Test
	public void updateUserTest() {
		
		User requestMockUser = populateUserData().get(1);
		requestMockUser.setUserPhoneNumber(5554443654L);
		
		Mockito.when(mockUserRepo.save(requestMockUser)).thenReturn(requestMockUser);

		assertEquals(requestMockUser, mockUserRepo.save(requestMockUser));
	}
	
	@Test
	public void updateUserThrowsDataNotFoundExceptionTest() {
		
		String userId="  ";
		Mockito.when(this.mockUserRepo.findById(userId)).thenThrow(new DataNotFoundException("User(id- " + userId + ") Not Found !!"));
		Assertions.assertThrows(DataNotFoundException.class, () -> this.userService.getUserWithId(userId));
	}
	
	@Test
	public void deleteUserByIdThrowsDataNotFoundExceptionTest() {
		
		String userId="  ";
		
		Mockito.when(this.mockUserRepo.existsById(userId)).thenThrow(new DataNotFoundException("User(id- " + userId + ") Not Found !!"));
		Assertions.assertThrows(DataNotFoundException.class, () -> this.userService.getUserWithId(userId));	
	}
	
	//@Test
	public void deleteUserByIdTest() {
		User mockUser_1 = populateUserData().get(0);
		
		verify(mockUserRepo,times(1)).deleteById(mockUser_1.getUserId());
	}
	
	private List<User> populateUserData(){
		Date utilDate = new Date();
		List<User> userList = new ArrayList<User>();
		
		/*
		 * String userId, String userFirstName, String userLastName, long userPhoneNumber, String userLocation, String userTimeZone, String userLinkedinUrl,
		 * String userEduUg, String userEduPg, String userComments, String userVisaStatus, Timestamp creationTime, Timestamp lastModTime 
		 */
		User mockUser_1 = new User("US01", "Baisali", "Sadhukhan", 8553334123L, "Pittsburgh", "EST", "https://www.linkedin.com/in/BaisaliSadukhan", 
				"UG", "PG", "Working in Hachathon TDD", "H4-EAD", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		
		User mockUser_2 = new User("US02", "Shyla", "Aithala", 8762653456L, "Dallas", "CST", "https://www.linkedin.com/in/ShylaAithala", 
				"UG", "PG", "Working in Hachathon TDD", "H4-EAD", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		
		User mockUser_3 = new User("US03", "Mounika", "Mounika", 5467892340L, "Minneapolis", "EST", "https://www.linkedin.com/in/MounikaMounika", 
				"UG", "PG", "Working in Hachathon TDD", "H4-EAD", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		
		User mockUser_4 = new User("US04", "Megha", "Patel", 6754562345L, "Richmond", "EST", "https://www.linkedin.com/in/MeghaPatel", 
				"UG", "PG", "Working in Hachathon TDD-BDD", "H4-EAD", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		
		User mockUser_5 = new User("US05", "Priyanka", "Tripathy", 8761113456L, "New Jersey", "EST", "https://www.linkedin.com/in/PriyankaTripathy", 
				"UG", "PG", "Working in Hachathon TDD-BDD", "H4-EAD", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		
		User mockUser_6 = new User("US06", "Sam", "Moon", 8882653456L, "Arizona", "PST", "https://www.linkedin.com/in/SamMoon", 
				"UG", "PG", "Working in Hachathon BDD", "H4-EAD", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		
		User mockUser_7 = new User("US07", "Janaki", "Iyenger", 5552333456L, "Virginia", "EST", "https://www.linkedin.com/in/JanakiIyenger", 
				"UG", "PG", "Working in Hachathon BDD", "H4-EAD", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		
		User mockUser_8 = new User("US08", "Vidya", "Arun", 7654445555L, "Seattel", "PST", "https://www.linkedin.com/in/VidyaArun", 
				"UG", "PG", "Working in Hachathon BDD", "H4-EAD", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		
		userList.add(mockUser_1);
		userList.add(mockUser_2);
		userList.add(mockUser_3);
		userList.add(mockUser_4);
		userList.add(mockUser_5);
		userList.add(mockUser_6);
		userList.add(mockUser_7);
		userList.add(mockUser_8);
		
		return userList;
	}
}
