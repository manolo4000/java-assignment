package com.leantech.assignment.kafka.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class EmailServiceTest {
	
	@Autowired
	private EmailService emailService;
	

	@Test
	void isEmailTest() {
		
		boolean result = emailService.isEmail("manuelpatsan2@hotmail.com");
		assertTrue(result);
	}
	
	@Test
	void isNotEmailTest() {
		
		boolean result = emailService.isEmail("Manuel Eduardo");
		assertFalse(result);
	}

}
