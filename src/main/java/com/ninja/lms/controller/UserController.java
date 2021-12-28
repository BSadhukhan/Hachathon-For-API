package com.ninja.lms.controller;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
import com.ninja.lms.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "Users API", value = "UsersAPI")
public class UserController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	UserService userService;
	
	//Fetch all User Details
	@GetMapping("/Users")
	@ApiOperation(value = "List all users")
	public ResponseEntity<List<UserDto>> getAllUsers() {
		List<UserDto> userSkillDtoList = userService.getAllUsers();
		return new ResponseEntity<>(userSkillDtoList, HttpStatus.OK);
	}
	
	//Fetch User Details by User ID
	@GetMapping("/Users/{id}")
	@ApiOperation(value = "List user by USER_ID")
	public ResponseEntity<UserDto> getUserById(@PathVariable("id") String userId) {
		
		UserDto userdto = userService.getUserWithId(userId);
		return new ResponseEntity<>(userdto, HttpStatus.OK);
	}

	//Create User
	@PostMapping("/Users")
	@ApiOperation(value = "Create a new user")
	public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) throws Exception{
		
		UserDto responseDto = new UserDto();
		responseDto = userService.insertUser(userDto);
		return ResponseEntity.created(new URI("/Users/" + responseDto.getUser_id())).body(responseDto);
	}

	//Update User
	@PutMapping("/Users/{id}")
	@ApiOperation(value = "Update an existing user")
	public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto, @PathVariable("id") String userId) throws Exception {
		
		UserDto responseDto = userService.updateUser(userDto, userId);
		return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
	}

	//Delete User
	@DeleteMapping("/Users/{id}")
	@ApiOperation(value = "Delete an existing user")
	public ResponseEntity<Map<String, String>> deleteUser(@PathVariable("id") String id) {
		Map<String, String> responseMap = new TreeMap<String, String>(Collections.reverseOrder());
		userService.deleteUserById(id);

		String msg = "The record has been deleted !!";
		responseMap.put("user_ id", id);
		responseMap.put("message_response", msg);
		
		return ResponseEntity.status(HttpStatus.OK).body(responseMap);
	}
}
