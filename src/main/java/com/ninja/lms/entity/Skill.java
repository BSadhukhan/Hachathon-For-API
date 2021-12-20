package com.ninja.lms.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="TBL_LMS_SKILL_MASTER")
public class Skill {
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int skillId;
	
	@Column
	String skillName;
	/*
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "skill")
	@JsonIgnore
	private UserSkillMap userSkill = new UserSkillMap();
	*/
	@Column
	private Timestamp creationTime;
	
	@Column
	private Timestamp lastModTime;
	
	public Skill() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Skill(int skillId, String skillName, UserSkillMap userSkill, Timestamp creationTime, Timestamp lastModTime) {
		super();
		this.skillId = skillId;
		this.skillName = skillName;
		//this.userSkill = userSkill;
		this.creationTime = creationTime;
		this.lastModTime = lastModTime;
	}

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	public String getSkillName() {
		return skillName;
	}

	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}
/*
	public UserSkillMap getUserSkill() {
		return userSkill;
	}

	public void setUserSkill(UserSkillMap userSkill) {
		this.userSkill = userSkill;
	}
*/
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
