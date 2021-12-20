package com.ninja.lms.dao;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ninja.lms.dto.UserSkillMapDto;

@Repository
public class UserSkillMapDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	final String INSERT_QUERY = "INSERT INTO tbl_lms_userskill_map(user_skill_id, user_id, skill_id, months_of_exp, creation_time, last_mod_time)"
			+ "	VALUES (?, ?, ?, ?, ?, ?);";

	final String UPDATE_QUERY = "UPDATE tbl_lms_userskill_map "
			+ "SET user_skill_id=?, user_id=?, skill_id=?, months_of_exp=?, creation_time=?, last_mod_time=? "
			+ "WHERE user_skill_id = ?";

	// final String DELETE_QUERY = "delete from employee where id = ?";

	public int insertUserSkillMap(UserSkillMapDto mapDto) {
		Date utilDate = new Date();
		return jdbcTemplate.update(INSERT_QUERY, mapDto.getUserSkillId(), mapDto.getUserId(), mapDto.getSkillId(),
				mapDto.getMonthsOfExp(), new Timestamp(utilDate.getTime()), new Timestamp(utilDate.getTime()));
	}

}
