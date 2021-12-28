package com.ninja.lms.dto;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;

public class UserSkillMapDto {
	
	@ApiModelProperty(hidden=true)
	private String user_skill_id;
	
	private String user_id;
	private int skill_id;	
	
	@NotNull(message = "Month of experience is required")
	private int months_of_exp;
	
	@ApiModelProperty(hidden=true)
	@JsonInclude(value = Include.NON_NULL)
	private String message_response;
	
	public UserSkillMapDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public UserSkillMapDto(String user_skill_id, String user_id, int skill_id, int months_of_exp,
			String message_response) {
		super();
		this.user_skill_id = user_skill_id;
		this.user_id = user_id;
		this.skill_id = skill_id;
		this.months_of_exp = months_of_exp;
		this.message_response = message_response;
	}


	public String getUser_skill_id() {
		return user_skill_id;
	}


	public void setUser_skill_id(String user_skill_id) {
		this.user_skill_id = user_skill_id;
	}


	public String getUser_id() {
		return user_id;
	}


	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}


	public int getSkill_id() {
		return skill_id;
	}


	public void setSkill_id(int skill_id) {
		this.skill_id = skill_id;
	}


	public int getMonths_of_exp() {
		return months_of_exp;
	}


	public void setMonths_of_exp(int months_of_exp) {
		this.months_of_exp = months_of_exp;
	}


	public String getMessage_response() {
		return message_response;
	}


	public void setMessage_response(String message_response) {
		this.message_response = message_response;
	}


	
}
