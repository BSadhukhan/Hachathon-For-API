package com.ninja.lms.Service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

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

import com.ninja.lms.dto.SkillDto;
import com.ninja.lms.entity.Skill;
import com.ninja.lms.exception.DataNotFoundException;
import com.ninja.lms.exception.FieldValidationException;
import com.ninja.lms.repository.SkillRepository;
import com.ninja.lms.repository.UserRepository;
import com.ninja.lms.repository.UserSkillMapRepository;
import com.ninja.lms.service.SkillService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = SkillService.class)
public class SkillServiceTest {
	@Autowired
	private SkillService skillService;

    @MockBean
    private SkillRepository mockSkillRepository;
    @MockBean
	UserRepository mockUserRepo;
	@MockBean
	UserSkillMapRepository userSkillRepository;
    
	@Test
	public void getSkillsTest() throws Exception {
		List<Skill> skill = new ArrayList<Skill>();
		Date utilDate = new Date();
		skill.add(new Skill(1, "Java", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime())));
		skill.add(new Skill(2, "SpringBoot", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime())));

		when(mockSkillRepository.findAll()).thenReturn(skill);
		assertEquals(2, skillService.getSkills().size());
	}
	
	@Test
	public void getSkillTest() throws Exception{
		Date utilDate = new Date();
		Skill mockSkill_1 = new Skill(1, "Java", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		
		Mockito.when(mockSkillRepository.findById(mockSkill_1.getSkillId())).thenReturn(Optional.of(mockSkill_1));
		assertEquals(Optional.of(mockSkill_1), mockSkillRepository.findById(mockSkill_1.getSkillId()));
	}
    
    @Test
    void getSkillThrowsExceptionWhenSkillIDNotFoundTest() {
    	int skillId = 99;
    	Mockito.when(this.mockSkillRepository.findById(skillId)).thenThrow(new DataNotFoundException("Skill(id- " + skillId + ") Not Found !!"));
    	
        Assertions.assertThrows(DataNotFoundException.class, () -> this.skillService.getSkill(skillId));
    }

	//@Test
	void saveSkillThrowsFieldValidationExceptionTest() {
		SkillDto skill = new SkillDto();
		skill.setSkill_id(1);
		skill.setSkill_name("");
		
		Mockito.when(this.skillService.saveSkill(skill)).thenThrow(new FieldValidationException("Failed to create Skill details as Skill Name is set to blank !!"));
	
		Assertions.assertThrows(FieldValidationException.class, () -> this.skillService.saveSkill(skill));
	}
	
	@Test
	void saveSkillTest() {
		Date utilDate = new Date();

		Skill skill = new Skill();
		skill.setSkillId(1);
		skill.setSkillName("Java");
		skill.setCreationTime(new Timestamp(utilDate.getTime()));
		skill.setLastModTime(new Timestamp(utilDate.getTime()));

		when(mockSkillRepository.save(skill)).thenReturn(skill);
		assertEquals(skill, mockSkillRepository.save(skill));
	}
	
	//@Test
	void updateSkillThrowsFieldValidationExceptionTest() {
		SkillDto skill = new SkillDto();
		skill.setSkill_id(1);
		skill.setSkill_name("");

		Mockito.when(skillService.updateSkill(skill, skill.getSkill_id())).thenThrow(new FieldValidationException("Failed to create Skill details as Skill Name is set to blank !!"));
		Assertions.assertThrows(FieldValidationException.class, () -> this.skillService.updateSkill(skill, skill.getSkill_id()));
	}

	@Test
	void updateSkillThrowsDataNotFoundExceptionTest() {
		int skillId = 99;
		Mockito.when(this.mockSkillRepository.findById(skillId)).thenThrow(new DataNotFoundException("Skill(id- " + skillId + ") Not Found !!"));
		
		Assertions.assertThrows(DataNotFoundException.class, () -> this.skillService.getSkill(skillId));
	}
	
	@Test
	void updateSkillTest() {
		Date utilDate = new Date();
		Skill skill = new Skill(115, "Cucumber", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		
		when(mockSkillRepository.save(skill)).thenReturn(skill);
		assertEquals(skill, mockSkillRepository.save(skill));
	}
	
	@Test
	void deleteSkillThrowsDataNotFoundExceptionTest() {
    	int skillId=0;
		Mockito.when(this.mockSkillRepository.findById(skillId)).thenThrow(new DataNotFoundException("Skill(id- " + skillId + ") Not Found !!"));

		Assertions.assertThrows(DataNotFoundException.class, () -> this.skillService.getSkill(skillId));
	}
	
	//@Test
	void deleteSkillTest() {
		Date utilDate = new Date();
		Skill requestMockSkill = new Skill(1, "Java", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		Optional optional = mockSkillRepository.findById(requestMockSkill.getSkillId());
		
		boolean isExistBeforeDelete = mockSkillRepository.findById(requestMockSkill.getSkillId()).isPresent();
		
		doNothing().when(this.mockSkillRepository).deleteById(requestMockSkill.getSkillId());

		//doNothing().when(mockSkillRepository).deleteSkill(requestMockSkill.getSkillId());
		
		boolean notExistAfterDelete = mockSkillRepository.findById(requestMockSkill.getSkillId()).isPresent();
		assertEquals(Optional.empty(), optional);
		assertTrue(isExistBeforeDelete);
		assertFalse(notExistAfterDelete);
	}

}
