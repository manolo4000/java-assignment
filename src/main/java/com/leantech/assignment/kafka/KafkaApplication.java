package com.leantech.assignment.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.leantech.assignment.kafka.dao.UsersDAO;
import com.leantech.assignment.kafka.models.Users;
import com.leantech.assignment.kafka.services.UserService;

@SpringBootApplication
public class KafkaApplication {
	

	@Autowired
	private static UserService userService;
	
	@Autowired
	private static UsersDAO usersDAO;
	
	
	
	public static void main(String[] args) {
		SpringApplication.run(KafkaApplication.class, args);
		/*
		String username = "manuelpatsan2@hotmail.com";
		
		
		Users def = new Users();
		def.setName("Manuel");
		def.setLastname("Patarroyo S");
		def.setUsername(username);
		def.setPassword("123456");
		
		userService.secureSave(def);
		
		try {
		
			userService.loadUserByUsername(username);
					
		}catch(Exception e) {
			
			System.out.println("SALIO ERROR");
			try { 
				
	
			}catch(Exception f) {
				System.out.println(f);
			}
		}
		*/
	}

}
