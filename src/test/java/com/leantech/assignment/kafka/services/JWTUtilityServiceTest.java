package com.leantech.assignment.kafka.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class JWTUtilityServiceTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private JWTUtilityService jwtUtilityService;
	
	String username = "manuelpatsan2@hotmail.com";
    String password = "123456";
    String body = "{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}";
    
    public String getToken() throws Exception {
    	
    	MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/auth")
	    		.contentType(MediaType.APPLICATION_JSON)
	            .content(body))
	    		.andExpect(status().isOk())
	            .andReturn();
		
	    
		String response = result.getResponse().getContentAsString();
		
		String content = new String(response.getBytes("UTF-8"));
		
		content = content.replace("token", "");
		content = content.replace("{\"\":\"", "");
		
		String token = content.replace("\"}", "");
		
		return token;

    }
    
    @Test
	void getUsernameFromTokenTest() throws Exception {
		String token = getToken();
		String result = jwtUtilityService.getUsernameFromToken(token);
		
		assertEquals(result, username);
	}
    
    @Test
	void getExpirationDateFromTokenTest() throws Exception {
		String token = getToken();
		Date result = jwtUtilityService.getExpirationDateFromToken(token);
		boolean isRealDate = result.after(new Date());
		assertTrue(isRealDate);
		
		
	}

}
