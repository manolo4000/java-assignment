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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@SpringBootTest
@AutoConfigureMockMvc
public class BookingsControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	String username = "manuelpatsan2@hotmail.com";
    String password = "123456";
    String body = "{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}";
    
    String checkin = "2021-09-01";
    String checkout = "2021-09-02";
    Integer people = 12;
    Integer rooms = 2;
    String booking = "{\"fechaIngreso\":\"" + checkin + "\", \"fechaSalida\":\"" + checkout 
    		+ "\", \"numeroPersonas\": " + people + ", \"numeroHabitaciones\":" + rooms +"}";
	
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
	public void consultarReservaUnauthorizedTest() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/consultar-reserva/" + 1))
			      .andExpect(status().isForbidden());
		
	}
	
	@Test
	public void consultarReservaAuthorizedTest() throws Exception {
	    
		String token = getToken();
	    
		mockMvc.perform(MockMvcRequestBuilders.get("/consultar-reserva/" + 1)
			      .header("Authorization", "Bearer " + token))
			      .andExpect(status().isOk());
		
	}
	
	@Test
	public void registrarReservaUnauthorizedTest() throws Exception {
		
		
		mockMvc.perform(MockMvcRequestBuilders.post("/registrar-reserva")
				.contentType(MediaType.APPLICATION_JSON)
				.content(booking))
			    .andExpect(status().isForbidden());
		
	}
	
	@Test
	public void registrarReservaAuthorizedTest() throws Exception {
		
		String token = getToken();
		
		mockMvc.perform(MockMvcRequestBuilders.post("/registrar-reserva")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(booking))
			    .andExpect(status().isOk());
		
	}
	
	@Test
	public void registrarReservaAuthorizedBadRequestTest() throws Exception {
		
		String token = getToken();
		
		String bookingErrors = "{\"fechaIngreso\":\"" + checkin + "\", \"fechaSalida\":\"" + checkout 
	    		+ "\", \"numeroPersonas\": " + (-2) + ", \"numeroHabitaciones\":" + (-3) +"}";
		
		mockMvc.perform(MockMvcRequestBuilders.post("/registrar-reserva")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(bookingErrors))
			    .andExpect(status().isBadRequest());
		
	}
}

