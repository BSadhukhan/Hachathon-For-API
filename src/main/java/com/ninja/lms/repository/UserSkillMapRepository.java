package com.ninja.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ninja.lms.entity.UserSkillMap;

@Repository
public interface UserSkillMapRepository extends JpaRepository<UserSkillMap, String> {
	
	
}
