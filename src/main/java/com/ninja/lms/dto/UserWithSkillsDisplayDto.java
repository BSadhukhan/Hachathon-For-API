package com.ninja.lms.dto;

import java.util.HashSet;
import java.util.Set;

public class UserWithSkillsDisplayDto {
	
	private String userId;
	private String userName;
	private String location;
	private Set<SkillExpDto> skills = new HashSet<>();
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Set<SkillExpDto> getSkills() {
		return skills;
	}
	public void setSkills(Set<SkillExpDto> skills) {
		this.skills = skills;
	}

}
