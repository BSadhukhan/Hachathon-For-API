package com.ninja.lms.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ninja.lms.dto.UserDto;
import com.ninja.lms.entity.User;
import com.ninja.lms.service.UserService;

@RestController
public class UserController {

	@Autowired
	UserService userService;

	@GetMapping("/Users")
	public ResponseEntity<List<UserDto>> getUsersall() {
		List<UserDto> userSkillDtoList = userService.getAllUsers();
		return new ResponseEntity<>(userSkillDtoList, HttpStatus.OK);
	}

	@GetMapping("/Users/{id}")
	public ResponseEntity<UserDto> getUserWithId(@PathVariable("id") String userId) {
		UserDto userdto = userService.getUserWithId(userId);
		return new ResponseEntity<>(userdto, HttpStatus.OK);
	}

	@PostMapping("/Users")
	public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) throws URISyntaxException{
		UserDto responseDto = new UserDto();
		responseDto = userService.insertUser(userDto);
		return ResponseEntity.created(new URI("/Users/" + responseDto.getUserId())).body(responseDto);
	}

	@PutMapping("/Users/{id}")
	public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable("id") String userId) {
		User responseDto = userService.updateUser(user, userId);
		return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
	}

	// Delete User
	@DeleteMapping("/Users/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable("id") String id) {
		userService.deleteById(id);
		String msg = "User - " + id + " has been deleted !!";
		return ResponseEntity.status(HttpStatus.OK).body(msg);
	}
}
