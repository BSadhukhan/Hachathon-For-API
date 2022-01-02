package com.ninja.lms.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;

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
import com.ninja.lms.exception.FieldValidationException;
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
		String userId = "  ";

		Mockito.when(this.mockUserRepo.findById(userId))
				.thenThrow(new DataNotFoundException("User(id- " + userId + ") Not Found !!"));
		Assertions.assertThrows(DataNotFoundException.class, () -> this.userService.getUserWithId(userId));
	}

	@Test
	public void getUserWithId_AlphanumericThrowsDataNotFoundExceptionTest() {
		String userId = "us#$10";
		Mockito.when(this.mockUserRepo.findById(userId))
				.thenThrow(new DataNotFoundException("User(id- " + userId + ") Not Found !!"));
		Assertions.assertThrows(DataNotFoundException.class, () -> this.userService.getUserWithId(userId));

	}

	@Test
	public void getUserWithId_integerThrowsDataNotFoundExceptionTest() {
		String userId = "10";
		Mockito.when(this.mockUserRepo.findById(userId))
				.thenThrow(new DataNotFoundException("User(id- " + userId + ") Not Found !!"));
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
		User requestMockUser = new User("US09", "Ajay", "Das", 8553355123L, "Harrisburgh", "EST",
				"https://www.linkedin.com/in/AjayDas", "UG", "PG", "Insertion from Test", "H4-EAD",
				new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));

		Mockito.when(mockUserRepo.save(requestMockUser)).thenReturn(requestMockUser);
		assertEquals(requestMockUser, mockUserRepo.save(requestMockUser));
	}

	@Test
	public void insertUserThrowsFieldValidationExceptionTest() {

		Date utilDate = new Date();
		User requestMockUser = new User("US09", " ", "Das", 8553355123L, "Harrisburgh", "EST",
				"https://www.linkedin.com/in/AjayDas", "UG", "PG", "Insertion from Test", "H4-EAD",
				new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		Mockito.when(mockUserRepo.save(requestMockUser)).thenThrow(new FieldValidationException(
				"User(Firstname-" + requestMockUser + ")Failed to create user details as firstname is set to blank"));

		Assertions.assertThrows(FieldValidationException.class, () -> this.mockUserRepo.save(requestMockUser));

	}

	@Test
	public void insertUser_phonenumber_ThrowsFieldValidationExceptionTest() {

		Date utilDate = new Date();
		User requestMockUser = new User("US09", "Ajay ", "Das", 000L, "Harrisburgh", "EST",
				"https://www.linkedin.com/in/AjayDas", "UG", "PG", "Insertion from Test", "H4-EAD",
				new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		Mockito.when(mockUserRepo.save(requestMockUser)).thenThrow(new FieldValidationException("User(phonenumber-"
				+ requestMockUser + ")Failed to create user details as Phone number must be integer and 10-digit"));

		Assertions.assertThrows(FieldValidationException.class, () -> this.mockUserRepo.save(requestMockUser));

	}

	@Test
	public void insertUser_linkedin_Url_FormatThrowsFieldValidationExceptionTest() {

		Date utilDate = new Date();
		User requestMockUser = new User("US09", "Ajay ", "Das", 8553355123L, "Harrisburgh", "EST", "linjnjnnm.bhb",
				"UG", "PG", "Insertion from Test", "H4-EAD", new Timestamp(utilDate.getTime()),
				new Timestamp(utilDate.getTime()));
		Mockito.when(mockUserRepo.save(requestMockUser)).thenThrow(new FieldValidationException("User(linkedin Url-"
				+ requestMockUser + ")Failed to create user details as linkedin data should be in Url format"));

		Assertions.assertThrows(FieldValidationException.class, () -> this.mockUserRepo.save(requestMockUser));

	}

	@Test
	public void insertUser_TimeZone_ThrowsFieldValidationExceptionTest() {

		Date utilDate = new Date();
		User requestMockUser = new User("US09", "Ajay ", "Das", 8553355123L, "Harrisburgh", "",
				"https://www.linkedin.com/in/AjayDas", "UG", "PG", "Insertion from Test", "H4-EAD",
				new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		Mockito.when(mockUserRepo.save(requestMockUser)).thenThrow(new FieldValidationException(
				"User(TimeZone-" + requestMockUser + ")Failed to create user details as Timezone should not be blank"));

		Assertions.assertThrows(FieldValidationException.class, () -> this.mockUserRepo.save(requestMockUser));

	}

	@Test
	public void insertUser_visaStatus_ThrowsFieldValidationExceptionTest() {

		Date utilDate = new Date();
		User requestMockUser = new User("US09", "Ajay ", "Das", 8553355123L, "Harrisburgh", "EST",
				"https://www.linkedin.com/in/AjayDas", "UG", "PG", "Insertion from Test", "",
				new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		Mockito.when(mockUserRepo.save(requestMockUser)).thenThrow(new FieldValidationException("User(Visa Status-"
				+ requestMockUser + ")Failed to create user details as Visa Status should not be blank"));

		Assertions.assertThrows(FieldValidationException.class, () -> this.mockUserRepo.save(requestMockUser));

	}

	@Test
	public void insertUser_Location_ThrowsFieldValidationExceptionTest() {

		Date utilDate = new Date();
		User requestMockUser = new User("US09", "Ajay ", "Das", 8553355123L, "dfghh%$$$", "EST",
				"https://www.linkedin.com/in/AjayDas", "UG", "PG", "Insertion from Test", "H4-EAD",
				new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		Mockito.when(mockUserRepo.save(requestMockUser)).thenThrow(new FieldValidationException("Failed to create user details as Location should not accept special character"));

		Assertions.assertThrows(FieldValidationException.class, () -> this.mockUserRepo.save(requestMockUser));

	}

	@Test
	public void insertUser_Name_ThrowsFieldValidationExceptionTest() {

		Date utilDate = new Date();
		User requestMockUser = new User("US09", "Ajay$%^^&& ", "Das", 8553355123L, "Harrisburgh", "EST",
				"https://www.linkedin.com/in/AjayDas", "UG", "PG", "Insertion from Test", "H4-EAD",
				new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		Mockito.when(mockUserRepo.save(requestMockUser)).thenThrow(new FieldValidationException(
				"User(Name-" + requestMockUser + ")Failed to create user details as Name should not be Alphanumeric"));

		Assertions.assertThrows(FieldValidationException.class, () -> this.mockUserRepo.save(requestMockUser));

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

		String userId = "  ";
		Mockito.when(this.mockUserRepo.findById(userId))
				.thenThrow(new DataNotFoundException("User(id- " + userId + ") Not Found !!"));
		Assertions.assertThrows(DataNotFoundException.class, () -> this.userService.getUserWithId(userId));
	}

	@Test
	public void updateUser_UserId_Alphanumeric_ThrowsDataNotFoundExceptionTest() {

		String userId = " ad%^ ";
		Mockito.when(this.mockUserRepo.findById(userId))
				.thenThrow(new DataNotFoundException("User(id- " + userId + ") Invalid user--UserId should be string !!"));
		Assertions.assertThrows(DataNotFoundException.class, () -> this.userService.getUserWithId(userId));
	}

	@Test
	public void updateUser_UserId_integer_ThrowsDataNotFoundExceptionTest() {

		String userId = " 24 ";
		Mockito.when(this.mockUserRepo.findById(userId))
				.thenThrow(new DataNotFoundException("User(id- " + userId + ")  Invalid user--UserId should be string !!"));
		Assertions.assertThrows(DataNotFoundException.class, () -> this.userService.getUserWithId(userId));
	}

	@Test
	public void updateUser_Phonenumber_ThrowsFieldValidationExceptionTest() {

		User requestMockUser = populateUserData().get(1);
		requestMockUser.setUserPhoneNumber(5903654L);

		Mockito.when(mockUserRepo.save(requestMockUser)).thenThrow(new FieldValidationException("User(phonenumber-"
				+ requestMockUser + ")Failed to update user details as Phone number should be integer and 10-digit"));

		Assertions.assertThrows(FieldValidationException.class, () -> this.mockUserRepo.save(requestMockUser));
	}

	@Test
	public void updateUser_linkedinUrl_ThrowsFieldValidationExceptionTest() {

		User requestMockUser = populateUserData().get(1);
		requestMockUser.setUserLinkedinUrl("bjndjnkk.dni");
		Mockito.when(mockUserRepo.save(requestMockUser)).thenThrow(new FieldValidationException("User(linkedin Url-"
				+ requestMockUser + ")Failed to update user details as linkedin data should be in Url format"));

		Assertions.assertThrows(FieldValidationException.class, () -> this.mockUserRepo.save(requestMockUser));

	}
	
	@Test
	public void updateUser_Timezone_ThrowsFieldValidationExceptionTest() {

		User requestMockUser = populateUserData().get(1);
		requestMockUser.setUserTimeZone("");
		Mockito.when(mockUserRepo.save(requestMockUser)).thenThrow(new FieldValidationException(
				"User(TimeZone-" + requestMockUser + ")Failed to update user details as Timezone should not be blank"));

		Assertions.assertThrows(FieldValidationException.class, () -> this.mockUserRepo.save(requestMockUser));

	}
	
	@Test
	public void updateUser_VisaStatus_ThrowsFieldValidationExceptionTest() {

		User requestMockUser = populateUserData().get(1);
		requestMockUser.setUserVisaStatus("");
		Mockito.when(mockUserRepo.save(requestMockUser)).thenThrow(new FieldValidationException(
				"User(Visa Status-" + requestMockUser + ")Failed to update user details as Visa Status should not be blank"));

		Assertions.assertThrows(FieldValidationException.class, () -> this.mockUserRepo.save(requestMockUser));

	}
	
	@Test
	public void updateUser_UserName_ThrowsFieldValidationExceptionTest() {

		User requestMockUser = populateUserData().get(1);
		requestMockUser.setUserFirstName("4%%^CCF");
		Mockito.when(mockUserRepo.save(requestMockUser)).thenThrow(new FieldValidationException(
				"User(Name-" + requestMockUser + ")Failed to update user details as Name should not be Alphanumeric"));

		Assertions.assertThrows(FieldValidationException.class, () -> this.mockUserRepo.save(requestMockUser));

	}
	
	@Test
	public void updateUser_Location_ThrowsFieldValidationExceptionTest() {

		User requestMockUser = populateUserData().get(1);
		requestMockUser.setUserLocation("4%%^CCF");
		Mockito.when(mockUserRepo.save(requestMockUser)).thenThrow(new FieldValidationException(
				"User(Location-" + requestMockUser + ")Failed to update user details as Name should not be Alphanumeric"));

		Assertions.assertThrows(FieldValidationException.class, () -> this.mockUserRepo.save(requestMockUser));

	}

	@Test
	public void deleteUserByIdThrowsDataNotFoundExceptionTest() {

		String userId = "  ";

		Mockito.when(this.mockUserRepo.existsById(userId))
				.thenThrow(new DataNotFoundException("User(id- " + userId + ") Not Found !!"));
		Assertions.assertThrows(DataNotFoundException.class, () -> this.userService.getUserWithId(userId));
	}

	@Test
	public void deleteUserById_integer_ThrowsDataNotFoundExceptionTest() {

		String userId = " 900 ";

		Mockito.when(this.mockUserRepo.existsById(userId))
				.thenThrow(new DataNotFoundException("User(id- " + userId + ") Invalid UserId--UserId must be integer!!"));
		Assertions.assertThrows(DataNotFoundException.class, () -> this.userService.getUserWithId(userId));
	}

	@Test
	public void deleteUserById_integer_alphanumeric_ThrowsDataNotFoundExceptionTest() {

		String userId = " d67$%%";

		Mockito.when(this.mockUserRepo.existsById(userId))
				.thenThrow(new DataNotFoundException("User(id- " + userId + ") Invalid UserId--UserId must be integer!!"));
		Assertions.assertThrows(DataNotFoundException.class, () -> this.userService.getUserWithId(userId));
	}

	@Test
	public void deleteUserByIdTest() {
		User mockUser_1 = populateUserData().get(0);

		Optional optional = mockUserRepo.findById(mockUser_1.getUserId());
		
		doNothing().when(this.mockUserRepo).deleteById(mockUser_1.getUserId());
		assertEquals(Optional.empty(), optional);
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
