package com.ninja.lms.dto;

import java.util.HashSet;
import java.util.Set;

public class UserWithSkillsDisplayDto {
	
	private String id;
	private String firstName;
	private String lastName;
	private Set<SkillExpDto> skillmap = new HashSet<>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Set<SkillExpDto> getSkillmap() {
		return skillmap;
	}
	public void setSkillmap(Set<SkillExpDto> skillmap) {
		this.skillmap = skillmap;
	}

}
