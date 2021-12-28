package com.ninja.lms.controller;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;

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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninja.lms.dto.ExceptionResponse;
import com.ninja.lms.dto.SkillDto;
import com.ninja.lms.entity.Skill;
import com.ninja.lms.exception.DataNotFoundException;
import com.ninja.lms.repository.SkillRepository;
import com.ninja.lms.repository.UserRepository;
import com.ninja.lms.repository.UserSkillMapRepository;
import com.ninja.lms.service.SkillService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = SkillController.class)
public class SkillControllerTest {
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	SkillService mockSkillService;
	
	@MockBean
	SkillRepository mockSkillRepo;
	@MockBean
	UserRepository mockUserRepo;
	@MockBean
	UserSkillMapRepository userSkillRepository;
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	@Test
	@WithMockUser(username="APIPROCESSING", password="2xx@Success")
	public void getAllSkillsTest() throws Exception {
		String uri = "/Skills";
		
		List<Skill> skillList = populateSkillData();
		
		List<SkillDto> skillDtoList = new ArrayList<SkillDto>();
		for(Skill skill : skillList) {
			SkillDto skillDto = new SkillDto();
			skillDto.setSkill_id(skill.getSkillId());
			skillDto.setSkill_name(skill.getSkillName());
			skillDtoList.add(skillDto);
		}
		String expectedJSON = mapper.writeValueAsString(skillDtoList);
		System.out.println(expectedJSON);
		
		Mockito.when(this.mockSkillRepo.findAll()).thenReturn(skillList);

		Mockito.when(mockSkillService.getSkills()).thenReturn(skillDtoList);
		
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
	public void getSkillByIdTest() throws Exception {
		
		Skill mockSkill_1 = populateSkillData().get(1);
		Optional<Skill> optionalSkill = Optional.of(mockSkill_1);
		
		SkillDto responseSkillDto = new SkillDto();
		responseSkillDto.setSkill_id(mockSkill_1.getSkillId());
		responseSkillDto.setSkill_name(mockSkill_1.getSkillName());
		String expectedJSON = mapper.writeValueAsString(responseSkillDto);
		
		String uri = "/Skills/" + mockSkill_1.getSkillId();
		
		Mockito.when(mockSkillRepo.findById(mockSkill_1.getSkillId())).thenReturn(optionalSkill);

		Mockito.when(mockSkillService.getSkill(mockSkill_1.getSkillId())).thenReturn(responseSkillDto);
		
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
	public void getSkillByIdThrowsValidationExceptionWhenParamEmpty() throws Exception {

		Skill mockSkill_1 = populateSkillData().get(1);
		Optional<Skill> optionalSkill = Optional.of(mockSkill_1);

		SkillDto responseSkillDto = new SkillDto();
		responseSkillDto.setSkill_id(mockSkill_1.getSkillId());
		responseSkillDto.setSkill_name(mockSkill_1.getSkillName());
		String expectedJSON = mapper.writeValueAsString(responseSkillDto);

		String uri = "/Skills/" + "abc123";

		Mockito.when(mockSkillRepo.findById(mockSkill_1.getSkillId())).thenReturn(optionalSkill);

		Mockito.when(mockSkillService.getSkill(mockSkill_1.getSkillId())).thenReturn(responseSkillDto);

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(uri)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
	}
	
	@Test
	@WithMockUser(username="APIPROCESSING", password="2xx@Success")
	public void getSkillById_NotFound_Test() throws Exception {
		
		Skill mockSkill_1 = populateSkillData().get(1);
		String uri = "/Skills/" + mockSkill_1.getSkillId();
		
		String errorMsg = "Skill(id- " + mockSkill_1.getSkillId() + ") Not Found !!";
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), errorMsg, uri);
		String exceptionJSON = mapper.writeValueAsString(exceptionResponse);
		
		Mockito.when(mockSkillRepo.findById(mockSkill_1.getSkillId())).thenReturn(Optional.ofNullable(null));
		Mockito.when(mockSkillService.getSkill(mockSkill_1.getSkillId())).thenThrow(new DataNotFoundException(errorMsg));
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(uri)
				.accept(MediaType.APPLICATION_JSON)
				.content(exceptionJSON)
				.contentType(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		MockHttpServletResponse response = result.getResponse();
		
		assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
		
		//assertEquals(new DataNotFoundException(errorMsg), result.getResolvedException());
		assertTrue(result.getResolvedException() instanceof DataNotFoundException);
		
		assertEquals(errorMsg, result.getResolvedException().getMessage());
		
	}
	
	@Test
	@WithMockUser(username="APIPROCESSING", password="2xx@Success")
	public void createSkillTest() throws Exception{
		
		String uri = "/Skills";
		Date utilDate = new Date();
		Skill requestMockSkill = new Skill(115, "Cucumber", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		
		SkillDto responseSkillDto = new SkillDto();
		responseSkillDto.setSkill_id(requestMockSkill.getSkillId());
		responseSkillDto.setSkill_name(requestMockSkill.getSkillName());
		responseSkillDto.setMessage_response("Successfully Created !!");
		
		String requestJson = mapper.writeValueAsString(responseSkillDto);
		String expectedJSON = mapper.writeValueAsString(responseSkillDto);
		
		Mockito.when(this.mockSkillRepo.save(requestMockSkill)).thenReturn(requestMockSkill);
		
		Mockito.when(mockSkillService.saveSkill(ArgumentMatchers.any())).thenReturn(responseSkillDto);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(uri)
				.accept(MediaType.APPLICATION_JSON)
				.content(requestJson)
				.contentType(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		MockHttpServletResponse response = result.getResponse();
		
		assertEquals(HttpStatus.CREATED.value(), response.getStatus());
		
		assertEquals("/Skills/" + responseSkillDto.getSkill_id(), response.getHeader(HttpHeaders.LOCATION));
		
		JSONAssert.assertEquals(expectedJSON, result.getResponse().getContentAsString(), false);
		
		String responseString = response.getContentAsString();
		SkillDto returnSkillDto = new ObjectMapper().readValue(responseString, SkillDto.class);
		assertEquals(requestMockSkill.getSkillId(), returnSkillDto.getSkill_id());
	}
	
	@Test
	@WithMockUser(username="APIPROCESSING", password="2xx@Success")
	public void updateSkillTest() throws Exception{
		
		Skill requestMockSkill = populateSkillData().get(2);
		requestMockSkill.setSkillName("Spring Boot");
		
		String uri = "/Skills/" + requestMockSkill.getSkillId();
		
		SkillDto responseSkillDto = new SkillDto();
		responseSkillDto.setSkill_id(requestMockSkill.getSkillId());
		responseSkillDto.setSkill_name(requestMockSkill.getSkillName());
		responseSkillDto.setMessage_response("Successfully Updated !!");
		
		String requestJson = mapper.writeValueAsString(responseSkillDto);
		String expectedJSON = mapper.writeValueAsString(responseSkillDto);
		
		Mockito.when(mockSkillRepo.save(requestMockSkill)).thenReturn(requestMockSkill);
		Mockito.when(mockSkillService.updateSkill(ArgumentMatchers.any(), anyInt())).thenReturn(responseSkillDto);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.put(uri)
				.accept(MediaType.APPLICATION_JSON)
				.content(requestJson)
				.contentType(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		MockHttpServletResponse response = result.getResponse();
		
		assertEquals(HttpStatus.CREATED.value(), response.getStatus());
		
		JSONAssert.assertEquals(expectedJSON, result.getResponse().getContentAsString(), false);
		
		String responseString = response.getContentAsString();
		SkillDto returnSkillDto = new ObjectMapper().readValue(responseString, SkillDto.class);
		assertEquals(requestMockSkill.getSkillId(), returnSkillDto.getSkill_id());
	}
	
	@Test
	@WithMockUser(username="APIPROCESSING", password="2xx@Success")
	public void updateSkillWhenInvalidParamaters() throws Exception{

		Skill requestMockSkill = populateSkillData().get(2);
		requestMockSkill.setSkillName("Spring Boot");
		String requestJson = mapper.writeValueAsString(requestMockSkill);
		String uri = "/Skills/" + "abc123";

		SkillDto responseSkillDto = new SkillDto();
		responseSkillDto.setSkill_id(requestMockSkill.getSkillId());
		responseSkillDto.setSkill_name(requestMockSkill.getSkillName());
		responseSkillDto.setMessage_response("Successfully Updated !!");
		
		Mockito.when(mockSkillRepo.save(requestMockSkill)).thenReturn(requestMockSkill);
		Mockito.when(mockSkillService.updateSkill(ArgumentMatchers.any(), anyInt())).thenReturn(responseSkillDto);

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.put(uri)
				.accept(MediaType.APPLICATION_JSON)
				.content(requestJson)
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
	}
	
	@Test
	@WithMockUser(username="APIPROCESSING", password="2xx@Success")
	public void deleteBySkillIdTest() throws Exception{
		Skill requestMockSkill = populateSkillData().get(2);
		String uri = "/Skills/" + requestMockSkill.getSkillId();
		
		Mockito.when(mockSkillRepo.findById(requestMockSkill.getSkillId())).thenReturn(Optional.of(requestMockSkill));
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.delete(uri)
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		
		String responseString = response.getContentAsString();
		
		Map<String, String> responseMap = new TreeMap<String, String>(Collections.reverseOrder());
		String msg = "The record has been deleted !!";
		responseMap.put("Skill_Id", String.valueOf(requestMockSkill.getSkillId()));
		responseMap.put("message_response", msg);
		
		String expectedJSON = mapper.writeValueAsString(responseMap);
		
		assertEquals(expectedJSON, responseString);
	}
	
	@Test
	@WithMockUser(username="APIPROCESSING", password="2xx@Success")
	public void deleteSkillFailsWhenInvalidParameter() throws Exception{
		//Skill requestMockSkill = populateSkillData().get(2);
		String uri = "/Skills/" + "abc123";

		//Mockito.when(mockSkillRepo.findById(requestMockSkill.getSkillId())).thenReturn(Optional.of(requestMockSkill));

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.delete(uri)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
	}
	
	private List<Skill> populateSkillData(){
		Date utilDate = new Date();
		//int skillId, String skillName, Timestamp creationTime, Timestamp lastModTime
		Skill mockSkill_1 = new Skill(110, "Java", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		Skill mockSkill_2 = new Skill(111, "SpringBoot", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		Skill mockSkill_3 = new Skill(112, "Jeera", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		Skill mockSkill_4 = new Skill(113, "Selenium", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		Skill mockSkill_5 = new Skill(114, "Postman", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		
		List<Skill> skillList = new ArrayList<Skill>();
		skillList.add(mockSkill_1);
		skillList.add(mockSkill_2);
		skillList.add(mockSkill_3);
		skillList.add(mockSkill_4);
		skillList.add(mockSkill_5);
		
		return skillList;
	}		
}
