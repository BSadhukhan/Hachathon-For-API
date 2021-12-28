package com.ninja.lms.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ninja.lms.config.UserIDGenerator;

@Entity
@Table(name="TBL_LMS_USERSKILL_MAP")
public class UserSkillMap implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4699717899183000961L;

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_skill_seq")
    @GenericGenerator(name = "user_skill_seq", strategy = "com.ninja.lms.config.UserSkillIDGenerator",
            parameters = {
            @Parameter(name = UserIDGenerator.INCREMENT_PARAM, value = "1"),
            @Parameter(name = UserIDGenerator.VALUE_PREFIX_PARAMETER, value = "US"),
            @Parameter(name = UserIDGenerator.NUMBER_FORMAT_PARAMETER, value = "%02d") })
	private String userSkillId;
	
	@ManyToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST})
	@JoinColumn(name="user_id", insertable = true, updatable = true)
	@JsonIgnore
	private User user;
	
	@OneToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST})
	@JoinColumn(name ="skill_id", insertable = true, updatable = true)
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
	@JsonIgnore
	public Timestamp getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Timestamp creationTime) {
		this.creationTime = creationTime;
	}
	@JsonIgnore
	public Timestamp getLastModTime() {
		return lastModTime;
	}

	public void setLastModTime(Timestamp lastModTime) {
		this.lastModTime = lastModTime;
	}
	
}
