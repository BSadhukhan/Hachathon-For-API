package com.ninja.lms.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;

public class UserDto {
	
	@ApiModelProperty(hidden=true)
	private String user_id;
	
	@NotBlank(message = "{NotBlank.Name}")
	private String name;
	
	@Min(value = 10, message="{Size.PhoneNumber}")
	@NotNull(message = "{NotNull.PhoneNumber}")
	private long phone_number;
	
	@NotBlank(message = "{NotBlank.Location}")
	private String location;
	
	@NotBlank(message = "{NotBlank.TimeZone}")
	private String time_zone;
	
	private String linkedin_url;
	
	@JsonInclude(value = Include.NON_NULL)
	private String education_ug;
	
	@JsonInclude(value = Include.NON_NULL)
	private String education_pg;
	
	@JsonInclude(value = Include.NON_NULL)
	@NotBlank(message = "{NotBlank.VisaStatus}")
	private String visa_status;
	
	@JsonInclude(value = Include.NON_NULL)
	private String comments;
	
	@ApiModelProperty(hidden=true)
	@JsonInclude(value = Include.NON_NULL)
	private String message_response;
	
	public UserDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public UserDto(String user_id, String name, long phone_number, String location, String time_zone,
			String linkedin_url, String education_ug, String education_pg, String visa_status, String comments) {
		super();
		this.user_id = user_id;
		this.name = name;
		this.phone_number = phone_number;
		this.location = location;
		this.time_zone = time_zone;
		this.linkedin_url = linkedin_url;
		this.education_ug = education_ug;
		this.education_pg = education_pg;
		this.visa_status = visa_status;
		this.comments = comments;
	}

	public UserDto(String user_id, String name, long phone_number, String location, String time_zone,
			String linkedin_url, String education_ug, String education_pg, String visa_status, String comments,
			String message_response) {
		super();
		this.user_id = user_id;
		this.name = name;
		this.phone_number = phone_number;
		this.location = location;
		this.time_zone = time_zone;
		this.linkedin_url = linkedin_url;
		this.education_ug = education_ug;
		this.education_pg = education_pg;
		this.visa_status = visa_status;
		this.comments = comments;
		this.message_response = message_response;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(long phone_number) {
		this.phone_number = phone_number;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getTime_zone() {
		return time_zone;
	}

	public void setTime_zone(String time_zone) {
		this.time_zone = time_zone;
	}

	public String getLinkedin_url() {
		return linkedin_url;
	}

	public void setLinkedin_url(String linkedin_url) {
		this.linkedin_url = linkedin_url;
	}

	public String getEducation_ug() {
		return education_ug;
	}

	public void setEducation_ug(String education_ug) {
		this.education_ug = education_ug;
	}

	public String getEducation_pg() {
		return education_pg;
	}

	public void setEducation_pg(String education_pg) {
		this.education_pg = education_pg;
	}

	public String getVisa_status() {
		return visa_status;
	}

	public void setVisa_status(String visa_status) {
		this.visa_status = visa_status;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getMessage_response() {
		return message_response;
	}

	public void setMessage_response(String message_response) {
		this.message_response = message_response;
	}
	
}
