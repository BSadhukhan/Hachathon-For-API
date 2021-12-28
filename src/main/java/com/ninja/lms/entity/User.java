package com.ninja.lms.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ninja.lms.config.UserIDGenerator;

@Entity
@Table(name="TBL_LMS_USER")
public class User implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4220387089612992112L;

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @GenericGenerator(name = "user_seq", strategy = "com.ninja.lms.config.UserIDGenerator",
            parameters = {
            @Parameter(name = UserIDGenerator.INCREMENT_PARAM, value = "1"),
            @Parameter(name = UserIDGenerator.VALUE_PREFIX_PARAMETER, value = "U"),
            @Parameter(name = UserIDGenerator.NUMBER_FORMAT_PARAMETER, value = "%02d") })
	private String userId;
	
	@Column
	private String userFirstName;
	
	@Column
	private String userLastName;
	
	@Column
	private long userPhoneNumber;
	
	@Column
	private String userLocation;
	
	@Column
	private String userTimeZone;
	
	@Column
	private String userLinkedinUrl;
	
	@Column
	private String userEduUg;
	
	@Column
	private String userEduPg;
	
	@Column 
	private String userComments;
	
	@Column 
	private String userVisaStatus;
	
	@Column
	private Timestamp creationTime;
	
	@Column
	private Timestamp lastModTime;

	@OneToMany(cascade = {CascadeType.DETACH, CascadeType.PERSIST}, mappedBy = "user")
	@JsonIgnore
	Set<UserSkillMap> userSkillMapSet = new HashSet<UserSkillMap>();
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public User(String userId, String userFirstName, String userLastName, long userPhoneNumber, String userLocation,
			String userTimeZone, String userLinkedinUrl, String userEduUg, String userEduPg, String userComments,
			String userVisaStatus, Timestamp creationTime, Timestamp lastModTime) {
		super();
		this.userId = userId;
		this.userFirstName = userFirstName;
		this.userLastName = userLastName;
		this.userPhoneNumber = userPhoneNumber;
		this.userLocation = userLocation;
		this.userTimeZone = userTimeZone;
		this.userLinkedinUrl = userLinkedinUrl;
		this.userEduUg = userEduUg;
		this.userEduPg = userEduPg;
		this.userComments = userComments;
		this.userVisaStatus = userVisaStatus;
		this.creationTime = creationTime;
		this.lastModTime = lastModTime;
		
	}

	public User(String userId, String userFirstName, String userLastName, long userPhoneNumber, String userLocation,
			String userTimeZone, String userLinkedinUrl, String userEduUg, String userEduPg, String userComments,
			String userVisaStatus, Timestamp creationTime, Timestamp lastModTime, Set<UserSkillMap> userSkillMapSet) {
		super();
		this.userId = userId;
		this.userFirstName = userFirstName;
		this.userLastName = userLastName;
		this.userPhoneNumber = userPhoneNumber;
		this.userLocation = userLocation;
		this.userTimeZone = userTimeZone;
		this.userLinkedinUrl = userLinkedinUrl;
		this.userEduUg = userEduUg;
		this.userEduPg = userEduPg;
		this.userComments = userComments;
		this.userVisaStatus = userVisaStatus;
		this.creationTime = creationTime;
		this.lastModTime = lastModTime;
		this.userSkillMapSet = userSkillMapSet;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserFirstName() {
		return userFirstName;
	}

	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}

	public String getUserLastName() {
		return userLastName;
	}

	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}

	public long getUserPhoneNumber() {
		return userPhoneNumber;
	}

	public void setUserPhoneNumber(long userPhoneNumber) {
		this.userPhoneNumber = userPhoneNumber;
	}

	public String getUserLocation() {
		return userLocation;
	}

	public void setUserLocation(String userLocation) {
		this.userLocation = userLocation;
	}

	public String getUserTimeZone() {
		return userTimeZone;
	}

	public void setUserTimeZone(String userTimeZone) {
		this.userTimeZone = userTimeZone;
	}

	public String getUserLinkedinUrl() {
		return userLinkedinUrl;
	}

	public void setUserLinkedinUrl(String userLinkedinUrl) {
		this.userLinkedinUrl = userLinkedinUrl;
	}

	public String getUserEduUg() {
		return userEduUg;
	}

	public void setUserEduUg(String userEduUg) {
		this.userEduUg = userEduUg;
	}

	public String getUserEduPg() {
		return userEduPg;
	}

	public void setUserEduPg(String userEduPg) {
		this.userEduPg = userEduPg;
	}

	public String getUserComments() {
		return userComments;
	}

	public void setUserComments(String userComments) {
		this.userComments = userComments;
	}

	public String getUserVisaStatus() {
		return userVisaStatus;
	}

	public void setUserVisaStatus(String userVisaStatus) {
		this.userVisaStatus = userVisaStatus;
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

	public Set<UserSkillMap> getUserSkillMapSet() {
		return userSkillMapSet;
	}

	public void setUserSkillMapSet(Set<UserSkillMap> userSkillMapSet) {
		this.userSkillMapSet = userSkillMapSet;
	}
	
	

	
}
