package com.ninja.lms.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;

public class SkillDto {
	
	@ApiModelProperty(hidden=true)
	private int skill_id;
	
	@NotBlank(message = "Skill name is required")
	private String skill_name;
	
	@ApiModelProperty(hidden=true)
	@JsonInclude(value = Include.NON_NULL)
	private String message_response;
	
	public SkillDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public SkillDto(int skill_id, String skill_name, String message_response) {
		super();
		this.skill_id = skill_id;
		this.skill_name = skill_name;
		this.message_response = message_response;
	}
	public int getSkill_id() {
		return skill_id;
	}


	public void setSkill_id(int skill_id) {
		this.skill_id = skill_id;
	}


	public String getSkill_name() {
		return skill_name;
	}


	public void setSkill_name(String skill_name) {
		this.skill_name = skill_name;
	}


	public String getMessage_response() {
		return message_response;
	}

	public void setMessage_response(String message_response) {
		this.message_response = message_response;
	}


}
