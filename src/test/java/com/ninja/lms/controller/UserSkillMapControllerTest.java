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

//import org.junit.Test;
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
@WebMvcTest(value = UserSkillMapController.class)
public class UserSkillMapControllerTest {
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	UserSkillMapService mockUserSkillMapService;
	
	@MockBean
	UserSkillMapRepository userSkillMapRepository;
	@MockBean
	UserRepository usrRepo;
	@MockBean
	SkillRepository skillRepo;
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	@Test
	@WithMockUser(username="APIPROCESSING", password="2xx@Success")
	public void getAllUserSkillMapDataTest() throws Exception {
	
		String uri = "/UserSkills";
		List<UserSkillMap> userSkillMapList = populateUserSkillMapData();
		List<UserSkillMapDto> dtoList = new ArrayList<UserSkillMapDto>();
		for (UserSkillMap mapObj : userSkillMapList) {
			UserSkillMapDto mapDto = populateUserSkillMapDto(mapObj);
			dtoList.add(mapDto);
		}
	
		String requestJSON = mapper.writeValueAsString(dtoList);
		
		Mockito.when(userSkillMapRepository.findAll()).thenReturn(userSkillMapList);
		Mockito.when(mockUserSkillMapService.fetchAllUserSkillMapData()).thenReturn(dtoList);
			
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(uri)
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		MockHttpServletResponse response = result.getResponse();		
		
		assertEquals(HttpStatus.OK.value(), response.getStatus());		
		JSONAssert.assertEquals(requestJSON, response.getContentAsString(), false);
			
	}
	
	@Test
	@WithMockUser(username="APIPROCESSING", password="2xx@Success")
	public void getUserSkillMapDataByIdTest() throws Exception {
	
		UserSkillMap mockUserSkillMap_1 = populateUserSkillMapData().get(1);
		String uri = "/UserSkills/" + mockUserSkillMap_1.getUserSkillId();

		UserSkillMapDto responseDto = populateUserSkillMapDto(mockUserSkillMap_1);
		String responseJSON = mapper.writeValueAsString(responseDto);
		
		Mockito.when(userSkillMapRepository.findById(mockUserSkillMap_1.getUserSkillId())).thenReturn(Optional.of(mockUserSkillMap_1));
		Mockito.when(mockUserSkillMapService.fetchUserSkillMapDataById(mockUserSkillMap_1.getUserSkillId())).thenReturn(responseDto);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(uri)
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		MockHttpServletResponse response = result.getResponse();
		
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		
		JSONAssert.assertEquals(responseJSON, response.getContentAsString(), false);
	}
	
	@Test
	@WithMockUser(username="APIPROCESSING", password="2xx@Success")
	public void getUserSkillMapDataWithId_NOTFOUND_Test() throws Exception {
		
		UserSkillMap mockUserSkillMap_1 = populateUserSkillMapData().get(1);
		String uri = "/UserSkills/" + mockUserSkillMap_1.getUserSkillId();
		
		String errorMsg = "User(id- " + mockUserSkillMap_1.getUserSkillId() + ") Not Found !!";
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), errorMsg, uri);
		String exceptionJSON = mapper.writeValueAsString(exceptionResponse);
		
		Mockito.when(userSkillMapRepository.findById(mockUserSkillMap_1.getUserSkillId())).thenReturn(Optional.ofNullable(null));
		Mockito.when(mockUserSkillMapService.fetchUserSkillMapDataById(mockUserSkillMap_1.getUserSkillId())).thenThrow(new DataNotFoundException(errorMsg));
		
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
	public void createUserSkillMapTest() throws Exception {
		String uri = "/UserSkills";
		Date utilDate = new Date();
		/*
		 * String userId, String userFirstName, String userLastName, long userPhoneNumber, String userLocation, String userTimeZone, String userLinkedinUrl, 
		 * String userEduUg, String userEduPg, String userComments, String userVisaStatus, Timestamp creationTime, Timestamp lastModTime, 
		 */
		User requestMockUser = new User("US03", "Mounika", "Mounika", 5467892340L, "Minneapolis", "EST", "https://www.linkedin.com/in/MounikaMounika", 
				"UG", "PG", "Working in Hachathon TDD", "H4-EAD", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		
		//int skillId, String skillName, UserSkillMap userSkill, Timestamp creationTime, Timestamp lastModTime
		Skill requestMockSkill = new Skill(113, "Selenium", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		
		//String userSkillId, User user, Skill skill, int monthsOfExp, Timestamp creationTime, Timestamp lastModTime
		UserSkillMap requestMockUserSkillMap = new UserSkillMap("US08", requestMockUser, requestMockSkill, 36, new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		String requestJson = mapper.writeValueAsString(requestMockUserSkillMap);
		
		UserSkillMapDto responseDto = populateUserSkillMapDto(requestMockUserSkillMap);
		responseDto.setMessage_response("Successfully Created !!");
		String responseJSON = mapper.writeValueAsString(responseDto);
		
		Mockito.when(userSkillMapRepository.save(requestMockUserSkillMap)).thenReturn(requestMockUserSkillMap);
		Mockito.when(mockUserSkillMapService.createUserSkillMap(ArgumentMatchers.any())).thenReturn(responseDto);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(uri)
				.accept(MediaType.APPLICATION_JSON)
				.content(requestJson)
				.contentType(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		MockHttpServletResponse response = result.getResponse();
		
		assertEquals(HttpStatus.CREATED.value(), response.getStatus());
		
		assertEquals("/UserSkills/" + responseDto.getUser_skill_id(), response.getHeader(HttpHeaders.LOCATION));
		
		JSONAssert.assertEquals(responseJSON, result.getResponse().getContentAsString(), false);
		
		String responseString = response.getContentAsString();
		UserSkillMapDto returnMapDto = new ObjectMapper().readValue(responseString, UserSkillMapDto.class);
		assertEquals(requestMockUserSkillMap.getUserSkillId(), returnMapDto.getUser_skill_id());
	}

	@Test
	@WithMockUser(username="APIPROCESSING", password="2xx@Success")
	public void updateUserSkillMapTest() throws Exception{
		Date utilDate = new Date();
		UserSkillMap requestMockUserSkillMap = populateUserSkillMapData().get(1);
		requestMockUserSkillMap.setMonthsOfExp(24);
		requestMockUserSkillMap.setLastModTime(new Timestamp(utilDate.getTime()));
		
		String requestJson = mapper.writeValueAsString(requestMockUserSkillMap);
		
		String uri = "/UserSkills/" + requestMockUserSkillMap.getUserSkillId();
		
		UserSkillMapDto responseUserSkillMapDto = populateUserSkillMapDto(requestMockUserSkillMap);
		responseUserSkillMapDto.setMessage_response("Successfully Updated !!");
		String responseJSON = mapper.writeValueAsString(responseUserSkillMapDto);
		
		Mockito.when(userSkillMapRepository.save(requestMockUserSkillMap)).thenReturn(requestMockUserSkillMap);
		Mockito.when(mockUserSkillMapService.updateUserSkillMap(ArgumentMatchers.any(), anyString())).thenReturn(responseUserSkillMapDto);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.put(uri)
				.accept(MediaType.APPLICATION_JSON)
				.content(requestJson)
				.contentType(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		MockHttpServletResponse response = result.getResponse();
		
		assertEquals(HttpStatus.CREATED.value(), response.getStatus());
		
		String responseString = response.getContentAsString();
		JSONAssert.assertEquals(responseJSON, responseString, false);
		
		UserSkillMapDto returnUsrSkillDto = new ObjectMapper().readValue(responseString, UserSkillMapDto.class);
		assertEquals(requestMockUserSkillMap.getMonthsOfExp(), returnUsrSkillDto.getMonths_of_exp());
	}
	
	@Test
	@WithMockUser(username="APIPROCESSING", password="2xx@Success")
	public void deleteUserSkillMapByIdTest() throws Exception{
		UserSkillMap requestMockUserSkillMap = populateUserSkillMapData().get(1);
		String uri = "/UserSkills/" + requestMockUserSkillMap.getUserSkillId();
		
		Mockito.when(userSkillMapRepository.findById(requestMockUserSkillMap.getUserSkillId())).thenReturn(Optional.of(requestMockUserSkillMap));
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.delete(uri)
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		
		String responseString = response.getContentAsString();
		
		Map<String, String> responseMap = new TreeMap<String, String>(Collections.reverseOrder());
		String msg = "The record has been deleted !!";
		responseMap.put("user_skill_id", String.valueOf(requestMockUserSkillMap.getUserSkillId()));
		responseMap.put("message_response", msg);
		
		String expectedJSON = mapper.writeValueAsString(responseMap);
		assertEquals(expectedJSON, responseString);
	}
	
	private List<UserSkillMap> populateUserSkillMapData(){
		List<UserSkillMap> mapList = new ArrayList<UserSkillMap>();
		Date utilDate = new Date();
		
		UserSkillMap userSkill_1, userSkill_2, userSkill_3, userSkill_4, userSkill_5;
		
		/** User Sample Data **/
		/*
		 * String userId, String userFirstName, String userLastName, long userPhoneNumber, String userLocation, String userTimeZone, String userLinkedinUrl, 
		 * String userEduUg, String userEduPg, String userComments, String userVisaStatus, Timestamp creationTime, Timestamp lastModTime, 
		 */ 
		User mockUser_1 = new User("US01", "Baisali", "Sadhukhan", 8553334123L, "Pittsburgh", "EST", "https://www.linkedin.com/in/BaisaliSadukhan", 
				"UG", "PG", "Working in Hachathon TDD", "H4-EAD", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		
		User mockUser_2 = new User("US02", "Shyla", "Aithala", 8762653456L, "Dallas", "CST", "https://www.linkedin.com/in/ShylaAithala", 
				"UG", "PG", "Working in Hachathon TDD", "H4-EAD", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		
		/** Skill Sample Data **/
		//int skillId, String skillName, UserSkillMap userSkill, Timestamp creationTime, Timestamp lastModTime		
		Skill mockSkill_1 = new Skill(110, "Java", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		Skill mockSkill_2 = new Skill(111, "SpringBoot", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		Skill mockSkill_3 = new Skill(112, "Jeera", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		
		/** User_SKILL_MAP Sample Data **/
		//String userSkillId, User user, Skill skill, int monthsOfExp, Timestamp creationTime, Timestamp lastModTime
		userSkill_1 = new UserSkillMap("US01", mockUser_1, mockSkill_1, 36, new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		userSkill_2 = new UserSkillMap("US02", mockUser_1, mockSkill_2, 8, new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		
		userSkill_3 = new UserSkillMap("US03", mockUser_1, mockSkill_1, 36, new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		userSkill_4 = new UserSkillMap("US04", mockUser_2, mockSkill_1, 24, new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		userSkill_5 = new UserSkillMap("US05", mockUser_2, mockSkill_3, 8, new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		
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
