package com.ninja.lms.controller;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninja.lms.dto.ExceptionResponse;
import com.ninja.lms.dto.UserDto;
import com.ninja.lms.entity.User;
import com.ninja.lms.exception.DataNotFoundException;
import com.ninja.lms.repository.SkillRepository;
import com.ninja.lms.repository.UserRepository;
import com.ninja.lms.repository.UserSkillMapRepository;
import com.ninja.lms.service.UserService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class)
public class UserControllerTest {
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService mockUserService;
	
	@MockBean
	private SkillRepository mockSkillRepo;
	
	@MockBean
	private UserRepository mockUserRepo;
	
	@MockBean
	private UserSkillMapRepository userSkillRepository;
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	@Test
	@WithMockUser(username="APIPROCESSING", password="2xx@Success")
	public void getAllUsersTest() throws Exception {
		String uri = "/Users";
		
		List<User> userList = populateUserData();
		
		List<UserDto> userDtoList = populateUserDtoData("GET");
		String expectedJSON = mapper.writeValueAsString(userDtoList);
		
		Mockito.when(this.mockUserRepo.findAll()).thenReturn(userList);
		Mockito.when(mockUserService.getAllUsers()).thenReturn(userDtoList);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(uri)
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		MockHttpServletResponse response = result.getResponse();
		
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		
		JSONAssert.assertEquals(expectedJSON, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	@WithMockUser(username="APIPROCESSING", password="2xx@Success")
	public void getUserWithIdTest() throws Exception {
		
		User mockUser_1 = populateUserData().get(1);
		String uri = "/Users/" + mockUser_1.getUserId();
		
		UserDto userDto = mapEntityToDto(mockUser_1, "GET");
		String expectedJSON = mapper.writeValueAsString(userDto);
		
		Mockito.when(mockUserRepo.findById(mockUser_1.getUserId())).thenReturn(Optional.of(mockUser_1));
		Mockito.when(mockUserService.getUserWithId(mockUser_1.getUserId())).thenReturn(userDto);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(uri)
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		MockHttpServletResponse response = result.getResponse();
		
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		
		JSONAssert.assertEquals(expectedJSON, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	@WithMockUser(username="APIPROCESSING", password="2xx@Success")
	public void getUserWithId_NOTFOUND_Test() throws Exception {
		
		User mockUser_1 = populateUserData().get(1);
		String uri = "/Users/" + mockUser_1.getUserId();
		
		String errorMsg = "User(id- " + mockUser_1.getUserId() + ") Not Found !!";
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), errorMsg, uri);
		String exceptionJSON = mapper.writeValueAsString(exceptionResponse);
		
		Mockito.when(mockUserRepo.findById(mockUser_1.getUserId())).thenReturn(Optional.ofNullable(null));
		Mockito.when(mockUserService.getUserWithId(mockUser_1.getUserId())).thenThrow(new DataNotFoundException(errorMsg));
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(uri)
				.accept(MediaType.APPLICATION_JSON)
				.content(exceptionJSON)
				.contentType(MediaType.APPLICATION_JSON);;
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		MockHttpServletResponse response = result.getResponse();
		
		assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
		
		//assertEquals(new DataNotFoundException(errorMsg), result.getResolvedException());
		assertTrue(result.getResolvedException() instanceof DataNotFoundException);
		
		assertEquals(errorMsg, result.getResolvedException().getMessage());
	}
	
	@Test
	@WithMockUser(username="APIPROCESSING", password="2xx@Success")
	public void insertUserTest() throws Exception {
		
		String uri = "/Users";
		Date utilDate = new Date();
		
		/*
		 * String userId, String userFirstName, String userLastName, long userPhoneNumber, String userLocation, String userTimeZone, 
		 * String userLinkedinUrl, String userEduUg, String userEduPg, String userComments, String userVisaStatus, Timestamp creationTime, 
		 * Timestamp lastModTime
		 */
		User requestMockUser = new User("US09", "Ajay", "Das", 8553355123L, "Harrisburgh", "EST", "https://www.linkedin.com/in/AjayDas", 
				"UG", "PG", "Insertion from Test", "H4-EAD", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		
		
		UserDto requestMockUserDto = new UserDto("US09", "Ajay,Das", 8553355123L, "Harrisburgh", "EST", "https://www.linkedin.com/in/AjayDas", 
				"UG", "PG", "H4-EAD", "Insertion from Test");
		requestMockUserDto.setUser_id("");
		String requestJSON = mapper.writeValueAsString(requestMockUserDto);		
		
		UserDto responseMockUserDto = mapEntityToDto(requestMockUser, "POST");
		String responseJSON = mapper.writeValueAsString(responseMockUserDto);
		
		Mockito.when(mockUserRepo.save(requestMockUser)).thenReturn(requestMockUser);
		Mockito.when(mockUserService.insertUser(ArgumentMatchers.any())).thenReturn(responseMockUserDto);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(uri)
				.accept(MediaType.APPLICATION_JSON)
				.content(requestJSON)
				.contentType(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		MockHttpServletResponse response = result.getResponse();
		
		assertEquals(HttpStatus.CREATED.value(), response.getStatus());
		
		assertEquals("/Users/" + responseMockUserDto.getUser_id(), response.getHeader(HttpHeaders.LOCATION));
		
		String responseString = response.getContentAsString();
		JSONAssert.assertEquals(responseJSON, responseString, false);
		
		UserDto returnUserDto = new ObjectMapper().readValue(responseString, UserDto.class);
		assertEquals(requestMockUser.getUserId(), returnUserDto.getUser_id());
	}
	
	@Test
	@WithMockUser(username="APIPROCESSING", password="2xx@Success")
	public void updateUserTest() throws Exception{
		Date utilDate = new Date();
		
		User requestMockUser = populateUserData().get(1);
		requestMockUser.setUserPhoneNumber(5554443654L);
		requestMockUser.setLastModTime(new Timestamp(utilDate.getTime()));
		
		UserDto requestMockUserDto = mapEntityToDto(requestMockUser, "PUT");
		requestMockUserDto.setUser_id("");
		requestMockUserDto.setMessage_response("");
		String requestJSON = mapper.writeValueAsString(requestMockUserDto);
		
		String uri = "/Users/" + requestMockUser.getUserId();
		
		UserDto responseMockUserDto = mapEntityToDto(requestMockUser, "PUT");
		String responseJSON = mapper.writeValueAsString(responseMockUserDto);
		
		Mockito.when(mockUserRepo.save(requestMockUser)).thenReturn(requestMockUser);
		Mockito.when(mockUserService.updateUser(ArgumentMatchers.any(), anyString())).thenReturn(responseMockUserDto);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.put(uri)
				.accept(MediaType.APPLICATION_JSON)
				.content(requestJSON)
				.contentType(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		MockHttpServletResponse response = result.getResponse();
		
		assertEquals(HttpStatus.CREATED.value(), response.getStatus());
		
		String responseString = response.getContentAsString();
		JSONAssert.assertEquals(responseJSON, responseString, false);
		
		UserDto returnUserDto = new ObjectMapper().readValue(responseString, UserDto.class);
		assertEquals(requestMockUser.getUserId(), returnUserDto.getUser_id());
		
	}
	
	@Test
	@WithMockUser(username="APIPROCESSING", password="2xx@Success")
	public void deleteUserTest() throws Exception{
		User requestMockUser = populateUserData().get(2);
		String uri = "/Users/" + requestMockUser.getUserId();
		
		Mockito.when(mockUserRepo.findById(requestMockUser.getUserId())).thenReturn(Optional.of(requestMockUser));
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.delete(uri)
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		
		String responseString = response.getContentAsString();
		
		Map<String, String> responseMap = new TreeMap<String, String>(Collections.reverseOrder());
		String msg = "The record has been deleted !!";
		responseMap.put("user_ id", String.valueOf(requestMockUser.getUserId()));
		responseMap.put("message_response", msg);
		
		String expectedJSON = mapper.writeValueAsString(responseMap);
		assertEquals(expectedJSON, responseString);
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
	
	private List<UserDto> populateUserDtoData(String requestType){
		
		List<UserDto> userDtoList = new ArrayList<UserDto>();
		
		List<User> userList = populateUserData();
		
		userList.stream().forEach(user -> {
			UserDto userDto = mapEntityToDto(user, requestType);
			userDtoList.add(userDto);
		});
		return userDtoList;
	}
	
	private UserDto mapEntityToDto(User user, String requestType) {
		UserDto userDto = new UserDto();
		userDto.setUser_id(user.getUserId());
		userDto.setName(user.getUserFirstName() + "," + user.getUserLastName());
		userDto.setPhone_number(user.getUserPhoneNumber());
		userDto.setLocation(user.getUserLocation());
		userDto.setTime_zone(user.getUserTimeZone());
		userDto.setLinkedin_url(user.getUserLinkedinUrl());
		
		if( !requestType.equalsIgnoreCase("") && (requestType.equalsIgnoreCase("POST") || requestType.equalsIgnoreCase("PUT")) ){
			String msg = requestType.equalsIgnoreCase("POST") ? "Successfully Created !!" : "Successfully Updated !!";
			userDto.setEducation_ug(user.getUserEduUg());
			userDto.setEducation_pg(user.getUserEduPg());
			userDto.setVisa_status(user.getUserVisaStatus());
			userDto.setComments(user.getUserComments());
			userDto.setMessage_response(msg);
		}
		return userDto;
	}
}
