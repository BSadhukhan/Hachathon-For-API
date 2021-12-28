package com.ninja.lms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ninja.lms.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>{
	
	@Query(nativeQuery = true, 
			value = "SELECT usr.user_id, usr.user_first_name, usr.user_last_name, usr.user_location, usr.user_phone_number, usr.user_time_zone, "
					+ "usr.user_linkedin_url, usr.user_edu_ug, usr.user_edu_pg, usr.user_comments, usr.user_visa_status, "
					+ "usr.creation_time, usr.last_mod_time, "
					+ "mp.user_skill_id, mp.months_of_exp, mp.skill_id "
					+ "FROM tbl_lms_user usr, tbl_lms_userskill_map mp, tbl_lms_skill_master skill "
					+ "WHERE usr.user_id = mp.user_id "
					+ "AND skill.skill_id = mp.skill_id "
					+ "AND skill.skill_id = :skillId "
					+ "ORDER BY usr.user_id, skill.skill_id")
	public List<User> findUsersBySkillId(@Param("skillId") int skillId);

}
