package com.ninja.lms.Service;

import static org.junit.Assert.*;
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
	private UserRepository mockUserRepo;
	@MockBean
	private UserSkillMapRepository userSkillRepository;
    
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
	public void getSkillsWhenNoSkillsInDatabase() throws Exception {
		when(mockSkillRepository.findAll()).thenReturn(List.of());
		assertThrows(DataNotFoundException.class,
				() -> skillService.getSkills());
	}
	
	@Test
	public void getSkillTest() throws Exception{
		Date utilDate = new Date();
		Skill mockSkill_1 = new Skill(1, "Java", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		
		Mockito.when(mockSkillRepository.findById(mockSkill_1.getSkillId())).thenReturn(Optional.of(mockSkill_1));
		assertEquals(mockSkill_1.getSkillId(), skillService.getSkill(mockSkill_1.getSkillId()).getSkill_id());
	}

	@Test
	public void getSkillWhenRepoReturnsEmpty() throws Exception{
		Date utilDate = new Date();
		Skill mockSkill_1 = new Skill(1, "Java", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));

		Mockito.when(mockSkillRepository.findById(mockSkill_1.getSkillId())).thenReturn(Optional.empty());
		assertThrows(DataNotFoundException.class, () -> skillService.getSkill(mockSkill_1.getSkillId()));
	}
    
    @Test
    void getSkillThrowsExceptionWhenSkillIDNotFoundTest() {
    	int skillId = 99;
    	Mockito.when(this.mockSkillRepository.findById(skillId)).thenThrow(new DataNotFoundException("Skill(id- " + skillId + ") Not Found !!"));
    	
        Assertions.assertThrows(DataNotFoundException.class, () -> this.skillService.getSkill(skillId));
    }

	@Test
	void saveSkillThrowsFieldValidationExceptionTest() {
		SkillDto skill = new SkillDto();
		skill.setSkill_id(1);
		skill.setSkill_name("abch#");

		Assertions.assertThrows(FieldValidationException.class, () -> this.skillService.saveSkill(skill));
	}

	@Test
	void saveSkillThrowsFieldValidationExceptionOnDuplicateName() {
		List<Skill> skills = new ArrayList<Skill>();
		Date utilDate = new Date();
		skills.add(new Skill(99, "Skill1", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime())));
		skills.add(new Skill(100, "Random-Skill2", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime())));

		when(mockSkillRepository.findAll()).thenReturn(skills);

		SkillDto skill = new SkillDto();
		skill.setSkill_id(1);
		skill.setSkill_name("Skill1");

		Assertions.assertThrows(FieldValidationException.class, () -> this.skillService.saveSkill(skill));
	}
	
	@Test
	void saveSkillTest() {
		SkillDto skillDto = new SkillDto();
		skillDto.setSkill_id(1);
		skillDto.setSkill_name("Java");

		Date utilDate = new Date();

		Skill skill = new Skill();
		skill.setSkillId(1);
		skill.setSkillName("Java");
		skill.setCreationTime(new Timestamp(utilDate.getTime()));
		skill.setLastModTime(new Timestamp(utilDate.getTime()));

		Mockito.when(mockSkillRepository.save(Mockito.any(Skill.class))).thenReturn(skill);
		assertEquals(skill.getSkillId(), skillService.saveSkill(skillDto).getSkill_id());
	}
	
	@Test
	void updateSkillThrowsFieldValidationExceptionTest() {
		SkillDto skill = new SkillDto();
		skill.setSkill_id(1);
		skill.setSkill_name("abch#");

		Assertions.assertThrows(FieldValidationException.class, () -> this.skillService.updateSkill(skill, skill.getSkill_id()));
	}

	@Test
	void updateSkillThrowsDataNotFoundExceptionWhenRepoIsEmpty() {
		int skillId = 99;
		SkillDto skill = new SkillDto();
		skill.setSkill_id(skillId);

		skill.setSkill_name("abch#");
		Mockito.when(this.mockSkillRepository.findAll()).thenReturn(List.of());

		Assertions.assertThrows(FieldValidationException.class, () -> this.skillService.updateSkill(skill, skillId));
	}

	@Test
	void updateSkillThrowsDataNotFoundExceptionTest() {
		int skillId = 99;
		SkillDto skill = new SkillDto();
		skill.setSkill_id(skillId);
		skill.setSkill_name("new-name");

		Mockito.when(this.mockSkillRepository.findById(skillId)).thenThrow(new DataNotFoundException("Skill(id- " + skillId + ") Not Found !!"));

		Assertions.assertThrows(FieldValidationException.class, () -> this.skillService.updateSkill(skill, skillId));
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

		Assertions.assertThrows(DataNotFoundException.class, () -> this.skillService.deleteSkill(skillId));
	}
	
	@Test
	void deleteSkillTest() {
		Date utilDate = new Date();
		Skill requestMockSkill = new Skill(1, "Java", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
		Optional optional = mockSkillRepository.findById(requestMockSkill.getSkillId());
		
		doNothing().when(this.mockSkillRepository).deleteById(requestMockSkill.getSkillId());
		assertEquals(Optional.empty(), optional);
		
	}
/*
	private void setupMockRepoWithSkills(){
		List<Skill> skill = new ArrayList<Skill>();
		Date utilDate = new Date();
		skill.add(new Skill(99, "Random-Skill1", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime())));
		skill.add(new Skill(100, "Random-Skill2", new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime())));

		when(mockSkillRepository.findAll()).thenReturn(skill);
	}
*/
}
