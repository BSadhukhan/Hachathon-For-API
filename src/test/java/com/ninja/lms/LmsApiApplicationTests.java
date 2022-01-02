package com.ninja.lms;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.ninja.lms.service.UserService;

@SpringBootTest
class LmsApiApplicationTests {

	@Autowired
	UserService userService;
	
	@Test
	public void contextLoads() {
		
	}


}
