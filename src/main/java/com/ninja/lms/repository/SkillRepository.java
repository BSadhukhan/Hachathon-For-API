package com.ninja.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ninja.lms.entity.Skill;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Integer> {
	
}