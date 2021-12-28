package com.ninja.lms.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class SkillExpDto {
	
	private int id;
	private String skill;
	private int exp;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSkill() {
		return skill;
	}
	public void setSkill(String skill) {
		this.skill = skill;
	}
	@JsonIgnore
	public int getExp() {
		return exp;
	}
	public void setExp(int exp) {
		this.exp = exp;
	}
	

}
