package com.leantech.assignment.kafka.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;
    
    @Test
   	public void authenticateMethodNotAllowedTest() throws Exception {

   		mockMvc.perform(MockMvcRequestBuilders.get("/auth"))
   			      .andExpect(status().isMethodNotAllowed());
   		
   	}
    
    @Test
   	public void authenticateUnAuthorizedTest() throws Exception {

    	String username = "manuelpatsan2@hotmail.com";
        String password = "1234567";
        String body = "{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}";
        
        mockMvc.perform(MockMvcRequestBuilders.post("/auth")
	    		.contentType(MediaType.APPLICATION_JSON)
	            .content(body))
	    		.andExpect(status().isForbidden());
   		
   	}
    
    @Test
   	public void authenticateAuthorizedTest() throws Exception {

    	String username = "manuelpatsan2@hotmail.com";
        String password = "123456";
        String body = "{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}";
        
        mockMvc.perform(MockMvcRequestBuilders.post("/auth")
	    		.contentType(MediaType.APPLICATION_JSON)
	            .content(body))
	    		.andExpect(status().isOk());
   		
   	}

}
