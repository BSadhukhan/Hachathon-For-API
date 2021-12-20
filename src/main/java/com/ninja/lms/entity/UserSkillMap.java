package com.ninja.lms.entity;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="TBL_LMS_USERSKILL_MAP")
public class UserSkillMap {
	
	@Id
	@Column
	private String userSkillId;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="user_id", insertable = false, updatable = false)
	@JsonIgnore
	private User user;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name ="skill_id", insertable = false, updatable = false)
	@JsonIgnore
	private Skill skill;
	
	@Column
	private int monthsOfExp;
	
	@Column
	private Timestamp creationTime;
	
	@Column
	private Timestamp lastModTime;
	
	public UserSkillMap() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserSkillMap(String userSkillId, User user, Skill skill, int monthsOfExp, Timestamp creationTime,
			Timestamp lastModTime) {
		super();
		this.userSkillId = userSkillId;
		this.user = user;
		this.skill = skill;
		this.monthsOfExp = monthsOfExp;
		this.creationTime = creationTime;
		this.lastModTime = lastModTime;
	}

	public String getUserSkillId() {
		return userSkillId;
	}

	public void setUserSkillId(String userSkillId) {
		this.userSkillId = userSkillId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Skill getSkill() {
		return skill;
	}

	public void setSkill(Skill skill) {
		this.skill = skill;
	}

	public int getMonthsOfExp() {
		return monthsOfExp;
	}

	public void setMonthsOfExp(int monthsOfExp) {
		this.monthsOfExp = monthsOfExp;
	}

	public Timestamp getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Timestamp creationTime) {
		this.creationTime = creationTime;
	}

	public Timestamp getLastModTime() {
		return lastModTime;
	}

	public void setLastModTime(Timestamp lastModTime) {
		this.lastModTime = lastModTime;
	}
	
}
