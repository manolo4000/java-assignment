package com.leantech.assignment.kafka.services;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.leantech.assignment.kafka.dao.UsersDAO;
import com.leantech.assignment.kafka.models.Users;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private UsersDAO usersDAO;
	
	public boolean match(String testpassword) {
		Users testUser = new Users();
		String testUsername = "user@test.com";
		try {
			testUser = usersDAO.findByUsername(testUsername);
			
		}catch (Exception e) {
			
			testUser.setId(1000);
			testUser.setName("Test Name");
			testUser.setLastname("Test Lastname");
			testUser.setUsername(testUsername);
			testUser.setPassword(testpassword);
			testUser = userService.secureSave(testUser);
		}
		
		boolean match = encoder.matches(testpassword, testUser.getPassword());
		
		return match;
		
	}
	
	@Test
	public void secureSaveMatchTest() {
		String testpassword = "unencrypted_pass";
		assertTrue(match(testpassword));
	}
	
	@Test
	public void secureSaveNotMatchTest() {
		String testpassword = "unencrypted_pass_NOT";
		assertFalse(match(testpassword));
	}
}
