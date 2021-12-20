package com.ninja.lms.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ninja.lms.dto.UserDto;
import com.ninja.lms.entity.User;
import com.ninja.lms.exception.UserNotFoundException;
import com.ninja.lms.repository.UserRepository;

@Service
public class UserService {

	@Resource
	UserRepository userRepo;

	public List<UserDto> getAllUsers() {
		List<UserDto> userListDTO = new ArrayList<UserDto>();
		List<User> userList = userRepo.findAll();
		userList.stream().forEach(user -> {
			UserDto usr = mapUserEntityToUserDto(user);
			userListDTO.add(usr);
		});
		return userListDTO;
	}

	public UserDto getUserWithId(String userId) {
		UserDto usrdto = new UserDto();

		Optional<User> user = userRepo.findById(userId);
		if (!user.isPresent()) {
			throw new UserNotFoundException("User(id- " + userId + ") Not Found !!");
		} else {
			usrdto = mapUserEntityToUserDto(user.get());
		}
		return usrdto;
	}

	public UserDto insertUser(UserDto userDto) {
		User user = new User();
		user = mapUserDtoToUserEntity(userDto);
		
		Date utilDate = new Date();
		user.setCreationTime(new Timestamp(utilDate.getTime()));
		user.setLastModTime(new Timestamp(utilDate.getTime()));

		UserDto newUserDto = mapUserEntityToUserDto(userRepo.save(user));

		return newUserDto;
	}

	public User updateUser(User user, String userId) {
		User modifiedUser = new User();
		Date utilDate = new Date();

		Optional<User> existingUser = userRepo.findById(userId);
		if (!existingUser.isPresent()) {
			throw new UserNotFoundException("User(id- " + userId + ") Not Found !!");
		} else {
			user.setLastModTime(new Timestamp(utilDate.getTime()));
			modifiedUser = userRepo.save(user);
		}
		
		return modifiedUser;

	}

	public void deleteById(String userId) {
		
		boolean exists = userRepo.existsById(userId);
		if(!exists)
			throw new UserNotFoundException("User(id- " + userId + ") Not Found !!");
		else
			userRepo.deleteById(userId);

	}
	
	private User mapUserDtoToUserEntity(UserDto userDto) {
		User user = new User();
		
		String[] name = userDto.getUserName().equals("") ? null : userDto.getUserName().split(",");
		
		user.setUserFirstName(name[0].trim());
		user.setUserLastName(name[1].trim());
		user.setUserPhoneNumber(userDto.getPhoneNumber());
		user.setUserLocation(userDto.getLocation());
		user.setUserTimeZone(userDto.getTimeZone());
		user.setUserLinkedinUrl(userDto.getLinkedinUrl());
		user.setUserEduUg(userDto.getEducationUG());
		user.setUserEduPg(userDto.getEducationPG());
		user.setUserVisaStatus(userDto.getVisaStatus());
		user.setUserComments(userDto.getUserComments());
		
		return user;
	}

	private UserDto mapUserEntityToUserDto(User user) {
		UserDto userDto = new UserDto();

		userDto.setUserId(user.getUserId());
		userDto.setUserName(user.getUserFirstName() + "," + user.getUserLastName());
		userDto.setPhoneNumber(user.getUserPhoneNumber());
		userDto.setLocation(user.getUserLocation());
		userDto.setTimeZone(user.getUserTimeZone());
		userDto.setLinkedinUrl(user.getUserLinkedinUrl());
		userDto.setEducationUG(user.getUserEduUg());
		userDto.setEducationPG(user.getUserEduPg());
		userDto.setVisaStatus(user.getUserVisaStatus());
		userDto.setUserComments(user.getUserComments());

		return userDto;
	}

	private Timestamp getTime(User user) {
		Timestamp t = user.getCreationTime();
		System.out.println(t);
		return t;
	}
}
