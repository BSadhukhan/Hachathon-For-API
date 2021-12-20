package com.ninja.lms.dto;

public class UserSkillMapDto {
	
	private String userSkillId;
	private String userId;
	private int skillId;	
	private int monthsOfExp;
	
	public String getUserSkillId() {
		return userSkillId;
	}
	public void setUserSkillId(String userSkillId) {
		this.userSkillId = userSkillId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getSkillId() {
		return skillId;
	}
	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}
	
	public int getMonthsOfExp() {
		return monthsOfExp;
	}
	public void setMonthsOfExp(int monthsOfExp) {
		this.monthsOfExp = monthsOfExp;
	}

}
